package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.files.FileUpload;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time : 10:34
 * Remark : 모델등록 클래스 ListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class IModelListDto {

//    @Value("${aci.aws.s3.bucket.url}")
//    private String AWSS3URL;

    private Long id;
    private FileUpload mdFileid;
    private String mdNumber; // 모델 번호
    private String mdName; // 모델명
    private MasterCode emType; // 모델타입
    private MasterCode mdType; // 모델종류
    private String mdSubname; // 모델약칭
    private String mdRemark; // 모델특이사항

    public String getMdFileid() {
        String fileurl = mdFileid.getFilePath()+"/"+mdFileid.getSaveFileName(); // 원본 크기
        //String fileurl = mdFileid.getFilePath()+"/s_"+mdFileid.getSaveFileName(); //미리보기 크기
        return fileurl;
    }

    public Long getId() {
        return id;
    }

    public String getMdNumber() {
        return mdNumber;
    }

    public String getMdName() {
        return mdName;
    }

    public String getEmType() {
        return emType.getName();
    }

    public String getMdType() {
        return mdType.getName();
    }

    public String getMdSubname() {
        return mdSubname;
    }

    public String getMdRemark() {
        return mdRemark;
    }


}
