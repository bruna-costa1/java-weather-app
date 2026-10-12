package bhcc.edu;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * The HandleStationClient class is responsible for reading date input from the console,
 * sending it to a weather server, and displaying the server's response. It manages the
 * connection to the server and processes incoming weather data.
 */

public class HandleStationClient {
    private static final int DEFAULT_NUM_THREADS = 5;
    private final BufferedReader terminalReader;
    private static Logger logger;
    private final Lock lock;
    private final HashMap<Date, Weather> weatherData;

    /**
     * Constructs a HandleStationClient instance, initializing necessary fields and
     * setting up the terminal reader and logger.
     */
    public HandleStationClient() {
        weatherData = new HashMap<>();
        lock = new ReentrantLock();
        terminalReader = new BufferedReader(new InputStreamReader(System.in));
        logger = LoggerFactory.getLogger(HandleStationClient.class);

    }

    /**
     * The main method serves as the entry point of the HandleStationClient program.
     * It imports weather data from a CSV file, creates a thread pool, and starts the
     * weather station and client handler in separate threads.
     *
     * @param args Command line arguments (not used).
     * @throws IOException    if an I/O error occurs when creating the socket, reading from, or writing to the server.
     * @throws ParseException if there is an error parsing the date from the CSV file.
     */

    public static void main(String[] args) throws IOException, ParseException {

        // Import Data
        HandleStationClient handleStationClient = new HandleStationClient();
        handleStationClient.importData(new File("weatherApp\\src\\main\\resources\\seattle-weather.csv"));

        // Create and Execute Thread Pool
        ExecutorService executor = Executors.newFixedThreadPool(DEFAULT_NUM_THREADS);

        WeatherStation weatherStation = new WeatherStation(handleStationClient);
        Client stationClient = new Client(handleStationClient);

        executor.execute(weatherStation);
        executor.execute(stationClient);
    }

    /**
     * Handles incoming client connections, retrieves weather data for requested dates,
     * and sends the corresponding Weather objects back to clients.
     *
     * @throws IOException            if an I/O error occurs while handling the client request.
     * @throws ClassNotFoundException if the class of a serialized object cannot be found.
     * @throws ParseException         if there is an error parsing the date received from the client.
     */
    public void handleStation() throws IOException, ClassNotFoundException, ParseException {
        try {
            while (true) {
                // Initialize server and socket
                ServerSocket serverSocket = new ServerSocket(8000);
                Socket stationSocket = serverSocket.accept();
                logger.info("Handling client request on thread " + Thread.currentThread().getName());

                ObjectInputStream objectInputStream = new ObjectInputStream(stationSocket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(stationSocket.getOutputStream());

                // Receive String from Client
                String dateString = (String) objectInputStream.readObject();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = dateFormat.parse(dateString);
                logger.info("Request received from client " + dateString);

                // Retrieve weather data for the given date
                Weather weather = weatherData.get(date);

                // Send Weather object to client
                objectOutputStream.writeObject(weather);

                serverSocket.close();
                stationSocket.close();
            }

        } catch (IOException | ParseException | ClassNotFoundException e) {
            logger.error("Error handling client request: ", e);
        }
    }

    /**
     * Handles client connections, sending date requests to the server and receiving
     * corresponding weather data. This method runs in a loop, continuously accepting
     * input and processing responses.
     *
     * @throws IOException          if an I/O error occurs while connecting to the server or during data transmission.
     * @throws InterruptedException if the thread is interrupted while waiting for a lock.
     */
    public void handleClient() throws IOException, InterruptedException {

        while (true) {
            lock.lock();
            try {
                // Initialize socket
                Socket clientSocket = new Socket("localHost", 8000);
                ObjectOutputStream toServer = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(clientSocket.getInputStream());

                // Send Serialized Date over Wire
                String input = inputFromTerminal();
                toServer.writeObject(input);
                toServer.flush();
                logger.info("Sending date " + input);

                // Read the response from the server
                Weather weather = (Weather) objectInputStream.readObject();
                logger.info("Response received from server.");

                // Output to console
                if (weather != null) System.out.println(weather + "\n");
                else {
                    System.out.println("Data Not Found" + "\n");
                }

                toServer.flush();
                clientSocket.close();

            } catch (Exception e) {
                logger.error("Unexpected error occurred: ", e);
            } finally {
                lock.unlock();
            }
        }

    }

    /**
     * Prompts the user to enter a valid date from the console and reads the input.
     *
     * @return the date entered by the user as a String.
     * @throws IOException if an I/O error occurs while reading from the console.
     */
    private String inputFromTerminal() throws IOException {
        // Get Input from console and converts to a Date
        System.out.println("Enter a valid date (yyyy-MM-dd)");
        return terminalReader.readLine();
    }


    /**
     * Imports weather data from a specified CSV file into the weatherData HashMap.
     * The file is expected to have weather data in each line, with each field separated by commas.
     *
     * @param file The CSV file containing weather data.
     * @throws IOException    If an I/O error occurs when reading the file.
     * @throws ParseException If an error occurs while parsing the date from the CSV file.
     */
    public void importData(File file) throws IOException, ParseException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

        String line = bufferedReader.readLine(); // Skip header line

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        while ((line = bufferedReader.readLine()) != null) {
            // Check if line is not empty
            if (!line.trim().isEmpty()) {
                String[] lines = line.split(",");

                Date date = dateFormat.parse(lines[0]);

                Weather weather = new Weather(
                        Double.parseDouble(lines[1]),
                        Double.parseDouble(lines[2]),
                        Double.parseDouble(lines[3]),
                        Double.parseDouble(lines[4]),
                        lines[5]
                );

                this.weatherData.put(date, weather);
            }
        }

        bufferedReader.close();

    }

    public HashMap<Date, Weather> getWeatherData() {
        return weatherData;
    }
}
