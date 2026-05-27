package br.com.pereiraeng.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

import br.com.pereiraeng.html.HTML;

public class IOutils {

	/**
	 * prefixo do URL de arquivos
	 */
	public static final String FILE_URL = "file:///";

	public static final String IP_HOME = "192.168.0.";

	public static void setHostPort(String host, String port) {
		if (host != null)
			System.setProperty("http.proxyHost", host);
		if (port != null)
			System.setProperty("http.proxyPort", port);
	}

	// == Funções de criação de objetos que manipulam a entrada e saída de dados ==

	/**
	 * Função que tenta estabelecer conexão com um ponto da WEB
	 * 
	 * @param server endereço
	 * @return <code>true</code> se for possível, <code>false</code> senão
	 */
	public static boolean pingHTML(String server) {
		return pingHTML(server, 2000);
	}

	/**
	 * Função que tenta estabelecer conexão com um ponto da WEB
	 * 
	 * @param str  sequência de caracteres do Universal Resource Locator
	 * @param time tempo limite, em milissegundos
	 * @return <code>true</code> se for possível, <code>false</code> senão
	 */
	public static boolean pingHTML(String str, int time) {
		int code = -1;
		try {
			URL url = new URI(str).toURL();
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			// opening a communications link to the resource
			connection.setConnectTimeout(time);
			// reading from a stream when a connection is established to a resource
			connection.setReadTimeout(5 * time);

			connection.setRequestMethod("HEAD");
			code = connection.getResponseCode();
			if (connection != null)
				connection.disconnect();
		} catch (IOException | URISyntaxException e) {
			System.err.println(e.getMessage());
		}
		if (code != HttpURLConnection.HTTP_OK)
			System.err.println(code);
		return code == HttpURLConnection.HTTP_OK;
	}

	public static boolean ping(String server) {
		return ping(server, 5000);
	}

	/**
	 * 
	 * @param server
	 * @param time   tempo limite, em milissegundos
	 * @return
	 */
	public static boolean ping(String server, int time) {
		InetAddress address = null;
		try {
			address = InetAddress.getByName(server);
		} catch (UnknownHostException e) {
			return false;
		}
		try {
			if (address.isReachable(time)) {
				System.out.println("Resposta do IP: " + address.getHostAddress());
				return true;
			} else {
				System.out.println("Host " + address.getHostName() + " is not reachable even once.");
				return false;
			}

		} catch (IOException e) {
			System.out.println("Network error.");
			return false;
		}
	}

	public static BufferedReader getBr(String url) {
		InputStream is = getIs(url);
		if (is != null)
			return new BufferedReader(new InputStreamReader(is));
		else
			return null;
	}

	public static BufferedReader getBrF(String file) {
		return getBr(new File(file));
	}

	public static BufferedReader getBr(File file) {
		try {
			return new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			System.err.println("O arquivo '" + file.getAbsolutePath() + "' não foi encontrado.");
		}
		return null;
	}

	/**
	 * 
	 * @param file
	 * @param charsetName
	 *                    <ul>
	 *                    <li>Cp858</i>
	 *                    <li>Cp437</i>
	 *                    <li>Cp775</i>
	 *                    <li>Cp850</i>
	 *                    <li>Cp852</i>
	 *                    <li>Cp855</i>
	 *                    <li>Cp857</i>
	 *                    <li>Cp862</i>
	 *                    <li>Cp866</i>
	 *                    <li>ISO8859_1</i>
	 *                    <li>ISO8859_2</i>
	 *                    <li>ISO8859_4</i>
	 *                    <li>ISO8859_5</i>
	 *                    <li>ISO8859_7</i>
	 *                    <li>ISO8859_9</i>
	 *                    <li>ISO8859_13</i>
	 *                    <li>ISO8859_15</i>
	 *                    <li>KOI8_R</i>
	 *                    <li>KOI8_U</i>
	 *                    <li>ASCII</i>
	 *                    <li>UTF8</i>
	 *                    <li>UTF-16</i>
	 *                    <li>UnicodeBigUnmarked</i>
	 *                    <li>UnicodeLittleUnmarked</i>
	 *                    <li>UTF_32</i>
	 *                    <li>UTF_32BE</i>
	 *                    <li>UTF_32LE</i>
	 *                    <li>UTF_32BE_BOM</i>
	 *                    <li>UTF_32LE_BOM</i>
	 *                    <li>Cp1250</i>
	 *                    <li>Cp1251</i>
	 *                    <li>Cp1252</i>
	 *                    <li>Cp1253</i>
	 *                    <li>Cp1254</i>
	 *                    <li>Cp1257</i>
	 *                    <li>UnicodeBig</i>
	 *                    <li>Cp737</i>
	 *                    <li>Cp874</i>
	 *                    </ul>
	 * @return
	 */
	public static BufferedReader getBr(File file, String charsetName) {
		try {
			return new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			System.err.println("O arquivo '" + file.getAbsolutePath() + "'  não foi encontrado.");
		}
		return null;
	}

