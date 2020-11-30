package com.techstar.om.dasi.controller.info;

import cn.hutool.core.util.ObjectUtil;
import com.techstar.om.dasi.controller.JpaCrudController;
import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import com.techstar.om.dasi.repos.info.CheckPointRepos;
import com.techstar.om.dasi.repos.info.CheckPointSchedulerRepos;
import com.techstar.om.dasi.repos.info.CheckTargetRepos;
import com.techstar.om.dasi.repos.result.CheckResultRepos;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "检查对象")
@RestController
@RequestMapping("/info/target")
public class CheckTargetController extends JpaCrudController<CheckTarget, Long, CheckTargetRepos> {
    @Autowired
    private CheckPointRepos checkPointRepos;
    @Autowired
    private CheckPointSchedulerRepos pointSchedulerRepos;
    @Autowired
    private CheckResultRepos checkResultRepos;

    @Override
    protected CheckTarget existed(CheckTarget data) {
        data.setUpdateTime(DateTime.now());
        return repos.findFirstByNameIgnoringCase(data.getName());
    }

    @ApiOperation("查询检查点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "query", dataType = "Long", value = "对象ID", required = true),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", value = "名称"),
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/point", method = {RequestMethod.GET})
    public Page<CheckPoint> pointOfTarget(@RequestParam(required = false) Long id,
                                          @RequestParam(required = false) String name,
                                          @PageableDefault(sort = {"name"}) @ApiIgnore Pageable pageable) {
        if (id == null) {
            return StringUtils.isEmpty(name) ? checkPointRepos.findAll(pageable)
                    : checkPointRepos.findByNameLikeIgnoringCase("%" + name + "%", pageable);
        }
        return StringUtils.isEmpty(name) ? checkPointRepos.findByTargetId(id, pageable)
                : checkPointRepos.findByTargetIdAndNameLikeIgnoringCase(id, "%" + name + "%", pageable);
    }

    @ApiOperation("禁用/启用检查对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "query", dataType = "Long", value = "ID", required = true),
            @ApiImplicitParam(name = "disable", paramType = "query", dataType = "Boolean", value = "是否禁用", required = true)
    })
    @RequestMapping(path = "/disable", method = {RequestMethod.POST})
    public Boolean disable(@RequestParam Long id, @RequestParam Boolean disable) {
        repos.updateDisableById(id, disable);
        return Boolean.TRUE.equals(disable);
    }

    @ApiOperation("复制对象到指定分组")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", paramType = "body", dataType = "String", value = "复制对象", required = true)
    })
    @RequestMapping(path = "/copy", method = {RequestMethod.POST})
    @Transactional
    public void copy(@RequestBody List<CheckTarget> data) {
        data.forEach(target -> {
            Assert.isTrue(!repos.existsByNameIgnoringCase(target.getName()),
                    String.format("已经存在名称为“%s”的检查对象，不能建立名称重复的检查对象！", target.getName()));
            final Long oldTargetId = target.getId();
            target.setId(null);
            final Long newTargetId = repos.save(target).getId();
            // 复制对象下的检查点
            List<CheckPoint> points = checkPointRepos.findByTargetId(oldTargetId).stream().map(x -> {
                CheckPoint point = ObjectUtil.clone(x);
                point.setId(null);
                point.setTarget(new CheckTarget(newTargetId));
                return point;
            }).collect(Collectors.toList());
            if (!points.isEmpty()) {
                checkPointRepos.saveAll(points);
            }
        });
    }

    @Override
    @Transactional
    public void delete(@PathVariable Long id) {
        checkResultRepos.deleteByTargetId(id);
        pointSchedulerRepos.deleteByPointTargetId(id);
        checkPointRepos.deleteByTargetId(id);
        super.delete(id);
    }
}
