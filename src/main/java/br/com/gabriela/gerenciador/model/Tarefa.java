package br.com.gabriela.gerenciador.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Tarefa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    private String descricao;

    private LocalDate dataLimite;

    @Enumerated(EnumType.STRING)
    private StatusTarefa status;

    public enum StatusTarefa {
        PENDENTE, EM_ANDAMENTO, CONCLUIDO
    }

    // Construtor sem argumentos (obrigatório para JPA)
    public Tarefa() {
        this.status = StatusTarefa.PENDENTE;
    }

    // Construtor com parâmetros
    public Tarefa(String titulo, String descricao, LocalDate dataLimite) {
        this();
        this.titulo = titulo;
        this.descricao = descricao;
        this.dataLimite = dataLimite;
    }

    public Tarefa(String titulo, String descricao, LocalDate dataLimite, StatusTarefa status) {
        this(titulo, descricao, dataLimite);
        this.status = status;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDate getDataLimite() {
        return dataLimite;
    }

    public void setDataLimite(LocalDate dataLimite) {
        this.dataLimite = dataLimite;
    }

    public StatusTarefa getStatus() {
        return status;
    }

    public void setStatus(StatusTarefa status) {
        this.status = status;
    }
}
