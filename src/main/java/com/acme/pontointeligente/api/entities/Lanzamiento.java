package com.acme.pontointeligente.api.entities;

import com.acme.pontointeligente.api.enums.TipoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Ivan on 1/10/2018.
 */
@Entity
@Table(name = "lancamento")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Lanzamiento implements Serializable {

    private static final long serialVersionUID = -234975906048535742L;

    private Long id;
    private LocalDate fecha;
    private String descripcion;
    private String localizacion;
    private LocalDateTime fechaHoraCreacion;
    private LocalDateTime fechaHoraModificacion;
    private TipoEnum tipo;
    private Funcionario funcionario;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "data", nullable = false)
    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Column(name = "descricao")
    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "localizacao")
    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    public TipoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoEnum tipo) {
        this.tipo = tipo;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    public Funcionario getFuncionario() {
        return funcionario;
    }

    public void setFuncionario(Funcionario funcionario) {
        this.funcionario = funcionario;
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
        return "Lanzamiento{" +
                "id=" + id +
                ", fecha=" + fecha +
                ", descripcion='" + descripcion + '\'' +
                ", localizacion='" + localizacion + '\'' +
                '}';
    }
}
