package kr.co.broadwave.aci.accounts;

import kr.co.broadwave.aci.bscodes.ApprovalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * @author Minkyu
 * Date : 2020-02-20
 * Time :
 * Remark :
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountDtoCollection {
    private String operator;
    private Double companyLatitude;
    private Double companyHardness;

    public String getOperator() {
        return operator;
    }

    public String getCompanyLatitude() {
        if(companyLatitude==null){
            return null;
        }else{
            return companyLatitude.toString();
        }
    }

    public String getCompanyHardness() {
        if(companyHardness==null){
            return null;
        }else{
            return companyHardness.toString();
        }
    }
}
