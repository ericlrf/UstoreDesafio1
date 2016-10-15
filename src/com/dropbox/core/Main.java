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
        System.out.println("5-Crie ou edite arquivos na pasta escolhida. ");
        System.out.println("Serão sincronizados automaticamente na nuvem.");
        while (true) {            
            sincronizacao.verificarPastaNuvem();
        }
    }
}
