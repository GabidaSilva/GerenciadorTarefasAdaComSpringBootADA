package br.com.gabriela.gerenciador.repository;

import br.com.gabriela.gerenciador.model.Tarefa;
import br.com.gabriela.gerenciador.model.Tarefa.StatusTarefa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByStatus(StatusTarefa status);

    Optional<Tarefa> findByTituloIgnoreCase(String titulo);

    @Transactional
    @Modifying
    @Query("DELETE FROM Tarefa t WHERE LOWER(t.titulo) = LOWER(:titulo)")
    int deleteByTituloIgnoreCase(String titulo);

    @Query("SELECT t FROM Tarefa t WHERE t.dataLimite BETWEEN :start AND :end AND t.status <> :excludedStatus")
    List<Tarefa> findTarefasProximasDoVencimento(
            LocalDate start,
            LocalDate end,
            StatusTarefa excludedStatus
    );
}
