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
//        String startInstall = [emInstallDate.,startInstall[1],startInstall[2],startInstall[3],"-",startInstall[4],startInstall[5],"-",startInstall[6],startInstall[7]];
//        String installArray = startInstall.join("");
//        String installArray = emInstallDate;
//        System.out.println("emInstallDate 받아온 설치일자 : "+emInstallDate);
        return emInstallDate;
    }
}
