package kr.co.broadwave.aci.files;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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
    private final ModelMapper modelMapper;


    @Autowired
    public FileUploadService(AWSS3Service awss3Service, FileUploadRepository fileUploadRepository, FileUploadRepositoryCustom fileUploadRepositoryCustom, ModelMapper modelMapper) {
        this.awss3Service = awss3Service;
        this.fileUploadRepository = fileUploadRepository;
        this.fileUploadRepositoryCustom = fileUploadRepositoryCustom;
        this.modelMapper = modelMapper;
    }
    //파일정보가져오기
    public FileUploadDto findById(Long fileId){
        Optional<FileUpload> optionalFileUpload = fileUploadRepository.findById(fileId);
        if (optionalFileUpload.isPresent()) {
            return modelMapper.map(optionalFileUpload.get(), FileUploadDto.class);
        } else {
            return null;
        }
    }
    //파일삭제
    public void del(Long fileId){
        Optional<FileUpload> optionalFileUpload = fileUploadRepository.findById(fileId);
        if (optionalFileUpload.isPresent()) {
            FileUpload fileUpload = optionalFileUpload.get();
            awss3Service.deleteObject(fileUpload.getFilePath(),fileUpload.getSaveFileName());
            //이미지면 썸네일도삭제
            if (fileUpload.getContentType().substring(0,5).toUpperCase().equals("IMAGE")){
                awss3Service.deleteObject(fileUpload.getFilePath(),"s_" + fileUpload.getSaveFileName());
            }
            fileUploadRepository.delete(fileUpload);
        }
    }
    //파일객체가져오디(다운로드용)
    public byte[] fileDownload(String filePath,String filename){
        try {
            return awss3Service.getObject(filePath, filename);
        }catch (Exception e){
            log.error(e.toString());
            return null;
        }

    }

    //파일목록조회
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
        String originalfileName = file.getName();
        String fileExtension = getExtension(originalfileName);
        String storedFileName = genId + "." + fileExtension+"png";
        try {
            //AWS S3저장후
            awss3Service.uploadObject(file, storedFileName, filePath);

            //RDS저장
            newfile.setSaveFileName(storedFileName);
            newfile.setFileName(file.getOriginalFilename());
            newfile.setContentType(file.getContentType());
            newfile.setFilePath(filePath);
            newfile.setFileFullPath(filePath +'/' + storedFileName);
            newfile.setSize(file.getSize());
            newfile.setInsertDateTime(LocalDateTime.now());
            return fileUploadRepository.save(newfile);

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
