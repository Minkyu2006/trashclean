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
                                                   @RequestParam(value="toVal", defaultValue="") String toVal,
                                                   @RequestParam(value="sumResultSelect", defaultValue="") String sumResultSelect){

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        ErrweightMapperDto errweightMapperDto = errweightService.findById2(Long.parseLong(String.valueOf(1)));

        if(errweightMapperDto==null){
            data.put("errweightMapperDto",1);
            res.addResponse("data", data);
            return ResponseEntity.ok(res.success());
        }else{
            List<ErrweightDataDto> errweightDataDtos = errweightService.findByErrweighttDataListQuerydsl(errweightMapperDto,fromVal,toVal);
            log.info("errweightDataDtos : "+errweightDataDtos);

            List<MasterCodeErrDto> masterRank = masterCodeService.findCodeList2(CodeType.C0018);
//            log.info("masterRank : "+masterRank);

            List<Integer> sumResult = new ArrayList<>();
            List<String> sumRank = new ArrayList<>();

            int ref1;
            int ref2;
            int ref3;
            for(int i=0; i<errweightDataDtos.size(); i++){
                Integer err01Cnt = errweightDataDtos.get(i).getErr01Cnt();
                Integer err02Cnt = errweightDataDtos.get(i).getErr02Cnt();
                Integer err03Cnt = errweightDataDtos.get(i).getErr03Cnt();
                Integer err04Cnt = errweightDataDtos.get(i).getErr04Cnt();
                Integer err05Cnt = errweightDataDtos.get(i).getErr05Cnt();
                Integer err06Cnt = errweightDataDtos.get(i).getErr06Cnt();
                Integer err07Cnt = errweightDataDtos.get(i).getErr07Cnt();
                Integer err08Cnt = errweightDataDtos.get(i).getErr08Cnt();
                Integer err09Cnt = errweightDataDtos.get(i).getErr09Cnt();
                Integer err10Cnt = errweightDataDtos.get(i).getErr10Cnt();
                int sumReseult = err01Cnt+err02Cnt+err03Cnt+err04Cnt+err05Cnt+err06Cnt+err07Cnt+err08Cnt+err09Cnt+err10Cnt;
                sumResult.add(sumReseult);

                ref1 = Integer.parseInt(masterRank.get(0).getBcRef2());
                ref2 = Integer.parseInt(masterRank.get(1).getBcRef2());
                ref3 = Integer.parseInt(masterRank.get(2).getBcRef2());

                if(sumReseult<ref1){
                    sumRank.add("정상");
                }else if(sumReseult<ref2){
                    sumRank.add("의심");
                }else if(sumReseult<ref3){
                    sumRank.add("점검요망");
                }else{
                    sumRank.add("오류");
                }

            }

            data.put("errweightDataDtos",errweightDataDtos);
            data.put("sumResult",sumResult);
            data.put("sumRank",sumRank);
            data.put("errweightMapperDto",2);

            res.addResponse("data", data);
            return ResponseEntity.ok(res.success());
        }

    }

}
