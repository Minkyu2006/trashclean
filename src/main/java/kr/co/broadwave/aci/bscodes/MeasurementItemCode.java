package kr.co.broadwave.aci.bscodes;

/**
 * @author InSeok
 * Date : 2019-04-30
 * Time : 09:50
 * Remark : 측정항목 코드
 */
public enum MeasurementItemCode {

    M001("M001", "압축강도","Mpa"),
    M002("M002", "탄산화깊이","mm"),
    M003("M003", "길이변형률","μ"),
    M004("M004", "초음파속도","km/s"),
    M005("M005", "철근부식량","g"),
    M006("M006", "철근부식면적률","%"),
    M007("M007", "염분함유량","%")
    ;

    private String code;
    private String desc;
    private String unit;

    MeasurementItemCode(String code, String desc,String unit) {
        this.code = code;
        this.desc = desc;
        this.unit = unit;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    public String getUnit() {
        return unit;
    }
}
