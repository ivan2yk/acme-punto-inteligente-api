package com.acme.pontointeligente.api.dtos;

import javax.validation.constraints.NotEmpty;
import java.util.Optional;

/**
 * Created by Ivan on 3/10/2018.
 */
public class LanzamientoDto {

    private Optional<Long> id = Optional.empty();
    private String data;
    private String tipo;
    private String description;
    private String localizacion;
    private Long funcionarioId;

    public Optional<Long> getId() {
        return id;
    }

    public void setId(Optional<Long> id) {
        this.id = id;
    }

    @NotEmpty
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public Long getFuncionarioId() {
        return funcionarioId;
    }

    public void setFuncionarioId(Long funcionarioId) {
        this.funcionarioId = funcionarioId;
    }

    @Override
    public String toString() {
        return "LanzamientoDto{" +
                "id=" + id +
                ", data='" + data + '\'' +
                ", tipo='" + tipo + '\'' +
                ", description='" + description + '\'' +
                ", localizacion='" + localizacion + '\'' +
                ", funcionarioId=" + funcionarioId +
                '}';
    }
}
