package com.techstar.om.dasi.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Collections;
import java.util.List;

public interface CrudController<T, ID> {
    @ApiOperation("列表")
    @RequestMapping(path = "/list", method = {RequestMethod.GET})
    default List<T> list() throws Exception {
        return Collections.emptyList();
    }

    @ApiOperation("保存")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "data", paramType = "body", dataType = "String",
                    value = "模型数据", required = true)
    })
    @RequestMapping(path = "/save", method = {RequestMethod.POST})
    T save(@RequestBody T data) throws Exception;

    @ApiOperation("删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", paramType = "path", dataType = "int",
                    value = "ID", required = true)
    })
    @RequestMapping(path = "/delete/{id}", method = {RequestMethod.POST})
    void delete(@PathVariable ID id) throws Exception;

    @ApiOperation("批量删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", paramType = "body", dataType = "String",
                    value = "ID数组", required = true, example = "[100, 200]")
    })
    @RequestMapping(path = "/batch/delete", method = {RequestMethod.POST})
    @Transactional
    default void batchDelete(@RequestBody List<ID> ids) throws Exception {
        for (ID id : ids) {
            delete(id);
        }
    }
}
