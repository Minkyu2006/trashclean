package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.bscodes.EmType;
import kr.co.broadwave.aci.bscodes.NowStateType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2019-11-01
 * Time : 14:18
 * Remark : 장비등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class EquipmentMapperDto {

    private String emNumber; // 장비 번호

    private String emCerealNumber; // 장비 시리얼 번호
    private String emDesignation; // 장비명칭
    private EmType emType; // 장비타입
    private String emAwsNumber; // AWS상 Device ID
    private String emEmbeddedNumber; // 임베디드 기판 번호
    private NowStateType emNowState; // 현재상태
    private String emAgency; // 소속운영사

}
