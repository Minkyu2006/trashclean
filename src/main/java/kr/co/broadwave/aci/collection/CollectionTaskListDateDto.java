package kr.co.broadwave.aci.collection;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2020-02-26
 * Time :
 * Remark : 수거업무리스트 클래스 TaskListDateDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class CollectionTaskListDateDto {
    private String ctCode; // 수거관리코드
    private String yyyymmdd; // 수거처리일

    public String getCtCode() {
        return ctCode;
    }

    public StringBuffer getYyyymmdd() {
        StringBuffer yyyymmddDate = new StringBuffer(yyyymmdd); // ex)2020-11-04
        yyyymmddDate.insert(4, '-');
        yyyymmddDate.insert(7, '-');
        return yyyymmddDate;
    }
}
