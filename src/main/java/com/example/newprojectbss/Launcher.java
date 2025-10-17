package com.example.newprojectbss;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Launcher extends JFrame {
    private static final String APP_PATH = "out/artifacts/newprojectbss_jar/newprojectbss.jar";
    private static final String VERSAO_LOCAL = "1.0.1";
    private static final String VERSAO_REMOTA_URL =
            "https://raw.githubusercontent.com/YgorBL00/test/refs/heads/main/src/main/java/com/example/newprojectbss/versao.txt";
    private static final String JAR_URL =
            "https://raw.githubusercontent.com/YgorBL00/test/main/out/artifacts/NewProjectbss_jar/newprojectbss.jar";

    private JLabel statusLabel;
    private JProgressBar progressBar;

    public Launcher() {
        setTitle("Launcher BSS");
        setSize(400, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        statusLabel = new JLabel("Iniciando verificação...", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.add(statusLabel, BorderLayout.NORTH);
        panel.add(progressBar, BorderLayout.CENTER);

        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Launcher launcher = new Launcher();
            launcher.setVisible(true);
            launcher.verificarAtualizacao();
        });
    }

    private void verificarAtualizacao() {
        new Thread(() -> {
            try {
                atualizarStatus("Verificando atualizações...");
                String versaoRemota = lerVersaoRemota();

                if (!VERSAO_LOCAL.equals(versaoRemota)) {
                    atualizarStatus("Nova versão " + versaoRemota + " encontrada. Baixando...");
                    progressBar.setIndeterminate(false);
                    baixarNovaVersao();
                    atualizarStatus("Atualização concluída!");
                } else {
                    atualizarStatus("Nenhuma atualização encontrada.");
                }

                Thread.sleep(1000);
                atualizarStatus("Iniciando aplicação...");
                iniciarApp();

            } catch (Exception e) {
                e.printStackTrace();
                atualizarStatus("Erro: " + e.getMessage());
                JOptionPane.showMessageDialog(this,
                        "Erro ao atualizar: " + e.getMessage(),
                        "Erro",
                        JOptionPane.ERROR_MESSAGE);
            }
        }).start();
    }

    private void atualizarStatus(String msg) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(msg));
    }

    private String lerVersaoRemota() throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(VERSAO_REMOTA_URL).openStream()))) {
            return in.readLine().trim();
        }
    }

    private void baixarNovaVersao() throws IOException {
        File appFile = new File(APP_PATH);
        appFile.getParentFile().mkdirs();

        try (InputStream is = new URL(JAR_URL).openStream()) {
            Files.copy(is, appFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private void iniciarApp() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", APP_PATH);
        pb.inheritIO();
        pb.start();
        System.exit(0);
    }
}
