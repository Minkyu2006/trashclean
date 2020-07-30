package kr.co.broadwave.aci.devicestats.errweight;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Minkyu
 * Date : 2020-07-30
 * Time :
 * Remark :
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name="bs_itainer_err_weight")
public class Errweight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id; // PK는 무조건 1

    @Column(name="err01_weight")
    private Double err01Weight; // 터치스크린

    @Column(name="err02_weight")
    private Double err02Weight; // 키패드

    @Column(name="err03_weight")
    private Double err03Weight; // 임베디드 보드

    @Column(name="err04_weight")
    private Double err04Weight; // 결제도어

    @Column(name="err05_weight")
    private Double err05Weight; // 폐기물 도어

    @Column(name="err06_weight")
    private Double err06Weight; // 저울 디바이스

    @Column(name="err07_weight")
    private Double err07Weight; // 결제 디바이스

    @Column(name="err08_weight")
    private Double err08Weight; // 티머니 디바이스

    @Column(name="err09_weight")
    private Double err09Weight; // 신용카드 디바이스

    @Column(name="err10_weight")
    private Double err10Weight; // LTE 디바이스

    @Column(name="insert_date")
    private LocalDateTime insert_date; // 데이터 업데이트 일시

    @Column(name="insert_id")
    private String insert_id; // 데이터 생성자

}
