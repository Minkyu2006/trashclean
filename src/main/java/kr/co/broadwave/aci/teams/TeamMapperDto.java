package kr.co.broadwave.aci.teams;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author InSeok
 * Date : 2019-04-24
 * Time : 14:42
 * Remark : Restcontroller 에서 @ModelAttribute 로 받기위한 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamMapperDto {
    private Long id;
    private String teamcode;
    private String teamname;
    private String remark;
    private String mode;
}
