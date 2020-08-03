package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.awsiot.ACIAWSLambdaService;
import kr.co.broadwave.aci.collection.iTainerCollection.CollectionTaskInstallCheckDto;
import kr.co.broadwave.aci.collection.iTainerCollection.CollectionTaskInstallService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/position")
public class PositionRestController {

    private final ModelMapper modelMapper;
    private final PositionService positionService;
    private final MasterCodeService masterCodeService;
    private final AccountService accountService;
    private final CollectionTaskInstallService collectionTaskInstallService;
    private final DashboardService dashboardService;
    private final ACIAWSLambdaService aciawsLambdaService;
    @Autowired
    public PositionRestController(ModelMapper modelMapper,
                                  PositionService positionService,
                                  MasterCodeService masterCodeService,
                                  AccountService accountService,
                                  CollectionTaskInstallService collectionTaskInstallService,
                                  DashboardService dashboardService,
                                  ACIAWSLambdaService aciawsLambdaService) {
        this.modelMapper = modelMapper;
        this.positionService = positionService;
        this.masterCodeService = masterCodeService;
        this.accountService = accountService;
        this.collectionTaskInstallService = collectionTaskInstallService;
        this.dashboardService = dashboardService;
        this.aciawsLambdaService = aciawsLambdaService;
    }

    // 장비 저장
    @PostMapping ("reg")
    public ResponseEntity<Map<String,Object>> positionReg(@ModelAttribute PositionMapperDto positionMapperDto, HttpServletRequest request) {

        AjaxResponse res = new AjaxResponse();

        Position position = modelMapper.map(positionMapperDto, Position.class);
        position.setDeviceid(null);
        position.setInstalldate(null);

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        // 로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        // 국가/지역 가져오기
        Optional<MasterCode> optionalPsCountry = masterCodeService.findById(positionMapperDto.getPsCountry());
        Optional<MasterCode> optionalPsLocation = masterCodeService.findById(positionMapperDto.getPsLocation());

        // 국가/지역코드가 존재하지않으면
        if (!optionalPsCountry.isPresent() || !optionalPsLocation.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E032.getCode(),ResponseErrorCode.E032.getDesc()));
        }else{
            // 국가/지역 저장
            position.setPsCountry(optionalPsCountry.get());
            position.setPsLocation(optionalPsLocation.get());
        }

        // 장비번호 가져오기(고유값)
        Optional<Position> optionalPosition = positionService.findByPsBaseCode(position.getPsBaseCode());
        //신규 및 수정여부
        if (optionalPosition.isPresent()) {
            //수정
            position.setId(optionalPosition.get().getId());
            position.setInsert_id(optionalPosition.get().getInsert_id());
            position.setInsertDateTime(optionalPosition.get().getInsertDateTime());
            position.setModify_id(currentuserid);
            position.setModifyDateTime(LocalDateTime.now());
        }else{
            //신규
            position.setInsert_id(currentuserid);
            position.setInsertDateTime(LocalDateTime.now());
            position.setModify_id(currentuserid);
            position.setModifyDateTime(LocalDateTime.now());
        }

        positionService.save(position);

        return ResponseEntity.ok(res.success());
    }

    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> positionList(@RequestParam (value="psBaseCode", defaultValue="") String psBaseCode,
                                                       @RequestParam (value="psBaseName", defaultValue="")String psBaseName,
                                                       @RequestParam (value="psCountry", defaultValue="")String psCountry,
                                                       @RequestParam (value="psLocation", defaultValue="")String psLocation,
                                                       @PageableDefault Pageable pageable){
        Long psCountryId = null;
        Long psLocationId = null;

        if(!psCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(psCountry);
            psCountryId = emCountrys.map(MasterCode::getId).orElse(null);
        }
        if(!psLocation.equals("")){
            Optional<MasterCode> emLocations = masterCodeService.findByCode(psLocation);
            psLocationId = emLocations.map(MasterCode::getId).orElse(null);
        }

        Page<PositionListDto> positionListDtos = positionService.findByPositionSearch(psBaseCode,psBaseName,psLocationId,psCountryId,pageable);

        return CommonUtils.ResponseEntityPage(positionListDtos);
    }

    // 거점 정보 보기
    @PostMapping ("info")
    public ResponseEntity<Map<String,Object>> positionInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("받아온 아이디값 : "+id);
        PositionDto positionDto = positionService.findByPositionInfo(id);
