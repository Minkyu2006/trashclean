package kr.co.broadwave.aci.collection.iTainerCollection;

import kr.co.broadwave.aci.bscodes.AccordiType;
import kr.co.broadwave.aci.bscodes.CiStatusType;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-06-02
 * Time :
 * Remark : CollectionTaskInstallDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionTaskInstallDto {

    private String ciCode; // 작업코드

    private AccordiType ciType; // 구분(배치,수거,배치/수거) -> enum관리

    private String ciPriority; // 우선순위(보통/긴급) - 마스터코드등록관리

    private String psBaseCode; // 거점코드

    private String deviceid; // 장비코드(iTainer)

    private String accountuserid; // 유저아이디(고유)
    private String accountname; // 유저이름

    private String vehiclenumber; // 차량번호
    private String vehiclename; // 차량명

    private String ciRemark; // 특이사항

    private CiStatusType ciStatus; // 배치상태

    public String getCiStatus() {
        return ciStatus.getDesc();
    }

    public String getCiCode() {
        return ciCode;
    }

    public String getCiType() {
        return ciType.getCode();
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

    public String getAccountname() {
        return accountname;
    }

    public String getAccountuserid() {
        return accountuserid;
    }

    public String getVehiclenumber() {
        return vehiclenumber;
    }

    public String getVehiclename() {
        return vehiclename;
    }

    public String getCiRemark() {
        return ciRemark;
    }

}
