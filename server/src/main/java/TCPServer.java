import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TCPServer {
    public static void main(String[] args) {
        int port = 65432;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("--- SERVER BIDIRECȚIONAL TCP ---");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("[CONECTAT] " + clientSocket.getRemoteSocketAddress());

                // THREAD FOR RECEIVING (From Python)
                new Thread(() -> {
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("\n[PYTHON]: " + line);
                            System.out.print("Tu (Server): ");
                        }
                    } catch (IOException e) { System.out.println("Client deconectat."); }
                }).start();

                try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true)) {
                    Scanner sc = new Scanner(System.in);
                    while (true) {
                        System.out.print("Tu (Server): ");
                        String response = sc.nextLine();
                        writer.println(response);
                    }
                } catch (Exception e) { break; }
            }
        } catch (IOException e) { e.printStackTrace(); }
    }
}