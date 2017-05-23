package github.dwladdimiroc.analyzeTwitter.detect;

import github.dwladdimiroc.analyzeTwitter.utils.Comunas;
import github.dwladdimiroc.analyzeTwitter.utils.Palabras;

public class Comuna {
	private Comunas comunas;
	private Palabras palabras;

	public Comuna(String path) {
		this.comunas = new Comunas();
		this.comunas.leerCiudades(path);

		this.palabras = new Palabras();
	}

	public boolean analyze(String text) {
		return this.palabras.contiene(this.comunas.listadoNombres(), text);
	}
}
