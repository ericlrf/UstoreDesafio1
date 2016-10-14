package com.dropbox.core;

import com.dropbox.core.util.Maybe;

/**
 * ATUALIZAR ALTERAÇÃO DE ARQUIVOS NA PASTA LOCAL
 *
 * @author Eric
 */
public class Sincronizacao {

    private DbxClient client;

    public Sincronizacao(EnvioArquivos envioArquivos) throws InterruptedException, DbxException {
        this.client = envioArquivos.autenticacao.client;
        Maybe<DbxEntry.WithChildren> metadataDirAtualizado = client.getMetadataWithChildrenIfChanged("/" + client.getAccountInfo().displayName.trim(), envioArquivos.hashDirAnterior);

        if (metadataDirAtualizado.isJust()) {
            DbxEntry.WithChildren lista = metadataDirAtualizado.getJust();
            for (DbxEntry filho : lista.children) {
                System.out.println("> " + filho.name);
            }
            System.out.println("-----------------------------");
        }

//        DbxEntry.WithChildren lista = client.getMetadataWithChildren("/" + client.getAccountInfo().displayName.trim());
        Thread.sleep(5000);
    }

    private void arquivosAddNaNuvem() {

    }

    private void arquivosDelNaNuvem() {

    }
}
