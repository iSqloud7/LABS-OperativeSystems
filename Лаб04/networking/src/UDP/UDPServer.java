package UDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {

    private DatagramSocket datagramSocket;
    private byte[] buffer;

    public UDPServer(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
        this.buffer = new byte[256];
        System.out.println("Server initialized on port: " + datagramSocket.getLocalPort());
    }

    @Override
    public void run() {
        DatagramPacket datagramPacket;
        System.out.println("Server is running and waiting for messages...");
        System.out.println("==================================================");
        while (true) {
            try {
                // Receive message
                datagramPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(datagramPacket);
                String receivedMessage = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                System.out.println("Server received: " + receivedMessage);

                // Generating response message
                String responseMessage;
                if ("login".equals(receivedMessage)) {
                    responseMessage = "logged in";
                } else if ("logout".equals(receivedMessage)) {
                    responseMessage = "loggedout";
                } else {
                    responseMessage = receivedMessage;
                }

                // Send response
                byte[] responseBuffer = responseMessage.getBytes();
                DatagramPacket responsePacket = new DatagramPacket(responseBuffer, responseBuffer.length, datagramPacket.getAddress(), datagramPacket.getPort());
                datagramSocket.send(responsePacket);
                // System.out.println("Response sent: " + responsePacket);

                // Loop break when logout message is received
                if ("logout".equals(receivedMessage)) {
                    System.out.println("Logout message received. Server is shutting down...");
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        datagramSocket.close();
    }

    public static void main(String[] args) {
        try {
            DatagramSocket datagramSocket = new DatagramSocket(9000);
            UDPServer udpServer = new UDPServer(datagramSocket);
            udpServer.start();
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }
}
