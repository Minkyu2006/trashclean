package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-02-04
 * Time :
 * Remark : 장비기본값셋팅 클래스 BaseListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentBaseListDto {

    private String emNumber; // 장비 번호
    private MasterCode emType; // 장비타입
    private MasterCode emCountry; // 국가
    private MasterCode emLocation; // 지역
    private Company company; // 소속사
    private IModel iModel; // 모델명,종류

    public String getEmNumber() {
        return emNumber;
    }

    public String getEmType() {
        return emType.getName();
    }

    public String getEmCountry() {
        return emCountry.getName();
    }

    public String getEmLocation() {
        return emLocation.getName();
    }

    public String getCompany() {
        return company.getCsOperator();
    }

    public String getiModel() {
        return iModel.getMdName()+"-"+iModel.getMdType().getName();
    }
}
