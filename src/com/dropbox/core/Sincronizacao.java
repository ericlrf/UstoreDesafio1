package com.dropbox.core;

import com.dropbox.core.util.Maybe;
import java.io.File;

/**
 * ATUALIZAR ALTERAÇÃO DE ARQUIVOS NA PASTA LOCAL
 *
 * @author Eric
 */
public class Sincronizacao {

    private DbxClient client;
    private String caminhoPastaLocal;
    private String cursor = null;

    public Sincronizacao(EnvioArquivos envioArquivos) {
        this.client = envioArquivos.getAutenticacao().getClient();
        this.caminhoPastaLocal = envioArquivos.getCaminhoPastaLocal();
//        Maybe<DbxEntry.WithChildren> modificacoesDir = client.getMetadataWithChildrenIfChanged("/" + client.getAccountInfo().displayName.trim(), envioArquivos.getMetadataDirAnterior().hash);
//        // IF: TRUE se conteudo do metadata-atualizado não coincide com o do metadata-anterior
//        if (modificacoesDir.isJust()) {
//            DbxEntry.WithChildren metadataDirAtualizado = modificacoesDir.getJust();
//            listarRevArquivos(metadataDirAtualizado);
//            atualizarPastaLocal(envioArquivos.getMetadataDirAnterior(), metadataDirAtualizado);
//            atualizarPastaNuvem();
//            envioArquivos.setMetadataDirAnterior(metadataDirAtualizado);
//        }
    }

    private void atualizarPastaLocal(DbxEntry.WithChildren metadataDirAnterior, DbxEntry.WithChildren metadataDirAtualizado) {
        for (DbxEntry filhoAtualizado : metadataDirAtualizado.children) {
            if (!metadataDirAnterior.children.contains(filhoAtualizado)) {
                for (DbxEntry filhoAnterior : metadataDirAnterior.children) {
                    // IF: TRUE se o arquivo foi editado/renomeado
                    if (filhoAtualizado.name.equals(filhoAnterior.name)) {
                        //del arq na pasta local
                    }
                }
                //baixar arq da nuvem
            }
        }
    }

    public void verificarPastaNuvem() throws InterruptedException, DbxException {
        DbxDelta<DbxEntry> result = client.getDeltaWithPathPrefix(cursor, "/" + client.getAccountInfo().displayName.trim());
        cursor = result.cursor;
        if (result.reset) {
            System.out.println("--Pasta na nuvem limpa--");
        }
        for (DbxDelta.Entry entry : result.entries) {
            if (entry.metadata == null) {
                System.out.println("Excluído: " + entry.lcPath);
            } else {
                System.out.println("Adicionado/Modificado: " + entry.lcPath);
            }
        }
        if (!result.hasMore) {
//            client.getLongpollDelta(cursor, 30);
            Thread.sleep(4000);
        }
    }

    private void listarRevArquivos(DbxEntry.WithChildren metadataDir) {
        for (DbxEntry filho : metadataDir.children) {
            System.out.print("> " + filho.name);
            System.out.println(" - " + filho.asFile().rev);
        }
        System.out.println("-----------------------------");
    }

}
