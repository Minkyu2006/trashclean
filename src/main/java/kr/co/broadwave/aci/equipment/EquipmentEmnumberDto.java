package kr.co.broadwave.aci.equipment;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-15
 * Time :
 * Remark :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentEmnumberDto {

    private String emNumber; // 장비 번호

    public String getEmNumber() {
        return emNumber;
    }
}
