package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-04-30
 * Time : 09:45
 * Remark : 시리즈(시험체) 정보 코드
 */
public enum SeriesCode {
    S001("S001", "해사 사용장기 시험체"),
    S002("S002", "내륙환경 기준 시험체"),
    S003("S003", "해안환경 기준 시험체"),
    S004("S004", "부순골재/재생모래 사용 시험체"),
    S005("S005", "고로슬래그/플라이애쉬 사용 시험체"),
    S006("S006", "실리카흄 사용 시험체"),
    S007("S007", "염분 함유 기준 시험체"),
    S999("S999", "미지정-> 사용하지않는 값")
    ;

    private String code;
    private String desc;

    SeriesCode(String code, String desc) {
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