	/**
	 * UTF8
	 * 
	 * @param file
	 * @return
	 */
	public static BufferedReader getBrUTF8(File file) {
		return getBr(file, "UTF8");
	}

	public static BufferedInputStream getBis(File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	// DATA INPUT STREAM

	public static DataInputStream getDis(String url) {
		InputStream is = getIs(url);
		if (is == null)
			return null;
		return new DataInputStream(is);
	}

	// INPUT STREAM

	private static InputStream getIs(String url) {
		if (url == null)
			return null;
		try {
			URLConnection urlConn = (new URI(url).toURL()).openConnection();
			urlConn.setDoInput(true);
			urlConn.setUseCaches(false);

			return urlConn.getInputStream();
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	// SCANNER

	/**
	 * 
	 * @param file
	 * @param charsetName
	 *                    <ul>
	 *                    <li>Cp858</i>
	 *                    <li>Cp437</i>
	 *                    <li>Cp775</i>
	 *                    <li>Cp850</i>
	 *                    <li>Cp852</i>
	 *                    <li>Cp855</i>
	 *                    <li>Cp857</i>
	 *                    <li>Cp862</i>
	 *                    <li>Cp866</i>
	 *                    <li>ISO8859_1</i>
	 *                    <li>ISO8859_2</i>
	 *                    <li>ISO8859_4</i>
	 *                    <li>ISO8859_5</i>
	 *                    <li>ISO8859_7</i>
	 *                    <li>ISO8859_9</i>
	 *                    <li>ISO8859_13</i>
	 *                    <li>ISO8859_15</i>
	 *                    <li>KOI8_R</i>
	 *                    <li>KOI8_U</i>
	 *                    <li>ASCII</i>
	 *                    <li>UTF8</i>
	 *                    <li>UTF-16</i>
	 *                    <li>UnicodeBigUnmarked</i>
	 *                    <li>UnicodeLittleUnmarked</i>
	 *                    <li>UTF_32</i>
	 *                    <li>UTF_32BE</i>
	 *                    <li>UTF_32LE</i>
	 *                    <li>UTF_32BE_BOM</i>
	 *                    <li>UTF_32LE_BOM</i>
	 *                    <li>Cp1250</i>
	 *                    <li>Cp1251</i>
	 *                    <li>Cp1252</i>
	 *                    <li>Cp1253</i>
	 *                    <li>Cp1254</i>
	 *                    <li>Cp1257</i>
	 *                    <li>UnicodeBig</i>
	 *                    <li>Cp737</i>
	 *                    <li>Cp874</i>
	 *                    </ul>
	 * @return
	 */
	public static Scanner getSc(String file, String charsetName) {
		return getSc(new File(file), charsetName);
	}

	/**
	 * 
	 * @param file
	 * @param charsetName
	 *                    <ul>
	 *                    <li>Cp858</i>
	 *                    <li>Cp437</i>
	 *                    <li>Cp775</i>
	 *                    <li>Cp850</i>
	 *                    <li>Cp852</i>
	 *                    <li>Cp855</i>
	 *                    <li>Cp857</i>
	 *                    <li>Cp862</i>
	 *                    <li>Cp866</i>
	 *                    <li>ISO8859_1</i>
	 *                    <li>ISO8859_2</i>
	 *                    <li>ISO8859_4</i>
	 *                    <li>ISO8859_5</i>
	 *                    <li>ISO8859_7</i>
	 *                    <li>ISO8859_9</i>
	 *                    <li>ISO8859_13</i>
	 *                    <li>ISO8859_15</i>
	 *                    <li>KOI8_R</i>
	 *                    <li>KOI8_U</i>
	 *                    <li>ASCII</i>
	 *                    <li>UTF8</i>
	 *                    <li>UTF-16</i>
	 *                    <li>UnicodeBigUnmarked</i>
	 *                    <li>UnicodeLittleUnmarked</i>
	 *                    <li>UTF_32</i>
	 *                    <li>UTF_32BE</i>
	 *                    <li>UTF_32LE</i>
	 *                    <li>UTF_32BE_BOM</i>
	 *                    <li>UTF_32LE_BOM</i>
	 *                    <li>Cp1250</i>
	 *                    <li>Cp1251</i>
	 *                    <li>Cp1252</i>
	 *                    <li>Cp1253</i>
	 *                    <li>Cp1254</i>
	 *                    <li>Cp1257</i>
	 *                    <li>UnicodeBig</i>
	 *                    <li>Cp737</i>
	 *                    <li>Cp874</i>
	 *                    </ul>
	 * @return
	 */
	public static Scanner getSc(File file, String charsetName) {
		Scanner input = null;
		try {
			input = new Scanner(new FileInputStream(file), charsetName);
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo " + file + " inexistente.");
		}
		return input;
	}

	public static Scanner getSc(String file) {
		return getSc(new File(file));
	}

	public static Scanner getSc(File file) {
		Scanner input = null;
		try {
			input = new Scanner(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return input;
	}

	// =============== Funções de leitura e escrita de arquivos ===============

	/**
	 * Função que lê o conteúdo de um dado arquivo, a partir do objeto
	 * {@link RandomAccessFile}
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser lido
	 * @return <code>String</code> do conteúdo do arquivo
	 */
	public static String readFile(File file) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "r");

			StringBuilder out = new StringBuilder();

			String str = null;
			while ((str = raf.readLine()) != null)
				out.append(str + "\n");

			raf.close();

			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Função que lê o conteúdo de um dado arquivo, a partir do objeto
	 * {@link Scanner}
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser lido
	 * @return <code>String</code> do conteúdo do arquivo
	 */
	public static String readFile2(File file) {
		StringBuilder out = new StringBuilder();
		Scanner input = getSc(file);

		if (input != null) {
			while (input.hasNext())
				out.append(input.nextLine() + "\n");
			input.close();
		} else
			return null;

		if (out.charAt(0) == 0xFEFF)
			return out.substring(1);
		else
			return out.toString();
	}

	/**
	 * Função que lê o conteúdo de um dado arquivo, a partir do objeto
	 * {@link BufferedReader}
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser lido
	 * @return <code>String</code> do conteúdo do arquivo
	 */
	public static String readFile3(File file) {
		return readFile3(file, null);
	}

	/**
	 * 
	 * @param file
	 * @param charsetName
	 *                    <ul>
	 *                    <li>Cp858</i>
	 *                    <li>Cp437</i>
	 *                    <li>Cp775</i>
	 *                    <li>Cp850</i>
	 *                    <li>Cp852</i>
	 *                    <li>Cp855</i>
	 *                    <li>Cp857</i>
	 *                    <li>Cp862</i>
	 *                    <li>Cp866</i>
	 *                    <li>ISO8859_1</i>
	 *                    <li>ISO8859_2</i>
	 *                    <li>ISO8859_4</i>
	 *                    <li>ISO8859_5</i>
	 *                    <li>ISO8859_7</i>
	 *                    <li>ISO8859_9</i>
	 *                    <li>ISO8859_13</i>
	 *                    <li>ISO8859_15</i>
	 *                    <li>KOI8_R</i>
	 *                    <li>KOI8_U</i>
	 *                    <li>ASCII</i>
	 *                    <li>UTF8</i>
	 *                    <li>UTF-16</i>
	 *                    <li>UnicodeBigUnmarked</i>
	 *                    <li>UnicodeLittleUnmarked</i>
	 *                    <li>UTF_32</i>
	 *                    <li>UTF_32BE</i>
	 *                    <li>UTF_32LE</i>
	 *                    <li>UTF_32BE_BOM</i>
	 *                    <li>UTF_32LE_BOM</i>
	 *                    <li>Cp1250</i>
	 *                    <li>Cp1251</i>
	 *                    <li>Cp1252</i>
	 *                    <li>Cp1253</i>
	 *                    <li>Cp1254</i>
	 *                    <li>Cp1257</i>
	 *                    <li>UnicodeBig</i>
	 *                    <li>Cp737</i>
	 *                    <li>Cp874</i>
	 *                    </ul>
	 * @return
	 */
	public static String readFile3(File file, String charsetName) {
		try {
			StringBuilder out = new StringBuilder();
			BufferedReader reader = null;
			if (charsetName != null)
				reader = getBr(file, charsetName);
			else
				reader = getBr(file);

			if (reader != null) {
				readAllLines(out, reader);
				reader.close();
			}

			return out.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Função que lê o conteúdo de um dado arquivo na pasta <code>resources</code>
	 * 
	 * @param path caminho do arquivo
	 * @return sequência de caracteres com o conteúdo do arquivo
	 */
	public static String readFileFromResources(String path) {
		return readFileFromUrl(ClassLoader.getSystemResource(path));
	}

	public static String readFileFromUrl(URL url) {
		StringBuilder out = new StringBuilder();
		readFileFromUrl(out, url);
		return out.toString();
	}

	private static void readFileFromUrl(StringBuilder out, URL url) {
		try {
			InputStream stream = url.openStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			readAllLines(out, reader);
			reader.close();
			stream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void readAllLines(StringBuilder out, BufferedReader reader) {
		try {
			String str = null;
			while ((str = reader.readLine()) != null) {
				out.append(str);
				out.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que cria um arquivo com um dado conteúdo através do objeto
	 * {@link FileWriter}
	 * 
	 * @param fileName objeto <code>File</code> com o nome e caminho do arquivo a
	 *                 ser criado
	 * @param content  conteúdo do arquivo
	 */
	public static void writeFile(File fileName, String content) {
		FileWriter fos = null;
		try {
			fos = new FileWriter(fileName);
			fos.write(content.replace("\n", System.getProperty("line.separator")));
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que cria um arquivo com um dado conteúdo através do objeto
	 * {@link FileWriter}
	 * 
	 * @param fileName caminho do arquivo a ser criado
	 * @param content  conteúdo do arquivo
	 */
	public static void writeFile(String fileName, String content) {
		writeFile(new File(fileName), content);
	}

	/**
	 * Função que cria um arquivo com um dado conteúdo através do objeto
	 * {@link RandomAccessFile}
	 * 
	 * @param file    objeto <code>File</code> com o nome e caminho do arquivo a ser
	 *                criado
	 * @param content conteúdo do arquivo
	 */
	public static void writeFile2(File file, String content) {
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.writeBytes(content.replace("\r\n", System.getProperty("line.separator")));
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que carrega um vetor de objetos de um arquivo
	 * 
	 * @param url  endereço do contexto da aplicação
	 * @param file nome do arquivo
	 * @return vetor de objetos carregados
	 */
	public static Object[] loadObjects(URL url, String file) {
		Object[] objects = null;
		try {
			objects = loadObjects(new File(new URL(url, file).toURI()));
		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * Função que carrega um vetor de objetos de um arquivo
	 * 
	 * @param file objeto {@link File} com o nome e caminho do arquivo a ser
	 *             carregado
	 * @return vetor de objetos carregados
	 */
	public static Object[] loadObjects(File file) {
		Object[] objects = null;
		try {
			objects = loadObjects(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * Função que carrega um vetor de objetos de um arquivo
	 * 
	 * @param inputStream input stream of bytes
	 * @return vetor de objetos carregados
	 */
	public static Object[] loadObjects(InputStream inputStream) {
		Object[] objects = null;
		try {
			ObjectInputStream ois = new ObjectInputStream(inputStream);
			objects = (Object[]) ois.readObject();
			ois.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return objects;
	}

	/**
	 * Função que cria um arquivo com um vetor de objetos
	 * 
	 * @param file    objeto <code>File</code> com o nome e caminho do arquivo a ser
	 *                criado
	 * @param objects objetos a serem inseridos no arquivo
	 */
	public static void saveObjects(File file, Object[] objects) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(objects);
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// =============== Funções relacionadas à atividade da Web ===============

	/**
	 * Função que retorna o código-fonte de uma página referencia pelo seu endereço
	 * URL
	 * 
	 * @param url endereço URL da página
	 * @return código-fonte da página
	 */
	public static String getSourcePage(String url) {
		return getSourcePage(url, true);
	}

	/**
	 * Função que retorna o código-fonte de uma página referencia pelo seu endereço
	 * URL
	 * 
	 * @param url                 endereço URL da página
	 * @param configureConnection se <code>true</code> configura-se a conexão
	 *                            incluindo-se um conjunto de headers HTTP mais
	 *                            próximo de um navegador, para driblar bloqueios de
	 *                            servidores
	 * @return código-fonte da página
	 */
	public static String getSourcePage(String url, boolean configureConnection) {
		return getSourcePage(url, configureConnection, null);
	}

	/**
	 * Função que retorna o código-fonte de uma página referencia pelo seu endereço
	 * URL
	 * 
	 * @param url                 endereço URL da página
	 * @param configureConnection se <code>true</code> configura-se a conexão
	 *                            incluindo-se um conjunto de headers HTTP mais
	 *                            próximo de um navegador, para driblar bloqueios de
	 *                            servidores
	 * @param charsetName
	 *                            <ul>
	 *                            <li>Cp858</i>
	 *                            <li>Cp437</i>
	 *                            <li>Cp775</i>
	 *                            <li>Cp850</i>
	 *                            <li>Cp852</i>
	 *                            <li>Cp855</i>
	 *                            <li>Cp857</i>
	 *                            <li>Cp862</i>
	 *                            <li>Cp866</i>
	 *                            <li>ISO8859_1</i>
	 *                            <li>ISO8859_2</i>
	 *                            <li>ISO8859_4</i>
	 *                            <li>ISO8859_5</i>
	 *                            <li>ISO8859_7</i>
	 *                            <li>ISO8859_9</i>
	 *                            <li>ISO8859_13</i>
	 *                            <li>ISO8859_15</i>
	 *                            <li>KOI8_R</i>
	 *                            <li>KOI8_U</i>
	 *                            <li>ASCII</i>
	 *                            <li>UTF8</i>
	 *                            <li>UTF-16</i>
	 *                            <li>UnicodeBigUnmarked</i>
	 *                            <li>UnicodeLittleUnmarked</i>
	 *                            <li>UTF_32</i>
	 *                            <li>UTF_32BE</i>
	 *                            <li>UTF_32LE</i>
	 *                            <li>UTF_32BE_BOM</i>
	 *                            <li>UTF_32LE_BOM</i>
	 *                            <li>Cp1250</i>
	 *                            <li>Cp1251</i>
	 *                            <li>Cp1252</i>
	 *                            <li>Cp1253</i>
	 *                            <li>Cp1254</i>
	 *                            <li>Cp1257</i>
	 *                            <li>UnicodeBig</i>
	 *                            <li>Cp737</i>
	 *                            <li>Cp874</i>
	 *                            </ul>
	 * @return código-fonte da página
	 */
	public static String getSourcePage(String url, boolean configureConnection, String charsetName,
			String[]... params) {
		StringBuilder out = new StringBuilder();
		try {
			URL site = new URI(url).toURL();
			URLConnection siteConnection = site.openConnection();

			if (configureConnection)
				configureHttpConnection(siteConnection);

			if (params.length > 0) {
				siteConnection.setDoOutput(true);
				PrintWriter wr = new PrintWriter(siteConnection.getOutputStream(), true);

				String parameters = params[0][0] + "=" + params[0][1];
				for (int i = 1; i < params.length; i++)
					parameters += "&" + params[i][0] + "=" + params[i][1];

				wr.println(parameters);
				wr.close();
			}

			BufferedReader reader = null;
			if (charsetName != null)
				reader = new BufferedReader(new InputStreamReader(siteConnection.getInputStream(), charsetName));
			else
				reader = new BufferedReader(new InputStreamReader(siteConnection.getInputStream()));

			readAllLines(out, reader);

			reader.close();
		} catch (IOException | URISyntaxException e) {
			System.err.println("Endereço inexistente: " + url);
			return null;
		}

		return out.toString();
	}

	private static void configureHttpConnection(URLConnection connection) {
		connection.setConnectTimeout(15000);
		connection.setReadTimeout(30000);

		connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
				+ "AppleWebKit/537.36 (KHTML, like Gecko) " + "Chrome/124.0.0.0 Safari/537.36");

		connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

		connection.setRequestProperty("Accept-Language", "pt-BR,pt;q=0.9,en;q=0.8");
		connection.setRequestProperty("Connection", "close");
	}

	/**
	 * Função que retorna o título de uma página da WEB
	 * 
	 * @param url endereço da página
	 * @return sequência de caracteres que designa o título da página
	 */
	public static String getHTMLtitle(String url) {
		String source = getSourcePage(url);

		if (source != null) {
			Matcher matcher = HTML.getGroupPattern("title").matcher(source);
			if (matcher.find()) {
				String out = matcher.group();
				return out.substring(7, out.length() - 8).trim().replace("\n", "");
			}
		}
		return null;
	}

	/**
	 * Função que faz o download de todos os links de uma página, criando os
	 * arquivos numa dada pasta do disco
	 * 
	 * @param url    página contendo os links
	 * @param folder pasta destino dos arquivos baixados
	 */
	public static void loadDirectory(String url, String folder) {
		BufferedReader reader = getBr(url);

		String str = null;

		try {
			while ((str = reader.readLine()) != null) {
				Matcher matcher1 = HTML.PATTERN_LINK.matcher(str);
				while (matcher1.find()) {
					str = matcher1.group();

					Matcher matcher2 = HTML.PATTERN_CONTENT.matcher(str);

					if (matcher2.find()) {
						str = matcher2.group();
						str = str.substring(1, str.length() - 1);

						boolean dir = str.endsWith("/");
						boolean par = dir && str.startsWith("/");

						if (dir && !par) { // recursão
							File ff = new File(folder);
							if (!ff.isDirectory())
								ff.mkdir();
							loadDirectory(url + str, folder + "/" + str);
						}
						if (!dir && str.contains(".")) { // arquivo
							download(url + "/" + str, folder, str);
						}
					}
				}
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// download DataInputStream & writeByte

	/**
	 * Função que salva um determinado arquivo de registro de dados localizado na
	 * rede numa dada pasta local
	 * 
	 * @param url        endereço do arquivo na Web
	 * @param folderName pasta de destino do arquivo
	 * @param filename   nome a ser dado ao arquivo
	 * @return arquivo que foi carregado da internet
	 */
	public static File download(String url, String folderName, String filename) {
		DataInputStream dis = IOutils.getDis(url);
		if (dis == null)
			return null;
		File folder = new File(folderName), file = null;
		if (!folder.isDirectory())
			folder.mkdir();
		try {
			file = new File(folderName + "/" + filename);
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			byte b;
			while (true) {
				try {
					b = dis.readByte();
				} catch (EOFException e) {
					break;
				}
				raf.writeByte(b);
			}
			dis.close();
			raf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * Função que salva um determinado arquivo de registro de dados localizado na
	 * rede
	 * 
	 * @param url  endereço do arquivo na Web
	 * @param file arquivo a ser salvo
	 * @return <code>true</code> se deu certo o download, <code>false</code> se não
	 */
	public static boolean download(String url, File file) {
		DataInputStream dis = IOutils.getDis(url);
		if (dis != null) {
			try {
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				byte s;
				while (true) {
					try {
						s = dis.readByte();
					} catch (EOFException e) {
						break;
					}
					raf.writeByte(s);
				}
				dis.close();
				raf.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	// download BufferedReader & writeBytes

	/**
	 * Função que salva um determinado arquivo de registro de dados localizado na
	 * rede numa dada pasta local
	 * 
	 * @param url        endereço do arquivo na Web
	 * @param folderName pasta de destino do arquivo
	 * @param filename   nome a ser dado ao arquivo
	 * @return arquivo que foi carregado da internet
	 */
	public static File download2(String url, String folderName, String filename) {
		BufferedReader reader = IOutils.getBr(url);
		File file = null;
		if (reader != null) {
			File folder = new File(folderName);
			if (!folder.isDirectory())
				folder.mkdir();
			try {
				file = new File(folderName + "/" + filename);
				RandomAccessFile raf = new RandomAccessFile(file, "rw");
				String str;
				while ((str = reader.readLine()) != null)
					raf.writeBytes(str + "\n");
				reader.close();
				raf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * Função que salva um determinado arquivo de registro de dados localizado na
	 * rede
	 * 
	 * @param url  endereço do arquivo na Web
	 * @param file arquivo a ser salvo
	 * @return <code>true</code> se deu certo o download, <code>false</code> se não
	 */
	public static boolean download2(String url, File file) {
		BufferedReader reader = IOutils.getBr(url);
		String str;
		try {
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			while ((str = reader.readLine()) != null)
				raf.writeBytes(str + "\n");
			reader.close();
			raf.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	// ================================== URL ==================================

	private static final Pattern URL_PAT = Pattern.compile("URL=.+");

	public static URL getURL(File file) {
		try {
			return new URI(getURLstring(file)).toURL();
		} catch (MalformedURLException | URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getURLstring(File file) {
		Matcher m = URL_PAT.matcher(IOutils.readFile(file));
		m.find();
		return m.group().substring(4);
	}

	// ======================== Files ========================

	/**
	 * Função que retorna os caracteres que indicam a extensão do arquivo
	 * 
	 * @param file objeto {@link File} associado ao arquivo
	 * @return caracteres da extensão do arquivo, em letras minúsculas
	 */
	public static String getExtension(File file) {
		String out = null;
		String filename = file.getName();
		int dotPosition = filename.lastIndexOf('.');

		if (dotPosition > 0 && dotPosition < filename.length() - 1)
			out = filename.substring(dotPosition + 1).toLowerCase();

		return out;
	}

	/**
	 * Função que retorna o nome do arquivo separado de sua extensão
	 * 
	 * @param file objeto {@link File} associado ao arquivo
	 * @return vetor de sequências de caracteres com duas posições, a primeira com o
	 *         nome do arquivo, a segundo com sua extensão, em letras minúsculas
	 */
	public static String[] getNameExtension(File file) {
		String[] out = new String[2];
		String filename = file.getName();
		int dotPosition = filename.lastIndexOf('.');

		if (dotPosition > 0 && dotPosition < filename.length() - 1) {
			out[0] = filename.substring(0, dotPosition);
			out[1] = filename.substring(dotPosition + 1).toLowerCase();
		}

		return out;
	}

	/**
	 * Função que retorna os arquivo com uma dada terminação contidos num dado
	 * diretório
	 * 
	 * @param folder diretório
	 * @param term   terminação do arquivo
	 * @return lista de arquivo com a terminação dada contidos no diretório dado
	 */
	public static File[] filterFiles(File folder, String term) {
		if (term == null)
			return null;
		File[] fs = folder.listFiles();
		List<File> out = new LinkedList<>();
		for (int i = 0; i < fs.length; i++) {
			File file = fs[i];
			if (term.equalsIgnoreCase(IOutils.getExtension(file)))
				out.add(file);
		}
		return out.toArray(new File[out.size()]);
	}

	/**
	 * Função que cria um novo objeto {@link File} a partir de um existente, mas com
	 * o nome do arquivo concatenado com uma sequência de caracteres dada
	 * 
	 * @param file   objeto que representa o arquivo
	 * @param append sequência de caracteres a ser concatenada ao nome do arquivo
	 * @return objeto {@link File} com o nome concatenado
	 */
	public static File appendFilename(File file, String append) {
		String[] filenameExtension = getNameExtension(file);
		return new File(file.getParent() + File.separator + filenameExtension[0] + append + "." + filenameExtension[1]);
	}

	/**
	 * Função que cria um novo objeto {@link File} a partir de um existente, mas a
	 * extensão do arquivo trocada
	 * 
	 * @param file   objeto que representa o arquivo
	 * @param newExt nova extensão, sem o ponto
	 * @return objeto {@link File} com a nova extensão
	 */
	public static File replaceExtension(File file, String newExt) {
		String absolutePath = file.getAbsolutePath();
		int dotPosition = absolutePath.lastIndexOf('.');
		if (dotPosition > 0)
			return new File(absolutePath.substring(0, dotPosition + 1) + newExt);
		else
			return new File(absolutePath + "." + newExt);
	}

	/**
	 * Função que retorna o caminho completo de um arquivo sem a designação de sua
	 * extensão
	 * 
	 * @param file objeto que representa o arquivo
	 * @return sequência de caracteres do caminho completo do arquivo, sem a
	 *         extensão
	 */
	public static String removeExtension(File file) {
		String absolutePath = file.getAbsolutePath();
		int dotPosition = absolutePath.lastIndexOf('.');
		if (dotPosition > 0)
			return absolutePath.substring(0, dotPosition);
		else
			return absolutePath;
	}

	/**
	 * Função que retorna o nome de um arquivo sem a designação de sua extensão
	 * 
	 * @param file objeto que representa o arquivo
	 * @return sequência de caracteres do nome do arquivo, sem a extensão
	 */
	public static String removeNameExtension(File file) {
		String filename = file.getName();
		int dotPosition = filename.lastIndexOf('.');
		if (dotPosition > 0)
			return filename.substring(0, dotPosition);
		else
			return filename;
	}

	private static final char[] FORBIDDEN_CHAR = { '\\', '/', ':', '*', '?', '"', '<', '>', '|' };

	public static final String FORBIDDEN_CHAR_PATTERN;

	static {
		StringBuilder sb = new StringBuilder("[^");
		for (int i = 0; i < FORBIDDEN_CHAR.length; i++) {
			char c = FORBIDDEN_CHAR[i];
			switch (c) {
			case '*':
			case '?':
				sb.append("\\");
				break;
			}
			sb.append(c);
		}
		sb.append("]");
		FORBIDDEN_CHAR_PATTERN = sb.toString();
	}

	/**
	 * Função que remove de uma sequência de caracteres os caracteres proibidos para
	 * nomes de arquivos
	 * 
	 * @param filename nome do arquivo
	 * @return nome do arquivo com os caracteres proibidos substituídos por espaços
	 *         brancos
	 */
	public static String removeForbiddenCharacters(String filename) {
		for (int i = 0; i < FORBIDDEN_CHAR.length; i++)
			filename = filename.replace(FORBIDDEN_CHAR[i], ' ');
		return filename;
	}

	/**
	 * Função que procura um dado diretório nas diferentes unidades do sistema e
	 * retorna a unidade em que o diretório foi encontrado
	 * 
	 * @param folder caminho do diretório (sem a designação da unidade de disco)
	 * @return leta que designa a unidade de disco
	 */
	public static char searchFolderDir(String folder) {
		File[] units = File.listRoots();
		char l = 'C';
		for (int i = 0; i < units.length; i++) {
			File f = new File(units[i].getAbsolutePath() + folder);
			if (f.exists() ? f.isDirectory() : false) {
				l = f.getAbsolutePath().charAt(0);
				break;
			}
		}
		return l;
	}

	// =============================== Images files ===============================

	/**
	 * Função que realiza a conversão de um arquivo para um outro formato
	 * 
	 * @param file    arquivo a ser convertido
	 * @param format  formato do novo arquivo de image
	 * @param newFile novo arquivo criado, no formato designado
	 */
	public static void convertImage(File file, String format, File newFile) {
		try {
			ImageIO.write(ImageIO.read(file), format, newFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ================================ Zip files ================================

	public static boolean decompressGzipFile(String url, File file) {
		try {
			return decompressGzipFile(getIs(url), new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			System.err.println("O arquivo " + url + " não foi encontrado.");
		}
		return false;
	}

	public static boolean decompressGzipFile(InputStream is, FileOutputStream fos) {
		if (is != null) {
			try {
				GZIPInputStream gis = new GZIPInputStream(is);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = gis.read(buffer)) != -1)
					fos.write(buffer, 0, len);
				// close resources
				fos.close();
				gis.close();
				is.close();
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static void compressGzipFile(InputStream is, FileOutputStream fos) {
		try {
			GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = is.read(buffer)) != -1)
				gzipOS.write(buffer, 0, len);
			// close resources
			gzipOS.close();
			fos.close();
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Função que cria um arquivo compactado contendo vários arquivos
	 * 
	 * @param zipFile arquivo compactado a ser criado
	 * @param files   arquivos que serão inseridos no arquivo compactado
	 */
	public static void compressGzipFile(File zipFile, File... files) {
		String[] paths = new String[files.length];
		Arrays.fill(paths, "");
		compressGzipFile(zipFile, paths, files);
	}

	/**
	 * Função que cria um arquivo compactado contendo vários arquivos
	 * 
	 * @param zipFile arquivo compactado a ser criado
	 * @param paths   caminho dos arquivos dentro do arquivo compactado
	 * @param files   arquivos que serão inseridos no arquivo compactado
	 */
	public static void compressGzipFile(File zipFile, String[] paths, File[] files) {
		if (paths.length != files.length)
			throw new IllegalArgumentException("o vetor de arquivos e de caminhos deve ter o mesmo tamanho");
		try {
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);

			byte[] buffer = new byte[1024];
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				ZipEntry ze = new ZipEntry(paths[i] + file.getName());
				zos.putNextEntry(ze);

				FileInputStream in = new FileInputStream(file.getAbsolutePath());

				int len;
				while ((len = in.read(buffer)) > 0)
					zos.write(buffer, 0, len);

				in.close();
			}

			zos.closeEntry();
			zos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static File[] unzip(File zipFile) {
		return unzip(zipFile, zipFile.getParentFile());
	}

	public static File[] unzip(File zipFile, File outputFolder) {
		byte[] buffer = new byte[1024];

		try {
			ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();

			List<File> out = new LinkedList<>();

			while (ze != null) {
				String fileName = ze.getName();
				File newFile = new File(outputFolder + File.separator + fileName);

				System.out.println("file unzip : " + newFile.getAbsoluteFile());

				// create all non exists folders
				// else you will hit FileNotFoundException for compressed folder
				new File(newFile.getParent()).mkdirs();

				FileOutputStream fos = new FileOutputStream(newFile);

				int len;
				while ((len = zis.read(buffer)) > 0)
					fos.write(buffer, 0, len);

				out.add(newFile);

				fos.close();
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close();

			return out.toArray(new File[out.size()]);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
}