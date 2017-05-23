package github.dwladdimiroc.analyzeTwitter.detect;

public class Localizacion {

	private String lugar;

	public Localizacion(String lugar) {
		this.lugar = lugar;
	}

	public boolean ubicar(String text) {
		if (text != null) {
			if (text.equals(this.lugar)) {
				return true;
			}
		}
		return false;
	}

}
