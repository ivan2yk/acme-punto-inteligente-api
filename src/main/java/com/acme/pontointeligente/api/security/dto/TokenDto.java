package com.acme.pontointeligente.api.security.dto;

/**
 * Created by Ivan on 4/10/2018.
 */
public class TokenDto {

    private String token;

    public TokenDto(String token) {
        this.token = token;
    }

    public TokenDto() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
