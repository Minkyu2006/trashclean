package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-02-18
 * Time :
 * Remark : 장비리스트 EquipmentCollectionListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentCollectionListDto {

    private Long id; // 아이디값
    private String emNumber; // 장비 번호
    private MasterCode emType; // 장비타입
    private MasterCode emCountry; // 국가
    private MasterCode emLocation; // 지역
    private IModel iModel; // 모델명,종류
    private MasterCode emState; // 설비상태

    public Long getId() {
        return id;
    }

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

    public String getiModel() {
        return iModel.getMdName()+"-"+iModel.getMdType().getName();
    }

    public String getEmState() {
        if(emState==null || !emState.getName().equals("운영")){
            return null;
        }else{
            return emState.getName();
        }
    }

}
