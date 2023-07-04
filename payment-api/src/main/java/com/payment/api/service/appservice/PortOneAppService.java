package com.payment.api.service.appservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payment.api.service.appservice.dto.PaymentCancelRequest;
import com.payment.common.aop.pointcut.LogTracer;
import com.payment.paymentintegration.payment.iamport.IamPortKeyConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@LogTracer
public class PortOneAppService {
    private WebClient webClient;

    private final IamPortKeyConfig keyConfig;

    private final ObjectMapper objectMapper;

    @PostConstruct
    public void initWebClient() {
        webClient = WebClient.create("https://api.iamport.kr");
    }

    public void paymentCancel(PaymentCancelRequest request) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("reason", request.getReason());
        formData.add("imp_uid", request.getImpUid());
        formData.add("amount", request.getAmount());
        formData.add("checksum", request.getAmount());

        webClient.post()
                .uri("/payments/cancel")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", getToken())
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String getToken() {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();

        formData.add("imp_key", keyConfig.getApiKey());
        formData.add("imp_secret", keyConfig.getApiSecret());

        String token = webClient.post()
                .uri("/users/getToken")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(formData))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(token);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String accessToken = jsonNode.get("response").get("access_token").asText();

        return accessToken;
    }
}
