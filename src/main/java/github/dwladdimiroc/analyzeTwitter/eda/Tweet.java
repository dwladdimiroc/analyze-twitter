package github.dwladdimiroc.analyzeTwitter.eda;

import java.util.Date;

public class Tweet {
	private long id;
	private Date timestamp;
	private String texto;

	private double[] geolocalizacion;
	private String localizacion;

	private String rawTweet;

	public Tweet() {
		this.id = 0;
		this.timestamp = null;
		this.texto = null;
		this.geolocalizacion = null;
		this.localizacion = null;
		this.rawTweet = null;
	}

	public Tweet(long id, Date timestamp, String texto, double[] geolocalizacion, String localizacion,
			String rawTweet) {
		this.id = id;
		this.timestamp = timestamp;
		this.texto = texto;
		this.geolocalizacion = geolocalizacion;
		this.localizacion = localizacion;
		this.rawTweet = rawTweet;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public double[] getGeolocalizacion() {
		return geolocalizacion;
	}

	public void setGeolocalizacion(double[] geolocalizacion) {
		this.geolocalizacion = geolocalizacion;
	}

	public String getLocalizacion() {
		return localizacion;
	}

	public void setLocalizacion(String localizacion) {
		this.localizacion = localizacion;
	}

	public String getRawTweet() {
		return rawTweet;
	}

	public void setRawTweet(String rawTweet) {
		this.rawTweet = rawTweet;
	}

}
