package kr.co.broadwave.aci.equipment;

import kr.co.broadwave.aci.accounts.AccountRole;
import kr.co.broadwave.aci.bscodes.ApprovalType;
import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Minkyu
 * Date : 2019-10-31
 * Time : 16:45
 * Remark : 장비등록 클래스 Dto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class EquipmentDto {

    private String emNumber; // 장비 번호

    private String emCerealNumber; // 장비 시리얼 번호
    private String emDesignation; // 장비명칭
    private String emType; // 장비타입
    private String emAwsNumber; // AWS상 Device ID
    private String emAgency; // 통신사정보(생략)
    private String emEmbeddedNumber; // 임베디드 기판 번호
    private String emNowState; // 현재상태

    private LocalDateTime insertDateTime;
    private String insert_id;
    private LocalDateTime modifyDateTime;
    private String modify_id;
}
