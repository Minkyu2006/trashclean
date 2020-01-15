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
        log.info("devicestatsDailyDtos : "+devicestatsDailyDtos);

        // 조회한 월의 일수구하기
        int year = sYear;
        int month = sDay-1;
        int day = 1;
        Calendar days = new GregorianCalendar(year, month, day);
        int daysOfMonth = days.getActualMaximum(Calendar.DAY_OF_MONTH);
        log.info(year+"년 " + (month+1)+"월의 일수: " +daysOfMonth);

        // 조회한 월의 yyyymmdd 구하고, 데이터를 리스트에 담기
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat ( "yyyyMMdd");
        //Date time = new Date();
        String yyyymmdd = null;

        List<Double> actuaterCnt = new ArrayList<>(); // 엑추에이터 작동횟수 -> (합계)
        List<Double> inputdoorjammingCnt = new ArrayList<>(); //투일구걸림횟수 -> (합계)
        List<Double> frontdoorsolopenCnt = new ArrayList<>(); // 솔레노이드센서 열림 횟수 -> (합계)
        List<Double> emitCnt = new ArrayList<>(); // 일일장비 배출횟수 -> (합계)
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

}
