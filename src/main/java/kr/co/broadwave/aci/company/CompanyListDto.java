package kr.co.broadwave.aci.company;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 16:45
 * Remark : 업체등록 클래스 ListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CompanyListDto {
    private Long id; // 업체관리 고유ID

    private String csNumber; // 관리코드

    private String csOperator; // 운영사명
    private MasterCode csDivision; // 업체구분
    private MasterCode csRegional; // 운영권역
    private String csRepresentative; // 대표자
    private String csBuisnessNumber; // 사업자번호
    private String csManager; // 담당자
    private String csTelephone; // 전화번호
    private String csFax; // 팩스번호

    public Long getId() {
        return id;
    }

    public String getCsNumber() {
        return csNumber;
    }

    public String getCsOperator() {
        return csOperator;
    }

    public String getCsDivision() {
        return csDivision.getName();
    }

    public String getCsRegional() {
        return csRegional.getName();
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
}
