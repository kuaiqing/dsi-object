package com.techstar.om.dasi.controller.result;

import com.techstar.om.dasi.controller.pojo.CheckResultExcelStyle;
import com.techstar.om.dasi.controller.pojo.CheckResultSummary;
import com.techstar.om.dasi.controller.pojo.CheckTargetStatus;
import com.techstar.om.dasi.domain.ECheckTargetType;
import com.techstar.om.dasi.domain.EPriority;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import com.techstar.om.dasi.jpa.result.CheckResult;
import com.techstar.om.dasi.repos.info.CheckPointRepos;
import com.techstar.om.dasi.repos.info.CheckTargetRepos;
import com.techstar.om.dasi.repos.result.CheckResultRepos;
import com.techstar.om.dasi.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.*;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.function.BinaryOperator;

import static java.util.stream.Collectors.*;


@Api(tags = "检查结果")
@RestController
@RequestMapping("/result")
public class CheckResultController {
    private ThreadLocal<Map<Integer, CellStyle>> cellStyles = ThreadLocal.withInitial(() -> new HashMap<>());

    private static final CheckResultExcelStyle[] columnStyles = {
            new CheckResultExcelStyle("序号", 6 * 256, HorizontalAlignment.CENTER),
            new CheckResultExcelStyle("检查对象", 14 * 256, HorizontalAlignment.LEFT),
            new CheckResultExcelStyle("检查项目", 26 * 256, HorizontalAlignment.LEFT),
            new CheckResultExcelStyle("巡检结果", 8 * 256, HorizontalAlignment.CENTER),
            new CheckResultExcelStyle("检查时间", 18 * 256, HorizontalAlignment.CENTER),
            new CheckResultExcelStyle("异常描述", 40 * 256, HorizontalAlignment.LEFT),

            new CheckResultExcelStyle("处理状态", 8 * 256, HorizontalAlignment.CENTER),
            new CheckResultExcelStyle("处理过程", 40 * 256, HorizontalAlignment.LEFT),
            new CheckResultExcelStyle("处理人", 6 * 256, HorizontalAlignment.CENTER),
    };

    @Autowired
    private CheckResultRepos resultRepos;
    @Autowired
    private CheckPointRepos pointRepos;
    @Autowired
    private CheckTargetRepos targetRepos;

