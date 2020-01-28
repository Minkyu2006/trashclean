package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.company.Company;
import kr.co.broadwave.aci.imodel.IModel;
import kr.co.broadwave.aci.mastercode.MasterCode;
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
