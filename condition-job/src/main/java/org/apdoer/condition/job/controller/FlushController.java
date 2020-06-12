package org.apdoer.condition.job.controller;


import org.apdoer.common.service.model.vo.ResultVo;
import org.apdoer.common.service.util.ResultVoBuildUtils;
import org.apdoer.condition.job.service.WorkJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/future/condition/flush")
public class FlushController {

    @Autowired
    private WorkJobService workJobService;

    /**
     * 内部通道刷新
     */
    @RequestMapping("/systemChannel")
    public ResultVo channelFlush() {
        this.workJobService.flush();
        return ResultVoBuildUtils.buildSuccessResultVo();
    }
}
