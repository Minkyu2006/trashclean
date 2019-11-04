package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.bscodes.EmType;
import kr.co.broadwave.aci.bscodes.NowStateType;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 14:18
 * Remark : 장비등록 클래스 ListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentListDto {
    private Long id; // 장비 고유ID

    private String emNumber; // 장비 번호

    private String emCerealNumber; // 장비 시리얼 번호
    private String emDesignation; // 장비명칭
    private MasterCode emType; // 장비타입
    private MasterCode emCountry; // 국가
    private MasterCode emLocation; // 지역
    private String emAwsNumber; // AWS상 Device ID
    private String emEmbeddedNumber; // 임베디드 기판 번호
    private String emAgency; // 소속운영사

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

    public String getEmCountry() {
        return emCountry.getName();
    }

    public String getEmLocation() {
        return emLocation.getName();
    }

    public String getEmAwsNumber() {
        return emAwsNumber;
    }

    public String getEmEmbeddedNumber() {
        return emEmbeddedNumber;
    }

    public String getEmAgency() {
        return emAgency;
    }
}
