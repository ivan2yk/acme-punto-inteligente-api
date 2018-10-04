package com.acme.pontointeligente.api.security.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Ivan on 4/10/2018.
 */
public class JwtAuthenticationDto {

    private String email;
    private String senha;

    @NotEmpty(message = "Email no debe ser vacio.")
    @Email(message = "Email es invalido")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = "Password no debe ser vacio")
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
