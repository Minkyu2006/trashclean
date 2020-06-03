package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-06-01
 * Time :
 * Remark :  배치/수거업무 거점코드 가져오기용.
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class PositionPopDto {

    private String psZoneCode; // 거점코드
    private String psZoneName; // 거점명칭
    private String deviceid; // 거점명칭



    public String getDeviceid() {
        return deviceid;
    }

    public String getPsZoneCode() {
        return psZoneCode;
    }

    public String getPsZoneName() {
        return psZoneName;
    }
}
