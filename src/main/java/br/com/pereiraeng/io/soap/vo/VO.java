package br.com.pereiraeng.io.soap.vo;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class VO {

	private final String label;

	public VO(String label) {
		this.label = label;
	}

	private String value;

	private List<VO> objects;

	public void set(String object) {
		this.value = object;
	}

	public void add(VO vo) {
		if (objects == null)
			objects = new LinkedList<>();
		objects.add(vo);
	}

	@Override
	public String toString() {
		return label + "=" + (value != null ? value.toString() : (objects != null ? objects.toString() : "NULL"));
	}

	public String get() {
		return value;
	}

	public VO get(String label) {
		if (objects != null) {
			for (VO vo : objects)
				if (vo.label.equals(label))
					return vo;
		}
		return null;
	}

	public Iterator<VO> iterator() {
		return objects.iterator();
	}

	public int size() {
		return objects.size();
	}
}
