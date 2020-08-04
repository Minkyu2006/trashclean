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
    private String emDashboard; // 대시보드가능여부
    private String emEmbeddedNumber; // 임베디드 기판 번호

    private Company company; // 소속운영사
    private IModel mdId; // 모델

    private String emInstallDate; // 설치일자
    private String emSubName; // 장비약칭
    private Double emLatitude; // 위도
    private Double emLongitude; // 경도

    private Double vInterval; // 센서 데이터 주기적 송신시간
    private Double vPresstime; // 압축 정지시간
    private Double vInputtime; // 투입구 열림 시간
    private Double vSolenoidtime; // 솔레노이드 열림 시간
    private Double vYellowstart; // 쓰레기량에 따른 노랑색등의 경계값
    private Double vRedstart; // 쓰레기량에 따른 적색등의 경계값

    private String vManager; // 담당수거원이름
    private String vManagerCall; // 담당수거원번호
    private String vManagerid; // 아이테이너장비 아이디
    private String vManagerPass; // 아이테이너장비 비번
    private String vLanguage; // 사용언어
    private String vMqttInterval; // mqtt 데이터전송주기(단위,초)
    private String vLoraInterval; // lora 데이터 전송주기(단위,초)
    private String vScaleSafeInterval; //
    private String vShutterIdleInterval; //
    private String vWastePressInterval; //
    private String vWasteCapacity; //
    private String vOzonTime; //
    private String vPayPreamt; //
    private String vPayUnitPrice; // 기준가격
    private String vPayUnitWeight; // 기준무게
    private String vPayMethod; //

    public String getEmDashboard() {
        return emDashboard;
    }

    public String getvManager() {
        return vManager;
    }

    public String getvManagerCall() {
        return vManagerCall;
    }

    public String getvManagerid() {
        return vManagerid;
    }

    public String getvManagerPass() {
        return vManagerPass;
    }

    public String getvLanguage() {
        return vLanguage;
    }

    public String getvMqttInterval() {
        return vMqttInterval;
    }

    public String getvLoraInterval() {
        return vLoraInterval;
    }

    public String getvScaleSafeInterval() {
        return vScaleSafeInterval;
    }

    public String getvShutterIdleInterval() {
        return vShutterIdleInterval;
    }

    public String getvWastePressInterval() {
        return vWastePressInterval;
    }

    public String getvWasteCapacity() {
        return vWasteCapacity;
    }

    public String getvOzonTime() {
        return vOzonTime;
    }

    public String getvPayPreamt() {
        return vPayPreamt;
    }

    public String getvPayUnitPrice() {
        return vPayUnitPrice;
    }

    public String getvPayUnitWeight() {
        return vPayUnitWeight;
    }

    public String getvPayMethod() {
        return vPayMethod;
    }

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

    public Double getEmLongitude() {
        return emLongitude;
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
