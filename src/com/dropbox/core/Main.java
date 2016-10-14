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
//        Autenticacao.solicitar();
        // EFETUAR UPLOAD DE ARQUIVOS NA NUVEM
        System.out.println("4-Digite o caminho de uma pasta neste computador. Exemplo: ");
        File path = new File("");
        System.out.println(path.getCanonicalPath());
        System.out.println("Para sincronizar seus respectivos arquivos na nuvem: ");
        Scanner entrada = new Scanner(System.in);
        String caminhoDir = entrada.next();
        path = new File(caminhoDir);
        System.out.println(path.getCanonicalPath());
        // criar rotina p/ ñ interromper a execucao deste App infinitamente
        System.out.println("5-Crie ou edite arquivos na pasta escolhida.");
        File[] conteudoDir = path.listFiles();
//        for (File arquivo : conteudoDir) {
//            System.out.println("> " + arquivo.getName());
        path = new File(conteudoDir[0].getCanonicalPath());
        FileInputStream inputStream = new FileInputStream(path);
//                DbxEntry.Folder diretorio = Autenticacao.client.createFolder("/" + Autenticacao.nomeCliente.trim());
//        DbxEntry.File arquivoCarregado = Autenticacao.client.uploadFile("/" + conteudoDir[0].getName(), DbxWriteMode.add(), path.length(), inputStream);
//        System.out.println(arquivoCarregado.toString());
        inputStream.close();
//        }
        System.out.println("--Arquivos carregados na nuvem--");
    }
}
