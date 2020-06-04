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
    C0007("C0007", "운영권역"),
    C0008("C0008", "단위"),
    C0009("C0009", "모델타입"),
    C0010("C0010", "차량소유구분"),
    C0011("C0011", "차량용도"),
    C0012("C0012", "차량상태"),
    C0013("C0013", "장비기본값"),
    C0014("C0014", "시스템주소"),
    C0015("C0015", "iTainer설비상태"),
    C0016("C0016", "iTainer우선순위"),
    C0017("C0017", "iTainer장비언어");

    private final String code;
    private final String desc;

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
