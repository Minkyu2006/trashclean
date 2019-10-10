package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-07-09
 * Remark : PSC교 부재 타입
 */
public enum PscMaterialType {
    PMT001("PMT001", "거더","상부구조"),
    PMT002("PMT002", "가로보/세로보","상부부조"),
    PMT003("PMT003", "긴장재","상부구조"),
    PMT004("PMT004", "신축이음","상부구조")
    ;

    private String code;
    private String desc;
    private String group;

    PscMaterialType(String code, String desc,String group) {
        this.code = code;
        this.desc = desc;
        this.group = group;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getGroup() {
        return group;
    }
}
