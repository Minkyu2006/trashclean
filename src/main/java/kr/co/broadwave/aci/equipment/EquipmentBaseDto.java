package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-02-05
 * Time :
 * Remark : 장비기본값셋팅 클래스 Dto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentBaseDto {
    private Long id; // 장비 고유ID
    private String emNumber; // 장비 번호

    private String emCerealNumber; // 장비 시리얼 번호
    private String emDesignation; // 장비명칭

    private MasterCode emTypeId; // 모델타입 아이디
    private MasterCode emCountryId; // 국가(Id값)
    private MasterCode emLocationId; // 지역(Id값)

    private String emAwsNumber; // AWS상 Device ID
    private String emEmbeddedNumber; // 임베디드 기판 번호

    private Company company; // 소속운영사
    private IModel mdId; // 모델

    private String emInstallDate; // 설치일자
    private String emSubName; // 장비약칭

    private Double emLatitude; // 위도
    private Double emLongitude; // 경도

    private LocalDateTime insertDateTime;
    private String insert_id;

    private String emCertificationNumber; // AWS보안인증서코드

    public String getEmCertificationNumber() {
        return emCertificationNumber;
    }

    public MasterCode getEmTypeId() {
        return emTypeId;
    }

    public MasterCode getEmCountryId() {
        return emCountryId;
    }

    public MasterCode getEmLocationId() {
        return emLocationId;
    }

    public Company getCompany() {
        return company;
    }

    public IModel getMdId() {
        return mdId;
    }

    public String getEmCerealNumber() {
        return emCerealNumber;
    }

    public String getEmDesignation() {
        return emDesignation;
    }

    public Long getId() {
        return id;
    }

    public String getEmNumber() {
        return emNumber;
    }

    public String getEmAwsNumber() {
        return emAwsNumber;
    }

    public String getEmEmbeddedNumber() {
        return emEmbeddedNumber;
    }

    public String getEmInstallDate() {
        return emInstallDate;
    }

    public String getEmSubName() {
        return emSubName;
    }

    public Double getEmLatitude() {
        return emLatitude;
    }

    public Double getEmLongitude() {
        return emLongitude;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

    public String getInsert_id() {
        return insert_id;
    }
}
