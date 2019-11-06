package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import lombok.*;

/**
 * @author InSeok
 * Date : 2019-08-02
 * Remark :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class MasterCodeDto {
    private Long id;
    private CodeType codeType;
    private String code;
    private String name;
    private String remark;

    public Long getId() {
        return id;
    }

    public String getCodeType() {
        return codeType.getDesc();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getRemark() {
        return remark;
    }

}
