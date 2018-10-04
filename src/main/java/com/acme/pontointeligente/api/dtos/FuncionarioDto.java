package com.acme.pontointeligente.api.dtos;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

/**
 * Created by Ivan on 3/10/2018.
 */
public class FuncionarioDto {

    private Long id;
    private String name;
    private String email;
    private Optional<String> senha = Optional.empty();
    private Optional<String> valorHora = Optional.empty();
    private Optional<String> qtdHorasTrabajoDia = Optional.empty();
    private Optional<String> qtdHorasAlmuerzo = Optional.empty();

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
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Optional<String> getSenha() {
        return senha;
    }

    public void setSenha(Optional<String> senha) {
        this.senha = senha;
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

    @Override
    public String toString() {
        return "FuncionarioDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", senha=" + senha +
                ", valorHora=" + valorHora +
                ", qtdHorasTrabajoDia=" + qtdHorasTrabajoDia +
                ", qtdHorasAlmuerzo=" + qtdHorasAlmuerzo +
                '}';
    }
}
