import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UDPServer {
    public static void main(String[] args) {
        int port = 65432;
        // Binding to 0.0.0.0 tells Java to listen on ALL network cards (including Tailscale)
        try (DatagramSocket socket = new DatagramSocket(port, InetAddress.getByName("0.0.0.0"))) {
            System.out.println("--- DEBUG: SERVER UDP ACTIV ---");
            System.out.println("Listening on port: " + port);
            System.out.println("Waiting for Python client...");

            Scanner scanner = new Scanner(System.in);
            byte[] receiveBuffer = new byte[1024];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);

                // This line blocks until a packet physically hits the network card
                socket.receive(receivePacket);

                String clientMsg = new String(receivePacket.getData(), 0, receivePacket.getLength(), StandardCharsets.UTF_8);
                System.out.println("\n[DATA RECEIVED] From: " + receivePacket.getAddress() + ":" + receivePacket.getPort());
                System.out.println("[MESSAGE]: " + clientMsg);

                // Reply logic
                System.out.print("Type reply to Python: ");
                String response = scanner.nextLine();
                byte[] sendData = response.getBytes(StandardCharsets.UTF_8);

                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length,
                        receivePacket.getAddress(), receivePacket.getPort()
                );
                socket.send(sendPacket);
                System.out.println("[SENT] Reply sent back.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}