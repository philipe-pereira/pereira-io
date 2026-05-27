
package br.com.pereiraeng.io;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientDownloader {

	public static String getSourcePage(String urlAlvo) throws IOException, InterruptedException {
		// 1. Criar o cliente HTTP
		HttpClient cliente = HttpClient.newHttpClient();

		// 2. Criar a requisição (Request)
		HttpRequest requisicao = HttpRequest.newBuilder().uri(URI.create(urlAlvo)).GET() // Método HTTP GET
				.header("User-Agent", "Mozilla/5.0") // Evita que alguns sites bloqueiem a requisição
				.build();

		// 3. Enviar a requisição e receber a resposta (Response)
		HttpResponse<String> resposta = cliente.send(requisicao, HttpResponse.BodyHandlers.ofString());

		// 4. Retornar o corpo da resposta (o HTML/conteúdo da página)
		return resposta.body();
	}
}
