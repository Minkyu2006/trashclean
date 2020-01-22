package kr.co.broadwave.aci.test;

import kr.co.broadwave.aci.common.AjaxResponse;
import kr.co.broadwave.aci.files.FileUpload;
import kr.co.broadwave.aci.files.FileUploadDto;
import kr.co.broadwave.aci.files.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author InSeok
 * Date : 2019-12-05
 * Remark :
 */
@Slf4j
@RestController
@RequestMapping("/api/test")
public class FileUploadRestController {

    @Value("${aci.aws.s3.bucket.url}")
    private String AWSS3URL;

    private AjaxResponse res = new AjaxResponse();
    HashMap<String, Object> data = new HashMap<>();


    private final FileUploadService fileUploadService;

    public FileUploadRestController(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    @PostMapping("fileuplod")
    public ResponseEntity noticeSave(MultipartHttpServletRequest multi, HttpServletRequest request) throws Exception {

        //String subject = multi.getParameter("subject"); // 파일말로 다른 Inpu 값있을경우

        //파일저장
        Iterator<String> files = multi.getFileNames();
        while(files.hasNext()) {
            String uploadFile = files.next();

            MultipartFile mFile = multi.getFile(uploadFile);
            //String fileName = mFile.getOriginalFilename();
            //파일이 존재할때만
            if (!mFile.isEmpty()) {
                //System.out.println("파일명 확인  : " + fileName);
                FileUpload save = fileUploadService.save(mFile);
                //====================다른파일저장로직시 활용부분!!!!!!!=========================
                //저장후 해당저장 값의 객체를반환하기때문에 다른테이블에 fk로 저장할수있다.
            }
        }
        return ResponseEntity.ok(res.success());
    }
    @PostMapping("filelist")
    public ResponseEntity filelist(){

        List<FileUploadDto> files = fileUploadService.findTop10ByALL();

        data.clear();
        data.put("datalist",files);
        data.put("awss3url",AWSS3URL);
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());
    }

    //파일삭제
    @PostMapping("filedel")
    public ResponseEntity filedel(@RequestParam (value="fileid", defaultValue="") String fileid){

        fileUploadService.del(Long.parseLong(fileid));
        data.clear();
        res.addResponse("data",data);

        return ResponseEntity.ok(res.success());

    }

}
