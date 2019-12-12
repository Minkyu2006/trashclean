package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.mastercode.MasterCode;
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
    private Long emCountry; // 국가
    private Long emLocation; // 지역
    private String emAwsNumber; // AWS상 Device ID
    private String emEmbeddedNumber; // 임베디드 기판 번호
    private String emSubName;
    private String operator;
    private Double emMaximumPayload;
    private Long emUnit;
    private String emInstallDate;
    private Double emLatitude; // 위도
    private Double emHardness; // 경도

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

    public Double getEmMaximumPayload() {
        return emMaximumPayload;
    }

    public Long getEmUnit() {
        return emUnit;
    }

    public String getEmInstallDate() {
        String installDate = emInstallDate.replaceAll("-", "");
        return installDate;
    }
}
