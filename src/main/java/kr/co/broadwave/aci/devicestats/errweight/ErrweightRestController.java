package kr.co.broadwave.aci.devicestats.errweight;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.mastercode.MasterCodeErrDto;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    private final MasterCodeService masterCodeService;
    @Autowired
    public ErrweightRestController(ErrweightService errweightService,
                                   AccountService accountService,
                                   ModelMapper modelMapper,
                                   MasterCodeService masterCodeService) {
        this.errweightService = errweightService;
        this.modelMapper = modelMapper;
        this.accountService = accountService;
        this.masterCodeService = masterCodeService;
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

    // 배출정보 리스트
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> list(@RequestParam(value="fromVal", defaultValue="") String fromVal,
                                                   @RequestParam(value="toVal", defaultValue="") String toVal){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        ErrweightMapperDto errweightMapperDto = errweightService.findById2(Long.parseLong(String.valueOf(1)));
//        log.info("errweightMapperDto : "+errweightMapperDto);

        List<MasterCodeErrDto> masterRanks = masterCodeService.findCodeList2(CodeType.C0018);
//        log.info("masterRanks : "+masterRanks);

        if(masterRanks==null){
            data.put("errweightMapperDto",1);
            res.addResponse("data", data);
            return ResponseEntity.ok(res.success());
        }else if(errweightMapperDto==null){
            data.put("errweightMapperDto",2);
            res.addResponse("data", data);
            return ResponseEntity.ok(res.success());
        }else{
            List<ErrweightDataDto> errweightDataDtos = errweightService.findByErrweighttDataListQuerydsl(errweightMapperDto,fromVal,toVal);
//            log.info("errweightDataDtos : "+errweightDataDtos);

            List<Integer> sumResult = new ArrayList<>();
            List<String> sumRank = new ArrayList<>();

            for (ErrweightDataDto errweightDataDto : errweightDataDtos) {
                Integer err01Cnt = errweightDataDto.getErr01Cnt();
                Integer err02Cnt = errweightDataDto.getErr02Cnt();
                Integer err03Cnt = errweightDataDto.getErr03Cnt();
                Integer err04Cnt = errweightDataDto.getErr04Cnt();
                Integer err05Cnt = errweightDataDto.getErr05Cnt();
                Integer err06Cnt = errweightDataDto.getErr06Cnt();
                Integer err07Cnt = errweightDataDto.getErr07Cnt();
                Integer err08Cnt = errweightDataDto.getErr08Cnt();
                Integer err09Cnt = errweightDataDto.getErr09Cnt();
                Integer err10Cnt = errweightDataDto.getErr10Cnt();
                int sumResultVal = err01Cnt + err02Cnt + err03Cnt + err04Cnt + err05Cnt + err06Cnt + err07Cnt + err08Cnt + err09Cnt + err10Cnt;
                sumResult.add(sumResultVal);

                for(MasterCodeErrDto v : masterRanks){
                    if(Integer.parseInt(v.getBcRef1())<=sumResultVal && sumResultVal < Integer.parseInt(v.getBcRef2())){
                        sumRank.add(v.getName());
                        break;
                    }
                }
            }
//            log.info("sumRank : "+sumRank);

            data.put("errweightDataDtos",errweightDataDtos);
            data.put("sumResult",sumResult);
            data.put("sumRank",sumRank);

            res.addResponse("data", data);
            return ResponseEntity.ok(res.success());
        }

    }

}
