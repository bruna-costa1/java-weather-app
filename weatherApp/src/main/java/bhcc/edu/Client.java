package bhcc.edu;

import java.io.*;


/**
 * Represents a client that interacts with a {@link HandleStationClient} instance.
 * <p>
 * This class implements the {@link Runnable} interface, allowing instances of
 * {@code Client} to be executed by a thread. The client is responsible for
 * handling requests to the {@code HandleStationClient}.
 * </p>
 */
public class Client implements Runnable {
    private final HandleStationClient handleStationClient;

    /**
     * Constructs a {@code Client} with the specified {@link HandleStationClient}.
     *
     * @param handleStationClient the {@code HandleStationClient} instance that
     *                            this client will use to handle requests.
     */
    public Client(HandleStationClient handleStationClient) {
        this.handleStationClient = handleStationClient;
    }


    /**
     * Executes the client's request handling process.
     * <p>
     * This method calls {@link HandleStationClient#handleClient()} to process
     * client requests. If an {@link IOException} or {@link InterruptedException}
     * occurs during the process, it wraps the exception in a {@code RuntimeException}.
     * </p>
     */
    @Override
    public void run() {
        try {
            handleStationClient.handleClient();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
