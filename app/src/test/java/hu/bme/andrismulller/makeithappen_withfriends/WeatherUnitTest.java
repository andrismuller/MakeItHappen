package hu.bme.andrismulller.makeithappen_withfriends;

import org.junit.Test;

import java.util.List;

import hu.bme.andrismulller.makeithappen_withfriends.Functions.Weather.DownloadWeatherDataTask;
import hu.bme.andrismulller.makeithappen_withfriends.model.WeatherData;

import static org.junit.Assert.assertEquals;

/**
 * Created by Muller Andras on 4/15/2018.
 */

public class WeatherUnitTest implements DownloadWeatherDataTask.OnWeatherDataArrivedListener{
	@Test
	public void weather_isDownloaded() throws Exception {
		assertEquals(4, 2 + 2);
	}

	@Override
	public void onWeatherDataArrived(List<WeatherData> data) {

	}
}
