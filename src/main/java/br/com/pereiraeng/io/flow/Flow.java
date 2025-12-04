package br.com.pereiraeng.io.flow;

/**
 * Interface das classes dos objetos que recebem uma fluxo de dados através da
 * função {@link #incomingData(Object)}
 * 
 * @author Philipe PEREIRA
 *
 * @param <K>
 *            classe dos objetos que serão recebidos
 */
public interface Flow<K> {

	/**
	 * Função que processa um dado que acaba de ser recebido
	 * 
	 * @param data
	 *            dado recebido
	 */
	public void incomingData(K data);
}
