package com.dropbox.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

/**
 * DEFINIR PASTA LOCAL, NA NUVEM E EFETUAR UPLOAD DOS ARQUIVOS JÁ EXISTENTES
 *
 * @author Eric
 */
public class EnvioArquivos {

    private File path;
    private File[] conteudoDir;
    private Autenticacao autenticacao;
    private String caminhoPastaLocal;
    private String caminhoPastaNuvem;
    private Date criacaoPastaNuvem;
    private DbxEntry.WithChildren metadataDirAnterior;

    public EnvioArquivos(Autenticacao autenticacao) throws IOException, DbxException {
        this.autenticacao = autenticacao;
        carregarDiretorioLocal();
        criarDiretorioNuvem(autenticacao);
    }

    /**
     * Define a pasta do computador local que será sincronizada na nuvem.
     * Não reconhece o caminho absoluto de uma pasta que contém palavras acentuadas.
     */
    private void carregarDiretorioLocal() throws IOException {
        System.out.println("4-Digite o caminho de uma pasta neste computador. Exemplo: ");
        path = new File("");
        System.out.println(path.getCanonicalPath());
        System.out.println("Para sincronizar seus respectivos arquivos na nuvem: ");
        Scanner entrada = new Scanner(System.in);
        caminhoPastaLocal = entrada.next(); // Não foi tratado pathnames com palavras acentuadas
        path = new File(caminhoPastaLocal);
        conteudoDir = path.listFiles();
    }

    private void criarDiretorioNuvem(Autenticacao autenticacao) throws IOException, DbxException {
        caminhoPastaNuvem = "/" + autenticacao.getClient().getAccountInfo().displayName;
        DbxEntry.Folder pastaNuvem = autenticacao.getClient().createFolder(caminhoPastaNuvem);
        DbxEntry.WithChildren metadataPastaNuvem = autenticacao.getClient().getMetadataWithChildren(caminhoPastaNuvem);
        if (!metadataPastaNuvem.children.isEmpty()) {
            autenticacao.getClient().delete(caminhoPastaNuvem);
            DbxEntry.Folder novaPastaNuvem = autenticacao.getClient().createFolder(caminhoPastaNuvem);
        }
        criacaoPastaNuvem = new Date(System.currentTimeMillis());
        for (File arquivo : conteudoDir) {
            path = new File(arquivo.getCanonicalPath());
            FileInputStream inputStream = new FileInputStream(path);
            DbxEntry.File arquivoCarregado = autenticacao.getClient().uploadFile(caminhoPastaNuvem + "/" + arquivo.getName(), DbxWriteMode.add(), path.length(), inputStream);
            inputStream.close();
        }
        metadataDirAnterior = autenticacao.getClient().getMetadataWithChildren(caminhoPastaNuvem);
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

    public String getCaminhoPastaNuvem() {
        return caminhoPastaNuvem;
    }

    public void setCaminhoPastaNuvem(String caminhoPastaNuvem) {
        this.caminhoPastaNuvem = caminhoPastaNuvem;
    }

    public DbxEntry.WithChildren getMetadataDirAnterior() {
        return metadataDirAnterior;
    }

    public void setMetadataDirAnterior(DbxEntry.WithChildren metadataDirAnterior) {
        this.metadataDirAnterior = metadataDirAnterior;
    }

    public Date getCriacaoPastaNuvem() {
        return criacaoPastaNuvem;
    }

    public void setCriacaoPastaNuvem(Date criacaoPastaNuvem) {
        this.criacaoPastaNuvem = criacaoPastaNuvem;
    }

}
