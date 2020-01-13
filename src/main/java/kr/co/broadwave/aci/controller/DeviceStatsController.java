package kr.co.broadwave.aci.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2019-01-06
 * Remark :
 */

@Controller
@RequestMapping("/devicestats")
public class DeviceStatsController {

    @RequestMapping("daysdevice")
    public String daysdevice(){
        return "devicestats/daysdevice";
    }

}
