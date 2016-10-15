package com.dropbox.core;

import java.io.IOException;

/**
 *
 * @author Eric
 */
public class Main {

    public static void main(String[] args) throws DbxException, IOException, InterruptedException {
        Autenticacao autenticacao = new Autenticacao();
        EnvioArquivos envioArquivos = new EnvioArquivos(autenticacao);
        Sincronizacao sincronizacao = new Sincronizacao(envioArquivos);
        System.out.println("5-Pronto!Os arquivos na pasta escolhida,");
        System.out.println("estão sincronizados com a nuvem.");
        while (true) {            
            sincronizacao.verificarPastaNuvem();
        }
    }
}