//        log.info("positionDto : "+positionDto);

        data.put("positionDto",positionDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 거점 삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> del(@RequestParam(value="psBaseCode", defaultValue="") String psBaseCode){
        AjaxResponse res = new AjaxResponse();

        // 삭제할수있는 거점인지 확인
        List<CollectionTaskInstallCheckDto> psBaseCodeCheck = collectionTaskInstallService.findByPsBaseCodeCheck(psBaseCode);
//        log.info("psBaseCodeCheck : " + psBaseCodeCheck);

        if(!psBaseCodeCheck.isEmpty()){
//            log.info("삭제불가");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E035.getCode(), ResponseErrorCode.E035.getDesc()));
        }else{
//            log.info("삭제가능");
            Optional<Position> optionalPosition = positionService.findByPsBaseCode(psBaseCode);
            if (!optionalPosition.isPresent()){
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
            }
            positionService.delete(optionalPosition.get());
        }

        return ResponseEntity.ok(res.success());
    }

    // 업무지시 거점리스트 가져오기
    @PostMapping("popuplist")
        public ResponseEntity<Map<String,Object>> popuplist(@RequestParam (value="psCountry", defaultValue="") String psCountry,
                                                               @RequestParam (value="psLocation", defaultValue="")String psLocation,
                                                               @RequestParam (value="deviceid", defaultValue="")String deviceid,
                                                               @RequestParam (value="division", defaultValue="")String division,
                                                               @PageableDefault Pageable pageable){

            AjaxResponse res = new AjaxResponse();
            HashMap<String, Object> data = new HashMap<>();

            Long psCountryId = null;
            Long psLocationId = null;

            if(!psCountry.equals("")){
                Optional<MasterCode> emCountrys = masterCodeService.findByCode(psCountry);
                psCountryId = emCountrys.map(MasterCode::getId).orElse(null);
            }
            if(!psLocation.equals("")){
                Optional<MasterCode> emLocations = masterCodeService.findByCode(psLocation);
                psLocationId = emLocations.map(MasterCode::getId).orElse(null);
            }

            Page<PositionPopListDto> positionListDtos = positionService.findByPositionPopSearch(psCountryId,psLocationId,deviceid,division,pageable);
            log.info("positionListDtos : "+positionListDtos.getContent());

            if(positionListDtos.getTotalElements()> 0 ){

                List<String> emNumbers = new ArrayList<>();
                HashMap<String,List<String>> deviceids = new HashMap<>();
                List<String> actuatorLevels = new ArrayList<>();
                List<String> disWeight = new ArrayList<>();
                List<Object> predictionList = new ArrayList<>();
                for (PositionPopListDto positionListDto : positionListDtos) {
                    if(positionListDto.getDeviceid() != null) {
                        emNumbers.add('"' + positionListDto.getDeviceid() + '"');
                        deviceids.put('"' + "deviceids" + '"', emNumbers);
                        String aswDeviceids = deviceids.toString().replace("=", ":").replace(" ", "");
                        HashMap<String, ArrayList> resData = dashboardService.getDeviceLastestState(aswDeviceids);
    //                    log.info("resData : "+resData);
                        emNumbers.remove(0);

                        if (resData.get("data").size() == 0) {
                            actuatorLevels.add("");
                            disWeight.add("");
                            predictionList.add("");
                        } else {
                            HashMap map = (HashMap) resData.get("data").get(0);
                            actuatorLevels.add((String) map.get("actuator_level"));
                            disWeight.add((String) map.get("dis_info_weight"));

                            JSONObject params = new JSONObject();
                            params.put("curlevel",map.get("actuator_level"));

                            HashMap<String, Object> prediction = aciawsLambdaService.getDeviceLevelPrediction(params);
                            Object map2 = prediction.get("data");
                            predictionList.add(map2);
                        }
                    }else{
                        actuatorLevels.add("");
                        disWeight.add("");
                        predictionList.add("");
                    }
                }

                data.put("actuatorLevels",actuatorLevels);
                data.put("disWeight",disWeight);
                data.put("predictionList",predictionList);

                data.put("datalist",positionListDtos.getContent());
                data.put("total_page",positionListDtos.getTotalPages());
                data.put("current_page",positionListDtos.getNumber() + 1);
                data.put("total_rows",positionListDtos.getTotalElements());
                data.put("current_rows",positionListDtos.getNumberOfElements());
                res.addResponse("data",data);
            }else{
                data.put("total_page",positionListDtos.getTotalPages());
                data.put("current_page",positionListDtos.getNumber() + 1);
                data.put("total_rows",positionListDtos.getTotalElements());
                data.put("current_rows",positionListDtos.getNumberOfElements());
                res.addResponse("data",data);
            }

            return ResponseEntity.ok(res.success());
        }

    // 거점코드 가져오기
    @PostMapping ("popupinfo")
    public ResponseEntity<Map<String,Object>> popupinfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("받아온 아이디값 : "+id);
        PositionPopDto positionPopDto = positionService.findByPositionPopInfo(id);
//        log.info("positionPopDto : "+positionPopDto);

        data.put("positionPopDto",positionPopDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

}
