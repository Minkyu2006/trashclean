package kr.co.broadwave.aci.files;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author InSeok
 * Date : 2019-12-05
 * Remark :
 */
@Slf4j
@Service
public class FileUploadService {

    @Value("${base.upload.directory}")
    private String uploadPath;


    private final AWSS3Service awss3Service;
    private final FileUploadRepository fileUploadRepository;
    private final FileUploadRepositoryCustom fileUploadRepositoryCustom;

    @Autowired
    public FileUploadService(AWSS3Service awss3Service, FileUploadRepository fileUploadRepository, FileUploadRepositoryCustom fileUploadRepositoryCustom) {
        this.awss3Service = awss3Service;
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadRepositoryCustom = fileUploadRepositoryCustom;
    }
    //File목록조회
    public List<FileUploadDto> findTop10ByALL(){

        return  fileUploadRepositoryCustom.findTop10ByALL();

    }
    //FILE 업로드 저장
    public FileUpload save(MultipartFile file) {
        FileUpload newfile = new FileUpload();
        //저장폴더관리(년월일)
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMdd");
        String filePath = "/"+ uploadPath + "/" + date.format(new Date());

        // 파일 중복명 처리
        String genId = UUID.randomUUID().toString();
        genId = genId.replace("-", "");
        String originalfileName = file.getOriginalFilename();
        String fileExtension = getExtension(originalfileName);
        String storedFileName = genId + "." + fileExtension;
        try {
            //AWS S3저장후
            awss3Service.uploadObject(file, storedFileName, filePath);

            //RDS저장
            newfile.setSaveFileName(storedFileName);
            newfile.setFileName(file.getOriginalFilename());
            newfile.setContentType(file.getContentType());
            newfile.setFilePath(filePath +'/' + storedFileName);
            newfile.setSize(file.getSize());
            newfile.setInsertDateTime(LocalDateTime.now());
            FileUpload saveFile = fileUploadRepository.save(newfile);
            return saveFile;

        }catch (Exception e){
            log.error(" FileUpload Error! :"+e.toString());
            return null;
        }






    }

    private String getExtension(String fileName) {
        int dotPosition = fileName.lastIndexOf('.');

        if (-1 != dotPosition && fileName.length() - 1 > dotPosition) {
            return fileName.substring(dotPosition + 1);
        } else {
            return "";
        }
    }

}
