package com.yizhi.monitorsystem2.collection.exception;

public class BaseException extends Exception {
    private static String message;

    public BaseException() {
        super();
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(int code) {
        switch (code) {
            case 1:
                message = "The MongoDb id not legal.";
                break;
            case 2:
                message = "Please check the parameter.";
                break;
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
