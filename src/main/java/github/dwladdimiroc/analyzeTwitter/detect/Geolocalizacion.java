package github.dwladdimiroc.analyzeTwitter.detect;

public class Geolocalizacion {

	private Double[][] rango;
	private boolean toroidalX;
	private boolean toroidalY;

	/**
	 * Constructor del Bolt Geolocalización
	 * 
	 * @param posicionSupIzq
	 *            Su primera posición del arreglo es la Latitud y la segunda la
	 *            Longitud
	 * @param posicionInfDer
	 *            Ídem
	 */
	public Geolocalizacion(Double[] posicionSupIzq, Double[] posicionInfDer) {
		double x1 = posicionSupIzq[1] + Double.valueOf(180);
		double y1 = posicionSupIzq[0] + Double.valueOf(90);

		double x2 = posicionInfDer[1] + Double.valueOf(180);
		double y2 = posicionInfDer[0] + Double.valueOf(90);

		this.rango = new Double[][] { { x1, y1 }, { x2, y2 } };

		if (this.rango[1][0] < this.rango[0][0]) {
			this.toroidalX = true;
		} else {
			this.toroidalX = false;
		}

		if (this.rango[0][1] < this.rango[1][1]) {
			this.toroidalY = true;
		} else {
			this.toroidalY = false;
		}

		// // Sup. Izq
		// logger.info(this.rango[0][0].toString()); // Lat1 (X1)
		// logger.info(this.rango[0][1].toString()); // Long1 (Y1)
		//
		// // Inf. Derecha
		// logger.info(this.rango[1][0].toString()); // Lat2 (X2)
		// logger.info(this.rango[1][1].toString()); // Long2 (Y2)
	}

	public boolean analyze(double[] posicion) {
		double x = posicion[1] + Double.valueOf(180);
		double y = posicion[0] + Double.valueOf(90);

		// logger.info("Rango: [{},{}] | Latitud: {}", this.rango[0][0],
		// this.rango[1][0], x);
		// logger.info("Rango: [{},{}] | Longitud: {}", this.rango[1][1],
		// this.rango[0][1], y);

		if (this.toroidalX) {
			if ((((this.rango[0][0] <= x) && (x <= 360)) || ((0 <= x) && (x <= this.rango[1][0])))
					&& ((this.rango[1][1] <= y) && (y <= this.rango[0][1]))) {
				return true;
			}
		} else if (this.toroidalY) {
			if (((this.rango[0][0] <= x) && (x <= this.rango[1][0]))
					&& (((this.rango[1][1] <= y) && (y <= 180)) || ((0 <= y) && (y <= this.rango[0][1])))) {
				return true;
			}
		} else if (this.toroidalX && this.toroidalY) {
			if ((((this.rango[0][0] <= x) && (x <= 360)) || ((0 <= x) && (x <= this.rango[1][0])))
					&& (((this.rango[1][1] <= y) && (y <= 180)) || ((0 <= y) && (y <= this.rango[0][1])))) {
				return true;
			}
		} else {
			if (((this.rango[0][0] <= x) && (x <= this.rango[1][0]))
					&& ((this.rango[1][1] <= y) && (y <= this.rango[0][1]))) {
				return true;
			}
		}

		return false;
	}

}
