package payment.example.app.service.appservice.callback;

import com.siot.IamportRestClient.exception.IamportResponseException;

import java.io.IOException;

@FunctionalInterface
public interface IamPortCallBack<T> {
    T call() throws IamportResponseException, IOException;
}
