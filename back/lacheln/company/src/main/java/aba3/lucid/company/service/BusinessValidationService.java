package aba3.lucid.company.service;


import aba3.lucid.domain.company.dto.BnoStatusRequest;
import aba3.lucid.domain.company.dto.BnoStatusResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BusinessValidationService {
    @Value("${nts.service-key}")
    private String serviceKey;

    @Value("${nts.url}")
    private String ntsUrl;

    private final RestTemplate restTemplate;

    public BnoStatusResponse.DataItem validateBusinessNumber(String cpBnRegNo) {
        //하이픈 제거: "123-45-67890" → "1234567890"
        String bNo = cpBnRegNo.replace("-","");

        //URL 구성
        String url = ntsUrl + "/status?serviceKey=" + serviceKey + "&returnType=JSON";

        //요청 바디 구성
        BnoStatusRequest request = new BnoStatusRequest(List.of(bNo));

        //Header 설정
        try {
            ObjectMapper mapper = new ObjectMapper();
            log.info("NTS 요청 JSON: {}", mapper.writeValueAsString(request));
        } catch (Exception ignored) {}

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<BnoStatusRequest> entity = new HttpEntity<>(request, headers);

       ResponseEntity<BnoStatusResponse> response = restTemplate.postForEntity(
               url, entity, BnoStatusResponse.class
       );

       BnoStatusResponse body = response.getBody();

       if(body == null ||
           body.getData() == null ||
           body.getData().isEmpty()) {

          throw new RuntimeException(
                  "사업자번호 조회 실패"
          );
    }
    return body.getData().get(0);
       }



}

