package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.files.FileUpload;
import kr.co.broadwave.aci.mastercode.MasterCode;
import kr.co.broadwave.aci.teams.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author Minkyu
 * Date : 2020-01-09
 * Remark :
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AccountDtoProfile {
    private FileUpload userPhoto;
    private String username;
    private String email;
    private String cellphone;
    private Team teamcode;
    private MasterCode positionid;

    public FileUpload getUserPhoto() {
        return userPhoto;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getCellphone() {
        return cellphone;
    }

    public Team getTeamcode() {
        return teamcode;
    }

    public MasterCode getPositionid() {
        return positionid;
    }
}
