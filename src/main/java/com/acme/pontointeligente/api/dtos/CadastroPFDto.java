package com.acme.pontointeligente.api.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Optional;

/**
 * Created by Ivan on 3/10/2018.
 */
public class CadastroPFDto {

    private Long id;
    private String name;
    private String email;
    private String senha;
    private String cpf;
    private Optional<String> valorHora = Optional.empty();
    private Optional<String> qtdHorasTrabajoDia = Optional.empty();
    private Optional<String> qtdHorasAlmuerzo = Optional.empty();
    private String cnpj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotEmpty
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NotEmpty
    @Email
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotEmpty
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Optional<String> getValorHora() {
        return valorHora;
    }

    public void setValorHora(Optional<String> valorHora) {
        this.valorHora = valorHora;
    }

    public Optional<String> getQtdHorasTrabajoDia() {
        return qtdHorasTrabajoDia;
    }

    public void setQtdHorasTrabajoDia(Optional<String> qtdHorasTrabajoDia) {
        this.qtdHorasTrabajoDia = qtdHorasTrabajoDia;
    }

    public Optional<String> getQtdHorasAlmuerzo() {
        return qtdHorasAlmuerzo;
    }

    public void setQtdHorasAlmuerzo(Optional<String> qtdHorasAlmuerzo) {
        this.qtdHorasAlmuerzo = qtdHorasAlmuerzo;
    }

    @NotEmpty
    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "CadastroPFDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", cpf='" + cpf + '\'' +
                ", valorHora=" + valorHora +
                ", qtdHorasTrabajoDia=" + qtdHorasTrabajoDia +
                ", qtdHorasAlmuerzo=" + qtdHorasAlmuerzo +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
