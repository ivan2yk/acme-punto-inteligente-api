package com.acme.pontointeligente.api.dtos;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

/**
 * Created by Ivan on 2/10/2018.
 */
public class CadastroPJDto {

    private Long id;
    private String name;
    private String email;
    private String cpf;
    private String razonSocial;
    private String cnpj;
    private String senha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty(message = "Name no puede ser vacio.")
    @Length(min = 3, max = 200, message = "Name debe contener entre 3 y 200 caracteres.")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty(message = "Email no puede ser vacio.")
    @Length(min = 5, max = 200, message = "Email debe contener entre 5 y 200 caracteres.")
    @Email(message = "Email invalido.")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty(message = "CPF no puede ser vacio.")
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @NotEmpty
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    @NotEmpty
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @NotEmpty
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "CadastroPJDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", razonSocial='" + razonSocial + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
