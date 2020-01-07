package kr.co.broadwave.aci.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Minkyu
 * Date : 2019-01-06
 * Remark :
 */

@Controller
@RequestMapping("/devicestatus")
public class DeviceStatusController {

    @RequestMapping("daysdevice")
    public String daysdevice(){
        return "devicestatus/daysdevice";
    }

}
