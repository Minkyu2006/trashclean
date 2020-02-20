package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.bscodes.ApprovalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 10:11
 * Remark : Account Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountMapperDto {

    private String userid;
    private String username;
    private String password;
    private String email;
    private String cellphone;
    private AccountRole role;
    private ApprovalType approvalType;
    private String teamcode;
    private String mode;
    private Long companyid;
    private Long positionid;
    private Integer userRefleshCheck;
    private Integer userRefleshCount;
}
