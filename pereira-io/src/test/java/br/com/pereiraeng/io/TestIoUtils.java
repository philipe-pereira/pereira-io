package br.com.pereiraeng.io;

import java.io.File;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestIoUtils {

	@Test
	void testFileExtension() {
		ClassLoader classLoader = getClass().getClassLoader();
		File fileTxt = new File(classLoader.getResource("empty.txt").getFile());

		File fileWithoutExtension = new File(classLoader.getResource("withoutExtension").getFile());

		assertEquals("txt", IOutils.getExtension(fileTxt));
		assertNull(IOutils.getExtension(fileWithoutExtension));
		
		assertEquals("empty", IOutils.removeNameExtension(fileTxt));
		assertEquals("withoutExtension", IOutils.removeNameExtension(fileWithoutExtension));
	}
}
