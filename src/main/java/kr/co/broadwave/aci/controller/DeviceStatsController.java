package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-01-06
 * Remark :
 */

@Controller
@RequestMapping("/devicestats")
public class DeviceStatsController {

    private final MasterCodeService masterCodeService;
    private final ACIAWSLambdaService aciawsLambdaService;
    private final DashboardService dashboardService;
    @Autowired
    public DeviceStatsController(MasterCodeService masterCodeService,
                                 DashboardService dashboardService,
                                 ACIAWSLambdaService aciawsLambdaService) {
        this.masterCodeService = masterCodeService;
        this.dashboardService = dashboardService;
        this.aciawsLambdaService = aciawsLambdaService;
    }

    //장비등록
    @RequestMapping("equipreg")
    public String equipreg(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);
        List<MasterCodeDto> modelTypes = masterCodeService.findCodeList(CodeType.C0009);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);
        model.addAttribute("modelTypes", modelTypes);

        return "devicestats/equipmentreg";
    }

    @RequestMapping("devicecontrol")
    public String devicecontrol(){
        return "devicestats/devicecontrol";
    }

    @RequestMapping("daysdevice")
    public String daysdevice(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "devicestats/daysdevice";
    }

    @RequestMapping("devicebasevalue")
    public String devicebasevalue(Model model){

        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "devicestats/devicebasevalue";
    }

    @RequestMapping("devicesearchinfo")
    public String devicesearchinfo(){
        return "devicestats/devicesearchinfo";
    }

    @RequestMapping("deviceinfo/{emNumber}")
    public String deviceinfo(Model model,@PathVariable String emNumber){

        // Json String emNumber 만들기
        String stateEmNumber = '"'+emNumber+'"';
        List<String> emNumbers = new ArrayList<>();
        emNumbers.add(stateEmNumber);
        HashMap<String,List<String>> deviceids = new HashMap<>();
        deviceids.put('"'+"deviceids"+'"',emNumbers);
        String aswDeviceid = deviceids.toString().replace("=",":").replace(" ","");

        HashMap<String, HashMap<String,String>> deviceInfoMap = aciawsLambdaService.getDeviceInfo(emNumber);
        HashMap<String, HashMap<String,String>> resData = dashboardService.getDeviceLastestState(aswDeviceid);

        System.out.println("emNumber : "+emNumber);
        System.out.println("deviceInfoMap : "+deviceInfoMap);
        System.out.println("resData : "+resData);

        HashMap<String,String> deviceInfo = deviceInfoMap.get("data");
//        HashMap<String,String> deviceAws = resData.get("data");
        System.out.println("deviceInfo : "+deviceInfo);
        //System.out.println("deviceAws : "+deviceAws);

        model.addAttribute("deviceid", deviceInfo.get("deviceid"));
//        model.addAttribute("equipdCountrys", equipdCountrys);
//        model.addAttribute("equipdTypes", equipdTypes);
//        model.addAttribute("equipdCountrys", equipdCountrys);

        return "devicestats/deviceinfo";
    }

}
