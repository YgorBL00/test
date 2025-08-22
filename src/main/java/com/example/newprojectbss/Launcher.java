package com.example.newprojectbss;

import javax.swing.JOptionPane;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class Launcher {
    private static final String APP_PATH = "out/artifacts/newprojectbss_jar/newprojectbss.jar";
    private static final String VERSAO_LOCAL = "1.0.0";
    private static final String VERSAO_REMOTA_URL =
            "https://raw.githubusercontent.com/YgorBL00/test/refs/heads/main/src/main/java/com/example/newprojectbss/versao.txt";
    private static final String JAR_URL =
            "https://raw.githubusercontent.com/YgorBL00/test/main/out/artifacts/NewProjectbss_jar/newprojectbss.jar";

    public static void main(String[] args) {
        try {
            JOptionPane.showMessageDialog(null, "Verificando atualizações...", "Launcher", JOptionPane.INFORMATION_MESSAGE);

            String versaoRemota = lerVersaoRemota();
            if (!VERSAO_LOCAL.equals(versaoRemota)) {
                JOptionPane.showMessageDialog(null, "Nova versão detectada: " + versaoRemota, "Atualização", JOptionPane.INFORMATION_MESSAGE);
                baixarNovaVersao();
                JOptionPane.showMessageDialog(null, "Atualização concluída!", "Launcher", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Nenhuma atualização encontrada. Iniciando app...", "Launcher", JOptionPane.INFORMATION_MESSAGE);
            }

            iniciarApp();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Erro ao atualizar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static String lerVersaoRemota() throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(VERSAO_REMOTA_URL).openStream()))) {
            return in.readLine().trim();
        }
    }

    private static void baixarNovaVersao() throws IOException {
        File appFile = new File(APP_PATH);
        appFile.getParentFile().mkdirs();

        try (InputStream is = new URL(JAR_URL).openStream()) {
            Files.copy(is, appFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void iniciarApp() throws IOException {
        ProcessBuilder pb = new ProcessBuilder("java", "-jar", APP_PATH);
        pb.inheritIO();
        pb.start();
        System.exit(0);
    }
}
