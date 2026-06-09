package aba3.lucid.domain.company.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class BnoStatusResponse {

    private List<DataItem> data;

    @Getter
    @Setter
    public static class DataItem {
        @JsonProperty("b_no")
        private String bNo;

        @JsonProperty("b_stt")
        private String businessStatus;

        @JsonProperty("tax_type")
        private String taxType;


    }

}
