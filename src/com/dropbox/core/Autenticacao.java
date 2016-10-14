package com.dropbox.core;

import java.util.Locale;
import java.util.Scanner;

/**
 * AUTENTICAÇÃO DO APP PELO USUÁRIO
 *
 * @author Eric
 */
public class Autenticacao {

    final static String APP_KEY = "taf1n7qqjq11vzu";
    final static String APP_SECRET = "mbdfggkxx5cfkzj";
    static String CODE = "1gsxh84tqGwAAAAAAAAXV5mqi9fj6PRfGfaYarYZqYU";
    

    public DbxWebAuthNoRedirect carregarChaveSenha(){
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxRequestConfig config = new DbxRequestConfig("FileSync/1.0", Locale.getDefault().toString());
        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(config, appInfo);
        return webAuth;
    }
    
    public String solicitarCodigo(){
        String authorizeUrl = carregarChaveSenha().start();
        System.out.println("1-Acesse o endereco abaixo num browser: ");
        System.out.println(authorizeUrl);
        System.out.print("2-Clique no botão 'Permitir'.");
        System.out.println("(Necessário estar logado numa conta de usuário)");
        System.out.println("3-Digite o código informado: ");
        Scanner entrada = new Scanner(System.in);
        return entrada.next();
    }
}
//        DbxAuthFinish authFinish = webAuth.finish(CODE);
//        String accessToken = authFinish.accessToken;
//        DbxClient client = new DbxClient(config, accessToken);
//        System.out.println("--App autenticada pelo usuario '" + client.getAccountInfo().displayName + "'--");
