package com.acme.pontointeligente.api.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ivan on 2/10/2018.
 */
public class Response<T> {

    private T data;
    private List<String> errors = new ArrayList<>();

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
