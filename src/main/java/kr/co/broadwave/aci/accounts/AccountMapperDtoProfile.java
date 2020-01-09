package kr.co.broadwave.aci.accounts;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2020-01-08
 * Time :
 * Remark : AccountMapperDtoProfile
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMapperDtoProfile {
    private String username;
    private String cellphone;
    private String email;
    private String team;
    private Long position;
}
