package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.bscodes.ProcStatsType;
import lombok.*;

import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Time :
 * Remark : 수거관리 클래스 Dto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionDto {
    private Long id;
    private LocalDateTime insertDateTime;
    private String insert_id;
    private Integer seq;
    private ProcStatsType procStatsType;

    public String getProcStatsType() {
        return procStatsType.getCode();
    }

    public Integer getSeq() {
        return seq;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

    public String getInsert_id() {
        return insert_id;
    }

    public Long getId() {
        return id;
    }

}
