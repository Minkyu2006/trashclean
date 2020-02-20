package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:45
 * Remark : 장비등록 클래스 Dto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentDto {
    private Long id; // 장비 고유ID
    private String emNumber; // 장비 번호
    private String emCerealNumber; // 장비 시리얼 번호
    private String emDesignation; // 장비명칭

    private MasterCode emType; // 모델타입 이름
    private MasterCode emTypeId; // 모델타입 아이디
    private MasterCode emCountryId; // 국가(Id값)
    private MasterCode emCountryName; // 국가(Name값)
    private MasterCode emLocationId; // 지역(Id값)
    private MasterCode emLocationName; // 지역(Name값)

    private String emAwsNumber; // AWS상 Device ID
    private String emEmbeddedNumber; // 임베디드 기판 번호

    private Company company; // 소속운영사
    private IModel mdId; // 모델

    private String emInstallDate; // 설치일자
    private String emSubName; // 장비약칭
    private Double emLatitude; // 위도
    private Double emHardness; // 경도

    private Double vInterval; // 센서 데이터 주기적 송신시간
    private Double vPresstime; // 압축 정지시간
    private Double vInputtime; // 투입구 열림 시간
    private Double vSolenoidtime; // 솔레노이드 열림 시간
    private Double vYellowstart; // 쓰레기량에 따른 노랑색등의 경계값
    private Double vRedstart; // 쓰레기량에 따른 적색등의 경계값

    private String emCertificationNumber; // AWS보안인증서코드

    public String getEmCertificationNumber() {
        return emCertificationNumber;
    }

    public Double getvYellowstart() {
        return vYellowstart;
    }

    public Double getvRedstart() {
        return vRedstart;
    }

    public Double getvInterval() {
        return vInterval;
    }

    public Double getvPresstime() {
        return vPresstime;
    }

    public Double getvInputtime() {
        return vInputtime;
    }

    public Double getvSolenoidtime() {
        return vSolenoidtime;
    }

    public Double getEmLatitude() {
        return emLatitude;
    }

    public Double getEmHardness() {
        return emHardness;
    }

    public Long getId() {
        return id;
    }

    public String getEmNumber() {
        return emNumber;
    }

    public String getEmCerealNumber() {
        return emCerealNumber;
    }

    public String getEmDesignation() {
        return emDesignation;
    }

    public String getEmType() {
        return emType.getName();
    }

    public Long getEmTypeId() {
        return emTypeId.getId();
    }

    public Long getEmCountryId() {
        return emCountryId.getId();
    }

    public String getEmCountryName() {
        return emCountryName.getName();
    }

    public Long getEmLocationId() {
        return emLocationId.getId();
    }

    public String getEmLocationName() {
        return emLocationName.getName();
    }

    public String getEmAwsNumber() {
        return emAwsNumber;
    }

    public String getEmEmbeddedNumber() {
        return emEmbeddedNumber;
    }

    public Company getCompany() {
        return company;
    }

    public IModel getMdId() {
        return mdId;
    }

    public String getEmInstallDate() {
        return emInstallDate;
    }

    public String getEmSubName() {
        return emSubName;
    }

}
