package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.bscodes.ApprovalType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author InSeok
 * Date : 2019-03-29
 * Time : 10:22
 * Remark :
 */
public interface AccountRepositoryCustom {
    Page<AccountDtoWithTeam> findAllBySearchStrings(String userid, String username,String email, Pageable pageable);
    Page<AccountDto> findAllByApproval(String username,String startDate, String endDate, Pageable pageable);
    Long saveApproval(Account account, ApprovalType approvalType, String loginId);

}
