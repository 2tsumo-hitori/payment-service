package com.payment.paymentintegration.payment.exception;

public class IamPortException extends RuntimeException{

    public IamPortException(Throwable cause) {
        super(cause);
    }

    public static class IamPortRunTimeException extends IamPortException {
        public IamPortRunTimeException(Throwable cause) {
            super(cause);
        }
    }

    public static class IamPortRunTimeIoException extends IamPortException {
        public IamPortRunTimeIoException(Throwable cause) {
            super(cause);
        }
    }
}
