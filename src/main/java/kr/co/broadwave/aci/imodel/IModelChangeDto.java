package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2019-01-06
 * Time : 10:34
 * Remark : IModelChangeDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class IModelChangeDto {

    private Long id;
    private String mdName;
    private MasterCode mdType;

    public Long getId() {
        return id;
    }

    public String getMdName() {
        return mdName;
    }

    public String getMdType() {
        return mdType.getName();
    }
}
