package com.techstar.om.dasi.controller.info;

import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.jpa.info.CheckPointScheduler;
import com.techstar.om.dasi.jpa.info.CheckPointSchedulerId;
import com.techstar.om.dasi.repos.info.CheckPointRepos;
import com.techstar.om.dasi.repos.info.CheckPointSchedulerRepos;
import com.techstar.om.dasi.utils.PageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;


@Api(tags = "检查点调度")
@RestController
@RequestMapping("/point/scheduler")
public class CheckPointSchedulerController {
    @Autowired
    private CheckPointSchedulerRepos pointSchedulerRepos;
    @Autowired
    private CheckPointRepos pointRepos;

    @ApiOperation("批量保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", paramType = "body", dataType = "String",
                    value = "模型数据", required = true)
    })
    @RequestMapping(path = "/batch/save", method = {RequestMethod.POST})
    public void save(@RequestBody List<CheckPointScheduler> data) {
        pointSchedulerRepos.saveAll(data);
    }

    @ApiOperation("获取已被调度的检查点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scheduler", paramType = "query", dataType = "Long",
                    value = "调度ID", required = true),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String",
                    value = "检查点名称"),
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/include", method = {RequestMethod.GET})
    public Page<CheckPointScheduler> include(@RequestParam Long scheduler, @RequestParam(required = false) String name,
                                             @PageableDefault(sort = {"point.name"}) @ApiIgnore Pageable pageable) {
        return StringUtils.isEmpty(name) ?
                pointSchedulerRepos.findByIdScheduler(scheduler, pageable) :
                pointSchedulerRepos.findByIdSchedulerAndPointNameLikeIgnoringCase(scheduler, "%" + name + "%", pageable);
    }

    @ApiOperation("获取未被调度的检查点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scheduler", paramType = "query", dataType = "Long",
                    value = "调度ID", required = true),
            @ApiImplicitParam(name = "target", paramType = "query", dataType = "Long",
                    value = "检查对象ID"),
            @ApiImplicitParam(name = "group", paramType = "query", dataType = "Long",
                    value = "对象分组ID"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String",
                    value = "检查点名称"),
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/exclude", method = {RequestMethod.GET})
    public Page<CheckPoint> exclude(@RequestParam Long scheduler,
                                    @RequestParam(required = false) Long target,
                                    @RequestParam(required = false) Long group,
                                    @RequestParam(required = false) String name,
                                    @PageableDefault(sort = {"target.name", "name"}) @ApiIgnore Pageable pageable) {
        Set<Long> existed = pointSchedulerRepos.findByIdScheduler(scheduler)
                .stream().map(x -> x.getId().getPoint()).collect(toSet());
        List<CheckPoint> values = pointRepos.findAll(pageable.getSort()).stream().unordered().parallel()
                .filter(x -> !existed.contains(x.getId()))
                .filter(x -> target == null || target.equals(x.getTarget().getId()))
                .filter(x -> group == null || group.equals(x.getTarget().getGroup().getId()))
                .filter(x -> StringUtils.isEmpty(name) || x.getName().contains(name))
                .collect(toList());
        return PageUtils.pageOf(values, pageable);
    }

    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "body", dataType = "String",
                    value = "ID", required = true),
    })
    @RequestMapping(path = "/delete", method = {RequestMethod.POST})
    public void delete(@RequestBody CheckPointSchedulerId id) {
        pointSchedulerRepos.deleteById(id);
    }

    @ApiOperation("批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "body", dataType = "String",
                    value = "ID数组", required = true, example = "[100, 200]")
    })
    @RequestMapping(path = "/batch/delete", method = {RequestMethod.POST})
    @Transactional
    public void batchDelete(@RequestBody List<CheckPointSchedulerId> ids) throws Exception {
        for (CheckPointSchedulerId id : ids) {
            delete(id);
        }
    }

}
