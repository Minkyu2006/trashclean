package kr.co.broadwave.aci.position;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-05-27
 * Time :
 * Remark : 거점등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class PositionMapperDto {

    private String psZoneCode; // 거점코드
    private Long psCountry; // 국가
    private Long psLocation; // 지역
    private String psZoneName; // 거점명칭
    private String psPostnumber; // 우편번호
    private String psAddress; // 주소
    private String psDetailAddress; // 상세주소
    private Double psLatitude; // GPS위도
    private Double psLongitude; // GPS경도

    public String getPsZoneCode() {
        return psZoneCode;
    }

    public Long getPsCountry() {
        return psCountry;
    }

    public Long getPsLocation() {
        return psLocation;
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
