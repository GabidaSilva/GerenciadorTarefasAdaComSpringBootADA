package br.com.gabriela.gerenciador.service;

import br.com.gabriela.gerenciador.model.Tarefa;
import br.com.gabriela.gerenciador.model.Tarefa.StatusTarefa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.gabriela.gerenciador.repository.TarefaRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class TarefaService {

    private final TarefaRepository tarefaRepository;
    private final int TITULO_MIN_LENGTH = 3;

    @Autowired
    public TarefaService(TarefaRepository tarefaRepository) {
        this.tarefaRepository = tarefaRepository;
    }

    // Adicione tratamento de exceções
    public boolean adicionarTarefa(Tarefa tarefa) {
        try {
            if (!validarTarefa(tarefa)) return false;
            tarefaRepository.save(tarefa);
            return true;
        } catch (Exception e) {
            System.err.println("Erro ao salvar tarefa: " + e.getMessage());
            return false;
        }
    }

    // Melhore a validação
    private boolean validarTarefa(Tarefa tarefa) {
        if (tarefa.getTitulo() == null || tarefa.getTitulo().trim().length() < TITULO_MIN_LENGTH) {
            throw new IllegalArgumentException("Título deve ter pelo menos " + TITULO_MIN_LENGTH + " caracteres");
        }
        if (tarefa.getDataLimite() == null || tarefa.getDataLimite().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data limite deve ser futura");
        }
        return true;
    }

    public List<Tarefa> listarTodas() {
        return tarefaRepository.findAll();
    }

    public List<Tarefa> filtrarPorStatus(StatusTarefa status) {
        return tarefaRepository.findByStatus(status);
    }

    public Optional<Tarefa> buscarPorTitulo(String titulo) {
        return tarefaRepository.findByTituloIgnoreCase(titulo);
    }

    public boolean atualizarStatus(String titulo, StatusTarefa novoStatus) {
        Optional<Tarefa> tarefaOpt = buscarPorTitulo(titulo);
        if (tarefaOpt.isPresent()) {
            Tarefa tarefa = tarefaOpt.get();
            tarefa.setStatus(novoStatus);
            tarefaRepository.save(tarefa);
            return true;
        }
        return false;
    }

    public boolean removerTarefa(String titulo) {
        return tarefaRepository.deleteByTituloIgnoreCase(titulo) > 0;
    }

    public List<Tarefa> obterTarefasProximasVencimento(int diasLimite) {
        LocalDate inicio = LocalDate.now();
        LocalDate fim = inicio.plusDays(diasLimite);
        return tarefaRepository.findTarefasProximasDoVencimento(inicio, fim, StatusTarefa.CONCLUIDO);
    }
    public List<Tarefa> ordenarPorDataLimite() {
        List<Tarefa> tarefas = tarefaRepository.findAll();
        tarefas.sort(Comparator.comparing(Tarefa::getDataLimite));
        return tarefas;
    }
}
