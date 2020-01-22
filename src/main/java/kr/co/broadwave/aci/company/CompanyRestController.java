package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
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
 * Date : 2019-10-31
 * Time : 14:22
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/company")
public class CompanyRestController {

    private AjaxResponse res = new AjaxResponse();
    private HashMap<String, Object> data = new HashMap<>();

    private final ModelMapper modelMapper;
    private final CompanyService companyService;
    private final AccountService accountService;
    private final MasterCodeService masterCodeService;

    @Autowired
    public CompanyRestController(ModelMapper modelMapper,
                                 AccountService accountService,
                                 MasterCodeService masterCodeService,
                                 CompanyService companyService) {
        this.accountService = accountService;
        this.masterCodeService = masterCodeService;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
    }

    // 업체 저장
    @PostMapping ("reg")
    public ResponseEntity<Map<String,Object>> companyReg(@ModelAttribute CompanyMapperDto companyMapperDto, HttpServletRequest request){

        Company company = modelMapper.map(companyMapperDto, Company.class);

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            log.info("업제등록 저장한 사람 아이디 미존재 : '" + currentuserid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        Optional<Company> optionalCompany = companyService.findByCsNumber(company.getCsNumber());
        //신규 및 수정여부
        if (optionalCompany.isPresent()){
            //수정
            company.setId(optionalCompany.get().getId());
            company.setInsert_id(optionalCompany.get().getInsert_id());
            company.setInsertDateTime(optionalCompany.get().getInsertDateTime());
            company.setModify_id(currentuserid);
            company.setModifyDateTime(LocalDateTime.now());
        }else{
            //신규
            company.setInsert_id(currentuserid);
            company.setInsertDateTime(LocalDateTime.now());
            company.setModify_id(currentuserid);
            company.setModifyDateTime(LocalDateTime.now());
        }

        // 업체구분/운영권역 가져오기
        Optional<MasterCode> optionalCsDivision = masterCodeService.findById(companyMapperDto.getCsDivision());
        Optional<MasterCode> optionalCsRegional = masterCodeService.findById(companyMapperDto.getCsRegional());
        //업체구분이 존재하지않으면
        if (!optionalCsDivision.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E018.getCode(),
                    ResponseErrorCode.E018.getDesc()));
        }else{
            // 업체구분 저장
            company.setCsDivision(optionalCsDivision.get());
        }
        //운영권역이 존재하지않으면sd
        if (!optionalCsRegional.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E019.getCode(),
                    ResponseErrorCode.E019.getDesc()));
        }else{
            // 운영권역 저장
            company.setCsRegional(optionalCsRegional.get());
        }

        Company save = companyService.save(company);

        log.info("업체등록 데이터 : "+save.toString());
        return ResponseEntity.ok(res.success());
    }

    // 업체 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> companyList(@RequestParam (value="csNumber", defaultValue="") String csNumber,
                                                        @RequestParam (value="csOperator", defaultValue="") String  csOperator,
                                                        @RequestParam (value="csDivision", defaultValue="") String  csDivision,
                                                        @RequestParam (value="csRegional", defaultValue="") String  csRegional,
                                                        @PageableDefault Pageable pageable){

        Long csDivisionType = null;
        Long csRegionalType = null;

        if(!csDivision.equals("")){
            Optional<MasterCode> csDivisions = masterCodeService.findByCode(csDivision);
            csDivisionType = csDivisions.map(MasterCode::getId).orElse(null);
        }
        if(!csRegional.equals("")){
            Optional<MasterCode> csRegionals = masterCodeService.findByCode(csRegional);
            csRegionalType = csRegionals.map(MasterCode::getId).orElse(null);
        }

        Page<CompanyListDto> companyDtos = companyService.findByCompanySearch(csNumber,csOperator,csDivisionType,csRegionalType,pageable);

        return CommonUtils.ResponseEntityPage(companyDtos);
    }

    // 업체 정보 보기
    @PostMapping ("info")
    public ResponseEntity<Map<String,Object>> companyInfo(@RequestParam (value="id", defaultValue="") Long id){

        CompanyDto company = companyService.findById(id);

        data.clear();
        data.put("company",company);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 업체 삭제
    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> companyDel(@RequestParam(value="csNumber", defaultValue="") String csNumber){

        Optional<Company> optionalCompany = companyService.findByCsNumber(csNumber);

        if (!optionalCompany.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }
        companyService.delete(optionalCompany.get());
        return ResponseEntity.ok(res.success());
    }

}
