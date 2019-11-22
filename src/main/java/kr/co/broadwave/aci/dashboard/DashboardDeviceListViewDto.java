package kr.co.broadwave.aci.dashboard;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2019-11-22
 * Remark :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class DashboardDeviceListViewDto {
    private Long id; // 장비 고유ID

    private String emNumber; // 장비 번호
    private MasterCode emType; // 장비타입
    private Double emMaximumPayload; // 최대적재량
    private MasterCode emUnit; // 단위
    private Company company; // 소속운영사
    private MasterCode emLocation; // 지역
    private MasterCode emCountry; // 국가
    private String emInstallDate; // 설치일자

    public Long getId() {
        return id;
    }

    public String getEmNumber() {
        return emNumber;
    }

    public String getEmType() {
        return emType.getName();
    }

    public Double getEmMaximumPayload() {
        return emMaximumPayload;
    }

    public String getEmUnit() {
        return emUnit.getName();
    }

    public String getCompany() {
        return company.getCsOperator();
    }

    public String getEmLocation() {
        return emLocation.getName();
    }

    public String getEmCountry() {
        return emCountry.getName();
    }

    public String getEmInstallDate() {
        String installDate = emInstallDate.substring(0,4)+"년 "+emInstallDate.substring(4,6)+"월 "+emInstallDate.substring(6,8)+"일";
        return installDate;
    }
}
