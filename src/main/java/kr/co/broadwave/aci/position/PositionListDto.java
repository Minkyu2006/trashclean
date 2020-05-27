package kr.co.broadwave.aci.position;

import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark : 거점리스트 클래스 PositionListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class PositionListDto {
    private Long id; // 고유값id
    private String psZoneCode; // 거점코드

    private MasterCode psCountry; // 국가
    private MasterCode psLocation; // 지역
    private String psZoneName; // 거점명칭
    private String psPostnumber; // 우편번호
    private String psAddress; // 주소
    private String psDetailAddress; // 상세주소
    private Double psLatitude; // GPS위도
    private Double psLongitude; // GPS경도

    public Long getId() {
        return id;
    }

    public String getPsCountry() {
        return psCountry.getName();
    }

    public String getPsLocation() {
        return psLocation.getName();
    }

    public String getPsZoneCode() {
        return psZoneCode;
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
}
