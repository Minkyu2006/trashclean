package kr.co.broadwave.aci.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author InSeok
 * Date : 2019-10-17
 * Remark :
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @RequestMapping("monitering")
    public String monitering(){
        return "dashboard/monitering";
    }

    @RequestMapping("graph")
    public String graph(){
        return "dashboard/graph";
    }

    @RequestMapping("layout")
    public String layout(){
        return "dashboard/layout";
    }

    @RequestMapping("dashboardTestPage")
    public String dashboardTestPage(){
        return "dashboard/dashboardTestPage";
    }

}
