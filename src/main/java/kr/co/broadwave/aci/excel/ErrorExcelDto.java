package kr.co.broadwave.aci.excel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * @author InSeok
 * Date : 2019-05-07
 * Time : 13:27
 * Remark : 엑셀다운시 잘못된요청일경우
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorExcelDto implements DtoExcel {
    private String msssage;

    @Override
    public List<String> toArray() {
        return Arrays.asList(this.msssage);
    }
}
