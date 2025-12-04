package br.com.pereiraeng.io;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

public class Comm {

	public static final int NO_ACCESS = 0;

	public static final int FULL_ACCESS = 0xFFFF;

	public static final char WEB = 'W';
	public static final char DIR = 'D';
	public static final char NET = 'N';
	public static final char SQL = 'B';
	public static final char XML = 'X';
	public static final char FIL = 'F';
	public static final char SSL = 'S';

	/**
	 * Tipos de fontes de dados
	 * 
	 * @author Philipe PEREIRA
	 *
	 */
	public enum DataSourceType {
		/**
		 * página web
		 */
		WEB,
		/**
		 * diretório
		 */
		DIR,
		/**
		 * arquivo
		 */
		FIL,
		/**
		 * servidor
		 */
		NET,
		/**
		 * base de dados SQL
		 */
		SQL,
		/**
		 * servidor XML
		 */
		XML,
		/**
		 * Secure Sockets Layer
		 */
		SSL;

		public static DataSourceType get(char type) {
			switch (type) {
			case Comm.XML:
				return XML;
			case Comm.NET:
				return NET;
			case Comm.SQL:
				return SQL;
			case Comm.FIL:
				return FIL;
			case Comm.DIR:
				return DIR;
			case Comm.WEB:
				return WEB;
			case Comm.SSL:
				return SSL;
			default:
				return null;
			}
		}
	}

	/**
	 * Função que inspeciona um conjunto de fonte de dados de modo a ser indicar o
	 * estado das conexões em função dos parâmetros de conexão repassados
	 * 
	 * @param srcName nome de fonte de dados
	 * @param paramss diferentes possibilidade de conexão, representadas por uma
	 *                tabela de dispersão que associa para cada potência de 2 os
	 *                parâmetros de conexão
	 * @param rede    inteiro-máscara para indicar quais fontes são arquivos ou
	 *                diretórios da rede local
	 * @param allowed inteiro-máscara para indicar quais fontes serão verificadas
	 * @return inteiro-máscara que indica o estado das conexões
	 */
	public static int getStatus(String srcName, Map<Integer, Object[]> paramss, final int rede, final int allowed) {
		int status = Comm.NO_ACCESS;
		for (Entry<Integer, Object[]> e : paramss.entrySet()) {
			int s = e.getKey();
			if ((s & allowed) > 0) {
				Object[] objs = e.getValue();
				char type = (char) objs[0];

				boolean ok = check(srcName, DataSourceType.get(type), (s & rede) > 0, objs);
				if (ok)
					status += s;
			}
		}
		return status;
	}

	/**
	 * Função que inspeciona uma dada fonte de dados de modo a ser indicar o estado
	 * das conexões em função dos parâmetros de conexão repassados
	 * 
	 * @param srcName nome de fonte de dados
	 * @param type    tipos de fontes de dados
	 * @param rede    <code>true</code> para indicar que está na rede,
	 *                <code>false</code> para indicar que está no ambiente local
	 * @param params  vetor de objetos com os parâmetros
	 * @return <code>true</code> para conexão ativa, <code>false</code> para inativa
	 */
	public static boolean check(String srcName, DataSourceType type, boolean rede, Object... params) {
		boolean ok = false;
		switch (type) {
		case WEB: // servidor
			System.out.printf("Tentando se conectar ao servidor do %s... ", srcName);
			ok = IOutils.pingHTML((String) params[1]);
			break;
		case SSL: // conexão SSL TODO solução auto-incremental
		case SQL: // base de dados SQL
//			System.out.printf(type == DataSourceType.SSL ? "Solicitando resposta SSL-%s... "
//					: "Checando conexão com a base de dados %s... ", srcName);
//			ok = SSLutils.ping((String) params[1]);
//
//			SQLadapter sql = (SQLadapter) params[1];
//			ok = sql != null
//					? (sql.getStatus() > 0 ? (params.length == 3 ? sql.exist((String) params[2]) : true) : false)
//					: false;
			break;
		case NET:
			System.out.printf("Tentando se conectar ao servidor do %s... ", srcName);
			ok = IOutils.ping((String) params[1]);
			break;
		case DIR: // diretório (local ou da rede)
			System.out.printf("Tentando acessar o diretório %s do %s... ", rede ? "da rede" : "local", srcName);
			File file = new File((String) params[1]);
			ok = file.isDirectory();
			break;
		case FIL: // arquivo (local ou da rede)
			System.out.printf("Tentando acessar o arquivo %s do %s... ", rede ? "da rede" : "local", srcName);
			file = new File((String) params[1]);
			ok = file.isFile();
			break;
		case XML: // arquivo XML
			System.out.printf("Solicitando resposta XML-%s... ", srcName);
//			ok = IOutils.pingXML((String) params[1], false);
			break;
		}

		if (ok)
			System.out.println("Conexão bem-sucedida!");
		else
			System.out.println("Não foi possível estabelecer uma conexão.");
		return ok;
	}
}
