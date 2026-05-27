package br.com.pereiraeng.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Enumeração dos tipos mais comuns dos arquivos
 * 
 * @author Philipe Pereira
 */
public enum FileType {
	DIRETORIO(),
	/**
	 * texto (alfanumérico)
	 */
	TEXTO_AFN(Extension.TXT),
	/**
	 * imagens (binária)
	 */
	IMAGENS(Extension.TIFF, Extension.TIF, Extension.GIF, Extension.JPEG, Extension.JPG, Extension.PNG),
	/**
	 * imagens (alfanumérico)
	 */
	IMAGENS_AFN(Extension.PBM, Extension.PGM, Extension.PPM),
	/**
	 * página web (alfanumérico, HTML)
	 */
	WEBPAGES(Extension.HTML, Extension.HTM),
	/**
	 * planilha (binário)
	 */
	PLANILHAS(Extension.XLS, Extension.XLSX/* open office */),
	/**
	 * texto (binário MS)
	 */
	TEXTOS_OFFICE(Extension.DOC, Extension.DOCX/* open office */),
	/**
	 * texto (binário Adobe)
	 */
	TEXTOS_PDF(Extension.PDF), OFFICE(Extension.DOC, Extension.DOCX, Extension.XLS, Extension.XLSX),
	CONTATOS(Extension.VCF, Extension.CSV, Extension.CONTACT), LINK(Extension.URL), TODOS();

	/**
	 * Enumeração das extensões mais comuns dos arquivos
	 * 
	 * @author Philipe Pereira
	 */
	public static enum Extension {
		TIFF, TIF, GIF, JPEG, JPG, PNG, PBM, PGM, PPM, TXT, HTML, HTM, XLS, XLSX, VCF, CSV, CONTACT, DOC, DOCX, PDF,
		URL;
	}

	public Extension[] extension;

	private FileType(Extension... extensoes) {
		this.extension = extensoes;
	}

	public static FileType getType(Extension extension) {
		FileType type = null;
		for (FileType t : FileType.values()) {
			for (Extension e : t.extension) {
				if (e.equals(extension)) {
					type = t;
					break;
				}
			}
			if (type != null)
				break;
		}
		return type;
	}

	public static FileType getType(String extension) {
		if (extension == null)
			return null;
		Extension e = null;
		try {
			e = Extension.valueOf(extension.toUpperCase());
		} catch (IllegalArgumentException exception) {
			System.out.println("Extensão desconhecida - " + extension);
		}

		if (e != null)
			return FileType.getType(e);
		else
			return null;
	}

	public static String[] extensionList(FileType... type) {
		ArrayList<String> list = new ArrayList<String>();
		for (FileType ft : type)
			for (Extension e : ft.extension)
				list.add(e.name());
		return list.toArray(new String[list.size()]);
	}

	
	public static FileType[] getFileTypesInDirectory(File directory) {
		HashSet<FileType> out = new HashSet<FileType>();
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++)
			if (files[i].isFile())
				out.add(getType(IOutils.getExtension(files[i])));
		return out.toArray(new FileType[out.size()]);
	}
}
