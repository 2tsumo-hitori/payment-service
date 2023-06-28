//package com.payment.api.service.appservice;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.payment.api.service.dto.PaymentCancelRequest;
//import okhttp3.mockwebserver.MockResponse;
//import okhttp3.mockwebserver.MockWebServer;
//import okhttp3.mockwebserver.RecordedRequest;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.reactive.function.client.WebClient;
//
//import static org.junit.Assert.assertEquals;
//
//@SpringBootTest
//class PortOneAppServiceTest {
//
//    @Autowired
//    PortOneAppService portOneAppService;
//
//    private MockWebServer mockWebServer;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//    WebClient webClient;
//
//    final String test_imp_uid = "imp_448280090638";
//
//    @BeforeEach
//    void setup() throws Exception {
//        mockWebServer = new MockWebServer();
//        mockWebServer.start();
//
//        String baseUrl = mockWebServer.url("/").toString();
//        webClient = WebClient.builder().baseUrl(baseUrl).build();
//    }
//
//    @AfterEach
//    public void tearDown() throws Exception {
//        mockWebServer.shutdown();
//    }
//
//    @Test
//    public void 결제_취소_성공() throws Exception {
//        PaymentCancelRequest paymentCancelRequest = new PaymentCancelRequest();
//        paymentCancelRequest.setImpUid(test_imp_uid);
//        paymentCancelRequest.setReason("reason");
//        paymentCancelRequest.setAmount("100");
//
//        MockResponse mockResponse = new MockResponse()
//                .setResponseCode(200)
//                .setHeader("Authorization", "token")
//                .setBody(objectMapper.writeValueAsString(paymentCancelRequest))
//                .addHeader("Content-Type", "application/json");
//
//        mockWebServer.enqueue(mockResponse);
//
//        portOneAppService.paymentCancel(paymentCancelRequest);
//
//        RecordedRequest recordedRequest = mockWebServer.takeRequest();
//        assertEquals("/payments/cancel", recordedRequest.getPath());
//        assertEquals("POST", recordedRequest.getMethod());
//    }
//}