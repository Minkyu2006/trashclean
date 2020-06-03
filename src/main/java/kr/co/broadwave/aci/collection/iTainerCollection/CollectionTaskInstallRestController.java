package kr.co.broadwave.aci.collection.iTainerCollection;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.bscodes.AccordiType;
import kr.co.broadwave.aci.bscodes.CiStatusType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.keygenerate.KeyGenerateService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import kr.co.broadwave.aci.vehicle.Vehicle;
import kr.co.broadwave.aci.vehicle.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Minkyu
 * Date : 2020-06-01
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/collection/iTainerCollection")
public class CollectionTaskInstallRestController {

    private final AccountService accountService;
    private final MasterCodeService masterCodeService;
    private final VehicleService vehicleService;
    private final CollectionTaskInstallService collectionTaskInstallService;
    private final KeyGenerateService keyGenerateService;

    @Autowired
    public CollectionTaskInstallRestController(CollectionTaskInstallService collectionTaskInstallService,
                                               VehicleService vehicleService,
                                               AccountService accountService,
                                               MasterCodeService masterCodeService,
                                               KeyGenerateService keyGenerateService) {
        this.accountService = accountService;
        this.keyGenerateService = keyGenerateService;
        this.collectionTaskInstallService = collectionTaskInstallService;
        this.masterCodeService = masterCodeService;
        this.vehicleService = vehicleService;
    }

