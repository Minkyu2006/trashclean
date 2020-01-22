package kr.co.broadwave.aci.vehicle;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.CompanyService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-01-21
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/vehicle")
public class VehicleRestController {

    private final ModelMapper modelMapper;
    private final VehicleService vehicleService;
    private final AccountService accountService;
    private final CompanyService companyService;
    private final MasterCodeService masterCodeService;

    @Autowired
    public VehicleRestController(ModelMapper modelMapper,
                                 AccountService accountService,
                                 MasterCodeService masterCodeService,
                                 CompanyService companyService,
                                 VehicleService vehicleService) {
        this.accountService = accountService;
        this.vehicleService = vehicleService;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
        this.masterCodeService = masterCodeService;
    }

    // 차량 저장
    @PostMapping("reg")
    public ResponseEntity<Map<String,Object>> vehicleReg(@ModelAttribute VehicleMapperDto vehicleMapperDto,HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();

        Vehicle vehicle = modelMapper.map(vehicleMapperDto, Vehicle.class);

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        // 차량소유구분/차량용도/차량상태 가져오기
        Optional<MasterCode> optionalVcShape = masterCodeService.findById(vehicleMapperDto.getVcShape());
        Optional<MasterCode> optionalVcUsage = masterCodeService.findById(vehicleMapperDto.getVcUsage());
        Optional<MasterCode> optionalVcStat = masterCodeService.findById(vehicleMapperDto.getVcState());

        // 차량소유구분/차량용도/차량상태 존재하지않으면
        if (!optionalVcShape.isPresent() || !optionalVcUsage.isPresent() || !optionalVcStat.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E023.getCode(), ResponseErrorCode.E023.getDesc()));
        }else{
            vehicle.setVcShape(optionalVcShape.get());
            vehicle.setVcUsage(optionalVcUsage.get());
            vehicle.setVcState(optionalVcStat.get());
        }

        Optional<Vehicle> optionalVehicle = vehicleService.findByVcNumber(vehicle.getVcNumber());

        //신규 및 수정여부
            if (optionalVehicle.isPresent()) {
                if(!vehicle.getId().equals(optionalVehicle.get().getId())){
                    if (optionalVehicle.get().getVcNumber().equals(vehicleMapperDto.getVcNumber())) {
                        return ResponseEntity.ok(res.fail(ResponseErrorCode.E022.getCode(), ResponseErrorCode.E022.getDesc()));
                    }
                }else{
                    //수정
                    vehicle.setId(optionalVehicle.get().getId());
                    vehicle.setInsert_id(optionalVehicle.get().getInsert_id());
                    vehicle.setInsertDateTime(optionalVehicle.get().getInsertDateTime());
                    vehicle.setModify_id(currentuserid);
                    vehicle.setModifyDateTime(LocalDateTime.now());
                }
                //수정
                vehicle.setId(optionalVehicle.get().getId());
                vehicle.setInsert_id(optionalVehicle.get().getInsert_id());
                vehicle.setInsertDateTime(optionalVehicle.get().getInsertDateTime());
                vehicle.setModify_id(currentuserid);
                vehicle.setModifyDateTime(LocalDateTime.now());
            } else {
                //신규
                vehicle.setInsert_id(currentuserid);
                vehicle.setInsertDateTime(LocalDateTime.now());
                vehicle.setModify_id(currentuserid);
                vehicle.setModifyDateTime(LocalDateTime.now());
            }

        // 소속운영사 아이디저장하기
        Optional<Company> optionalCompany = companyService.findByCsOperator(vehicleMapperDto.getOperator());
        //운영사가 존재하지않으면
        if (!optionalCompany.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E005.getCode(), ResponseErrorCode.E005.getDesc()));
        }else{
            Company company = optionalCompany.get();
            vehicle.setCompany(company);
        }

        vehicleService.save(vehicle);

        return ResponseEntity.ok(res.success());
    }



    // 차량 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> vehicleList(@RequestParam (value="vcNumber", defaultValue="") String vcNumber,
                                      @RequestParam (value="vcName", defaultValue="") String  vcName,
                                      @RequestParam (value="vcShape", defaultValue="")String vcShape,
                                      @RequestParam (value="vcUsage", defaultValue="")String vcUsage,
                                      @RequestParam (value="vcState", defaultValue="")String vcState,
                                      @PageableDefault Pageable pageable){

        Long vcShapeId = null;
        Long vcUsageId = null;
        Long vcStateId = null;

        if(!vcShape.equals("")){
            Optional<MasterCode> vcShapes = masterCodeService.findByCode(vcShape);
            vcShapeId = vcShapes.map(MasterCode::getId).orElse(null);
//            if(vcShapes.isPresent()){
//                vcShapeId = vcShapes.get().getId();
//            }else{
//                vcShapeId = null;
//            }
        }
        if(!vcUsage.equals("")){
            Optional<MasterCode> vcUsages = masterCodeService.findByCode(vcUsage);
            vcUsageId = vcUsages.map(MasterCode::getId).orElse(null);
        }
        if(!vcState.equals("")){
            Optional<MasterCode> vcStates = masterCodeService.findByCode(vcState);
            vcStateId = vcStates.map(MasterCode::getId).orElse(null);
        }

        Page<VehicleListDto> vehicleListDtos =
                vehicleService.findByVehicleSearch(vcNumber,vcName,vcShapeId,vcUsageId,vcStateId,pageable);

        return CommonUtils.ResponseEntityPage(vehicleListDtos);
    }

    // 차량 정보 보기
    @PostMapping ("info")
    public ResponseEntity<Map<String,Object>> vehicleInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        VehicleDto vehicleDto = vehicleService.findById(id);

        data.put("vehicle",vehicleDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 차량 삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> vehicleDel(@RequestParam(value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();

        Optional<Vehicle> optionalVehicle = vehicleService.findById2(id);

        if (!optionalVehicle.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }

        vehicleService.delete(optionalVehicle.get());

        return ResponseEntity.ok(res.success());
    }


}
