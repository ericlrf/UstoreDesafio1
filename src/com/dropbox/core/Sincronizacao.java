package com.dropbox.core;

import com.dropbox.core.util.Maybe;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * ATUALIZAR ALTERAÇÃO DE ARQUIVOS (DA PASTA NA NUVEM) NA PASTA LOCAL
 *
 * @author Eric
 */
public class Sincronizacao { // corrigir variaveis para caminhoPastaNuvem

    private DbxClient client;
    private EnvioArquivos envioArquivos;
    private String caminhoPastaLocal;
    private String caminhoPastaNuvem;
    private String cursor = null;

    public Sincronizacao(EnvioArquivos envioArquivos) {
        this.client = envioArquivos.getAutenticacao().getClient();
        this.caminhoPastaLocal = envioArquivos.getCaminhoPastaLocal();
        this.caminhoPastaNuvem = envioArquivos.getCaminhoPastaNuvem();
        this.envioArquivos = envioArquivos;
    }

    public void atualizarPastaLocal() throws InterruptedException, DbxException, IOException {
        DbxDelta<DbxEntry> result = client.getDeltaWithPathPrefix(cursor, caminhoPastaNuvem);
        cursor = result.cursor;
        for (DbxDelta.Entry entry : result.entries) {
            if (entry.metadata == null) {
                String fileName = entry.lcPath;
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//                System.out.println("Excluído: " + entry.lcPath);
                File deletedFile = new File(caminhoPastaLocal + "\\" + fileName);
                deletedFile.delete();
            } else {
                DbxEntry file = client.getMetadata(entry.lcPath);
//                System.out.println("Adicionado/Modificado: " + entry.lcPath);
                FileOutputStream outputStream = new FileOutputStream(caminhoPastaLocal + "\\" + file.name);
                try {
                    DbxEntry.File downloadedFile = client.getFile(entry.lcPath, null, outputStream);
                } finally {
                    outputStream.close();
                }
            }
        }
        if (!result.hasMore) {
            envioArquivos.setMetadataDirAnterior(client.getMetadataWithChildren(caminhoPastaNuvem));
//            client.getLongpollDelta(cursor, 30);
            Thread.sleep(4000);
        }
    }

    public void atualizarPastaNuvem() {

    }

    private void listarRevArquivos(DbxEntry.WithChildren metadataDir) {
        for (DbxEntry filho : metadataDir.children) {
            System.out.print("> " + filho.name);
            System.out.println(" - " + filho.asFile().rev);
        }
        System.out.println("-----------------------------");
    }

//    private void atualizarPasta(DbxEntry.WithChildren metadataDirAnterior, DbxEntry.WithChildren metadataDirAtualizado) {
//        for (DbxEntry filhoAtualizado : metadataDirAtualizado.children) {
//            if (!metadataDirAnterior.children.contains(filhoAtualizado)) {
//                for (DbxEntry filhoAnterior : metadataDirAnterior.children) {
//                    // IF: TRUE se o arquivo foi editado/renomeado
//                    if (filhoAtualizado.name.equals(filhoAnterior.name)) {
//                        //del arq na pasta local
//                    }
//                }
//                //baixar arq da nuvem
//            }
//        }
//    }
//        Maybe<DbxEntry.WithChildren> modificacoesDir = client.getMetadataWithChildrenIfChanged(caminhoPastaNuvem, envioArquivos.getMetadataDirAnterior().hash);
//        // IF: TRUE se conteudo do metadata-atualizado não coincide com o do metadata-anterior
//        if (modificacoesDir.isJust()) {
//            DbxEntry.WithChildren metadataDirAtualizado = modificacoesDir.getJust();
//            listarRevArquivos(metadataDirAtualizado);
//            atualizarPastaLocal(envioArquivos.getMetadataDirAnterior(), metadataDirAtualizado);
//            atualizarPastaNuvem();
//            envioArquivos.setMetadataDirAnterior(metadataDirAtualizado);
//        }
}
