package kr.co.broadwave.aci.devicestats;

import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.dashboard.DashboardDeviceListViewDto;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.equipment.Equipment;
import kr.co.broadwave.aci.equipment.EquipmentEmnumberDto;
import kr.co.broadwave.aci.equipment.EquipmentService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2020-01-14
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/deviestats")
public class DeviestatsRestController {

    private final ModelMapper modelMapper;
    private final MasterCodeService masterCodeService;
    private final EquipmentService equipmentService;
    private final DevicestatusService devicestatusService;
    private final DashboardService dashboardService;
    @Autowired
    public DeviestatsRestController(ModelMapper modelMapper,
                                    DashboardService dashboardService,
                                    EquipmentService equipmentService,
                                    MasterCodeService masterCodeService,
                                    DevicestatusService devicestatusService) {
        this.modelMapper = modelMapper;
        this.dashboardService = dashboardService;
        this.equipmentService = equipmentService;
        this.masterCodeService = masterCodeService;
        this.devicestatusService = devicestatusService;
    }

    // 카운트리스트 조회
    @PostMapping ("countListView")
    public ResponseEntity countInfoView(@RequestParam(value="emNumber", defaultValue="") String emNumber,
                                        @RequestParam (value="monthDate", defaultValue="") String monthDate,
                                        @RequestParam (value="emType", defaultValue="")String emType,
                                        @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                        @RequestParam (value="emLocation", defaultValue="")String emLocation){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("emNumber : "+emNumber);
//        log.info("monthDate : "+monthDate);
//        log.info("emType : "+emType);
//        log.info("emCountry : "+emCountry);
//        log.info("emLocation : "+emLocation);
        log.info("받은날짜 monthDate : "+monthDate);
        int sYear = Integer.parseInt(monthDate.substring(0,4));
        int sDay = Integer.parseInt(monthDate.substring(5,7));
        String sMonth = monthDate.substring(0,7).replace("-","");
        log.info("검색한 sMonth : "+sMonth);

        Long emTypeId = null;
        Long emCountryId = null;
        Long emLocationId = null;

        if(!emType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
            emTypeId = emTypes.get().getId();
        }
        if(!emCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(emCountry);
            emCountryId = emCountrys.get().getId();
        }
        if(!emLocation.equals("")){
            Optional<MasterCode> emLocations = masterCodeService.findByCode(emLocation);
            emLocationId = emLocations.get().getId();
        }

        List<EquipmentEmnumberDto> equipmentemNumber =
                equipmentService.queryDslDeviceEmNumber(emNumber, emTypeId, emCountryId, emLocationId);
        //log.info("equipmentemNumber : "+equipmentemNumber);

        List<String> deviceid = new ArrayList<>(); // 장비아이디 리스트
        for(int i=0; i<equipmentemNumber.size(); i++){
            deviceid.add(equipmentemNumber.get(i).getEmNumber());
        }
        //log.info("조회할 장비아이디 : "+deviceid);

        List<DevicestatsDailyDto> devicestatsDailyDtos = devicestatusService.queryDslDeviceDaily(deviceid,sMonth);
        //log.info("devicestatsDailyDtos : "+devicestatsDailyDtos);

        // 조회한 월의 일수구하기
        int year = sYear;
        int month = sDay-1;
        int day = 1;
        Calendar days = new GregorianCalendar(year, month, day);
        int daysOfMonth = days.getActualMaximum(Calendar.DAY_OF_MONTH);
        //log.info(year+"년 " + (month+1)+"월의 일수: " +daysOfMonth);

        // 조회한 월의 yyyymmdd 구하고, 데이터를 리스트에 담기
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");
        //Date time = new Date();
        String yyyymmdd = null;

        List<Double> actuaterCnt = new ArrayList<>(); // 엑추에이터 작동횟수 -> (합계)
        List<Double> inputdoorjammingCnt = new ArrayList<>(); //투일구걸림횟수 -> (합계)
        List<Double> frontdoorsolopenCnt = new ArrayList<>(); // 솔레노이드센서 열림 횟수 -> (합계)
        List<Double> emitCnt = new ArrayList<>(); // 일일장비 투입횟수 -> (합계)
        List<Double> fullLevel = new ArrayList<>(); // 해당일자의 쓰레기양 -> (평균)

        int d=0;
        for(int j=0; j<daysOfMonth; j++) {
            cal.set(sYear,sDay-1,day);
            cal.add(Calendar.DATE,j);
            yyyymmdd = format.format(cal.getTime());
            //log.info("검색할 날짜 : "+yyyymmdd);
            if(d<devicestatsDailyDtos.size()) {
                if (yyyymmdd.equals(devicestatsDailyDtos.get(d).getYyyymmdd())) {
                    //log.info("검색 날짜(리스트에 데이터넣기) : "+devicestatsDailyDtos.get(d).getYyyymmdd());
                    actuaterCnt.add(devicestatsDailyDtos.get(d).getActuaterCnt());
                    inputdoorjammingCnt.add(devicestatsDailyDtos.get(d).getInputdoorjammingCnt());
                    frontdoorsolopenCnt.add(devicestatsDailyDtos.get(d).getFrontdoorsolopenCnt());
                    emitCnt.add(devicestatsDailyDtos.get(d).getEmitCnt());
                    fullLevel.add((double) Math.round(devicestatsDailyDtos.get(d).getFullLevel() * 10 / 10.0));
                    d++;
                }else {
                    actuaterCnt.add(0.0);
                    inputdoorjammingCnt.add(0.0);
                    frontdoorsolopenCnt.add(0.0);
                    emitCnt.add(0.0);
                    fullLevel.add(0.0);
                }
            }else {
                actuaterCnt.add(0.0);
                inputdoorjammingCnt.add(0.0);
                frontdoorsolopenCnt.add(0.0);
                emitCnt.add(0.0);
                fullLevel.add(0.0);
            }
        }

//        log.info("actuaterCnt : "+actuaterCnt);
//        log.info("inputdoorjammingCnt : "+inputdoorjammingCnt);
//        log.info("frontdoorsolopenCnt : "+frontdoorsolopenCnt);
//        log.info("emitCnt : "+emitCnt);
//        log.info("fullLevel : "+fullLevel);

        data.clear();
        data.put("sYear",sYear);
        data.put("sDay",sDay);
        data.put("actuaterCnt",actuaterCnt);
        data.put("inputdoorjammingCnt",inputdoorjammingCnt);
        data.put("frontdoorsolopenCnt",frontdoorsolopenCnt);
        data.put("emitCnt",emitCnt);
        data.put("fullLevel",fullLevel);
        data.put("deviceid",deviceid);

        res.addResponse("data",data);
        return ResponseEntity.ok(res.success());
    }

