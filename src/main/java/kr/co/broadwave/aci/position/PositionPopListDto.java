package kr.co.broadwave.aci.position;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-06-03
 * Time :
 * Remark : 배치/수거용 거점리스트 클래스 PositionPopListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class PositionPopListDto {
    private Long id; // 고유값id

    private String psBaseName; // 거점명칭
    private String deviceid; // 우편번호
    private LocalDateTime installdate; // 배치일자

    public Long getId() {
        return id;
    }

    public String getPsBaseName() {
        return psBaseName;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getInstalldate() {
        if(installdate != null) {
            return installdate.toString().substring(0,4)+installdate.toString().substring(5, 7)+installdate.toString().substring(8, 10);
        }else{
            return null;
        }
    }
}
