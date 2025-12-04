package br.com.pereiraeng.io.flow;

public class Accumulator implements Flow<String> {

	private StringBuilder out = new StringBuilder();

	@Override
	public void incomingData(String data) {
		this.out.append(data + "\n");
	}

	public String get() {
		return out.toString();
	}
}
