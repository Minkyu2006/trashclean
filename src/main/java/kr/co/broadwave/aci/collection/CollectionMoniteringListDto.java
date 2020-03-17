package kr.co.broadwave.aci.collection;

import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-01-28
 * Time :
 * Remark : 수거관리 클래스 DtoList
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionMoniteringListDto {
    private Long id;
    private String deviceid;
    private LocalDateTime completeDateTime;

    public String getDeviceid() {
        return deviceid;
    }

    public LocalDateTime getCompleteDateTime() {
        return completeDateTime;
    }

    public Long getId() {
        return id;
    }
}
