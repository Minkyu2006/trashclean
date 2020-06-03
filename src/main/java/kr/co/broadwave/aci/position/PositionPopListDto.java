package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-06-03
 * Time :
 * Remark : 배치/수거용 거점리스트 클래스 PositionPopListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class PositionPopListDto {
    private Long id; // 고유값id

    private String psZoneName; // 거점명칭
    private String deviceid; // 우편번호


    public Long getId() {
        return id;
    }

    public String getPsZoneName() {
        return psZoneName;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
