package com.acme.pontointeligente.api.entities;

import com.acme.pontointeligente.api.enums.PerfilEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Ivan on 1/10/2018.
 */
@Entity
@Table(name = "funcionario")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Funcionario implements Serializable {

    private static final long serialVersionUID = -8668084483166903568L;

    private Long id;
    private String name;
    private String email;
    private String senha;
    private String cpf;
    private BigDecimal valorHora;
    private Float qtdHorasTrabajoDia;
    private Float qtdHorasAlmuerzo;
    private PerfilEnum perfil;
    private LocalDateTime fechaHoraCreacion;
    private LocalDateTime fechaHoraModificacion;
    private Empresa empresa;
    private List<Lanzamiento> lanzamientos;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "nome")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "senha")
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Column(name = "cpf")
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Column(name = "valor_hora")
    public BigDecimal getValorHora() {
        return valorHora;
    }

    @Transient
    public Optional<BigDecimal> getValorHoraOpt() {
        return Optional.ofNullable(valorHora);
    }

    public void setValorHora(BigDecimal valorHora) {
        this.valorHora = valorHora;
    }

    @Column(name = "qtd_horas_trabalho_dia")
    public Float getQtdHorasTrabajoDia() {
        return qtdHorasTrabajoDia;
    }

    public void setQtdHorasTrabajoDia(Float qtdHorasTrabajoDia) {
        this.qtdHorasTrabajoDia = qtdHorasTrabajoDia;
    }

    @Column(name = "qtd_horas_almoco")
    public Float getQtdHorasAlmuerzo() {
        return qtdHorasAlmuerzo;
    }

    public void setQtdHorasAlmuerzo(Float qtdHorasAlmuerzo) {
        this.qtdHorasAlmuerzo = qtdHorasAlmuerzo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    public PerfilEnum getPerfil() {
        return perfil;
    }

    public void setPerfil(PerfilEnum perfil) {
        this.perfil = perfil;
    }

    @Column(name = "data_criacao")
    public LocalDateTime getFechaHoraCreacion() {
        return fechaHoraCreacion;
    }

    public void setFechaHoraCreacion(LocalDateTime fechaHoraCreacion) {
        this.fechaHoraCreacion = fechaHoraCreacion;
    }

    @Column(name = "data_atualizacao")
    public LocalDateTime getFechaHoraModificacion() {
        return fechaHoraModificacion;
    }

    public void setFechaHoraModificacion(LocalDateTime fechaHoraModificacion) {
        this.fechaHoraModificacion = fechaHoraModificacion;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @OneToMany(mappedBy = "funcionario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Lanzamiento> getLanzamientos() {
        return lanzamientos;
    }

    public void setLanzamientos(List<Lanzamiento> lanzamientos) {
        this.lanzamientos = lanzamientos;
    }

    @PreUpdate
    public void preUpdate() {
        this.fechaHoraModificacion = LocalDateTime.now();
    }

    @PrePersist
    public void prePersist() {
        this.fechaHoraCreacion = LocalDateTime.now();
        this.fechaHoraModificacion = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Funcionario{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
