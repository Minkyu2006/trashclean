package kr.co.broadwave.aci.controller;

import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.dashboard.DashboardDeviceListViewDto;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.devicestats.errweight.ErrweightMapperDto;
import kr.co.broadwave.aci.devicestats.errweight.ErrweightService;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Minkyu
 * Date : 2019-01-06
 * Remark :
 */
@Slf4j
@Controller
@RequestMapping("/devicestats")
public class DeviceStatsController {

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private final MasterCodeService masterCodeService;
    private final ACIAWSLambdaService aciawsLambdaService;
    private final DashboardService dashboardService;
    private final ErrweightService errweightService;

    @Autowired
    public DeviceStatsController(MasterCodeService masterCodeService,
                                 DashboardService dashboardService,
                                 ACIAWSLambdaService aciawsLambdaService,
                                 ErrweightService errweightService) {
        this.masterCodeService = masterCodeService;
        this.dashboardService = dashboardService;
        this.aciawsLambdaService = aciawsLambdaService;
        this.errweightService = errweightService;
    }

    //장비등록
    @RequestMapping("equipreg")
    public String equipreg(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);
        List<MasterCodeDto> modelTypes = masterCodeService.findCodeList(CodeType.C0009);
        List<MasterCodeDto> vLanguages = masterCodeService.findCodeList(CodeType.C0017);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);
        model.addAttribute("modelTypes", modelTypes);
        model.addAttribute("vLanguages", vLanguages);

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
    public String deviceinfo(Model model,@PathVariable String emNumber,
                             @RequestParam(value="efVer", defaultValue="") String efVer,
                             @RequestParam(value="filePath", defaultValue="") String filePath){
        // Json String emNumber 만들기
        String stateEmNumber = '"'+emNumber+'"';
        String emType = emNumber.substring(0,4);
//        log.info("emType : "+emType);

        List<String> emNumbers = new ArrayList<>();
        emNumbers.add(stateEmNumber);
        HashMap<String,List<String>> deviceids = new HashMap<>();
        deviceids.put('"'+"deviceids"+'"',emNumbers);
        String aswDeviceid = deviceids.toString().replace("=",":").replace(" ","");

        HashMap<String, HashMap<String,String>> deviceInfoMap = aciawsLambdaService.getDeviceInfo(emNumber);
        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(aswDeviceid);
        DashboardDeviceListViewDto deviceInfoListDtos = dashboardService.findByDashboardListViewDeviceInfo(emNumber);
        HashMap<String,HashMap<String,Object>> onOfflineData = aciawsLambdaService.getDeviceonlineCheck(emNumber);
//        deviceOnOffstatus.add(Boolean.parseBoolean(String.valueOf((onOfflineData.get("data").get("online")))));
//        deviceOnOffTime.add(onOfflineData.get("data").get("timestamp"));

        HashMap<String,String> deviceInfo = deviceInfoMap.get("data");
        HashMap<String,Object> onOffline = onOfflineData.get("data");

        boolean onoffline = Boolean.parseBoolean(String.valueOf(onOffline.get("online")));

        HashMap awsData = null;
        if(resData.get("data").size()!=0) {
            awsData = (HashMap) resData.get("data").get(0);

//            System.out.println("awsDatax/데이터 존재 : "+awsData);

            model.addAttribute("emType",emType);

            // 장비상태정보(정상,주의,심각)
            model.addAttribute("status","equipment__stat "+awsData.get("status"));
            model.addAttribute("stausName",awsData.get("status"));
            model.addAttribute("devcieInstalDateBtw", deviceInfoListDtos.getEmInstallDate());

            // 장비상태정보
            if(!emType.equals("ITAI")) {
                model.addAttribute("temp_brd",awsData.get("temp_brd")+"℃");
                model.addAttribute("level",awsData.get("level")+"%");
                if(awsData.get("batt_voltage").equals("na")){
                    model.addAttribute("batt_voltage", awsData.get("batt_voltage"));
                }else{
                    model.addAttribute("batt_voltage",awsData.get("batt_voltage") + "V");
                }
                model.addAttribute("solar_current", awsData.get("solar_current") + "A");
                model.addAttribute("solar_voltage", awsData.get("solar_voltage") + "V");
            }else{
                model.addAttribute("temp_brd",awsData.get("s_tmp2")+"℃");
                model.addAttribute("level",awsData.get("actuator_level")+"%");
                model.addAttribute("batt_voltage",awsData.get("dis_info_level")+"%");
                model.addAttribute("solar_current",awsData.get("dis_info_weight")+"g");

                String language = (String)awsData.get("language");
                if(language.equals("ko")){
                    model.addAttribute("solar_voltage","한국어");
                }else if(language.equals("en")){
                    model.addAttribute("solar_voltage","영어");
                }else{
                    model.addAttribute("solar_voltage","중국어");
                }

            }

            if(awsData.get("gps_la").equals("na") || awsData.get("gps_lo").equals("") || awsData.get("gps_la").equals("na") || awsData.get("gps_lo").equals("")) {
                model.addAttribute("gps_la", "GPS정보 없음");
                model.addAttribute("gps_lo", "GPS정보 없음");
            }else{
                model.addAttribute("gps_la", awsData.get("gps_la"));
                model.addAttribute("gps_lo", awsData.get("gps_lo"));
            }

            if(onoffline){
                model.addAttribute("offlineName","온라인");
                model.addAttribute("onoffline","equipment__connect on");

                if(!emType.equals("ITAI")) {
                    if (!awsData.get("rsrp").equals("")) {
                        int rsrp = Integer.parseInt(String.valueOf(awsData.get("rsrp")));

                        if (rsrp < -120) {
                            model.addAttribute("rsrp", "/assets/images/icon__wifi-0.png");
                        } else if (rsrp < -115) {
                            model.addAttribute("rsrp", "/assets/images/icon__wifi-1.png");
                        } else if (rsrp < -109) {
                            model.addAttribute("rsrp", "/assets/images/icon__wifi-2.png");
                        } else if (rsrp < -103) {
                            model.addAttribute("rsrp", "/assets/images/icon__wifi-3.png");
                        } else {
                            model.addAttribute("rsrp", "/assets/images/icon__wifi-4.png");
                        }
                    } else {
                        model.addAttribute("rsrp", "/assets/images/icon__wifi-off.png");
                    }
                }
            }else{
                model.addAttribute("offlineName","오프라인");
                model.addAttribute("onoffline","equipment__connect off");
                model.addAttribute("offlineTime",onOffline.get("timestamp"));
                if(!emType.equals("ITAI")) {
                    model.addAttribute("rsrp", "/assets/images/icon__wifi-off.png");
                }
            }
        }else {
            // 장비상태정보없음
            model.addAttribute("status","equipment__stat unknowon");
            model.addAttribute("stausName","unknowon");

            model.addAttribute("offlineName","알수없음");
            model.addAttribute("onoffline","equipment__connect unknowon");

            if(!emType.equals("ITAI")) {
                model.addAttribute("rsrp", "/assets/images/icon__wifi-off.png");
            }
//            System.out.println("awsData데이터 존재하지 않음");
        }

//        System.out.println("emNumber : "+emNumber);
//        System.out.println("deviceInfoListDtos : "+deviceInfoListDtos);
//        System.out.println("deviceInfo : "+deviceInfo);
//        System.out.println("onOffline : "+onOffline);

        model.addAttribute("header_deviceid",deviceInfo.get("deviceid"));

        // 장비이미지전송
        if(!deviceInfoListDtos.getFilePath().equals("/defaultimage")) {
            model.addAttribute("filePath", AWSS3URL + deviceInfoListDtos.getFilePath());
            model.addAttribute("saveFileName", "/s_"+deviceInfoListDtos.getSaveFileName());
        }else{
            model.addAttribute("filePath", AWSS3URL + deviceInfoListDtos.getFilePath());
            model.addAttribute("saveFileName", deviceInfoListDtos.getSaveFileName());
        }

        // 장비정보
        model.addAttribute("emtype", deviceInfoListDtos.getEmType());
        model.addAttribute("mdName", deviceInfoListDtos.getMdName());
        model.addAttribute("company", deviceInfoListDtos.getCompany());
        model.addAttribute("emCountryLoaction", deviceInfoListDtos.getEmCountry()+"/"+deviceInfoListDtos.getEmLocation());
        model.addAttribute("instalDate", deviceInfoListDtos.getEmInstallDate().substring(0,4)+"년 "+deviceInfoListDtos.getEmInstallDate().substring(4,6)+"월 "+deviceInfoListDtos.getEmInstallDate().substring(6,8)+"일");

        // 장비상세정보
        if(!deviceInfo.get("timestamp").equals("")) {
            model.addAttribute("updateTime", deviceInfo.get("timestamp"));
        }
        if(!deviceInfo.get("firmware").equals("")) {
            model.addAttribute("firmware", "v" + deviceInfo.get("firmware"));
        }
        if(!deviceInfo.get("serialno").equals("")) {
            model.addAttribute("serialno", deviceInfo.get("serialno"));
        }
        if(!deviceInfo.get("carrier").equals("")) {
            model.addAttribute("carrier", deviceInfo.get("carrier"));
        }
        if(!deviceInfo.get("phonenumber").equals("")) {
            model.addAttribute("phonenumber", deviceInfo.get("phonenumber"));
        }
        if(!deviceInfo.get("blemacaddr").equals("")) {
            model.addAttribute("blemacaddr", deviceInfo.get("blemacaddr"));
        }
        if(!deviceInfo.get("loraid").equals("")) {
            model.addAttribute("loraid", deviceInfo.get("loraid"));
        }

        if(efVer!=null &&  filePath!=null){
            model.addAttribute("efVer",efVer);
            model.addAttribute("efFilePath",filePath);
        }

        return "devicestats/deviceinfo";
    }

    @RequestMapping("firmware")
    public String firmware(Model model){
        List<MasterCodeDto> equipdTypes = masterCodeService.findCodeList(CodeType.C0003);
        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdTypes", equipdTypes);
        model.addAttribute("equipdCountrys", equipdCountrys);

        return "devicestats/firmware";
    }

    @RequestMapping("report")
    public String report(Model model){

        List<MasterCodeDto> equipdCountrys = masterCodeService.findCodeList(CodeType.C0004);

        model.addAttribute("equipdCountrys", equipdCountrys);

        return "devicestats/report";
    }

    @RequestMapping("exhaustinfo")
    public String exhaustinfo(){
        return "devicestats/exhaustinfo";
    }

    @RequestMapping("errorweight")
    public String errorweight(Model model){
        ErrweightMapperDto errweightMapperDto;
        errweightMapperDto = errweightService.findById2(Long.parseLong(String.valueOf(1)));
//        log.info("errweightMapperDto : "+errweightMapperDto);
        if(errweightMapperDto!=null){
            model.addAttribute("errweightMapperDto", errweightMapperDto);
        }else{
            model.addAttribute("errweightMapperDto", errweightMapperDto);
        }

        return "devicestats/errorweight";
    }

    @RequestMapping("errorindices")
    public String errorindices(){
        return "devicestats/errorindices";
    }

}