    @ApiOperation("告警数量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "priority", paramType = "query", dataType = "EPriority", value = "告警等级"),
    })
    @RequestMapping(path = "/alert", method = {RequestMethod.GET})
    public int resultAlert(@RequestParam(required = false) EPriority priority) {
        return resultRepos.countByPriorityGreaterThanEqual(Optional.ofNullable(priority).orElse(EPriority.Warn));
    }

    @ApiOperation("返回首页统计结果")
    @RequestMapping(path = "/summary", method = {RequestMethod.GET})
    public CheckResultSummary resultSummary() {
        Map<Long, CheckResult> idResults = resultRepos.findAll().stream().collect(toMap(k -> k.getId(), v -> v));
        List<CheckTargetStatus> targetStatuses = pointRepos.findAll().stream()
                .map(point -> new CheckTargetStatus(
                        Optional.ofNullable(idResults.get(point.getId())).orElseGet(() -> { // 没有检查结果的检查点也要统计
                            return virtualCheckResult(point.getTarget(), "没有检查结果");
                        })))
                .collect(toList());
        // 没有检查点的也要统计
        Set<String> targetNames = targetStatuses.stream().map(x -> x.getName()).collect(toSet());
        targetStatuses.addAll(
                targetRepos.findAll().stream()
                        .filter(x -> !targetNames.contains(x.getName()))
                        .map(x -> new CheckTargetStatus(virtualCheckResult(x, "沒有檢查项目")))
                        .collect(toList()));
        targetStatuses.sort(Comparator.comparing(x -> x.getName()));
        return CheckResultSummary.builder()
                .alerts(targetStatuses.stream().collect(
                        groupingBy(x -> x.getTargetType(),
                                groupingBy(x -> x.getPriority(), counting()))))
                .statuses(targetStatuses.stream().collect(
                        groupingBy(x -> x.getTargetType(),
                                groupingBy(x -> x.getGroup(),
                                        toMap(k -> k.getName(), v -> v,
                                                BinaryOperator.maxBy(Comparator.comparing(x -> x.getPriority())),
                                                LinkedHashMap::new)))))
                .build();
    }

    private CheckResult virtualCheckResult(CheckTarget target, String hint) {
        CheckResult result = new CheckResult();
        result.setPriority(EPriority.Unknown);
        result.setHint(hint);
        result.setTarget(target);
        result.setCheckTime(DateTime.now());
        return result;
    }

    @ApiOperation("列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", paramType = "query", dataType = "ECheckTargetType", value = "对象类型"),
            @ApiImplicitParam(name = "target", paramType = "query", dataType = "Long", value = "检查对象"),
            @ApiImplicitParam(name = "priority", paramType = "query", dataType = "EPriority", value = "告警等级"),

            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/list", method = {RequestMethod.GET})
    public Page<CheckResult> list(@RequestParam(required = false) ECheckTargetType type,
                                  @RequestParam(required = false) Long target,
                                  @RequestParam(required = false) EPriority priority,
                                  @PageableDefault(sort = {"target.name", "point.name"}) @ApiIgnore Pageable pageable) {
        CheckResult result = new CheckResult();
        CheckTarget checkTarget = new CheckTarget(target);
        checkTarget.setUpdateTime(null);
        checkTarget.setTargetType(type);
        result.setTarget(checkTarget);
        Example<CheckResult> example = Example.of(result);
        List<CheckResult> values = resultRepos.findAll(example, pageable.getSort());
        if (priority != null) {
            values = values.stream().filter(x -> x.getPriority().ordinal() >= priority.ordinal()).collect(toList());
        }
        return PageUtils.pageOf(values, pageable);
    }

    @ApiOperation("导出Excel")
    @RequestMapping(path = "/excel", method = {RequestMethod.GET})
    public void exportToExcel(HttpServletResponse response) {
        Map<String, List<CheckResult>> typeResults = resultRepos.findAll().stream()
                .sorted(Comparator.comparing((CheckResult x) -> x.getPoint().getTarget().getTargetType().getName())
                        .thenComparing((CheckResult x) -> x.getPoint().getTarget().getName())
                        .thenComparing(x -> x.getPoint().getName()))
                .collect(groupingBy(x -> x.getPoint().getTarget().getTargetType().getName(), LinkedHashMap::new, toList()));
        XSSFWorkbook wb = new XSSFWorkbook();
        typeResults.forEach((type, results) -> {
            XSSFSheet sheet = wb.createSheet(type);
            setSheetTitle(wb, sheet);
            for (int idx = 0; idx < results.size(); idx++) {
                setCellValue(wb, sheet, idx + 1, results.get(idx));
            }
        });
        try {
            String filename = String.format("DASI(%s)", DateTime.now().toString("yyyy-MM-dd"));
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO8859-1") + ".xlsx");
            OutputStream outputStream = response.getOutputStream();
            try {
                wb.write(outputStream);
            } finally {
                outputStream.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setSheetTitle(XSSFWorkbook wb, XSSFSheet sheet) {
        XSSFRow row = sheet.createRow(0);
        for (int idx = 0; idx < columnStyles.length; idx++) {
            // 设置标题和列
            CheckResultExcelStyle excelStyle = columnStyles[idx];
            sheet.setColumnWidth(idx, excelStyle.getColumnWidth());
            XSSFCell cell = row.createCell(idx);
            CellStyle style = cellStyle(wb, true, excelStyle.getHAlign(), null);
            cell.setCellStyle(style);
            cell.setCellType(CellType.STRING);
            cell.setCellValue(excelStyle.getTitle());
        }
    }

    private void setCellValue(XSSFWorkbook wb, XSSFSheet sheet, Integer rownum, CheckResult result) {
        XSSFRow row = sheet.createRow(rownum);
        setCellValue(wb, row, 0, rownum.toString());
        setCellValue(wb, row, 1, result.getPoint().getTarget().getName());
        setCellValue(wb, row, 2, result.getPoint().getName());
        setCellValue(wb, row, 3, result.getPriority().getName(), true, result.getPriority());
        setCellValue(wb, row, 4, result.getCheckTime().toString("yyyy-MM-dd HH:mm"));
        setCellValue(wb, row, 5, result.getHint());
    }

    private void setCellValue(XSSFWorkbook wb, XSSFRow row, Integer idx, String val) {
        setCellValue(wb, row, idx, val, false, EPriority.Info);
    }

    private void setCellValue(XSSFWorkbook wb, XSSFRow row, Integer idx, String val, Boolean bold, EPriority priority) {
        XSSFCell cell = row.createCell(idx);
        Short color = null;
        // 字体颜色
        switch (priority) {
            case Warn:
                color = IndexedColors.PINK.index;
                break;
            case Alarm:
                color = IndexedColors.RED.index;
                break;
            case Error:
                color = IndexedColors.DARK_RED.index;
                break;
        }
        CellStyle style = cellStyle(wb, bold, columnStyles[idx].getHAlign(), color);
        cell.setCellStyle(style);
        cell.setCellType(CellType.STRING);
        cell.setCellValue(val);
    }

    private CellStyle cellStyle(XSSFWorkbook wb,
                                Boolean bold, HorizontalAlignment hAlign, Short color) {
        int hash = Objects.hash(bold, hAlign, color);
        Map<Integer, CellStyle> styles = cellStyles.get();
        CellStyle style = styles.get(hash);
        if (style == null) {
            style = wb.createCellStyle();
            // 对齐
            style.setAlignment(hAlign);
            // 字体
            XSSFFont font = wb.createFont();
            font.setBold(bold);
            font.setFontName("微软雅黑");
            font.setFontHeight(9);
            if (color != null) {
                font.setColor(color);
            }
            style.setFont(font);
            styles.put(hash, style);
        }
        return style;
    }

}
