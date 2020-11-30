package com.techstar.om.dasi.controller.info;

import com.techstar.om.dasi.controller.JpaCrudController;
import com.techstar.om.dasi.jpa.info.CheckPoint;
import com.techstar.om.dasi.repos.info.CheckPointRepos;
import com.techstar.om.dasi.repos.info.CheckPointSchedulerRepos;
import com.techstar.om.dasi.repos.result.CheckResultRepos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api(tags = "检查点")
@RestController
@RequestMapping("/info/point")
public class CheckPointController extends JpaCrudController<CheckPoint, Long, CheckPointRepos> {
    @Autowired
    private CheckPointSchedulerRepos pointSchedulerRepos;
    @Autowired
    private CheckResultRepos checkResultRepos;

    @Override
    protected CheckPoint existed(CheckPoint data) {
        data.setUpdateTime(DateTime.now());
        return repos.findFirstByTargetIdAndNameIgnoringCase(data.getTarget().getId(), data.getName());
    }

    @ApiOperation("禁用/启动检查点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "query", dataType = "Long", value = "ID", required = true),
            @ApiImplicitParam(name = "disable", paramType = "query", dataType = "Boolean", value = "是否禁用", required = true)
    })
    @RequestMapping(path = "/disable", method = {RequestMethod.POST})
    public Boolean disable(@RequestParam Long id, @RequestParam Boolean disable) {
        repos.updateDisableById(id, disable);
        return Boolean.TRUE.equals(disable);
    }

    @ApiOperation("复制检查项到指定检查对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "target", paramType = "query", dataType = "Long", value = "检查对象", required = true),
            @ApiImplicitParam(name = "data", paramType = "body", dataType = "String", value = "复制检查项", required = true)
    })
    @RequestMapping(path = "/copy", method = {RequestMethod.POST})
    @Transactional
    public void copy(@RequestBody List<CheckPoint> data) {
        data.forEach(point -> {
            Assert.isTrue(!repos.existsByTargetIdAndNameIgnoringCase(point.getTarget().getId(), point.getName()),
                    String.format("目标检查对象下已经存在名称为“%s”的检查项目，不能建立名称重复的检查项目！", point.getName()));
            point.setId(null);
            repos.save(point);
        });
    }

    @Override
    @Transactional
    public void delete(@PathVariable Long id) {
        checkResultRepos.deleteByPointId(id);
        pointSchedulerRepos.deleteByIdPoint(id);
        super.delete(id);
    }

}
