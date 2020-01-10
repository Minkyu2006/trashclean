package kr.co.broadwave.aci.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2020-01-09
 * Time :
 * Remark :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMapperDtoModify {
    private String oldpassword;
    private String newpassword;
    private String passwordconfirm;
}
