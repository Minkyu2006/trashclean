package kr.co.broadwave.aci.vehicle;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

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
public class VehicleListDto {
    private Long id;
    private String vcNumber; // 차량번호
    private String vcName; // 차량명
    private String vcShape; // 차량소유구분
    private String vcUsage; // 차량용도
    private String vcStartDate; // 운행시작일
    private String vcEndDate; // 운행종료일
    private String vcManagement; // 차량관리
    private String company;

    public Long getId() {
        return id;
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

    public String getCompany() {
        return company;
    }
}
