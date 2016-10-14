package com.dropbox.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * EFETUAR UPLOAD DE ARQUIVOS NA NUVEM
 *
 * @author Eric
 */
public class EnvioArquivos {

    private File path;
    private File[] conteudoDir;

    public EnvioArquivos(Autenticacao autenticacao) throws IOException, DbxException {
        carregarDiretorioLocal();
        criarDiretorioNuvem(autenticacao);
    }

    private void carregarDiretorioLocal() throws IOException {
        System.out.println("4-Digite o caminho de uma pasta neste computador. Exemplo: ");
        path = new File("");
        System.out.println(path.getCanonicalPath());
        System.out.println("Para sincronizar seus respectivos arquivos na nuvem: ");
        Scanner entrada = new Scanner(System.in);
        String caminhoDir = entrada.next(); // Não foi tratado pathnames com acentos
        path = new File(caminhoDir);
        conteudoDir = path.listFiles();
    }

    private void criarDiretorioNuvem(Autenticacao autenticacao) throws IOException, DbxException {
        for (File arquivo : conteudoDir) {
            System.out.println("> " + arquivo.getName());
            path = new File(arquivo.getCanonicalPath());
            FileInputStream inputStream = new FileInputStream(path);
            DbxEntry.Folder diretorio = autenticacao.client.createFolder("/" + autenticacao.client.getAccountInfo().displayName.trim());
            DbxEntry.File arquivoCarregado = autenticacao.client.uploadFile("/" + autenticacao.client.getAccountInfo().displayName.trim() + "/" + arquivo.getName(), DbxWriteMode.add(), path.length(), inputStream);
            inputStream.close();
        }
        System.out.println("--Arquivos carregados na nuvem--");
    }
}
