package github.dwladdimiroc.analyzeTwitter.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.opencsv.CSVWriter;

import github.dwladdimiroc.analyzeTwitter.detect.Comuna;
import github.dwladdimiroc.analyzeTwitter.detect.Geolocalizacion;
import github.dwladdimiroc.analyzeTwitter.detect.Localizacion;
import github.dwladdimiroc.analyzeTwitter.eda.Tweet;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterObjectFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Twitter {

	private BlockingQueue<Tweet> messageQueue = new LinkedBlockingQueue<Tweet>();
	private Thread tStream;

	private CSVWriter streamTwitterCSV;

	public Twitter() {
		try {
			streamTwitterCSV = new CSVWriter(new FileWriter("output/streamTwitter.csv"), ';');
			streamTwitterCSV.writeNext(new String[] { "Text" });
			streamTwitterCSV.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		tStream = new Thread(new AnalyzeStream(this.messageQueue));
	}

	public void execute() {
		try {
			tStream.start();
			connectAndRead();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public class AnalyzeStream implements Runnable {
		private Comuna comuna;
		private Geolocalizacion geolocalizacion;
		private Localizacion localizacion;

		private BlockingQueue<Tweet> messageQueue;

		public AnalyzeStream() {
			this.comuna = new Comuna("/comunas.json");
			this.geolocalizacion = new Geolocalizacion(new Double[] { -57.050292, -61.567383 },
					new Double[] { -57.050292, -61.567383 });
			this.localizacion = new Localizacion("Chile");

			this.messageQueue = new LinkedBlockingQueue<Tweet>();
		}

		public AnalyzeStream(BlockingQueue<Tweet> messageQueue) {
			this.comuna = new Comuna("/comunas.json");
			this.geolocalizacion = new Geolocalizacion(new Double[] { -57.050292, -61.567383 },
					new Double[] { -57.050292, -61.567383 });
			this.localizacion = new Localizacion("Chile");

			this.messageQueue = messageQueue;
		}

		@Override
		public void run() {
			while (true) {
				Tweet tweet = this.messageQueue.poll();
				if (tweet != null) {
					if (detect(tweet)) {
						// System.out.println("[Tweet] Text: " +
						// tweet.getTexto() + " | Localización: "
						// + tweet.getLocalizacion() + " | Geolocalización: " +
						// tweet.getGeolocalizacion());
						writeTweet(tweet.getTexto());
					}
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}

		private boolean detect(Tweet tweet) {
			// if (this.comuna.analyze(tweet.getTexto())) {
			// return true;
			// }

			if (tweet.getGeolocalizacion() != null) {
				if (this.geolocalizacion.analyze(tweet.getGeolocalizacion())) {
					return true;
				}
			}

			if (tweet.getLocalizacion() != null) {
				if (this.localizacion.ubicar(tweet.getLocalizacion())) {
					return true;
				}
			}

			return false;
		}

		private void writeTweet(String text) {
			streamTwitterCSV.writeNext(new String[] { text });

			try {
				streamTwitterCSV.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void connectAndRead() throws Exception {
		ConfigurationBuilder cb = new ConfigurationBuilder();
		Properties twitterProperties = new Properties();
		File twitter4jPropsFile = new File("config/twitter4j.properties");
		if (!twitter4jPropsFile.exists()) {
			System.out.println("Cannot find twitter4j.properties file in this location :["
					+ twitter4jPropsFile.getAbsolutePath() + "]");
			return;
		}

		twitterProperties.load(new FileInputStream(twitter4jPropsFile));

		cb = new ConfigurationBuilder();

		cb.setOAuthConsumerKey(twitterProperties.getProperty("oauth.consumerKey"));
		cb.setOAuthConsumerSecret(twitterProperties.getProperty("oauth.consumerSecret"));
		cb.setOAuthAccessToken(twitterProperties.getProperty("oauth.accessToken"));
		cb.setOAuthAccessTokenSecret(twitterProperties.getProperty("oauth.accessTokenSecret"));

		cb.setJSONStoreEnabled(true);
		cb.setIncludeMyRetweetEnabled(false);

		TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		StatusListener statusListener = new StatusListener() {

			@Override
			public void onException(Exception ex) {
			}

			@Override
			public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
			}

			@Override
			public void onStatus(Status status) {
				String rawJSON = TwitterObjectFactory.getRawJSON(status);

				Tweet tweet = new Tweet();

				tweet.setTexto(status.getText());
				tweet.setId(status.getId());
				tweet.setTimestamp(status.getCreatedAt());
				tweet.setRawTweet(rawJSON);

				if (status.getGeoLocation() != null) {
					tweet.setGeolocalizacion(new double[] { status.getGeoLocation().getLatitude(),
							status.getGeoLocation().getLongitude() });
				}
				if (status.getPlace() != null) {
					tweet.setLocalizacion(status.getPlace().getCountry());
				}

				messageQueue.offer(tweet);
			}

			@Override
			public void onScrubGeo(long userId, long upToStatusId) {
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
			}

			@Override
			public void onStallWarning(StallWarning arg0) {
			}

		};

		twitterStream.addListener(statusListener);
		twitterStream.sample();
	}
}
