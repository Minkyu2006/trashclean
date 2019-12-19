package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.awsiot.ACIAWSIoTDeviceService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.company.CompanyService;
import kr.co.broadwave.aci.dashboard.DashboardService;
import kr.co.broadwave.aci.equipment.*;
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
import java.util.HashMap;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time : 10:22
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/model")
public class IModelRestController {

    private final DashboardService dashboardService;
    private final ModelMapper modelMapper;
    private final EquipmentService equipmentService;
    private final AccountService accountService;
    private final CompanyService companyService;
    private final MasterCodeService masterCodeService;
    private final ACIAWSIoTDeviceService aciawsIoTDeviceService;

    @Autowired
    public IModelRestController(ModelMapper modelMapper,
                                AccountService accountService,
                                CompanyService companyService,
                                DashboardService dashboardService,
                                MasterCodeService masterCodeService,
                                EquipmentService equipmentService, ACIAWSIoTDeviceService aciawsIoTDeviceService) {
        this.accountService = accountService;
        this.dashboardService = dashboardService;
        this.companyService = companyService;
        this.masterCodeService = masterCodeService;
        this.equipmentService = equipmentService;
        this.modelMapper = modelMapper;
        this.aciawsIoTDeviceService = aciawsIoTDeviceService;
    }

    // 모델등록
    @PostMapping ("reg")
    public ResponseEntity equipmentReg(@ModelAttribute IModelMapperDto imodelMapperDto, HttpServletRequest request){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();














        return ResponseEntity.ok(res.success());
    }























    // 모델 리스트
    @PostMapping("list")
    public ResponseEntity equipmentList(@RequestParam (value="emNumber", defaultValue="") String emNumber,
                                                        @RequestParam (value="emDesignation", defaultValue="") String  emDesignation,
                                                        @RequestParam (value="emType", defaultValue="")String emType,
                                                        @RequestParam (value="emCountry", defaultValue="")String emCountry,
                                                        @PageableDefault Pageable pageable){

        Long emTypeId = null;
        Long emCountryId = null;

        if(!emType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
            emTypeId = emTypes.get().getId();
        }
        if(!emCountry.equals("")){
            Optional<MasterCode> emCountrys = masterCodeService.findByCode(emCountry);
            emCountryId = emCountrys.get().getId();
        }

        Page<EquipmentListDto> equipmentListDtos =
                equipmentService.findByEquipmentSearch(emNumber,emDesignation,emTypeId,emCountryId,pageable);

        return CommonUtils.ResponseEntityPage(equipmentListDtos);
    }

    // 모델 삭제
    @PostMapping("del")
    public ResponseEntity equipmentDel(@RequestParam(value="emNumber", defaultValue="") String emNumber){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Optional<Equipment> optionalEquipment = equipmentService.findByEmNumber(emNumber);

        if (!optionalEquipment.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }
        equipmentService.delete(optionalEquipment.get());
        return ResponseEntity.ok(res.success());
    }


}
