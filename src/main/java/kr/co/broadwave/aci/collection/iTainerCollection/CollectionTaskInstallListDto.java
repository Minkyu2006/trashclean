package kr.co.broadwave.aci.collection.iTainerCollection;

import kr.co.broadwave.aci.bscodes.AccordiType;
import kr.co.broadwave.aci.bscodes.CiStatusType;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-06-02
 * Time :
 * Remark : 배치/수거 리스트 클래스 CollectionTaskInstallListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionTaskInstallListDto {
    private String ciCode; // 작업코드
    private AccordiType ciType; // 구분(배치,수거,배치/수거) -> enum관리
    private String ciPriority; // 우선순위(보통/긴급) - 마스터코드등록관리
    private String psBaseCode; // 거점코드
    private String deviceid; // 장비코드(iTainer)
    private String accountId; // 수거원 조회하고 -> 선택한 수거원 고유아이디값 등록
    private String vehicleId; // 차량 조회하고 -> 선택한 차량 고유아이디값 등록
    private CiStatusType ciStatus; // 배치상태(지시|완료) -> enum관리

    public String getCiCode() {
        return ciCode;
    }

    public String getCiType() {
        return ciType.getDesc();
    }

    public String getCiPriority() {
        return ciPriority;
    }

    public String getPsBaseCode() {
        return psBaseCode;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getAccountId() {
        return accountId;
    }

    public String getCiStatus() {
        return ciStatus.getDesc();
    }
}
