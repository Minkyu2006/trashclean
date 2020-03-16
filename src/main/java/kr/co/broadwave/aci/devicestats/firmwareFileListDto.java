package kr.co.broadwave.aci.devicestats;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
public class firmwareFileListDto {

    private Long id;
    private String fileFullPath; // 파일경로
    private MasterCode efType; // 모델타입
    private String efVer; // 버전
    private String efRemark; // 비고

    public String getFileFullPath() {
        return fileFullPath;
    }

    public Long getId() {
        return id;
    }

    public String getEfType() {
        return efType.getName();
    }

    public String getEfVer() {
        return efVer;
    }

    public String getEfRemark() {
        return efRemark;
    }
}
