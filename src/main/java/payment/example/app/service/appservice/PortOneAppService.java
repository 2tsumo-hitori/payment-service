package payment.example.app.service.appservice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import payment.example.KeyConfig;
import payment.example.app.service.dto.PaymentCancelRequest;

@Service
@RequiredArgsConstructor
public class PortOneAppService {
    private WebClient webClient;

    private final KeyConfig keyConfig;
    private Gson gson = new Gson();

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

        JsonObject jsonObject = gson.fromJson(token, JsonObject.class);

        String accessToken = jsonObject.getAsJsonObject("response").get("access_token").getAsString();

        return accessToken;
    }
}
