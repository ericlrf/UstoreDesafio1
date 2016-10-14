package com.dropbox.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.Scanner;

/**
 *
 * @author Eric
 */
public class Main {

    public static void main(String[] args) throws DbxException, IOException {
        Autenticacao autenticacao = new Autenticacao();
        // EFETUAR UPLOAD DE ARQUIVOS NA NUVEM
        System.out.println("4-Digite o caminho de uma pasta neste computador. Exemplo: ");
        File path = new File("");
        System.out.println(path.getCanonicalPath());
        System.out.println("Para sincronizar seus respectivos arquivos na nuvem: ");
        Scanner entrada = new Scanner(System.in);
        String caminhoDir = entrada.next(); // Não foi tratado pathnames com acentos
        path = new File(caminhoDir);
        File[] conteudoDir = path.listFiles();
        for (File arquivo : conteudoDir) {
            System.out.println("> " + arquivo.getName());
            path = new File(arquivo.getCanonicalPath());
            FileInputStream inputStream = new FileInputStream(path);
            DbxEntry.Folder diretorio = autenticacao.client.createFolder("/" + autenticacao.client.getAccountInfo().displayName.trim());
            DbxEntry.File arquivoCarregado = autenticacao.client.uploadFile("/" + autenticacao.client.getAccountInfo().displayName.trim() + "/" + arquivo.getName(), DbxWriteMode.add(), path.length(), inputStream);
//        System.out.println(arquivoCarregado.toString());
            inputStream.close();
        }
        System.out.println("--Arquivos carregados na nuvem--");
    }
}
// System.out.println("5-Crie ou edite arquivos na pasta escolhida.");
