import bhcc.edu.HandleStationClient;
import bhcc.edu.Weather;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Test class for {@link HandleStationClient} to verify data import functionality.
 * <p>
 * This class tests the import of weather data from a CSV file into the
 * {@link HandleStationClient} instance and validates that the imported data
 * is as expected.
 * </p>
 */

public class HandleSCTest {

    /**
     * Tests the {@link HandleStationClient#importData(File)} method to ensure
     * that weather data is correctly imported from a CSV file.
     * <p>
     * This test verifies that after calling {@code importData()}, the
     * {@code weatherData} map in {@link HandleStationClient} is not empty,
     * indicating that data was successfully loaded.
     * </p>
     *
     * @throws IOException    if an error occurs while reading the file.
     * @throws ParseException if an error occurs during date parsing.
     */
    @Test
    public void testImport() throws IOException, ParseException {
        HandleStationClient handleStationClient = new HandleStationClient();
        handleStationClient.importData(new File("src/main/resources/seattle-weather.csv"));
        HashMap<Date, Weather> testData = handleStationClient.getWeatherData();

        assertFalse(testData.isEmpty());
    }


    /**
     * Tests the first weather data entry imported by the {@link HandleStationClient#importData(File)} method.
     * <p>
     * This test verifies that the data associated with a specific date ("2012-01-01")
     * in the CSV file matches an expected {@link Weather} object. It ensures that the
     * data import method correctly populates weather data with accurate information.
     * </p>
     *
     * @throws IOException    if an error occurs while reading the file.
     * @throws ParseException if an error occurs during date parsing.
     */
    @Test
    public void testFirstElement() throws ParseException, IOException {
        HandleStationClient handleStationClient = new HandleStationClient();
        handleStationClient.importData(new File("src/main/resources/seattle-weather.csv"));
        HashMap<Date, Weather> testData = handleStationClient.getWeatherData();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = dateFormat.parse("2012-01-01");

        Weather expectedWeather = new Weather(0.0, 12.8, 5.0, 4.7, "drizzle");

        assertEquals(testData.get(date), expectedWeather);
    }

}
