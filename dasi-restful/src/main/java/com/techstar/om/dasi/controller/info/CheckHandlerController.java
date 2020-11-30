package com.techstar.om.dasi.controller.info;

import com.techstar.om.dasi.jpa.info.CheckHandler;
import com.techstar.om.dasi.repos.info.CheckHandlerRepos;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "检查对象分组")
@RestController
@RequestMapping("/info/handler")
public class CheckHandlerController {
    @Autowired
    private CheckHandlerRepos checkHandlerRepos;

    @ApiOperation("列表")
    @RequestMapping(path = "/list", method = {RequestMethod.GET})
    List<CheckHandler> list() {
        return checkHandlerRepos.findAll();
    }
}
