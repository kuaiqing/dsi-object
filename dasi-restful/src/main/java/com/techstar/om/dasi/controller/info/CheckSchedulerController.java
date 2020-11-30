package com.techstar.om.dasi.controller.info;

import com.techstar.om.dasi.controller.JpaCrudController;
import com.techstar.om.dasi.jpa.info.CheckScheduler;
import com.techstar.om.dasi.repos.info.CheckPointSchedulerRepos;
import com.techstar.om.dasi.repos.info.CheckSchedulerRepos;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = "巡检调度")
@RestController
@RequestMapping("/info/scheduler")
public class CheckSchedulerController extends JpaCrudController<CheckScheduler, Long, CheckSchedulerRepos> {

    @Autowired
    private CheckPointSchedulerRepos pointSchedulerRepos;

    @Override
    protected CheckScheduler existed(CheckScheduler data) {
        return repos.findFirstByNameIgnoringCase(data.getName());
    }

    @Override
    @Transactional
    public void delete(@PathVariable Long id) {
        pointSchedulerRepos.deleteByIdPoint(id);
        super.delete(id);
    }

}
