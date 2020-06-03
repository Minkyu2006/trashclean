package kr.co.broadwave.aci.company;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-02-20
 * Time :
 * Remark : 업체등록 클래스 CompanyAccountDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CompanyAccountDto {
    private Long id; // 업체관리 고유ID
    private String csOperator; // 운영사명

    public Long getId() {
        return id;
    }

    public String getCsOperator() {
        return csOperator;
    }
}
