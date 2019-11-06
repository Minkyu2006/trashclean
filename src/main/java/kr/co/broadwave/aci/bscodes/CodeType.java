package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-07-31
 * Remark : 코드 대분류코드
 */
public enum CodeType {
    C0001("C0001", "직급"),
    C0002("C0002", "관련부처"),
    C0003("C0003", "장비타입"),
    C0004("C0004", "국가"),
    C0005("C0005", "지역"),
    C0006("C0006", "업체구분"),
    C0007("C0007", "운영권역");

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
