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
    //사용자승인
    @RequestMapping("monitering")
    public String accountApproval(){
        return "dashboard/monitering";
    }
}
