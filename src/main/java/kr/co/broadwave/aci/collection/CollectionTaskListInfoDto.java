package kr.co.broadwave.aci.collection;

import kr.co.broadwave.aci.bscodes.ProcStatsType;
import lombok.*;

/**
 * @author Minkyu
 * Date : 2020-01-30
 * Time :
 * Remark : 수거업무리스트 클래스 TaskListInfoDto
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
public class CollectionTaskListInfoDto {
    private Long id;
    private String ctCode; // 수거관리코드
    private Integer ctSeq; // 수거처리순번
    private String yyyymmdd; // 수거처리일
    private String deviceid; // 장비코드
    private String devicetype; // 장비타입
    private String devicemodeltype; // 장비모델종류
    private String company; // 운영사
    private String filePath; // 파일경로 1
    private String saveFileName; // 파일경로 2
    private String emCountry;
    private String emLoation;
    private Double mdmaximum;
    private String mdunit;
    private ProcStatsType procStatsType;

    public Integer getCtSeq() {
        return ctSeq;
    }

    public ProcStatsType getProcStatsType() {
        return procStatsType;
    }

    public String getEmCountry() {
        return emCountry;
    }

    public String getEmLoation() {
        return emLoation;
    }

    public Double getMdmaximum() {
        return mdmaximum;
    }

    public String getMdunit() {
        return mdunit;
    }

    public String getFilePath() {
        if(filePath==null){
            filePath = "/defaultimage";
            return filePath;
        }else{
            return filePath;
        }
    }

    public String getSaveFileName() {
        if(saveFileName==null){
            saveFileName = "/model.jpg";
            return saveFileName;
        }else{
            return "/s_"+saveFileName;
        }
    }

    public Long getId() {
        return id;
    }

    public String getCtCode() {
        return ctCode;
    }

    public StringBuffer getYyyymmdd() {
        StringBuffer yyyymmddDate = new StringBuffer(yyyymmdd); // ex)2020-11-04
        yyyymmddDate.insert(4,'-');
        yyyymmddDate.insert(7,'-');
        return yyyymmddDate;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public String getDevicemodeltype() {
        return devicemodeltype;
    }

    public String getCompany() {
        return company;
    }
}
