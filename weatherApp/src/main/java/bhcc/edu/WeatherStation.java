package bhcc.edu;

import java.io.*;
import java.text.ParseException;

/**
 * The WeatherStation class represents a server-side application that receives weather data from a client.
 * It implements the Runnable interface to enable multithreading operations, allowing concurrent handling of multiple clients.
 */
public class WeatherStation implements Runnable {
    HandleStationClient handleStationClient;

    /**
     * Constructs a WeatherStation instance with the specified HandleStationClient.
     *
     * @param handleStationClient the HandleStationClient instance used for handling client requests.
     */
    public WeatherStation(HandleStationClient handleStationClient) {
        this.handleStationClient = handleStationClient;
    }

    /**
     * Receives a date string from the client, retrieves the corresponding Weather object,
     * and sends it back to the client.
     * This method runs in a separate thread, allowing for concurrent handling of multiple client connections.
     *
     */
    @Override
    public void run() {
        try {
            handleStationClient.handleStation();
        } catch (IOException | ClassNotFoundException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}

