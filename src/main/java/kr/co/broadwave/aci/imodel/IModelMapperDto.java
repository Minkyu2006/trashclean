package kr.co.broadwave.aci.imodel;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Minkyu
 * Date : 2019-12-19
 * Time : 10:34
 * Remark : 모델등록 클래스 MapperDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Slf4j
public class IModelMapperDto {

    private String mdNumber; // 모델 번호
    private String mdName; // 모델명
    private Long emType; // 모델타입
    private Long mdType; // 모델종류
    private String mdSubname; // 모델약칭
    private String mdRemark; // 모델특이사항
    private Double mdMaximumPayload; //최대적재량
    private Long mdUnit; // 단위

    public Double getMdMaximumPayload() {
        return mdMaximumPayload;
    }

    public Long getMdUnit() {
        return mdUnit;
    }

    public String getMdNumber() {
        return mdNumber;
    }

    public String getMdName() {
        return mdName;
    }

    public Long getEmType() {
        return emType;
    }

    public Long getMdType() {
        return mdType;
    }

    public String getMdSubname() {
        return mdSubname;
    }

    public String getMdRemark() {
        return mdRemark;
    }

}
