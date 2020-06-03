package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-02-21
 * Time :
 * Remark : 수거업무등록 모델,장비타입 가져오는 클래스 EquipmentCollectionRegDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class EquipmentCollectionRegDto {

    private Equipment id; // 고유값 아이디
    private String emNumber; // 장비 번호
    private MasterCode emType; // 장비타입
    private String mdSubname;
    private String mdTypeName; // 모델타입이름

    public String getMdSubname() {
        return mdSubname;
    }

    public String getMdTypeName() {
        return mdTypeName;
    }

    public Equipment getId() {
        return id;
    }

    public String getEmNumber() {
        return emNumber;
    }

    public MasterCode getEmType() {
        return emType;
    }

}
