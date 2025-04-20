package br.com.gabriela.gerenciador;

import br.com.gabriela.gerenciador.model.Tarefa;
import br.com.gabriela.gerenciador.model.Tarefa.StatusTarefa;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import br.com.gabriela.gerenciador.service.TarefaService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
@EnableScheduling
public class Main implements CommandLineRunner {

    @Autowired
    private TarefaService tarefaService;

    private final Scanner scanner = new Scanner(System.in);
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Override
    public void run(String... args) {
        System.out.println("Gerenciador de Tarefas iniciado!");

        adicionarTarefasExemplo();

        boolean executando = true;
        while (executando) {
            exibirMenu();
            int opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    cadastrarTarefa();
                    break;
                case 2:
                    listarTodasTarefas();
                    break;
                case 3:
                    filtrarPorStatus();
                    break;
                case 4:
                    listarPorDataLimite();
                    break;
                case 5:
                    atualizarStatusTarefa();
                    break;
                case 0:
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        }
        scanner.close();
        System.out.println("Gerenciador de Tarefas encerrado!");

    }

    private void exibirMenu() {
        System.out.println("\n==== GERENCIADOR DE TAREFAS ====");
        System.out.println("1. Cadastrar Nova Tarefa");
        System.out.println("2. Listar Todas as Tarefas");
        System.out.println("3. Filtrar por Status");
        System.out.println("4. Listar por Data Limite");
        System.out.println("5. Atualizar Status de Tarefa");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private int lerOpcao() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void cadastrarTarefa() {
        System.out.println("\n-- Cadastrar Nova Tarefa --");

        System.out.print("Título: ");
        String titulo = scanner.nextLine();

        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();

        LocalDate dataLimite = null;
        while (dataLimite == null) {
            System.out.print("Data Limite (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();
            try {
                dataLimite = LocalDate.parse(dataStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Formato de data inválido. Use dd/MM/yyyy.");
            }
        }

        System.out.println("Status:");
        System.out.println("1. PENDENTE");
        System.out.println("2. EM_ANDAMENTO");
        System.out.println("3. CONCLUIDO");
        System.out.print("Escolha (1-3): ");

        StatusTarefa status;
        switch (lerOpcao()) {
            case 1: status = StatusTarefa.PENDENTE; break;
            case 2: status = StatusTarefa.EM_ANDAMENTO; break;
            case 3: status = StatusTarefa.CONCLUIDO; break;
            default:
                System.out.println("Opção inválida, usando PENDENTE como padrão.");
                status = StatusTarefa.PENDENTE;
        }

        Tarefa novaTarefa = new Tarefa(titulo, descricao, dataLimite, status);

        if (tarefaService.adicionarTarefa(novaTarefa)) {
            System.out.println("Tarefa cadastrada com sucesso!");

        }
    }

    private void listarTodasTarefas() {
        System.out.println("\n-- Todas as Tarefas --");
        List<Tarefa> tarefas = tarefaService.listarTodas();

        if (tarefas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
            return;
        }

        exibirTarefas(tarefas);
    }


    private void filtrarPorStatus() {
        System.out.println("\n-- Filtrar por Status --");
        System.out.println("1. PENDENTE");
        System.out.println("2. EM_ANDAMENTO");
        System.out.println("3. CONCLUIDO");
        System.out.print("Escolha (1-3): ");

        StatusTarefa status;
        switch (lerOpcao()) {
            case 1: status = StatusTarefa.PENDENTE; break;
            case 2: status = StatusTarefa.EM_ANDAMENTO; break;
            case 3: status = StatusTarefa.CONCLUIDO; break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        List<Tarefa> tarefasFiltradas = tarefaService.filtrarPorStatus(status);

        if (tarefasFiltradas.isEmpty()) {
            System.out.println("Nenhuma tarefa com status " + status);
            return;
        }

        System.out.println("\nTarefas com status " + status + ":");
        exibirTarefas(tarefasFiltradas);
    }

    private void listarPorDataLimite() {
        System.out.println("\n-- Tarefas Ordenadas por Data Limite --");
        List<Tarefa> tarefasOrdenadas = tarefaService.ordenarPorDataLimite();

        if (tarefasOrdenadas.isEmpty()) {
            System.out.println("Nenhuma tarefa cadastrada.");
            return;
        }

        exibirTarefas(tarefasOrdenadas);
    }

    private void atualizarStatusTarefa() {
        System.out.println("\n-- Atualizar Status de Tarefa --");
        System.out.print("Digite o título da tarefa: ");
        String titulo = scanner.nextLine();

        System.out.println("Novo status:");
        System.out.println("1. PENDENTE");
        System.out.println("2. EM_ANDAMENTO");
        System.out.println("3. CONCLUIDO");
        System.out.print("Escolha (1-3): ");

        StatusTarefa novoStatus;
        switch (lerOpcao()) {
            case 1: novoStatus = StatusTarefa.PENDENTE; break;
            case 2: novoStatus = StatusTarefa.EM_ANDAMENTO; break;
            case 3: novoStatus = StatusTarefa.CONCLUIDO; break;
            default:
                System.out.println("Opção inválida!");
                return;
        }

        if (tarefaService.atualizarStatus(titulo, novoStatus)) {
            System.out.println("Status atualizado com sucesso!");
        } else {
            System.out.println("Tarefa não encontrada.");
        }
    }

    private void exibirTarefas(List<Tarefa> tarefas) {
        System.out.println("----------------------------------------");
        System.out.printf("%-20s | %-12s | %-12s%n", "TÍTULO", "DATA LIMITE", "STATUS");
        System.out.println("----------------------------------------");

        tarefas.forEach(tarefa -> {
            System.out.printf("%-20s | %-12s | %-12s%n",
                    limitarTexto(tarefa.getTitulo(), 20),
                    tarefa.getDataLimite().format(formatter),
                    tarefa.getStatus());
        });

        System.out.println("----------------------------------------");
    }

    private static String limitarTexto(String texto, int limite) {
        if (texto.length() <= limite) {
            return texto;
        }
        return texto.substring(0, limite - 3) + "...";
    }

    private void adicionarTarefasExemplo() {
        // Adicionar algumas tarefas para teste
        tarefaService.adicionarTarefa(new Tarefa(
                "Estudar Java",
                "Revisar conceitos de Streams e CompletableFuture",
                LocalDate.now().plusDays(1),
                StatusTarefa.PENDENTE));

        tarefaService.adicionarTarefa(new Tarefa(
                "Fazer compras",
                "Comprar itens para o escritório",
                LocalDate.now().plusDays(2),
                StatusTarefa.EM_ANDAMENTO));

        tarefaService.adicionarTarefa(new Tarefa(
                "Enviar relatório",
                "Preparar e enviar relatório mensal",
                LocalDate.now().plusDays(5),
                StatusTarefa.PENDENTE));
    }
}
