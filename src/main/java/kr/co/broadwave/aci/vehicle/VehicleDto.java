package kr.co.broadwave.aci.vehicle;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : 차량등록 클래스 Dto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class VehicleDto {
    private Long Id;
    private String vcNumber; // 차량번호
    private String vcName; // 차량명
    private MasterCode vcShape; // 차량소유구분
    private MasterCode vcUsage; // 차량용도
    private MasterCode vcState; // 차량상
    private String vcStartDate; // 운행시작일
    private String vcEndDate; // 운행종료일
    private String vcManagement; // 차량관리번호
    private Company company; // 소속운영사

    public Long getId() {
        return Id;
    }

    public String getVcNumber() {
        return vcNumber;
    }

    public String getVcName() {
        return vcName;
    }

    public Long getVcShape() {
        return vcShape.getId();
    }

    public Long getVcUsage() {
        return vcUsage.getId();
    }

    public Long getVcState() {
        return vcState.getId();
    }

    public String getVcStartDate() {
        return vcStartDate;
    }

    public String getVcEndDate() {
        return vcEndDate;
    }

    public String getVcManagement() {
        return vcManagement;
    }

    public Company getCompany() {
        return company;
    }
}
