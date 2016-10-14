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
        EnvioArquivos envioArquivos = new EnvioArquivos(autenticacao);
    }
}
// System.out.println("5-Crie ou edite arquivos na pasta escolhida.");
