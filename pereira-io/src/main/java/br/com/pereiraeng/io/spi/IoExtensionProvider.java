package br.com.pereiraeng.io.spi;

public interface IoExtensionProvider {

	boolean supports(Object owner, Object... args);

	boolean ping(Object owner, Object... args);

}
