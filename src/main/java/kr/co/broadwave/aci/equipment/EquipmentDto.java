package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.bscodes.EmType;
import kr.co.broadwave.aci.bscodes.NowStateType;
import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
