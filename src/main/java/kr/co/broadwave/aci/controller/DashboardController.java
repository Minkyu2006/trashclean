package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author InSeok
 * Date : 2019-10-17
 * Remark :
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    private final MasterCodeService masterCodeService;

    @Autowired
    public DashboardController(MasterCodeService masterCodeService) {
        this.masterCodeService = masterCodeService;
    }

    @RequestMapping("dashboard")
    public String dashboardTestPage(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> agencys = masterCodeService.findCodeList(CodeType.C0006);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("agencys", agencys);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "dashboard/dashboard";
    }

    @RequestMapping("dashboard1")
    public String dashboardTestPage1(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> agencys = masterCodeService.findCodeList(CodeType.C0006);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("agencys", agencys);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "dashboard/dashboard1";
    }

}
