package com.spring.ApiSystem.external.etl;

import org.springframework.stereotype.Service;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@Order(2)
public class EtlService implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(EtlService.class);

    private final Path projectPath = Path.of("etl").toAbsolutePath().normalize();
    private final Path pythonMainPath = projectPath.resolve("main.py");
    private final Path pythonVenvPath = projectPath.resolve(
            isWindows() ? ".venv/Scripts/python.exe" : ".venv/bin/python"
    );
    private final Path requirementsPath = projectPath.resolve("requirements.txt");
    private final Path resultLogsSummary = projectPath.resolve("etl_summary.log");
    private final Path resultLogsError = projectPath.resolve("etl_errors.log");

    @Override
    public void run(String... args) {
        try {
            executeEtl();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Execução automática do ETL foi interrompida.", e);
        } catch (Exception e) {
            log.error("Falha na execução automática do ETL. A API continuará iniciando.", e);
        }
    }

    public void executeEtl() throws IOException, InterruptedException {
        constructPythonVenv();
        Files.deleteIfExists(resultLogsSummary);
        Files.deleteIfExists(resultLogsError);

        ProcessBuilder pb = new ProcessBuilder(pythonVenvPath.toString(), pythonMainPath.toString());
        pb.directory(projectPath.toFile());
        pb.inheritIO();
        Process process = pb.start();
        int exitCode;
        try {
            exitCode = process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw e;
        }

        if (exitCode != 0) {
            if (verifyLogsPython()) {
                String summaryLog = Files.exists(resultLogsSummary)
                        ? readLogs(resultLogsSummary)
                        : "Resumo não gerado.";
                String errorLog = Files.exists(resultLogsError)
                        ? readLogs(resultLogsError)
                        : "Log de erros não gerado.";
                log.error("Erro do ETL:\nResumo:\n{}\nErros:\n{}",
                        summaryLog, errorLog);
            }
            throw new IllegalStateException(
                    "Erro ao executar o script Python. Código de saída: " + exitCode
            );
        }

        if (!Files.exists(resultLogsSummary)) {
            throw new IOException("Log de resumo do Python não encontrado.");
        }
        String errorLog = Files.exists(resultLogsError)
                ? readLogs(resultLogsError)
                : "Nenhum erro registrado.";
        log.info("Resumo do ETL:\n{}\nErros do ETL:\n{}",
                readLogs(resultLogsSummary), errorLog);
        System.out.println("Script Python executado com sucesso.");
    }

    private void constructPythonVenv() throws IOException, InterruptedException {
        boolean created = false;
        if (!Files.exists(pythonVenvPath)) {
            String pythonCommand = isWindows() ? "py" : "python3";
            runProcess(new ProcessBuilder(pythonCommand, "-m", "venv", ".venv"));
            created = true;
        }

        if (!Files.exists(pythonVenvPath)) {
            throw new IOException("Executável Python do ambiente virtual não foi criado.");
        }

        if (created && Files.exists(requirementsPath)) {
            runProcess(new ProcessBuilder(
                    pythonVenvPath.toString(), "-m", "pip", "install", "-r", requirementsPath.toString()
            ));
        }
    }

    private int runProcess(ProcessBuilder processBuilder) throws IOException, InterruptedException {
        processBuilder.directory(projectPath.toFile());
        processBuilder.inheritIO();
        int exitCode = processBuilder.start().waitFor();
        if (exitCode != 0) {
            throw new IllegalStateException("Processo Python finalizado com código " + exitCode);
        }
        return exitCode;
    }

    private boolean verifyLogsPython() {
        return Files.exists(resultLogsSummary) || Files.exists(resultLogsError);
    }

    private String readLogs(Path logPath) throws IOException {
        return Files.readString(logPath);
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

}
