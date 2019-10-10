package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author InSeok
 * Date : 2019-08-02
 * Remark :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterCodeMapperDto {
    private CodeType codeType;
    private String code;
    private String name;
    private String remark;
}
