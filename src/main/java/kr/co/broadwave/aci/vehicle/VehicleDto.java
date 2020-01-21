package kr.co.broadwave.aci.vehicle;

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
public class VehicleDto {
    private Long Id;
    private String vcNumber; // 차량번호
    private String vcName; // 차량명
    private String vcShape; // 차량소유구분
    private String vcUsage; // 차량용도
    private String vcStartDate; // 운행시작일
    private String vcEndDate; // 운행종료일
    private String vcManagement; // 차량관리
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

    public String getVcShape() {
        return vcShape;
    }

    public String getVcUsage() {
        return vcUsage;
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
