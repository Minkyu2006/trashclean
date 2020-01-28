package kr.co.broadwave.aci.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Minkyu
 * Date : 2020-01-23
 * Remark :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDtoCollectionList {
    private String userid;
    private String username;
    private AccountRole role;

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role.getDesc();
    }

}
