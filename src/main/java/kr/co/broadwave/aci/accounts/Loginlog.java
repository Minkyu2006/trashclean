package kr.co.broadwave.aci.accounts;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author InSeok
 * Date : 2019-04-22
 * Time : 10:15
 * Remark :
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="bs_login_log")
public class Loginlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="login_id")
    private Long id;

    @ManyToOne(targetEntity = Account.class,fetch = FetchType.LAZY)
    @JoinColumn(name="account_id")
    private Account loginAccount;

    @Column(name="user_id")
    private String userid;

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="login_ip")
    private String loginIp;

    @Column(name="success_yn")
    private String successYN;



}
