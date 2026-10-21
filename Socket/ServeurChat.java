import java.net.*;
import java.io.*;
import java.util.*;

public class ServeurChat {
    // Liste thread-safe des flux de sortie
    private static final List<PrintWriter> clients = 
        Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(5000);
        System.out.println("Serveur Chat démarré");

        while (true) {
            Socket client = serverSocket.accept();
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            
            clients.add(out);

            System.out.println("Client connecté. Total : " + clients.size());
            new Thread(new ChatHandler(client, out)).start();
        }
    }

    static class ChatHandler implements Runnable {
        private Socket client;
        private PrintWriter out;

        public ChatHandler(Socket socket, PrintWriter out) {
            this.client = socket;
            this.out = out;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter out = new PrintWriter(client.getOutputStream(), true);

                String ligne;
                while ((ligne = in.readLine()) != null) {
                    System.out.println(Thread.currentThread().getName() + " Reçu : " + ligne);
                    out.println("ECHO: " + ligne);
                    broadcast(ligne);
                }

                System.out.println(Thread.currentThread().getName() + " déconnecté");
                in.close();
                out.close();
                client.close();

            } catch (IOException e) {
            
            }finally {
                synchronized (clients) {
                    clients.remove(out);
                }
                try { client.close(); } catch (IOException ignored) {}
            }
        }

        private void broadcast(String message) {
            synchronized (clients) {
                for (PrintWriter writer : clients) {
                    if (writer != out) {  // Exclut l'émetteur
                        writer.println(">>> " + message);
                    }
                }
            }
        }
    }
}