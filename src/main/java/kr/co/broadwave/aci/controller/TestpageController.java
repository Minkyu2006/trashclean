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
@RequestMapping("/testpage")
public class TestpageController {
    private final MasterCodeService masterCodeService;

    @Autowired
    public TestpageController(MasterCodeService masterCodeService) {
        this.masterCodeService = masterCodeService;
    }

    @RequestMapping("monitering")
    public String monitering(){
        return "testpage/monitering";
    }

    @RequestMapping("maptest")
    public String maptest(){
        return "testpage/maptest";
    }

    @RequestMapping("layout")
    public String layout(){
        return "testpage/layout";
    }

    @RequestMapping("devicecontroltest")
    public String devicecontroltest(){
        return "testpage/devicecontroltest";
    }

    @RequestMapping("fileupload")
    public String fileupload(){
        return "testpage/fileupload";
    }

}
