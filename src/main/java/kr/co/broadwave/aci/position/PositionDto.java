package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark : 거점등록 클래스
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class PositionDto {

    private String psZoneCode; // 거점코드

    private MasterCode psCountry; // 국가
    private MasterCode psLocation; // 지역
    private String psZoneName; // 거점명칭
    private String psPostnumber; // 우편번호
    private String psAddress; // 주소
    private String psDetailAddress; // 상세주소
    private Double psLatitude; // GPS위도
    private Double psLongitude; // GPS경도
    private LocalDateTime insertDateTime; // 작성일자
    private String insert_id; // 작성자
    private LocalDateTime modifyDateTime; // 변경일자
    private String modify_id; // 변경자

    public String getPsZoneCode() {
        return psZoneCode;
    }

    public String getPsCountry() {
        return psCountry.getName();
    }

    public String getPsLocation() {
        return psLocation.getName();
    }

    public String getPsZoneName() {
        return psZoneName;
    }

    public String getPsPostnumber() {
        return psPostnumber;
    }

    public String getPsAddress() {
        return psAddress;
    }

    public String getPsDetailAddress() {
        return psDetailAddress;
    }

    public Double getPsLatitude() {
        return psLatitude;
    }

    public Double getPsLongitude() {
        return psLongitude;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

    public String getInsert_id() {
        return insert_id;
    }

    public LocalDateTime getModifyDateTime() {
        return modifyDateTime;
    }

    public String getModify_id() {
        return modify_id;
    }
}
