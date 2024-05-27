package TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {

    private int serverPort;
    private static int messageCount = 0;

    public TCPServer(int serverPort) {
        this.serverPort = serverPort;
    }

    public static synchronized void incrementMessageCount() {
        messageCount++;
    }

    public static synchronized int getMessageCount() {
        return messageCount;
    }

    @Override
    public void run() {
        System.out.println("Server is starting...");

        try (ServerSocket serverSocket = new ServerSocket(serverPort)) {
            System.out.println("Server is started!!!");
            System.out.println("===(Waiting for clients...)===");

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    System.out.printf("New client connected: %s:%d\n", clientSocket.getInetAddress(), clientSocket.getPort());
                    System.out.println("==================================================");

                    new WorkerThread(clientSocket).start();
                } catch (IOException e) {
                    System.out.println("Error accepting client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Socket server failed to start: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TCPServer tcpServer = new TCPServer(7000);
        tcpServer.start();
    }
}