package com.spring.ApiSystem.horariopersonal;

import com.spring.ApiSystem.enums.DiaSemana;
import com.spring.ApiSystem.enums.TipoHorario;
import com.spring.ApiSystem.personal.Personal;
import jakarta.persistence.*;


import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "disponibilidade_personal")
public class DisponibilidadePersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_disponibilidade_personal")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "personal_usuario_idusuario", nullable = false)
    private Personal personal;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana diaSemana;

    @Enumerated(EnumType.STRING)
    @Column(name = "disponibilidade_personalcol", nullable = false)
    private TipoHorario tipo;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fim", nullable = false)
    private LocalTime horaFim;

    public DisponibilidadePersonal(DiaSemana diaSemana, LocalTime horaFim, LocalTime horaInicio, Long id, Personal personal, TipoHorario tipo) {
        this.diaSemana = diaSemana;
        this.horaFim = horaFim;
        this.horaInicio = horaInicio;
        this.id = id;
        this.personal = personal;
        this.tipo = tipo;
    }

    public DisponibilidadePersonal() {
    }

    public DiaSemana getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(DiaSemana diaSemana) {
        this.diaSemana = diaSemana;
    }

    public LocalTime getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(LocalTime horaFim) {
        this.horaFim = horaFim;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

    public TipoHorario getTipo() {
        return tipo;
    }

    public void setTipo(TipoHorario tipo) {
        this.tipo = tipo;
    }
}
