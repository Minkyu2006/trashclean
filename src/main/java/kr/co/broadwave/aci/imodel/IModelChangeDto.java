package kr.co.broadwave.aci.imodel;

import lombok.*;

/**
 * @author Minkyu
 * Date : 2019-01-06
 * Time : 10:34
 * Remark : IModelChangeDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class IModelChangeDto {

    private Long id;
    private String mdSubname;

    public Long getId() {
        return id;
    }

    public String getMdSubname() {
        return mdSubname;
    }
}
