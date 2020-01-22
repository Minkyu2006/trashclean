package kr.co.broadwave.aci.vehicle;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : 차량등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class VehicleMapperDto {
    private String Id;
    private String vcNumber; // 차량번호
    private String vcName; // 차량명
    private Long vcShape; // 차량소유구분
    private Long vcUsage; // 차량용도
    private Long vcState; // 차량상태
    private String vcStartDate; // 운행시작일
    private String vcEndDate; // 운행종료일
    private String vcManagement; // 차량관리
    private String operator; // 소속사이름받기

    public Long getVcShape() {
        return vcShape;
    }

    public Long getVcUsage() {
        return vcUsage;
    }

    public Long getVcState() {
        return vcState;
    }

    public String getId() {
        return Id;
    }

    public String getVcNumber() {
        return vcNumber;
    }

    public String getOperator() {
        return operator;
    }

    public String getVcName() {
        return vcName;
    }

    public String getVcStartDate() {
        String startDate = vcStartDate.replaceAll("-", ""); // ex)20201104
        return startDate;
    }

    public String getVcEndDate() {
        String endDate = vcEndDate.replaceAll("-", "");
        return endDate;
    }

    public String getVcManagement() {
        return vcManagement;
    }
}
