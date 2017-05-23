package github.dwladdimiroc.analyzeTwitter.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Palabras {
	private String limpiar(String texto) {
		String clearText = texto;

		clearText = clearText.replace("á", "a");
		clearText = clearText.replace("é", "e");
		clearText = clearText.replace("í", "i");
		clearText = clearText.replace("ó", "o");
		clearText = clearText.replace("ú", "u");

		clearText = clearText.replace("Á", "A");
		clearText = clearText.replace("É", "E");
		clearText = clearText.replace("Í", "I");
		clearText = texto.replace("Ó", "O");
		clearText = clearText.replace("Ú", "U");

		return clearText.toLowerCase();
	}

	public boolean contiene(List<String> palabras, String texto) {
		for (String palabra : palabras) {
			String patron = ".*\\b" + palabra.toLowerCase() + "\\b.*";
			if (limpiar(texto).matches(patron))
				return true;
		}
		return false;
	}

	public List<String> leerDiccionario(String ruta) {
		List<String> palabras = new ArrayList<String>();

		String linea = null;
		try {
			InputStream input = getClass().getResourceAsStream(ruta);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));

			while ((linea = bufferedReader.readLine()) != null) {
				palabras.add(linea);
			}

			bufferedReader.close();
			input.close();
		} catch (FileNotFoundException ex) {
			System.out.println("No se puede abrir el archivo '" + ruta + "'");
		} catch (IOException ex) {
			System.out.println("Error al leer el archivo '" + ruta + "'");
		}

		return palabras;
	}
}
