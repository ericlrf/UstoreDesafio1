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
    private Autenticacao autenticacao;
    private String caminhoPastaLocal;
    private DbxEntry.WithChildren metadataDirAnterior;

    public EnvioArquivos(Autenticacao autenticacao) throws IOException, DbxException {
        this.autenticacao = autenticacao;
        carregarDiretorioLocal();
        criarDiretorioNuvem(autenticacao);
    }

    private void carregarDiretorioLocal() throws IOException {
        System.out.println("4-Digite o caminho de uma pasta neste computador. Exemplo: ");
        path = new File("");
        System.out.println(path.getCanonicalPath());
        System.out.println("Para sincronizar seus respectivos arquivos na nuvem: ");
        Scanner entrada = new Scanner(System.in);
        caminhoPastaLocal = entrada.next(); // Não foi tratado pathnames com acentos
        path = new File(caminhoPastaLocal);
        conteudoDir = path.listFiles();
    }

    private void criarDiretorioNuvem(Autenticacao autenticacao) throws IOException, DbxException {
        DbxEntry.Folder diretorio = autenticacao.getClient().createFolder("/" + autenticacao.getClient().getAccountInfo().displayName.trim());
        for (File arquivo : conteudoDir) {
            System.out.println("> " + arquivo.getName());
            path = new File(arquivo.getCanonicalPath());
            FileInputStream inputStream = new FileInputStream(path);
            DbxEntry.File arquivoCarregado = autenticacao.getClient().uploadFile("/" + autenticacao.getClient().getAccountInfo().displayName.trim() + "/" + arquivo.getName(), DbxWriteMode.add(), path.length(), inputStream);
            inputStream.close();
        }
        metadataDirAnterior = autenticacao.getClient().getMetadataWithChildren("/" + autenticacao.getClient().getAccountInfo().displayName.trim());
        System.out.println("--Arquivos carregados na nuvem--");
    }

    public Autenticacao getAutenticacao() {
        return autenticacao;
    }

    public void setAutenticacao(Autenticacao autenticacao) {
        this.autenticacao = autenticacao;
    }

    public String getCaminhoPastaLocal() {
        return caminhoPastaLocal;
    }

    public void setCaminhoPastaLocal(String caminhoPastaLocal) {
        this.caminhoPastaLocal = caminhoPastaLocal;
    }

    public DbxEntry.WithChildren getMetadataDirAnterior() {
        return metadataDirAnterior;
    }

    public void setMetadataDirAnterior(DbxEntry.WithChildren metadataDirAnterior) {
        this.metadataDirAnterior = metadataDirAnterior;
    }

}
