package TCP;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TCPClient extends Thread {

    private String serverName;
    private int serverPort;

    public TCPClient(String serverName, int serverPort) {
        this.serverName = serverName;
        this.serverPort = serverPort;
    }

    @Override
    public void run() {
        try (Socket clientSocket = new Socket(serverName, serverPort);
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter message: ");
            String message = scanner.nextLine();
            System.out.println("Sending login message to the server...");
            System.out.println("==================================================");

            writer.println(message);
            writer.flush();

            String response = reader.readLine();
            System.out.println("Got message from server: " + response);
            if ("logged in".equals(response)) {
                while (true) {
                    System.out.println("Enter message: ");
                    message = scanner.nextLine();

                    writer.println(message);
                    writer.flush();

                    response = reader.readLine();
                    System.out.println("Response from server: " + response);

//                if ("logout".equals(message)) {
//                    System.out.println("Logout message sent. Client is shutting down...");
//                    break;
//                }
                    if ("logged out".equals(response)) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
            e.printStackTrace();
        }
    }

        public static void main(String[] args) {
        TCPClient tcpClient = new TCPClient("localhost", 7000);
        tcpClient.start();
    }
}
