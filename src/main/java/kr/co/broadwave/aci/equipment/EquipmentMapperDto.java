package kr.co.broadwave.aci.equipment;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 14:18
 * Remark : 장비등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class EquipmentMapperDto {

    private String emNumber; // 장비 번호

    private String emCerealNumber; // 장비 시리얼 번호
    private String emDesignation; // 장비명칭
    private Long emType; // 장비타입
    private Long iModel; // 모델ID저장
    private Long emCountry; // 국가
    private Long emLocation; // 지역
    private String emAwsNumber; // AWS상 Device ID
    private String emEmbeddedNumber; // 임베디드 기판 번호
    private String emSubName;
    private String operator;
    private String emInstallDate;
    private Double emLatitude; // 위도
    private Double emHardness; // 경도

    private Double vInterval; // 센서 데이터 주기적 송신시간
    private Double vPresstime; // 압축 정지시간
    private Double vInputtime; // 투입구 열림 시간
    private Double vSolenoidtime; // 솔레노이드 열림 시간
    private Double vYellowstart; // 쓰레기량에 따른 노랑색등의 경계값
    private Double vRedstart; // 쓰레기량에 따른 적색등의 경계값

    private String emCertificationNumber; // 인증번호

    public String getEmCertificationNumber() {
        return emCertificationNumber;
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

    public Double getvYellowstart() {
        return vYellowstart;
    }

    public Double getvRedstart() {
        return vRedstart;
    }

    public Double getEmLatitude() {
        return emLatitude;
    }

    public Double getEmHardness() {
        return emHardness;
    }

    public String getEmSubName() {
        return emSubName;
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

    public Long getEmType() {
        return emType;
    }

    public Long getiModel() {
        return iModel;
    }

    public Long getEmCountry() {
        return emCountry;
    }

    public Long getEmLocation() {
        return emLocation;
    }

    public String getEmAwsNumber() {
        return emAwsNumber;
    }

    public String getEmEmbeddedNumber() {
        return emEmbeddedNumber;
    }

    public String getOperator() {
        return operator;
    }

    public String getEmInstallDate() {
        String installDate = emInstallDate.replaceAll("-", "");
        return installDate;
    }
}
