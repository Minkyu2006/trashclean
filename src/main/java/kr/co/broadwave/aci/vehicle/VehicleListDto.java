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

    public StringBuffer getVcStartDate() {
        StringBuffer startDate = new StringBuffer(vcStartDate); // ex)2020-11-04
        startDate.insert(4,'-');
        startDate.insert(7,'-');
        return startDate;
    }

    public Object getVcEndDate() {
        if(!vcEndDate.equals("")){
            StringBuffer endDate = new StringBuffer(vcEndDate);
            endDate.insert(4,'-');
            endDate.insert(7,'-');
            return endDate;
        }else{
            return "";
        }
    }

    public String getVcManagement() {
        return vcManagement;
    }

    public String getCompany() {
        return company;
    }
}
