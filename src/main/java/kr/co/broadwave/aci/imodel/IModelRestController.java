package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.accounts.Account;
import kr.co.broadwave.aci.accounts.AccountService;
import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.common.CommonUtils;
import kr.co.broadwave.aci.common.ResponseErrorCode;
import kr.co.broadwave.aci.files.FileUpload;
import kr.co.broadwave.aci.files.FileUploadService;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.mastercode.MasterCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
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

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private final ModelMapper modelMapper;
    private final AccountService accountService;
    private final MasterCodeService masterCodeService;
    private final IModelService iModelService;
    private final FileUploadService fileUploadService;

    @Autowired
    public IModelRestController(ModelMapper modelMapper,
                                FileUploadService fileUploadService,
                                AccountService accountService,
                                IModelService iModelService,
                                MasterCodeService masterCodeService) {
        this.accountService = accountService;
        this.fileUploadService = fileUploadService;
        this.masterCodeService = masterCodeService;
        this.iModelService = iModelService;
        this.modelMapper = modelMapper;
    }

    // 모델등록
    @PostMapping ("reg")
    public ResponseEntity modelReg(@ModelAttribute IModelMapperDto imodelMapperDto,MultipartHttpServletRequest multi,
                                   HttpServletRequest request) throws Exception {

        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        IModel iModel = modelMapper.map(imodelMapperDto, IModel.class);
        String currentuserid = CommonUtils.getCurrentuser(request);

        Optional<Account> optionalAccount = accountService.findByUserid(currentuserid);
        //로그인한 사람 아이디가존재하지않으면 에러처리
        if (!optionalAccount.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E014.getCode(),
                    ResponseErrorCode.E014.getDesc() + "'" + currentuserid + "'"));
        }

        //모델타입 코드가 존재하지않으면
        Optional<MasterCode> optionalMdType = masterCodeService.findById(imodelMapperDto.getMdType());
        Optional<MasterCode> optionalEmType = masterCodeService.findById(imodelMapperDto.getEmType());
        Optional<MasterCode> optionalMdUnit = masterCodeService.findById(imodelMapperDto.getMdUnit());

        if (!optionalMdType.isPresent()) {
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E021.getCode(),
                    ResponseErrorCode.E021.getDesc()));
        } else {
            //모델타입 저장
            iModel.setMdType(optionalMdType.get());
            iModel.setEmType(optionalEmType.get());
            iModel.setMdUnit(optionalMdUnit.get());
        }

        //모델번호 가져오기(고유값)
        Optional<IModel> optionalImodel = iModelService.findByMdNumber(iModel.getMdNumber());
        if (optionalImodel.isPresent()) {
            //수정
            iModel.setId(optionalImodel.get().getId());
            iModel.setInsert_id(optionalImodel.get().getInsert_id());
            iModel.setInsertDateTime(optionalImodel.get().getInsertDateTime());
            iModel.setModify_id(currentuserid);
            iModel.setModifyDateTime(LocalDateTime.now());
        }else{
            //신규
            iModel.setInsert_id(currentuserid);
            iModel.setInsertDateTime(LocalDateTime.now());
            iModel.setModify_id(currentuserid);
            iModel.setModifyDateTime(LocalDateTime.now());
        }

        //파일저장
        Iterator<String> files = multi.getFileNames();
        String uploadFile = files.next();
        MultipartFile mFile = multi.getFile(uploadFile);

        // 저장할 파일이 존재할때만 실행
        if(!mFile.isEmpty()) {
            FileUpload fileUpload = fileUploadService.save(mFile);
            iModel.setMdFileid(fileUpload);
        }else{
            //파일은 존재하지않으나, 기존파일이 존재할경우
            if (optionalImodel.isPresent()) {
                iModel.setMdFileid(optionalImodel.get().getMdFileid());
            }else{
                //파일은 존재하지않으나, 기존파일이 없을경
                iModel.setMdFileid(null);
            }
        }
        iModelService.save(iModel);

        //파일수정일때 실행
        if(optionalImodel.isPresent()) {
            if(optionalImodel.get().getMdFileid() != null) {
                fileUploadService.del(optionalImodel.get().getMdFileid().getId());
            }
        }

        return ResponseEntity.ok(res.success());
    }


    // 모델 리스트
    @PostMapping("list")
    public ResponseEntity modelList(@RequestParam (value="mdName", defaultValue="") String mdName,
                                                        @RequestParam (value="emType", defaultValue="") String  emType,
                                                        @RequestParam (value="mdType", defaultValue="") String  mdType,
                                                        @RequestParam (value="mdRemark", defaultValue="")String mdRemark,
                                                        @PageableDefault Pageable pageable){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        Long emTypeId = null;
        Long mdTypeId = null;

        if(!emType.equals("")){
            Optional<MasterCode> emTypes = masterCodeService.findByCode(emType);
            emTypeId = emTypes.get().getId();
        }

        if(!mdType.equals("")){
            Optional<MasterCode> mdTypes = masterCodeService.findByCode(mdType);
            mdTypeId = mdTypes.get().getId();
        }

        Page<IModelListDto> iModelListDtos =
                iModelService.findByIModelSearch(mdName,emTypeId,mdTypeId,mdRemark,pageable);

        if(iModelListDtos.getTotalElements()> 0 ){
            data.clear();
            data.put("datalist",iModelListDtos.getContent());
            data.put("awss3url",AWSS3URL);
            data.put("total_page",iModelListDtos.getTotalPages());
            data.put("current_page",iModelListDtos.getNumber() + 1);
            data.put("total_rows",iModelListDtos.getTotalElements());
            data.put("current_rows",iModelListDtos.getNumberOfElements());

            res.addResponse("data",data);
        }else{
            data.clear();
            data.put("total_page",iModelListDtos.getTotalPages());
            data.put("current_page",iModelListDtos.getNumber() + 1);
            data.put("total_rows",iModelListDtos.getTotalElements());
            data.put("current_rows",iModelListDtos.getNumberOfElements());

            res.addResponse("data",data);
        }
        return ResponseEntity.ok(res.success());
    }

    // 모델 정보보기
    @PostMapping ("info")
    public ResponseEntity modelInfo(@RequestParam (value="id", defaultValue="") Long id){
        AjaxResponse res = new AjaxResponse();
        HashMap<String, Object> data = new HashMap<>();

        IModelDto iModel = iModelService.findById(id);
        log.info("받아온 모아이디값 : "+id);

        data.clear();
        if(iModel.getMdFileid()==null){
            data.put("filepath","/defaultimage");
            data.put("filename","/model.jpg");
        }else{
            data.put("filepath",iModel.getMdFileid().getFilePath());
            data.put("filename","/s_"+iModel.getMdFileid().getSaveFileName());
        }

        data.put("iModel",iModel);
        data.put("awss3url",AWSS3URL);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    // 모델 삭제
    @PostMapping("del")
    public ResponseEntity modeltDel(@RequestParam(value="mdNumber", defaultValue="") String mdNumber){
        AjaxResponse res = new AjaxResponse();

        Optional<IModel> iModel = iModelService.findByMdNumber(mdNumber);
        if (!iModel.isPresent()){
            return ResponseEntity.ok(res.fail(ResponseErrorCode.E003.getCode(), ResponseErrorCode.E003.getDesc()));
        }

        iModelService.delete(iModel.get());

        if (iModel.get().getMdFileid()!=null) {
            //log.info("기존파일 존재하면 삭제");
            fileUploadService.del(iModel.get().getMdFileid().getId());
        }

        return ResponseEntity.ok(res.success());
    }


}
