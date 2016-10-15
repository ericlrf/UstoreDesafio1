package com.dropbox.core;

import java.util.Locale;
import java.util.Scanner;

/**
 * AUTENTICAÇÃO DO APP PELO USUÁRIO
 *
 * @author Eric
 */
public class Autenticacao {

    private final String APP_KEY = "taf1n7qqjq11vzu";
    private final String APP_SECRET = "mbdfggkxx5cfkzj";
    private String code = "1gsxh84tqGwAAAAAAAAXW6-ER--8omrmxY0uHBGhkDc";
    private DbxRequestConfig config;
    private DbxWebAuthNoRedirect webAuth;
    private DbxClient client;

    public Autenticacao() throws DbxException {
        carregarChaveSenha();
        solicitarCodigo();
        criarUsuario();
    }

    private void carregarChaveSenha() {
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        config = new DbxRequestConfig("FileSync/1.0", Locale.getDefault().toString());
        webAuth = new DbxWebAuthNoRedirect(config, appInfo);
    }

    private void solicitarCodigo() {
        String authorizeUrl = webAuth.start();
        System.out.println("1-Acesse o endereco abaixo num browser: ");
        System.out.println(authorizeUrl);
        System.out.print("2-Clique no botão 'Permitir'.");
        System.out.println("(Necessário estar logado numa conta de usuário)");
        System.out.println("3-Digite o código informado: ");
        Scanner entrada = new Scanner(System.in);
        code = entrada.next();

    }

    private void criarUsuario() throws DbxException {
        DbxAuthFinish authFinish = webAuth.finish(code);
        String accessToken = authFinish.accessToken;
        client = new DbxClient(config, accessToken);
        System.out.println("--App autenticada pelo usuario '" + client.getAccountInfo().displayName + "'--");
    }

    public DbxClient getClient() {
        return client;
    }

    public void setClient(DbxClient client) {
        this.client = client;
    }
    
}
