package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-07-31
 * Remark : 코드 대분류코드
 */
public enum CodeType {
    C0001("C0001", "직급"),
    C0002("C0002", "관련부처"),
    C0003("C0003", "출동일지완료후메일수신자");

    private String code;
    private String desc;

    CodeType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
