package kr.co.broadwave.aci.bscodes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author InSeok
 * Date : 2019-05-03
 * Time : 15:06
 * Remark : Enum 을 Json 으로 반환할때 사용하기위한 클래스
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommonCode {
    private String code;
    private String name;
}
