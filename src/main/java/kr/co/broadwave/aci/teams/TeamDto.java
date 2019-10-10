package kr.co.broadwave.aci.teams;

import kr.co.broadwave.aci.excel.DtoExcel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author InSeok
 * Date : 2019-04-02
 * Time : 14:20
 * Remark : Team Dto
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamDto implements DtoExcel {

    private Long id;
    private String teamcode;
    private String teamname;
    private String remark;

    @Override
    public List<String> toArray() {
        return Arrays.asList(this.teamcode, this.teamname,this.remark);
    }

}
