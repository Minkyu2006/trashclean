package kr.co.broadwave.aci.collection;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2020-01-30
 * Time :
 * Remark : 수거업무리스트 클래스 TaskListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class CollectionTaskListDeviceDto {
    private Long id;
    private Integer ctSeq;
    private String deviceid; // 장비코드

    public Integer getCtSeq() {
        return ctSeq;
    }

    public Long getId() {
        return id;
    }

    public String getDeviceid() {
        return deviceid;
    }
}
