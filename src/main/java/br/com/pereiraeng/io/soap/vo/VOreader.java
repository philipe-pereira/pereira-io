package br.com.pereiraeng.io.soap.vo;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import br.com.pereiraeng.xml.XMLadapter;

public class VOreader extends XMLadapter {

	private static final String MULTI_REF = "multiRef";

	private Map<Integer, VO> index;

	private String qName;

	public VO getVo() {
		return index.get(this.mainId);
	}

	public void setqName(String qName) {
		this.qName = qName;
	}

	// ============================= XML READER =============================

	private transient int mainId = -1;

	private Stack<VO> voStack;

	@Override
	public void startDocument() throws SAXException {
		if (this.qName != null) {
			index = new HashMap<>();
			voStack = new Stack<>();
		}
	}

	@Override
	public void startElement(String qName, Attributes atts) {
		if (this.qName != null) {
			if (this.qName.equals(qName))
				this.mainId = Integer.parseInt(atts.getValue("href").substring(3));

			if (mainId >= 0) {
				VO vo = null;

				if (MULTI_REF.equals(qName)) { // este objeto tem um número de idenficação
					String ids = atts.getValue("id");
					vo = this.index.get(Integer.parseInt(ids.substring(2)));
				} else {
					String ids = atts.getValue("href");
					vo = new VO(qName);
					if (ids != null) // este objeto faz referência a um outro objeto
						this.index.put(Integer.parseInt(ids.substring(3)), vo);
				}

				if (voStack.size() > 0)
					voStack.peek().add(vo);
				voStack.push(vo);
			}
		}
	}

	@Override
	public void characters(String s) {
		if (this.qName != null) {
			if (voStack.size() > 0)
				voStack.peek().set(s);
		}
	}

	@Override
	public void endElement(String qName) {
		if (this.qName != null) {
			if (this.voStack.size() > 0)
				voStack.pop();
		}
	}

	@Override
	public void endDocument() throws SAXException {
	}
}
