import java.io.*;
import java.net.*;
import java.util.Random;

public class VMServer {
    // Porta su cui il server ascolta (come da specifica PDF: 4400)
    private static final int PORT = 4400;

    public static void main(String[] args) {
        System.out.println("VM Server avviato sulla porta " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                // Accetta nuove connessioni e avvia un thread dedicato per ogni client
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuovo client connesso: " + clientSocket.getInetAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * Classe che gestisce la comunicazione con un singolo client.
 */
class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private volatile boolean sendingData = false; // Flag per controllare il loop di invio
    private Thread senderThread; // Thread dedicato all'invio dei dati

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Ricevuto da client: " + inputLine);

                // 1. Gestione comando START
                if (inputLine.startsWith("start:vm")) {
                    String vmId = inputLine.split(":")[1];

                    // Controllo validità VM (solo vm1, vm2, vm3 sono valide)
                    if (isValidVM(vmId)) {
                        startSendingData(vmId);
                    } else {
                        out.println("Not Found");
                        out.println("*");
                    }
                }
                // 2. Gestione comando STOP
                else if (inputLine.equals("stop")) {
                    stopSendingData();
                }
                // 3. Gestione comando DISCONNECT
                else if (inputLine.equals("disconnect")) {
                    stopSendingData();
                    break; // Esce dal loop e chiude la connessione
                }
                // 4. Gestione comandi non riconosciuti
                else {
                    out.println("error");
                    break; // Chiude connessione come da specifiche
                }
            }
        } catch (IOException e) {
            System.err.println("Connessione interrotta con il client.");
        } finally {
            closeConnection();
        }
    }

    /**
     * Verifica se la VM richiesta è valida (vm1, vm2, vm3).
     */
    private boolean isValidVM(String vmStr) {
        return vmStr.equals("vm1") || vmStr.equals("vm2") || vmStr.equals("vm3");
    }

    /**
     * Avvia un thread separato per inviare i dati, permettendo al thread principale
     * di rimanere in ascolto per comandi "stop" o "disconnect".
     */
    private void startSendingData(String vmName) {
        // Se c'è già un invio in corso, lo fermiamo prima di iniziarne uno nuovo
        stopSendingData();

        sendingData = true;
        senderThread = new Thread(() -> {
            Random rand = new Random();
            long startTime = System.currentTimeMillis();
            long maxDuration = 30000; // 30 secondi 

            System.out.println("Inizio invio dati per " + vmName);

            while (sendingData && (System.currentTimeMillis() - startTime < maxDuration)) {
                try {
                    // Genera valori casuali tra 0 e 100
                    int cpu = rand.nextInt(101);
                    int mem = rand.nextInt(101);
                    int disk = rand.nextInt(101);
                    int net = rand.nextInt(101);

                    // Formato: cpu:mem:disk:net 
                    String data = String.format("%d:%d:%d:%d", cpu, mem, disk, net);
                    out.println(data);

                    // Simula intervallo di aggiornamento (es. 1 secondo)
                    Thread.sleep(1000);

                } catch (InterruptedException e) {
                    // Thread interrotto, usciamo dal loop
                    break;
                } catch (Exception e) {
                    break;
                }
            }

            // Appena finito il loop (per timeout o stop manuale), invia "*"
            if (sendingData) { // Se è finito per timeout
                out.println("*");
                sendingData = false;
            } else {
                // Se è stato fermato manualmente, inviamo "*" qui se non inviato altrove
                // Ma da specifica, "*" va inviato al termine dell'invio.
                out.println("*");
            }
            System.out.println("Fine invio dati per " + vmName);
        });
        senderThread.start();
    }

    /**
     * Ferma l'invio dei dati impostando il flag a false e interrompendo il thread.
     */
    private void stopSendingData() {
        if (sendingData) {
            sendingData = false;
            if (senderThread != null) {
                senderThread.interrupt();
            }
        }
    }

    private void closeConnection() {
        try {
            stopSendingData();
            if (socket != null && !socket.isClosed()) socket.close();
            System.out.println("Connessione chiusa.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}