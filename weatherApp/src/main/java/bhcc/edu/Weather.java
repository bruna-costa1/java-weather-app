package bhcc.edu;

import java.io.Serializable;


/**
 * Represents weather data for a specific location and time.
 * <p>
 * This class implements {@link Serializable} to allow instances to be serialized
 * for transmission over a network or for storage. It encapsulates various weather
 * attributes such as precipitation, maximum and minimum temperatures, wind speed,
 * and weather conditions.
 * </p>
 */
public class Weather implements Serializable {
    private double precipitation;
    private double temp_max;
    private double temp_min;
    private double wind;
    private String weather;


    /**
     * Constructs a {@code Weather} object with the specified weather attributes.
     *
     * @param precipitation the amount of precipitation (in mm)
     * @param temp_max the maximum temperature (in degrees Celsius)
     * @param temp_min the minimum temperature (in degrees Celsius)
     * @param wind the wind speed (in km/h)
     * @param weather a description of the weather condition (e.g., "sunny", "rainy")
     */
    public Weather(double precipitation, double temp_max, double temp_min, double wind, String weather) {
        this.precipitation = precipitation;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.wind = wind;
        this.weather = weather;
    }

    /**
     * Compares this weather object with another for equality.
     *
     * @param o the object to be compared
     * @return {@code true} if the specified object is equal to this weather object;
     *         {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Weather weather = (Weather) o;
        return Double.compare(weather.precipitation, precipitation) == 0 &&
                Double.compare(weather.temp_max, temp_max) == 0 &&
                Double.compare(weather.temp_min, temp_min) == 0 &&
                Double.compare(weather.wind, wind) == 0;
    }

    /**
     * Returns a string representation of the weather object.
     *
     * @return a string containing the precipitation, maximum and minimum temperatures,
     *         wind speed, and weather condition.
     */

    @Override
    public String toString() {
        return "Weather{" +
                "precipitation=" + precipitation +
                ", temp_max=" + temp_max +
                ", temp_min=" + temp_min +
                ", wind=" + wind +
                ", weather='" + weather + '\'' +
                '}';
    }
}