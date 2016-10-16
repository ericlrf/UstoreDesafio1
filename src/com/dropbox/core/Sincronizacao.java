package com.dropbox.core;

import com.dropbox.core.util.Maybe;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * ATUALIZAR ALTERAÇÃO DE ARQUIVOS DA PASTA NA NUVEM E DA PASTA LOCAL
 *
 * @author Eric
 */
public class Sincronizacao { // corrigir variaveis para caminhoPastaNuvem

    private File path;
    private DbxClient client;
    private EnvioArquivos envioArquivos;
    private String caminhoPastaLocal;
    private String caminhoPastaNuvem;
    private Date ultimaModificacaoNuvem;
    private Date criacaoPastaNuvem;
    private String cursor = null;

    public Sincronizacao(EnvioArquivos envioArquivos) {
        this.client = envioArquivos.getAutenticacao().getClient();
        this.caminhoPastaLocal = envioArquivos.getCaminhoPastaLocal();
        this.caminhoPastaNuvem = envioArquivos.getCaminhoPastaNuvem();
        this.criacaoPastaNuvem = envioArquivos.getCriacaoPastaNuvem();
        this.envioArquivos = envioArquivos;
    }

    public void atualizarPastaLocal() throws InterruptedException, DbxException, IOException {
        DbxDelta<DbxEntry> result = client.getDeltaWithPathPrefix(cursor, caminhoPastaNuvem);
        cursor = result.cursor;
        for (DbxDelta.Entry entry : result.entries) {
            if (entry.metadata == null) { // TRUE se arquivo na nuvem foi excluido
                String fileName = entry.lcPath;
                fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
//                System.out.println("Excluído: " + entry.lcPath);
                File deletedFile = new File(caminhoPastaLocal + "\\" + fileName);
                deletedFile.delete();
                ultimaModificacaoNuvem = new Date(System.currentTimeMillis());
            } else { // FALSE se arquivo na nuvem foi adicionado/editado
                DbxEntry file = client.getMetadata(entry.lcPath);
                if (file.isFile()) { // Este app não implementa sincronização de sub-pastas
//                    System.out.println("Adicionado/Modificado: " + entry.lcPath);
                    FileOutputStream outputStream = new FileOutputStream(caminhoPastaLocal + "\\" + file.name);
                    try {
                        DbxEntry.File downloadedFile = client.getFile(entry.lcPath, null, outputStream);
                    } finally {
                        outputStream.close();
                    }
                    ultimaModificacaoNuvem = new Date(System.currentTimeMillis());
                }
            }
        }
        if (!result.hasMore) {
            System.out.println("--" + ultimaModificacaoNuvem + "--");
            envioArquivos.setMetadataDirAnterior(client.getMetadataWithChildren(caminhoPastaNuvem));
            Thread.sleep(4000);
        }
    }

    public void atualizarPastaNuvem() throws DbxException, IOException {
        int contador = 0;
        Date date;
        File pasta = new File(caminhoPastaLocal);
        for (File arquivoLocal : pasta.listFiles()) {
            if (arquivoLocal.isFile()) { // Este app não implementa sincronização de sub-pastas
                for (DbxEntry arquivoNuvem : envioArquivos.getMetadataDirAnterior().children) {
                    if (arquivoNuvem.isFile()) { // Este app não implementa sincronização sub-pastas
                        if (arquivoLocal.getName().equalsIgnoreCase(arquivoNuvem.name)) { // existe 1 arquivo na nuvem com nome do arquivoLocal
                            date = new Date(arquivoLocal.lastModified());
                            if (date.before(criacaoPastaNuvem)) { // arquivoLocal adicionado/editado antes da criacao da pasta na nuvem
                                // deletar arquivo na nuvem com nome do arquivoLocal
                                client.delete(caminhoPastaNuvem + "/" + arquivoNuvem.name);
                                // upload arquivoLocal
                                path = new File(arquivoLocal.getCanonicalPath());
                                FileInputStream inputStream = new FileInputStream(path);
                                DbxEntry.File arquivoCarregado = client.uploadFile(caminhoPastaNuvem + "/" + arquivoLocal.getName(), DbxWriteMode.add(), path.length(), inputStream);
                                inputStream.close();
                            }
                            if (date.after(ultimaModificacaoNuvem)) { // arquivoLocal adicionado/editado depois da ultima atualizacao da nuvem na pastaLocal
                                // deletar arquivo na nuvem com nome do arquivoLocal
                                client.delete(caminhoPastaNuvem + "/" + arquivoNuvem.name);
                                // upload arquivoLocal
                                path = new File(arquivoLocal.getCanonicalPath());
                                FileInputStream inputStream = new FileInputStream(path);
                                DbxEntry.File arquivoCarregado = client.uploadFile(caminhoPastaNuvem + "/" + arquivoLocal.getName(), DbxWriteMode.add(), path.length(), inputStream);
                                inputStream.close();
                            }
                        } else { // verificado mais um arquivo na nuvem sem o nome do arquivoLocal
                            contador++;
                        }
                    }
                }
                if (contador == envioArquivos.getMetadataDirAnterior().children.size()) { // não existe nenhum arquivo na nuvem com nome do arquivoLocal
                    //upload arquivoLocal
                    path = new File(arquivoLocal.getCanonicalPath());
                    FileInputStream inputStream = new FileInputStream(path);
                    DbxEntry.File arquivoCarregado = client.uploadFile(caminhoPastaNuvem + "/" + arquivoLocal.getName(), DbxWriteMode.add(), path.length(), inputStream);
                    inputStream.close();
                }
            }
        }
        contador = 0;
        if (pasta.listFiles().length < envioArquivos.getMetadataDirAnterior().children.size()) { // quantidade de arquivos na pastaLocal é menor que na nuvem
            for (DbxEntry arquivoNuvem : envioArquivos.getMetadataDirAnterior().children) {
                if (arquivoNuvem.isFile()) { // Este app não implementa sincronização sub-pastas
                    for (File arquivoLocal : pasta.listFiles()) {
                        if (arquivoLocal.isFile()) { // Este app não implementa sincronização de sub-pastas
                            if (!arquivoNuvem.name.equalsIgnoreCase(arquivoLocal.getName())) { // verificado mais um arquivoLocal sem o nome do arquivo na nuvem
                                contador++;
                            }
                        }
                    }
                    if (contador == pasta.listFiles().length) { // não existe nenhum arquivoLocal com nome do arquivo na nuvem
                        // deletar arquivo na nuvem
                        client.delete(caminhoPastaNuvem + "/" + arquivoNuvem.name);
                    }
                }
            }
        }
        System.out.println("------------------------------");
    }
}
