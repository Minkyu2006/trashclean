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
    private String vcShape; // 차량소유구분
    private String vcUsage; // 차량용도
    private String vcStartDate; // 운행시작일
    private String vcEndDate; // 운행종료일
    private String vcManagement; // 차량관리
    private String operator; // 소속사이름받기

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

    public String getVcShape() {
        return vcShape;
    }

    public String getVcUsage() {
        return vcUsage;
    }

    public String getVcStartDate() {
        String startDate = vcStartDate.replaceAll("-", "");
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
