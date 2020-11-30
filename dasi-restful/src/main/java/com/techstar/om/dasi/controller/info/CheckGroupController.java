package com.techstar.om.dasi.controller.info;

import com.techstar.om.dasi.controller.JpaCrudController;
import com.techstar.om.dasi.jpa.info.CheckTarget;
import com.techstar.om.dasi.jpa.info.CheckTargetGroup;
import com.techstar.om.dasi.repos.info.CheckTargetGroupRepos;
import com.techstar.om.dasi.repos.info.CheckTargetRepos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "检查对象分组")
@RestController
@RequestMapping("/info/group")
public class CheckGroupController extends JpaCrudController<CheckTargetGroup, Long, CheckTargetGroupRepos> {
    @Autowired
    private CheckTargetRepos checkTargetRepos;

    @Override
    protected CheckTargetGroup existed(CheckTargetGroup data) {
        return repos.findFirstByCodeIgnoringCase(data.getCode());
    }

    @Override
    protected void assertExisted(CheckTargetGroup data) {
        assertExisted(data, String.format("已经存在编号为“%s”的记录，不能建立名称重复的记录！"
                , data.getCode()));
    }

    @ApiOperation("查询检查对象")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "query", dataType = "String", value = "分组ID"),
            @ApiImplicitParam(name = "name", paramType = "query", dataType = "String", value = "对象名称"),
            @ApiImplicitParam(name = "page", paramType = "query", dataType = "int", value = "页码"),
            @ApiImplicitParam(name = "size", paramType = "query", dataType = "int", value = "分页大小"),
            @ApiImplicitParam(name = "sort", paramType = "query", dataType = "String", allowMultiple = true,
                    value = "格式: property(,asc|desc)")
    })
    @RequestMapping(path = "/target", method = {RequestMethod.GET})
    public Page<CheckTarget> targetOfGroup(@RequestParam(required = false) Long id,
                                           @RequestParam(required = false) String name,
                                           @PageableDefault(sort = {"name"}) @ApiIgnore Pageable pageable) {
        if (id == null) {
            return StringUtils.isEmpty(name) ? checkTargetRepos.findAll(pageable)
                    : checkTargetRepos.findByNameLikeIgnoringCase("%" + name + "%", pageable);
        }
        return StringUtils.isEmpty(name) ? checkTargetRepos.findByGroupId(id, pageable)
                : checkTargetRepos.findByGroupIdAndNameLikeIgnoringCase(id, "%" + name + "%", pageable);
    }

    @Override
    public void delete(@PathVariable Long id) {
        Assert.isTrue(!checkTargetRepos.existsByGroupId(id), "该分组下存在检查对象，不能删除");
        super.delete(id);
    }

}
