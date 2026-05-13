package br.com.pereiraeng.io;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import br.com.pereiraeng.core.Flow;

/**
 * Classe abstrata do objeto que faz a leitura de um arquivo, repassando pela
 * função {@link #incomingData(String)} cada uma das linhas que o compõe
 * 
 * @author Philipe PEREIRA
 *
 */
public abstract class Parser implements Flow<String> {

	/**
	 * Função que procede com a leitura de um arquivo
	 * 
	 * @param file arquivo a ser lido
	 */
	public void parse(File file) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			String str = null;
			while ((str = raf.readLine()) != null)
				incomingData(str);
			raf.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * Função que procede com a leitura de um arquivo
	 * 
	 * @param file caminho para o arquivo
	 */
	public void parse(String file) {
		parse(new File(file));
	}
}
