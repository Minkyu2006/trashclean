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
@Slf4j
public class IModelListDto {

    private Long id;

    private String filePath; // 파일경로 1
    private String saveFileName; // 파일경로 2

    private String mdNumber; // 모델 번호
    private String mdName; // 모델명
    private MasterCode emType; // 모델타입
    private MasterCode mdType; // 모델종류
    private String mdSubname; // 모델약칭
    private String mdRemark; // 모델특이사항
    private Double mdMaximumPayload; // 최대적재량
    private MasterCode mdUnit; // 단위

    public String getFilePath() {
        if(filePath==null){
            filePath = "/defaultimage";
            return filePath;
        }else{
            return filePath;
        }
    }

    public String getSaveFileName() {
        if(saveFileName==null){
            saveFileName = "/model.jpg";
            return saveFileName;
        }else{
            String savefile = "/s_"+saveFileName;
            return savefile;
        }
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

    public Double getMdMaximumPayload() {
        return mdMaximumPayload;
    }

    public String getMdUnit() {
        return mdUnit.getName();
    }
}
