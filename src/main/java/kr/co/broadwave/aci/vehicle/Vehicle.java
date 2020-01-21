package kr.co.broadwave.aci.vehicle;

import kr.co.broadwave.aci.company.Company;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-01-20
 * Time :
 * Remark : 차량등록 클래스
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="vc_id")
    private Long id; // 차량고유ID

    @Column(unique = true,name="vc_number")
    private String vcNumber; // 차량번호

    @Column(name="vc_name")
    private String vcName; // 차량명

    @Column(name="vc_shape")
    private String vcShape; // 차량소유구분

    @Column(name="vc_usage")
    private String vcUsage; // 차량용도

    @Column(name="vc_start_date")
    private String vcStartDate; // 운행시작일

    @Column(name="vc_end_date")
    private String vcEndDate; // 운행종료일

    @ManyToOne(targetEntity = Company.class,fetch = FetchType.EAGER)
    @JoinColumn(name="cs_id")
    private Company company; // 소속운영사

    @Column(name="vc_management")
    private String vcManagement; // 차량관리

    @Column(name="insert_date")
    private LocalDateTime insertDateTime;

    @Column(name="insert_id")
    private String insert_id;

    @Column(name="modify_date")
    private LocalDateTime modifyDateTime;

    @Column(name="modify_id")
    private String modify_id;

}
