package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.dashboard.DashboardDeviceListViewDto;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

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
    public String deviceinfo(Model model,@PathVariable String emNumber, @PageableDefault Pageable pageable){

        // Json String emNumber 만들기
        String stateEmNumber = '"'+emNumber+'"';
        List<String> emNumbers = new ArrayList<>();
        emNumbers.add(stateEmNumber);
        HashMap<String,List<String>> deviceids = new HashMap<>();
        deviceids.put('"'+"deviceids"+'"',emNumbers);
        String aswDeviceid = deviceids.toString().replace("=",":").replace(" ","");

        HashMap<String, HashMap<String,String>> deviceInfoMap = aciawsLambdaService.getDeviceInfo(emNumber);
        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(aswDeviceid);
        Page<DashboardDeviceListViewDto> deviceInfoListDtos = dashboardService.findByDashboardListView(emNumber, null, null, null, pageable);
        HashMap<String,HashMap<String,Object>> onOfflineData = aciawsLambdaService.getDeviceonlineCheck(emNumber);
//        deviceOnOffstatus.add(Boolean.parseBoolean(String.valueOf((onOfflineData.get("data").get("online")))));
//        deviceOnOffTime.add(onOfflineData.get("data").get("timestamp"));

        HashMap<String,String> deviceInfo = deviceInfoMap.get("data");
        HashMap<String,Object> onOffline = onOfflineData.get("data");
        HashMap awsData = (HashMap)resData.get("data").get(0);

//        System.out.println("emNumber : "+emNumber);
//        System.out.println("deviceInfoListDtos : "+deviceInfoListDtos.getContent());
//        System.out.println("awsData : "+awsData);
//        System.out.println("deviceInfo : "+deviceInfo);
//        System.out.println("onOffline : "+onOffline);

        String stausName = null;
        if(awsData.get("status")=="normal"){
            stausName="정상";
        }else if(awsData.get("status")=="caution"){
            stausName="주의";
        }else{
            stausName="심각";
        }

        boolean onoffline = Boolean.parseBoolean(String.valueOf(onOfflineData.get("online")));
        //System.out.println("onoffline : "+onoffline);
        if(onoffline){
            model.addAttribute("offlineName","온라인");
            model.addAttribute("onoffline","equipment__connect on");
        }else{
            model.addAttribute("offlineName","오프라인");
            model.addAttribute("onoffline","equipment__connect off");
            model.addAttribute("offlineTime",onOffline.get("timestamp"));
        }

        model.addAttribute("header_deviceid","장비코드 : "+deviceInfo.get("deviceid"));

        // 장비온오프라인정보
        model.addAttribute("status","equipment__stat "+awsData.get("status"));
        model.addAttribute("stausName",stausName);

        // 장비이미지전송
        model.addAttribute("AWSS3URL", AWSS3URL);
        model.addAttribute("filePath", deviceInfoListDtos.getContent().get(0).getFilePath());
        model.addAttribute("saveFileName", deviceInfoListDtos.getContent().get(0).getSaveFileName());

        // 장비정보
        model.addAttribute("deviceid",deviceInfo.get("deviceid"));
        model.addAttribute("emtype", deviceInfoListDtos.getContent().get(0).getEmType());
        model.addAttribute("mdName", deviceInfoListDtos.getContent().get(0).getMdName());
        model.addAttribute("mdMaxUnit", deviceInfoListDtos.getContent().get(0).getMdMaximumPayload()+" "+deviceInfoListDtos.getContent().get(0).getMdUnit());
        model.addAttribute("company", deviceInfoListDtos.getContent().get(0).getCompany());
        model.addAttribute("emCountryLoaction", deviceInfoListDtos.getContent().get(0).getEmCountry()+"/"+deviceInfoListDtos.getContent().get(0).getEmLocation());
        model.addAttribute("instalDate", deviceInfoListDtos.getContent().get(0).getEmInstallDate().substring(0,4)+"년 "+deviceInfoListDtos.getContent().get(0).getEmInstallDate().substring(4,6)+"월 "+deviceInfoListDtos.getContent().get(0).getEmInstallDate().substring(6,8)+"일");

        // 장비상태정보
        model.addAttribute("devcieInstalDateBtw", deviceInfoListDtos.getContent().get(0).getEmInstallDate());
        model.addAttribute("temp_brd",awsData.get("temp_brd")+"℃");
        model.addAttribute("level",awsData.get("level")+"%");
        model.addAttribute("batt_level",awsData.get("batt_level")+"%");
        model.addAttribute("solar_current",awsData.get("solar_current")+"A");
        model.addAttribute("solar_voltage",awsData.get("solar_voltage")+"V");

        // 장비상세정보
        model.addAttribute("updateTime",deviceInfo.get("timestamp"));
        model.addAttribute("firmware",deviceInfo.get("firmware"));
        model.addAttribute("serialno",deviceInfo.get("serialno"));
        model.addAttribute("carrier",deviceInfo.get("carrier"));
        model.addAttribute("phonenumber",deviceInfo.get("phonenumber"));
        model.addAttribute("blemacaddr",deviceInfo.get("blemacaddr"));
        model.addAttribute("loraid",deviceInfo.get("loraid"));

        return "devicestats/deviceinfo";
    }

}
