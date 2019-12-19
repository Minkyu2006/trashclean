package kr.co.broadwave.aci.imodel;

import kr.co.broadwave.aci.mastercode.MasterCode;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time : 10:34
 * Remark : 모델등록 클래스 ListDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class IModelListDto {
    private Long id;

    private String mdNumber; // 모델 번호
    private String mdName; // 모델명
    private MasterCode mdType; // 모델타입
    private String mdSubname; // 모델약칭
    private String mdRemark; // 모델특이사항

    public Long getId() {
        return id;
    }

    public String getMdNumber() {
        return mdNumber;
    }

    public String getMdName() {
        return mdName;
    }

    public String getMdType() {
        return mdType.getName();
    }

    public String getMdSubname() {
        return mdSubname;
    }

    public String getMdRemark() {
        return mdRemark;
    }

}