    // iSolarbin 수거업무 저장
    @PostMapping("reg")
    public ResponseEntity<Map<String,Object>> iSolarbinReg(@RequestParam(value="ciCode", defaultValue="") String ciCode,
                                                           @RequestParam(value="ciType", defaultValue="") String ciType,
                                                           @RequestParam(value="ciPriority", defaultValue="") String ciPriority,
                                                           @RequestParam(value="psZoneCode", defaultValue="") String psZoneCode,
                                                           @RequestParam(value="psZoneName", defaultValue="") String psZoneName,
                                                           @RequestParam(value="deviceid", defaultValue="") String deviceid,
                                                           @RequestParam(value="vehicleNumber", defaultValue="") String vehicleNumber,
                                                           @RequestParam(value="accountUserId", defaultValue="") String accountUserId,
                                                           @RequestParam(value="ciRemark", defaultValue="") String ciRemark,
                                                           HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(), ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        // 우선순위값
        Optional<MasterCode> ciPrioritys;
        if (!ciPriority.equals("")) {
            ciPrioritys = masterCodeService.findByCode(ciPriority);
        }else{
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E033.getCode(), ResponseErrorCode.E033.getDesc() + "'" + currentuserid + "'" ));
        }

        //기본값넣기 배차상태 -> 지시
        CiStatusType cst01 = CiStatusType.valueOf("CST01");

        // 유저아이디/배차차량 가져오기
        Optional<Account> optionalUserId = accountService.findByUserid(accountUserId);
        Optional<Vehicle> optionalVehicleId = vehicleService.findByVcNumber(vehicleNumber);
        AccordiType ciTypes;

        if(!ciCode.equals("")) {
            // 수정
            Optional<CollectionTaskInstall> optionalCollectionTaskInstall = collectionTaskInstallService.findByCiCode(ciCode);
            CollectionTaskInstall collectionTaskInstall = new CollectionTaskInstall();
            if (!optionalCollectionTaskInstall.isPresent()) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(), ResponseErrorCode.E004.getDesc() + "'" + currentuserid + "'"));
            }else {
                ciTypes = AccordiType.valueOf(ciType);

                collectionTaskInstall.setId(optionalCollectionTaskInstall.get().getId());

                if (!optionalUserId.isPresent() || !optionalVehicleId.isPresent() || !ciPrioritys.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.E034.getCode(), ResponseErrorCode.E034.getDesc() + "'" + currentuserid + "'"));
                }else{

                    collectionTaskInstall.setCiCode(ciCode);
                    collectionTaskInstall.setCiType(ciTypes);
                    collectionTaskInstall.setCiPriority(ciPrioritys.get());
                    collectionTaskInstall.setPsZoneCode(psZoneCode);
                    collectionTaskInstall.setPsZoneName(psZoneName);
                    collectionTaskInstall.setDeviceid(deviceid);
                    collectionTaskInstall.setAccountId(optionalUserId.get());
                    collectionTaskInstall.setVehicleId(optionalVehicleId.get());
                    collectionTaskInstall.setCiRemark(ciRemark);

                    collectionTaskInstall.setCiStatus(cst01);
                    collectionTaskInstall.setCiCompleteDate(null);

                    collectionTaskInstall.setInsert_id(optionalCollectionTaskInstall.get().getInsert_id());
                    collectionTaskInstall.setInsertDateTime(optionalCollectionTaskInstall.get().getInsertDateTime());
                    collectionTaskInstall.setModify_id(currentuserid);
                    collectionTaskInstall.setModifyDateTime(LocalDateTime.now());

                    collectionTaskInstallService.save(collectionTaskInstall);
                }
            }

        }else{
            // 신규작성
            if(!ciType.equals("AT00")){
                SimpleDateFormat todayFormat = new SimpleDateFormat("yyMMdd");
                Date time = new Date();
                String today = todayFormat.format(time);
                String ciCodeSet = keyGenerateService.keyGenerate("cl_task_install", "TI"+today, currentuserid);
                ciTypes = AccordiType.valueOf(ciType);
                if (!optionalUserId.isPresent() || !optionalVehicleId.isPresent() || !ciPrioritys.isPresent()) {
                    return ResponseEntity.ok(res.fail(ResponseErrorCode.E034.getCode(), ResponseErrorCode.E034.getDesc() + "'" + currentuserid + "'"));
                }else{
                    CollectionTaskInstall collectionTaskInstall = new CollectionTaskInstall();

                    collectionTaskInstall.setCiCode(ciCodeSet);
                    collectionTaskInstall.setCiType(ciTypes);
                    collectionTaskInstall.setCiPriority(ciPrioritys.get());
                    collectionTaskInstall.setPsZoneCode(psZoneCode);
                    collectionTaskInstall.setPsZoneName(psZoneName);
                    collectionTaskInstall.setDeviceid(deviceid);
                    collectionTaskInstall.setAccountId(optionalUserId.get());
                    collectionTaskInstall.setVehicleId(optionalVehicleId.get());
                    collectionTaskInstall.setCiRemark(ciRemark);

                    collectionTaskInstall.setCiStatus(cst01);
                    collectionTaskInstall.setCiCompleteDate(null);

                    collectionTaskInstall.setInsert_id(currentuserid);
                    collectionTaskInstall.setInsertDateTime(LocalDateTime.now());
                    collectionTaskInstall.setModify_id(currentuserid);
                    collectionTaskInstall.setModifyDateTime(LocalDateTime.now());

                    collectionTaskInstallService.save(collectionTaskInstall);

                }
            }else{
                SimpleDateFormat todayFormat = new SimpleDateFormat("yyMMdd");
                Date time = new Date();
                String today = todayFormat.format(time);

                for(int i=0; i<2; i++){
                    if(i==0){
                        ciTypes = AccordiType.valueOf("AT01");
                    }else{
                        ciTypes = AccordiType.valueOf("AT02");
                    }

                    if (!optionalUserId.isPresent() || !optionalVehicleId.isPresent() || !ciPrioritys.isPresent()) {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.E034.getCode(), ResponseErrorCode.E034.getDesc() + "'" + currentuserid + "'"));
                    }else {
                        CollectionTaskInstall collectionTaskInstall = new CollectionTaskInstall();

                        String ciCodeSet = keyGenerateService.keyGenerate("cl_task_install", "TI" + today, currentuserid);

                        collectionTaskInstall.setCiCode(ciCodeSet);
                        collectionTaskInstall.setCiType(ciTypes);
                        collectionTaskInstall.setCiPriority(ciPrioritys.get());
                        collectionTaskInstall.setPsZoneCode(psZoneCode);
                        collectionTaskInstall.setPsZoneName(psZoneName);
                        collectionTaskInstall.setDeviceid(deviceid);
                        collectionTaskInstall.setAccountId(optionalUserId.get());
                        collectionTaskInstall.setVehicleId(optionalVehicleId.get());
                        collectionTaskInstall.setCiRemark(ciRemark);

                        collectionTaskInstall.setCiStatus(cst01);
                        collectionTaskInstall.setCiCompleteDate(null);

                        collectionTaskInstall.setInsert_id(currentuserid);
                        collectionTaskInstall.setInsertDateTime(LocalDateTime.now());
                        collectionTaskInstall.setModify_id(currentuserid);
                        collectionTaskInstall.setModifyDateTime(LocalDateTime.now());

                        collectionTaskInstallService.save(collectionTaskInstall);
                    }
                }
            }
        }
        return ResponseEntity.ok(res.success());
    }

    // 배치/수거 업무 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> list(@RequestParam (value="ciType", defaultValue="") String ciType,
                                                   @RequestParam (value="ciPriority", defaultValue="") String  ciPriority,
                                                   @RequestParam (value="ciCode", defaultValue="")String ciCode,
                                                   @RequestParam (value="psZoneCode", defaultValue="")String psZoneCode,
                                                   @RequestParam (value="deviceid", defaultValue="")String deviceid,
                                                   @PageableDefault Pageable pageable){

        AccordiType ciTypes = null;
        Long ciPriorityId = null;

        if(!ciType.equals("")){
            ciTypes = AccordiType.valueOf(ciType);
        }
        if(!ciPriority.equals("")){
            Optional<MasterCode> ciPrioritys = masterCodeService.findByCode(ciPriority);
            ciPriorityId = ciPrioritys.map(MasterCode::getId).orElse(null);
        }

        Page<CollectionTaskInstallListDto> collectionTaskInstallListDtos =
                collectionTaskInstallService.findByCollectionTaskInstallSearch(ciTypes,ciPriorityId,ciCode,psZoneCode,deviceid,pageable);
//        log.info("collectionTaskInstallListDtos : "+collectionTaskInstallListDtos.getContent());

        return CommonUtils.ResponseEntityPage(collectionTaskInstallListDtos);
    }

    // 배치/수거 정보 보기
    @PostMapping ("info")
    public ResponseEntity<Map<String,Object>> info(@RequestParam (value="ciCode", defaultValue="") String ciCode){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

//        log.info("받아온 아이디값 : "+id);
        List<CollectionTaskInstallDto> collectionTaskInstallDtos = collectionTaskInstallService.findByCollectionTaskInstallInfo(ciCode);
//        log.info("collectionTaskInstallDtos : "+collectionTaskInstallDtos);

        data.put("collectionTaskInstallDtos",collectionTaskInstallDtos);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 배치/수거 삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> del(@RequestParam(value="ciCode", defaultValue="") String ciCode){
        AjaxResponse res = new AjaxResponse();

        Optional<CollectionTaskInstall> optionalCollectionTaskInstall = collectionTaskInstallService.findByCiCode(ciCode);

        if (!optionalCollectionTaskInstall.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }
        collectionTaskInstallService.delete(optionalCollectionTaskInstall.get());
        return ResponseEntity.ok(res.success());
    }

}
