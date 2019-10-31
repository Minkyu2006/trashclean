package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.teams.Team;
import kr.co.broadwave.aci.teams.TeamDto;
import kr.co.broadwave.aci.teams.TeamMapperDto;
import kr.co.broadwave.aci.teams.TeamService;
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
 * @author InSeok
 * Date : 2019-04-02
 * Time : 14:09
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

    @Autowired
    public CompanyRestController(ModelMapper modelMapper,
                                 AccountService accountService,
                                 CompanyService companyService) {
        this.accountService = accountService;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
    }

    @PostMapping ("reg")
    public ResponseEntity companyReg(@ModelAttribute CompanyDtoMapperDto companyDtoMapperDto,HttpServletRequest request){

        Company company = modelMapper.map(companyDtoMapperDto, Company.class);

        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);

        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            log.info("출동일지 저장한 사람 아이디 미존재 : '" + currentuserid + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

//        CompanyDto optionalCompany = companyService.findByCsNumber(company.getCsNumber());
        Optional<Company> optionalCompany = companyService.findByCsNumber(company.getCsNumber());
        //신규 및 수정여부
        if (optionalCompany.isPresent()){
            //수정
            company.setId(optionalCompany.get().getId());
            company.setInsert_id(optionalCompany.get().getInsert_id());
            company.setInsertDateTime(optionalCompany.get().getInsertDateTime());
//            company.setId(optionalCompany.getId());
//            company.setInsert_id(optionalCompany.getInsert_id());
//            company.setInsertDateTime(optionalCompany.getInsertDateTime());
            company.setModify_id(currentuserid);
            company.setModifyDateTime(LocalDateTime.now());
        }else{
            //신규
            company.setInsert_id(currentuserid);
            company.setInsertDateTime(LocalDateTime.now());
            company.setModify_id(currentuserid);
            company.setModifyDateTime(LocalDateTime.now());
        }

        Company save = companyService.save(company);

        log.info("업체등록 데이터 : "+save.toString());
        return ResponseEntity.ok(res.success());
    }

//    @PostMapping("list")
//    public ResponseEntity companyList(@RequestParam (value="", defaultValue="") String 1,
//                                   @RequestParam (value="", defaultValue="") String  2,
//                                   @PageableDefault Pageable pageable){
//
//        Page<Dto> s = Service.(1,2, pageable);
//
//        return CommonUtils.ResponseEntityPage(s);
//    }

}
