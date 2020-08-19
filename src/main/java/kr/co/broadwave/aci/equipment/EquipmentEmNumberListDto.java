package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-08-18
 * Time :
 * Remark : EquipmentEmNumberListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentEmNumberListDto {

    private String emNumber; // 장비 번호

    public String getEmNumber() {
        return emNumber;
    }

    public void setEmNumber(String emNumber) {
        this.emNumber = emNumber;
    }
}
