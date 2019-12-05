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
@RequestMapping("/testPage")
public class TestpageController {
    private final MasterCodeService masterCodeService;

    @Autowired
    public TestpageController(MasterCodeService masterCodeService) {
        this.masterCodeService = masterCodeService;
    }

    @RequestMapping("monitering")
    public String monitering(){
        return "testPage/monitering";
    }

    @RequestMapping("maptest")
    public String maptest(){
        return "testPage/maptest";
    }

    @RequestMapping("layout")
    public String layout(){
        return "testPage/layout";
    }

    @RequestMapping("dashboardTestPage")
    public String dashboardTestPage(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> agencys = masterCodeService.findCodeList(CodeType.C0006);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("agencys", agencys);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "testPage/dashboardTestPage";
    }

    @RequestMapping("devicecontroltest")
    public String devicecontroltest(){
        return "testPage/devicecontroltest";
    }

    @RequestMapping("fileupload")
    public String fileupload(){
        return "testPage/fileupload";
    }

}
