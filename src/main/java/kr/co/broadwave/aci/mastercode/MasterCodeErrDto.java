package kr.co.broadwave.aci.mastercode;

import kr.co.broadwave.aci.bscodes.CodeType;
import lombok.*;

import javax.persistence.Column;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Remark :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class MasterCodeErrDto {

    private String name;
    private String bcRef1;
    private String bcRef2;

    public String getName() {
        return name;
    }

    public String getBcRef1() {
        return bcRef1;
    }

    public String getBcRef2() {
        return bcRef2;
    }
}
