package payment.example.app.service.appservice.callback;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static payment.example.common.exception.IamPortException.*;
import static payment.example.common.support.validate.PreCondition.itemNameValidate;
import static payment.example.common.support.validate.PreCondition.itemPriceValidate;

@Component
@RequiredArgsConstructor
@Transactional
public class IamPortTemplate implements PaymentTemplate{

    private final IamportClient iamportClient;

    @Override
    public <T> T execute(ValidatePayment validatePayment, IamPortCallBack<T> T) {
        try {
            Payment payment = iamportClient.paymentByImpUid(validatePayment.request().getImpUid()).getResponse();

            itemNameValidate(validatePayment.getItemName().equals(payment.getName()));
            itemPriceValidate(validatePayment.request().getAmount() == payment.getAmount().intValue());

            return T.call();
        } catch (IamportResponseException e) {
            throw new IamPortRunTimeException(e);
        } catch (IOException e) {
            throw new IamPortRunTimeIoException(e);
        }
    }
}
