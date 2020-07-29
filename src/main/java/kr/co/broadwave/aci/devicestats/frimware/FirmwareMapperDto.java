package kr.co.broadwave.aci.devicestats.frimware;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-03-12
 * Time :
 * Remark : 업데이트파일등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class FirmwareMapperDto {

    private Long efType; // 모델타입
    private String efVer; // 팜웨어버전
    private String efRemark; // 비고

    public Long getEfType() {
        return efType;
    }

    public String getEfVer() {
        return "v"+efVer;
    }

    public String getEfRemark() {
        return efRemark;
    }
}
