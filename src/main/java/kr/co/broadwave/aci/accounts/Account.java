package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.bscodes.ApprovalType;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.teams.Team;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author InSeok
 * Date : 2019-03-25
 * Time : 09:31
 * Remark : 사용자정보 클래스
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;

    @Column(unique = true,name="user_id")
    private String userid;

    @Column(name="user_name")
    private String username;

    @Column(name="user_password")
    private String password;

    @Column(name="user_email")
    private String email;

    @Column(name="user_cellphone")
    private String cellphone;

    @Enumerated(EnumType.STRING)
    @Column(name="user_role")
    private AccountRole role;

    @Enumerated(EnumType.STRING)
    @Column(name="user_approval_type")
    private ApprovalType approvalType;

    @ManyToOne(targetEntity = Team.class,fetch = FetchType.LAZY)
    @JoinColumn(name="team_id")
    private Team team;

    @ManyToOne(targetEntity = MasterCode.class,fetch = FetchType.LAZY)
    @JoinColumn(name="position_id")
    private MasterCode position;


    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

    @Column(name="approval_date")
    private LocalDateTime approvalDateTime;

    @Column(name="approval_id")
    private String approval_id;









}
