package br.com.pereiraeng.io.spi;

import java.util.ServiceLoader;

public final class IoExtensions {

	private IoExtensions() {
	}

	public static boolean pingExtensions(Object owner, Object... args) {
		ServiceLoader<IoExtensionProvider> loader = ServiceLoader.load(IoExtensionProvider.class);

		boolean out = false;
		for (IoExtensionProvider provider : loader) {
			if (provider.supports(owner, args)) {
				out = provider.ping(owner, args);
				break;
			}
		}
		return out;
	}
}