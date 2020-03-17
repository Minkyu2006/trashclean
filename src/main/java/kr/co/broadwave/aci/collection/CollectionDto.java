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
    private String ctCode;
    private String yyyymmdd;
    private Integer seq;
    private ProcStatsType procStatsType;
    private String insert_id;
    private LocalDateTime insertDateTime;

    public String getYyyymmdd() {
        return yyyymmdd;
    }

    public String getCtCode() {
        return ctCode;
    }

    public ProcStatsType getProcStatsType() {
        return procStatsType;
    }

    public Integer getSeq() {
        return seq;
    }

    public String getInsert_id() {
        return insert_id;
    }

    public LocalDateTime getInsertDateTime() {
        return insertDateTime;
    }

    public Long getId() {
        return id;
    }

}
