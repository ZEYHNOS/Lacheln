package aba3.lucid.domain.company.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BnoStatusRequest {


        @JsonProperty("b_no")
        private List<String> bnRegNo;  //사업자등록번호 (하이폰 없이 10자리)

        //국세청 /status API 스펙이 요청은 array으로 하고 있어서 List<String> 맞아요

}
