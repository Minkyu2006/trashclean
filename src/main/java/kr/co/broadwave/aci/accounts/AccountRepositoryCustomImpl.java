package kr.co.broadwave.aci.accounts;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.mastercode.QMasterCode;
import kr.co.broadwave.aci.teams.QTeam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author InSeok
 * Date : 2019-03-29
 * Time : 10:24
 * Remark :
 */
@Repository
public class AccountRepositoryCustomImpl extends QuerydslRepositorySupport implements AccountRepositoryCustom {

    public AccountRepositoryCustomImpl() {
        super(Account.class);
    }


    @Override
    public Page<AccountDtoWithTeam> findAllBySearchStrings(String userid, String username,String email, Pageable pageable) {
        QAccount qAccount  = QAccount.account;
        QTeam qTeam = QTeam.team;
        QMasterCode qMasterCode = QMasterCode.masterCode;

        JPQLQuery<AccountDtoWithTeam> query = from(qAccount)
                .innerJoin(qAccount.team,qTeam)
                .innerJoin(qAccount.position,qMasterCode)
                .select(Projections.constructor(AccountDtoWithTeam.class,
                        qAccount.userid,
                        qAccount.username,
                        qAccount.cellphone,
                        qAccount.email,
                        qAccount.role,
                        qTeam.teamcode,
                        qTeam.teamname,
                        qMasterCode.code,
                        qMasterCode.name

                ));


        if (userid != null && !userid.isEmpty()){
            query.where(qAccount.userid.containsIgnoreCase(userid));
        }
        if (username != null && !username.isEmpty()){
            query.where(qAccount.username.containsIgnoreCase(username));
        }
        if (email != null && !email.isEmpty()){
            query.where(qAccount.email.containsIgnoreCase(email));
        }


        final List<AccountDtoWithTeam> accounts = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(accounts, pageable, query.fetchCount());
    }


    //사용자 승인 조회 쿼리
    @Override
    public Page<AccountDto> findAllByApproval(String username, String startDate, String endDate,Pageable pageable) {
        QAccount account  = QAccount.account;


        JPQLQuery<AccountDto> query = from(account)
                .select(Projections.constructor(AccountDto.class,
                        account.userid,
                        account.username,
                        account.email,
                        account.cellphone,
                        account.role,
                        account.approvalType,
                        account.insertDateTime
                ));



        if (username != null && !username.isEmpty()){
            query.where(account.username.containsIgnoreCase(username));
        }

        query.where(account.approvalType.eq(ApprovalType.AT01));
        //StartDate
        LocalDateTime startDateTime = LocalDateTime.of(1950, 1, 1, 0, 0, 0);
        if (startDate != null && !startDate.isEmpty()){
            int year = Integer.parseInt(startDate.substring(0,4));
            int month = Integer.parseInt(startDate.substring(4,6));
            int day = Integer.parseInt(startDate.substring(6,8));
            startDateTime = LocalDateTime.of(year, month, day, 0, 0, 0);

        }

        //StartDate
        LocalDateTime endDateTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);
        if (endDate != null && !endDate.isEmpty()){
            int year = Integer.parseInt(endDate.substring(0,4));
            int month = Integer.parseInt(endDate.substring(4,6));
            int day = Integer.parseInt(endDate.substring(6,8));
            endDateTime = LocalDateTime.of(year, month, day, 23, 59, 59);

        }

        query.where(account.insertDateTime.between(startDateTime,endDateTime));

        final List<AccountDto> accounts = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(accounts, pageable, query.fetchCount());
    }

    @Override
    @Transactional
    public Long saveApproval(Account account, ApprovalType approvalType, String loginId) {
        QAccount qAccount  = QAccount.account;

        return update(qAccount).where(qAccount.id.eq(account.getId()))
                .set(qAccount.approvalType,approvalType)
                .set(qAccount.approval_id,loginId)
                .set(qAccount.approvalDateTime,LocalDateTime.now())
                .execute();


    }
}
