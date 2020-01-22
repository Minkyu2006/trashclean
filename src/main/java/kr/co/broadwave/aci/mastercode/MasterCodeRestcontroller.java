package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.files.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author InSeok
 * Date : 2019-08-02
 * Remark :
 */
@Slf4j
@RestController
@RequestMapping("/api/mastercode")
public class MasterCodeRestcontroller {
    private AjaxResponse res = new AjaxResponse();
    HashMap<String, Object> data = new HashMap<>();

    private final ModelMapper modelMapper;
    private final MasterCodeService masterCodeService;

    @Autowired
    public MasterCodeRestcontroller(ModelMapper modelMapper,
                                    MasterCodeService masterCodeService) {
        this.modelMapper = modelMapper;
        this.masterCodeService = masterCodeService;
    }

    //마스터코드 조회
    @PostMapping("list")
    public ResponseEntity<Map<String,Object>> noticeList(@RequestParam(value="codetype", defaultValue="") String codetype,
                                                         @RequestParam(value="code", defaultValue="") String code,
                                                         @RequestParam(value="name", defaultValue="") String name,
                                                         Pageable pageable){

        CodeType codeType = null;

        if (!codetype.equals("")){
            codeType = CodeType.valueOf(codetype);
        }

        //log.info("마스터코드  조회 / 조회조건 : codetype / '" + codetype + "' code / '" + code + "' name / '" + name + "'");

        Page<MasterCodeDto> masterCodes = masterCodeService.findAllBySearchStrings(codeType, code,name, pageable);
        return CommonUtils.ResponseEntityPage(masterCodes);

    }

    @PostMapping("reg")
    public ResponseEntity<Map<String,Object>> noticeSave(@ModelAttribute MasterCodeMapperDto masterCodeMapperDto,
                                     HttpServletRequest request) throws Exception {
        MasterCode masterCode = modelMapper.map(masterCodeMapperDto, MasterCode.class);

        String currentuserid = CommonUtils.getCurrentuser(request);

        //이미값이 존재하는지 확인
        Optional<MasterCode> optionalMasterCode = masterCodeService.findByCoAndCodeTypeAndCode(masterCode.getCodeType(), masterCode.getCode());
        if (optionalMasterCode.isPresent()){
            masterCode.setId(optionalMasterCode.get().getId());
            masterCode.setModify_id(currentuserid);
            masterCode.setModifyDateTime(LocalDateTime.now());
        }else {
            masterCode.setInsert_id(currentuserid);
            masterCode.setInsertDateTime(LocalDateTime.now());
            masterCode.setModify_id(currentuserid);
            masterCode.setModifyDateTime(LocalDateTime.now());
        }

        MasterCode saveMastercode = masterCodeService.save(masterCode);

        //log.info("마스터코드 저장 성공 : " + saveMastercode.toString() );
        return ResponseEntity.ok(res.success());


    }

    @PostMapping ("mastercode")
    public ResponseEntity<Map<String,Object>> team(@RequestParam (value="id", defaultValue="") Long id
    ){
        //log.info("마스터코드 단일 조회  / id: '" + id +"'");

        Optional<MasterCode> optionalMasterCode = masterCodeService.findById(id);

        if (!optionalMasterCode.isPresent()){
            //log.info("특정 마스터코드 조회실패 : 조회할 데이터가 존재하지않음 , 조회대상 id: '" + id +"'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E004.getCode(),ResponseErrorCode.E004.getDesc()));
        }
        MasterCode masterCode = optionalMasterCode.get();

        data.clear();
        data.put("datarow",masterCode);
        res.addResponse("data",data);

        //log.info("마스터코드 조회 성공 : " + masterCode.toString() );
        return ResponseEntity.ok(res.success());

    }

    @PostMapping("del")
    public ResponseEntity<Map<String,Object>> teamdel(@RequestParam(value="codetype", defaultValue="") String codetype,
                                  @RequestParam(value="code", defaultValue="") String code){
        CodeType codeType = null;

        if (!codetype.equals("")){
            codeType = CodeType.valueOf(codetype);
        }

        //log.info("마스터코드 삭제 / codetype: '" + codetype + "', code ='" + code + "'");

        Optional<MasterCode> optionalMasterCode = masterCodeService.findByCoAndCodeTypeAndCode(codeType, code);

        //정보가있는지 체크
        if (!optionalMasterCode.isPresent()){
            //log.info("코드삭제실패 : 삭제할 데이터가 존재하지않음 , 삭제대상 codetype: '" + codetype + "', code ='" + code + "'");
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(),ResponseErrorCode.E003.getDesc()));
        }
        MasterCode masterCode = optionalMasterCode.get();

        masterCodeService.delete(masterCode);

        return ResponseEntity.ok(res.success());
    }

}
