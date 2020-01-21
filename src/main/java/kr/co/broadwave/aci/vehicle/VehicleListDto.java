package kr.co.broadwave.aci.vehicle;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-21
 * Time :
 * Remark : 차량등록 클래스 ListDto
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
    private String vcState; // 차량상태
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

    public String getVcState() {
        return vcState;
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
