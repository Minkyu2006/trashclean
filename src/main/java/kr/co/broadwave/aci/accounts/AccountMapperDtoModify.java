package kr.co.broadwave.aci.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author InSeok
 * Date : 2019-06-11
 * Time : 10:29
 * Remark :
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMapperDtoModify {
    private String userid;
    private String username;
    private String password;
    private String oldpassword;
    private String passwordconfirm;
    private String email;
    private String cellphone;

}
