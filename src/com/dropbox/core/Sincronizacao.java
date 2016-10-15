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

    public Sincronizacao(EnvioArquivos envioArquivos) throws InterruptedException, DbxException {
        this.client = envioArquivos.getAutenticacao().getClient();
        this.caminhoPastaLocal = envioArquivos.getCaminhoPastaLocal();
        Maybe<DbxEntry.WithChildren> modificacoesDir = client.getMetadataWithChildrenIfChanged("/" + client.getAccountInfo().displayName.trim(), envioArquivos.getMetadataDirAnterior().hash);
        // IF: TRUE se conteudo do metadata-atualizado não coincide com o do metadata-anterior
        if (modificacoesDir.isJust()) {
            DbxEntry.WithChildren metadataDirAtualizado = modificacoesDir.getJust();
            listarRevArquivos(metadataDirAtualizado);
            atualizarPastaLocal(envioArquivos.getMetadataDirAnterior(), metadataDirAtualizado);
            envioArquivos.setMetadataDirAnterior(metadataDirAtualizado);
        }
        Thread.sleep(5000);
    }

    private void atualizarPastaLocal(DbxEntry.WithChildren metadataDirAnterior, DbxEntry.WithChildren metadataDirAtualizado) {
        
    }

    private void listarRevArquivos(DbxEntry.WithChildren metadataDir) {
        for (DbxEntry filho : metadataDir.children) {
            System.out.print("> " + filho.name);
            System.out.println(" - " + filho.asFile().rev);
        }
        System.out.println("-----------------------------");
    }
}
