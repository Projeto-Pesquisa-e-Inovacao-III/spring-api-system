package com.spring.ApiSystem.domain.exercicio;

import com.spring.ApiSystem.domain.equipamento.Equipamento;
import com.spring.ApiSystem.domain.partecorpo.ParteCorpo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exercise")
public class Exercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String gifUrl;

    @Lob
    @Column(name = "instructions_sumarization", columnDefinition = "LONGTEXT")
    private String instructionsSumarization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_body_part")
    private ParteCorpo parteCorpo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_equipment")
    private Equipamento equipamento;

    @OneToMany(mappedBy = "exercicio", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExercicioMusculoSecundario> musculosSecundarios = new ArrayList<>();

    public Exercicio() {}

    public Exercicio(Long id, String name, String gifUrl, String instructionsSumarization,
                     ParteCorpo parteCorpo, Equipamento equipamento) {
        this.id = id;
        this.name = name;
        this.gifUrl = gifUrl;
        this.instructionsSumarization = instructionsSumarization;
        this.parteCorpo = parteCorpo;
        this.equipamento = equipamento;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getGifUrl() { return gifUrl; }
    public void setGifUrl(String gifUrl) { this.gifUrl = gifUrl; }
    public String getInstructionsSumarization() { return instructionsSumarization; }
    public void setInstructionsSumarization(String value) { this.instructionsSumarization = value; }
    public ParteCorpo getParteCorpo() { return parteCorpo; }
    public void setParteCorpo(ParteCorpo parteCorpo) { this.parteCorpo = parteCorpo; }
    public Equipamento getEquipamento() { return equipamento; }
    public void setEquipamento(Equipamento equipamento) { this.equipamento = equipamento; }
    public List<ExercicioMusculoSecundario> getMusculosSecundarios() { return musculosSecundarios; }
    public void setMusculosSecundarios(List<ExercicioMusculoSecundario> value) { this.musculosSecundarios = value; }
}
