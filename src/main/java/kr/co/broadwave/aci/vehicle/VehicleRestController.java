package kr.co.broadwave.aci.vehicle;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.awsiot.ACIAWSIoTDeviceService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.company.CompanyDto;
import kr.co.broadwave.aci.company.CompanyListDto;
import kr.co.broadwave.aci.company.CompanyService;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.equipment.*;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.imodel.IModelChangeDto;
import kr.co.broadwave.aci.imodel.IModelService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeDto;
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
import java.util.List;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 14:22
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

    @Autowired
    public VehicleRestController(ModelMapper modelMapper,
                                 AccountService accountService,
                                 CompanyService companyService,
                                 VehicleService vehicleService) {
        this.accountService = accountService;
        this.vehicleService = vehicleService;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
    }

    // 차량 저장
    @PostMapping ("reg")
    public ResponseEntity vehicleReg(@ModelAttribute VehicleMapperDto vehicleMapperDto,HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Vehicle vehicle = modelMapper.map(vehicleMapperDto, Vehicle.class);

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        // 차량아이디 가져오기(고유값)
        Optional<Vehicle> optionalVehicle = vehicleService.findById2(vehicle.getId());

        //신규 및 수정여부
        if (optionalVehicle.isPresent()) {
            if (optionalVehicle.get().getVcNumber().equals(vehicleMapperDto.getVcNumber())) {
                return ResponseEntity.ok(res.fail(ResponseErrorCode.E008.getCode(), ResponseErrorCode.E008.getDesc()));
            }
            //수정
            vehicle.setId(optionalVehicle.get().getId());
            vehicle.setInsert_id(optionalVehicle.get().getInsert_id());
            vehicle.setInsertDateTime(optionalVehicle.get().getInsertDateTime());
            vehicle.setModify_id(currentuserid);
            vehicle.setModifyDateTime(LocalDateTime.now());
        }else{
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

        Vehicle save = vehicleService.save(vehicle);

        //log.info("장비등록 데이터 : "+save.toString());
        return ResponseEntity.ok(res.success());
    }



    // 차량 리스트
    @PostMapping("list")
    public ResponseEntity vehicleList(@RequestParam (value="vcNumber", defaultValue="") String vcNumber,
                                                        @RequestParam (value="vcName", defaultValue="") String  vcName,
                                                        @RequestParam (value="vcShape", defaultValue="")String vcShape,
                                                        @RequestParam (value="vcUsage", defaultValue="")String vcUsage,
                                                        @PageableDefault Pageable pageable){

        Page<VehicleListDto> vehicleListDtos =
                vehicleService.findByVehicleSearch(vcNumber,vcName,vcShape,vcUsage,pageable);

        return CommonUtils.ResponseEntityPage(vehicleListDtos);
    }

    // 장비 정보 보기
    @PostMapping ("info")
    public ResponseEntity vehicleInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        VehicleDto vehicleDto = vehicleService.findById(id);
        log.info("vehicleDto : "+vehicleDto);
        log.info("받아온 아이디값 : "+id);

        data.clear();
        data.put("vehicle",vehicleDto);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 차량 삭제
    @PostMapping("del")
    public ResponseEntity vehicleDel(@RequestParam(value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<Vehicle> optionalVehicle = vehicleService.findById2(id);

        if (!optionalVehicle.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }

        vehicleService.delete(optionalVehicle.get());

        return ResponseEntity.ok(res.success());
    }


}