    // 조회할 시간그래프 그리기
    @PostMapping ("hourInfoGraph")
    public ResponseEntity hourInfoGraph(@RequestParam(value="sendDate", defaultValue="") String sendDate,
                                        @RequestParam(value="deviceid", defaultValue="") List<String> deviceid) {
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        List<DevicestatsDailyHourLevelDto> devicestatsDailyHourLevelDtos =
                devicestatusService.queryDslDeviceDailyHourLevel(deviceid,sendDate);
        log.info("받은 날짜 : "+sendDate);
        log.info("받은 장치아이디 : "+deviceid);
        log.info("devicestatsDailyHourLevelDtos : "+devicestatsDailyHourLevelDtos);

        List<String> xhour = new ArrayList<>(); // 24시간 넣는 리스트

        List<Double> hourFullLevel = new ArrayList<>(); // 평균배출량(시간) -> (평균)

        List<Double> hourEmitCnt = new ArrayList<>(); // 일일장비 투입횟수(시간) -> (합계)
        List<Double> hourActuaterCnt = new ArrayList<>(); // 엑추에이터 작동횟수(시간) -> (합계)
        List<Double> hourInputdoorjammingCnt = new ArrayList<>(); //투일구걸림횟수(시간) -> (합계)
        List<Double> hourFrontdoorsolopenCnt = new ArrayList<>(); // 솔레노이드센서 열림 횟수(시간) -> (합계)


        String yyyy = sendDate.substring(0, 4);
        String mm = sendDate.substring(4, 6);
        String dd = sendDate.substring(6, 8);

        int d=0;
        for(int i=0; i<24; i++) {

            if (i < 10) {
                xhour.add(yyyy + '년' + mm + '월' + dd + '일' + '0' + i + '시');
            } else {
                xhour.add(yyyy + '년' + mm + '월' + dd + '일' + i + '시');
            }

            if (d < devicestatsDailyHourLevelDtos.size()) {
                if (i == (Integer.parseInt(devicestatsDailyHourLevelDtos.get(d).getHh()))) {
                    hourEmitCnt.add(devicestatsDailyHourLevelDtos.get(d).getEmitCnt());
                    hourActuaterCnt.add(devicestatsDailyHourLevelDtos.get(d).getActuaterCnt());
                    hourInputdoorjammingCnt.add(devicestatsDailyHourLevelDtos.get(d).getInputdoorjammingCnt());
                    hourFrontdoorsolopenCnt.add(devicestatsDailyHourLevelDtos.get(d).getFrontdoorsolopenCnt());
                    hourFullLevel.add((double) Math.round(devicestatsDailyHourLevelDtos.get(d).getFullLevel() * 10 / 10.0));
                    d++;
                } else {
                    hourEmitCnt.add(0.0);
                    hourActuaterCnt.add(0.0);
                    hourInputdoorjammingCnt.add(0.0);
                    hourFrontdoorsolopenCnt.add(0.0);
                    hourFullLevel.add(0.0);
                }
            } else {
                hourEmitCnt.add(0.0);
                hourActuaterCnt.add(0.0);
                hourInputdoorjammingCnt.add(0.0);
                hourFrontdoorsolopenCnt.add(0.0);
                hourFullLevel.add(0.0);
            }
        }

        List<HashMap<String,Object>> heatMaphourEmitCnt = new ArrayList<>(); // 히트맵에 들어갈 데이터 투입횟수(시간)
        List<HashMap<String,Object>> heatMaphourActuaterCnt = new ArrayList<>(); // 히트맵에 들어갈 데이터 엑추에이터 작동횟수(시간)
        List<HashMap<String,Object>> heatMaphourInputdoorjammingCnt = new ArrayList<>(); // 히트맵에 들어갈 데이터 투일구걸림횟수(시간)
        List<HashMap<String,Object>> heatMaphourFrontdoorsolopenCnt = new ArrayList<>(); // 히트맵에 들어갈 데이터 솔레노이드센서 열림 횟수(시간)
        HashMap<String,Object> emitmapData;
        HashMap<String,Object> actuaterData;
        HashMap<String,Object> inputdoorjammingData;
        HashMap<String,Object> frontdoorsolopenData;

        for(int i=0; i<24; i++){
            emitmapData = new HashMap<>();
            actuaterData = new HashMap<>();
            inputdoorjammingData = new HashMap<>();
            frontdoorsolopenData = new HashMap<>();

            String strxhour = xhour.get(i);
            String strxhour2 = xhour.get(i).substring(11,13)+":00";
            Double emitCnt = hourEmitCnt.get(i);
            Double actuaterCnt = hourActuaterCnt.get(i);
            Double inputdoorjammingCnt = hourInputdoorjammingCnt.get(i);
            Double frontdoorsolopenCnt = hourFrontdoorsolopenCnt.get(i);

            emitmapData.put("d",strxhour);
            emitmapData.put("x",strxhour2);
            emitmapData.put("y","투입횟수");
            emitmapData.put("v",emitCnt);

            actuaterData.put("d",strxhour);
            actuaterData.put("x",strxhour2);
            actuaterData.put("y","모터작동횟수");
            actuaterData.put("v",actuaterCnt);

            inputdoorjammingData.put("d",strxhour);
            inputdoorjammingData.put("x",strxhour2);
            inputdoorjammingData.put("y","투입구걸림횟수");
            inputdoorjammingData.put("v",inputdoorjammingCnt);

            frontdoorsolopenData.put("d",strxhour);
            frontdoorsolopenData.put("x",strxhour2);
            frontdoorsolopenData.put("y","문열림횟수");
            frontdoorsolopenData.put("v",frontdoorsolopenCnt);

            heatMaphourEmitCnt.add(emitmapData);
            heatMaphourActuaterCnt.add(actuaterData);
            heatMaphourInputdoorjammingCnt.add(inputdoorjammingData);
            heatMaphourFrontdoorsolopenCnt.add(frontdoorsolopenData);

        }

//        log.info("총 시간(x축) : "+xhour);
//        log.info("꺾은선그래프 데이터 : "+hourFullLevel);
//        log.info("히트맵그래프 데이터 투입횟수 : "+heatMaphourEmitCnt);
//        log.info("히트맵그래프 데이터 모터작동횟수 : "+heatMaphourActuaterCnt);
//        log.info("히트맵그래프 데이터 투입구걸림횟수 : "+heatMaphourInputdoorjammingCnt);
//        log.info("히트맵그래프 데이터 문열림횟수 : "+heatMaphourFrontdoorsolopenCnt);

        data.clear();
        data.put("xhour",xhour);
        data.put("hourFullLevel",hourFullLevel);
        data.put("heatMaphourEmitCnt",heatMaphourEmitCnt);
        data.put("heatMaphourActuaterCnt",heatMaphourActuaterCnt);
        data.put("heatMaphourInputdoorjammingCnt",heatMaphourInputdoorjammingCnt);
        data.put("heatMaphourFrontdoorsolopenCnt",heatMaphourFrontdoorsolopenCnt);

        res.addResponse("data", data);
        return ResponseEntity.ok(res.success());
    }

}
