package com.techstar.om.dasi.controller.result;

import com.techstar.om.dasi.jpa.result.CheckResultDiskFree;
import com.techstar.om.dasi.jpa.result.CheckResultHttpService;
import com.techstar.om.dasi.jpa.result.CheckResultProcessStatus;
import com.techstar.om.dasi.jpa.result.CheckResultRecordNumber;
import com.techstar.om.dasi.repos.result.CheckResultDiskFreeRepos;
import com.techstar.om.dasi.repos.result.CheckResultHttpServiceRepos;
import com.techstar.om.dasi.repos.result.CheckResultProcessStatusRepos;
import com.techstar.om.dasi.repos.result.CheckResultRecordNumberRepos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Api(tags = "检查结果历史")
@RestController
@RequestMapping("/result/history")
public class CheckResultHistoryController {
    @Autowired
    private CheckResultDiskFreeRepos diskFreeRepos;
    @Autowired
    private CheckResultHttpServiceRepos httpServiceRepos;
    @Autowired
    private CheckResultProcessStatusRepos processStatusRepos;
    @Autowired
    private CheckResultRecordNumberRepos recordNumberRepos;


    @ApiOperation("磁盘使用率")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "point", paramType = "query", dataType = "Long", value = "检查项目"),
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "String", value = "开始日期"),
            @ApiImplicitParam(name = "end", paramType = "query", dataType = "String", value = "截止日期"),
            @ApiImplicitParam(name = "mount", paramType = "query", dataType = "String", value = "查询目录"),

            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/disk/free", method = {RequestMethod.GET})
    public Page<CheckResultDiskFree> diskFree(@RequestParam Long point,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime start,
                                              @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime end,
                                              @RequestParam(required = false) String mount,
                                              @PageableDefault(sort = {"id.mount", "id.appliedTime"}) @ApiIgnore Pageable pageable) {
        return diskFreeRepos.findAll((root, cq, cb) -> {
            Path id = root.get("id");
            List<Predicate> wheres = keyWheres(id, cb, point, start, end);
            if (StringUtils.isNotBlank(mount)) {
                wheres.add(cb.equal(id.get("mount"), mount));
            }
            return cb.and(wheres.toArray(new Predicate[wheres.size()]));
        }, pageable);
    }

    @ApiOperation("HTTP服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "point", paramType = "query", dataType = "Long", value = "检查项目"),
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "String", value = "开始日期"),
            @ApiImplicitParam(name = "end", paramType = "query", dataType = "String", value = "截止日期"),

            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/http/service", method = {RequestMethod.GET})
    public Page<CheckResultHttpService> httpService(@RequestParam Long point,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime start,
                                                    @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime end,
                                                    @PageableDefault(sort = {"id.appliedTime"}) @ApiIgnore Pageable pageable) {
        return httpServiceRepos.findAll((root, cq, cb) -> {
            List<Predicate> wheres = keyWheres(root.get("id"), cb, point, start, end);
            return cb.and(wheres.toArray(new Predicate[wheres.size()]));
        }, pageable);
    }

    @ApiOperation("进程状态")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "point", paramType = "query", dataType = "Long", value = "检查项目"),
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "String", value = "开始日期"),
            @ApiImplicitParam(name = "end", paramType = "query", dataType = "String", value = "截止日期"),
            @ApiImplicitParam(name = "command", paramType = "query", dataType = "String", value = "进程命令"),

            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/process/status", method = {RequestMethod.GET})
    public Page<CheckResultProcessStatus> processStatus(@RequestParam Long point,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime start,
                                                        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime end,
                                                        @RequestParam(required = false) String command,
                                                        @PageableDefault(sort = {"id.command", "id.appliedTime"}) @ApiIgnore Pageable pageable) {
        return processStatusRepos.findAll((root, cq, cb) -> {
            Path id = root.get("id");
            List<Predicate> wheres = keyWheres(id, cb, point, start, end);
            if (StringUtils.isNotBlank(command)) {
                wheres.add(cb.equal(id.get("command"), command));
            }
            return cb.and(wheres.toArray(new Predicate[wheres.size()]));
        }, pageable);
    }

    @ApiOperation("记录量")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "point", paramType = "query", dataType = "Long", value = "检查项目"),
            @ApiImplicitParam(name = "start", paramType = "query", dataType = "String", value = "开始日期"),
            @ApiImplicitParam(name = "end", paramType = "query", dataType = "String", value = "截止日期"),
            @ApiImplicitParam(name = "object", paramType = "query", dataType = "String", value = "对象名称"),
            @ApiImplicitParam(name = "metric", paramType = "query", dataType = "String", value = "测点名称"),

            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/record/number", method = {RequestMethod.GET})
    public Page<CheckResultRecordNumber> recordNumber(@RequestParam Long point,
                                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime start,
                                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") DateTime end,
                                                      @RequestParam(required = false) String object,
                                                      @RequestParam(required = false) String metric,
                                                      @PageableDefault(sort = {"id.object", "id.metric", "id.appliedTime"}) @ApiIgnore Pageable pageable) {
        return recordNumberRepos.findAll((root, cq, cb) -> {
            Path id = root.get("id");
            List<Predicate> wheres = keyWheres(id, cb, point, start, end);
            if (StringUtils.isNotBlank(object)) {
                wheres.add(cb.equal(id.get("object"), object));
            }
            if (StringUtils.isNotBlank(metric)) {
                wheres.add(cb.equal(id.get("metric"), metric));
            }
            return cb.and(wheres.toArray(new Predicate[wheres.size()]));
        }, pageable);
    }


    private <T> List<Predicate> keyWheres(Path id, CriteriaBuilder cb, Long point, DateTime start, DateTime end) {
        return new ArrayList<Predicate>() {{
            add(cb.equal(id.get("point"), point));
            add(cb.greaterThanOrEqualTo(id.get("appliedTime"), start));
            add(cb.lessThanOrEqualTo(id.get("appliedTime"), end));
        }};
    }

}
