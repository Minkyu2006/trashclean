package kr.co.broadwave.aci.devicestats.errweight;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Time :
 * Remark : Ajax 용 Rest Controller
 */
@Slf4j
@RestController
@RequestMapping("/api/errweight")
public class ErrweightRestController {

    private final ModelMapper modelMapper;
    private final ErrweightService errweightService;
    private final AccountService accountService;

    @Autowired
    public ErrweightRestController(ErrweightService errweightService,
                                   AccountService accountService,
                                   ModelMapper modelMapper) {
        this.errweightService = errweightService;
        this.modelMapper = modelMapper;
        this.accountService = accountService;
    }

    // 가중치셋팅
    @PostMapping("save")
    public ResponseEntity<Map<String,Object>> save(@ModelAttribute ErrweightMapperDto errweightMapperDto,
                                                   HttpServletRequest request){

        AjaxResponse res = new AjaxResponse();

        Errweight errweight = modelMapper.map(errweightMapperDto, Errweight.class);

        String currentuserid = CommonUtils.getCurrentuser(request);
        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'" ));
        }

        errweight.setId(Long.parseLong(String.valueOf(1)));
        errweight.setInsert_date(LocalDateTime.now());
        errweight.setInsert_id(currentuserid);

        errweightService.save(errweight);

        return ResponseEntity.ok(res.success());

    }

}
