package br.com.gabriela.gerenciador.util;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import br.com.gabriela.gerenciador.service.TarefaService;
import java.time.format.DateTimeFormatter;

@Component
public class Notificador {
    private final TarefaService tarefaService;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Notificador(TarefaService tarefaService) {
        this.tarefaService = tarefaService;
    }

    @Scheduled(fixedRate = 3600000) // 1 hora
    public void verificarTarefas() {
        System.out.println("\n=== Verificando tarefas próximas ===");
        tarefaService.obterTarefasProximasVencimento(2)
                .forEach(t -> System.out.printf(
                        "⚠ [%s] Vence em: %s\n",
                        t.getTitulo(),
                        t.getDataLimite().format(formatter)
                ));
    }

}
