package kr.co.broadwave.aci.collection.iTainerCollection;

import kr.co.broadwave.aci.bscodes.CiStatusType;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-06-03
 * Time :
 * Remark : CollectionTaskInstallCheckDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionTaskInstallCheckDto {

    private String ciCode; // 작업코드
    private String psBaseCode; // 거점코드
    private CiStatusType ciStatus; // 거점코드

    public String getCiCode() {
        return ciCode;
    }

    public String getPsBaseCode() {
        return psBaseCode;
    }

    public String getCiStatus() {
        return ciStatus.getDesc();
    }
}
