package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:45
 * Remark : 업체등록 클래스 Dto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CompanyDto {
    private Long id; // 업체관리 고유ID
    private String csNumber; // 관리코드

    private String csOperator; // 운영사명
    private String csOperatorSub; // 운영사명(약칭)
    private MasterCode csDivisionId; // 업체구분(Id)
    private MasterCode csDivisionName; // 업체구분(Name)
    private MasterCode csRegionalId; // 운영권역(Id)
    private MasterCode csRegionalName; // 운영권역(Name)
    private String csRepresentative; // 대표자
    private String csBuisnessNumber; // 사업자번호
    private String csManager; // 담당자
    private String csTelephone; // 전화번호
    private String csFax; // 팩스번호
    private Double csLatitude; // 차량위치위도
    private Double csHardness; // 차량위치경도

    private LocalDateTime insertDateTime;
    private String insert_id;
    private LocalDateTime modifyDateTime;
    private String modify_id;

    public Long getId() {
        return id;
    }

    public String getCsNumber() {
        return csNumber;
    }

    public String getCsOperator() {
        return csOperator;
    }

    public String getCsOperatorSub() {
        return csOperatorSub;
    }

    public Long getCsDivisionId() {
        return csDivisionId.getId();
    }

    public String getCsDivisionName() {
        return csDivisionName.getName();
    }

    public Long getCsRegionalId() {
        return csRegionalId.getId();
    }

    public String getCsRegionalName() {
        return csRegionalName.getName();
    }

    public String getCsRepresentative() {
        return csRepresentative;
    }

    public String getCsBuisnessNumber() {
        return csBuisnessNumber;
    }

    public String getCsManager() {
        return csManager;
    }

    public String getCsTelephone() {
        return csTelephone;
    }

    public String getCsFax() {
        return csFax;
    }

    public Double getCsLatitude() {
        return csLatitude;
    }

    public Double getCsHardness() {
        return csHardness;
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
