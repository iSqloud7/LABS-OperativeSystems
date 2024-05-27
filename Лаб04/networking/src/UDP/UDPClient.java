package UDP;

import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UDPClient extends Thread {

    private String serverName;
    private int port;

    public UDPClient(String serverName, int port) {
        this.serverName = serverName;
        this.port = port;
        System.out.println("Client initialized with server: " + serverName + " on port: " + port);
    }


    @Override
    public void run() {
        try (DatagramSocket datagramSocket = new DatagramSocket();
             Scanner scanner = new Scanner(System.in)) {
            InetAddress serverAddress = InetAddress.getByName(serverName);
            System.out.println("Client connected to server at address: " + serverAddress);
            System.out.println("==================================================");

            String message;
            while (true) {
                System.out.print("Enter message: ");
                message = scanner.nextLine();
                sendMessage(datagramSocket, serverAddress, message);
                System.out.println("Message sent: " + message);

                String response = receiveMessage(datagramSocket);
                System.out.println("Client received: " + response);

                if ("logout".equals(message)) {
                    System.out.println("Logout message sent. Client is shutting down...");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(DatagramSocket datagramSocket, InetAddress address, String message) throws IOException {
        byte[] buffer = message.getBytes();
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length, address, port);
        datagramSocket.send(datagramPacket);
    }

    public String receiveMessage(DatagramSocket datagramSocket) throws IOException {
        byte[] buffer = new byte[256];
        DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
        datagramSocket.receive(datagramPacket);
        String receivedMessage = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
        System.out.println("Received response from server: " + receivedMessage);
        return receivedMessage;
    }

    public static void main(String[] args) {
        UDPClient udpClient = new UDPClient("localhost", 9000);
        udpClient.start();
    }
}