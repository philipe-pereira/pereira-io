package br.com.pereiraeng.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
	 * @param filepath caminho para o arquivo
	 */
	public void parse(String filepath) {
		parse(new File(filepath));
	}

	/**
	 * Função que procede com a leitura de um arquivo
	 * 
	 * @param file arquivo a ser lido
	 */
	public void parse(File file) {
		try {
			parse(new BufferedReader(new FileReader(file)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que procede com a leitura de um arquivo
	 * 
	 * @param stream stream de dados
	 */
	public void parse(InputStream stream) {
		parse(new BufferedReader(new InputStreamReader(stream)));
	}

	private void parse(BufferedReader reader) {
		try {
			String str = null;
			while ((str = reader.readLine()) != null)
				incomingData(str);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
