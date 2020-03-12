package kr.co.broadwave.aci.equipment;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-03-10
 * Time :
 * Remark :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentCollectionTypeDto {

    private String emNumber; // 장비 번호
    private String mdName; // 모델 약칭
    private String mdTypeName; // 모델타입이름

    public String getMdname() {
        return mdName;
    }

    public String getMdTypeName() {
        return mdTypeName;
    }

    public String getEmNumber() {
        return emNumber;
    }

}
