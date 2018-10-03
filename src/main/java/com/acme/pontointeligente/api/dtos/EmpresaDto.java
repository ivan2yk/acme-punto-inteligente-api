package com.acme.pontointeligente.api.dtos;

/**
 * Created by Ivan on 3/10/2018.
 */
public class EmpresaDto {

    private Long id;
    private String razonSocial;
    private String cnpj;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    @Override
    public String toString() {
        return "EmpresaDto{" +
                "id=" + id +
                ", razonSocial='" + razonSocial + '\'' +
                ", cnpj='" + cnpj + '\'' +
                '}';
    }
}
