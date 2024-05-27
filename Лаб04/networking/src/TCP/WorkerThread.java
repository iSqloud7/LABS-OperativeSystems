package TCP;

import java.io.*;
import java.net.Socket;

public class WorkerThread extends Thread {

    private Socket socket;

    public WorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true)) {

            String message = reader.readLine();
            if ("login".equals(message)) {
                writer.println("logged in");
                writer.flush();
                TCPServer.incrementMessageCount();
                System.out.println("Received from client: " + message);
                System.out.println("Total messages received: " + TCPServer.getMessageCount());


                while (true) {
                    System.out.println("==================================================");
                    message = reader.readLine();
                    if (message == null) break;
                    TCPServer.incrementMessageCount();
                    System.out.println("Received from client: " + message);
                    System.out.println("Total messages received: " + TCPServer.getMessageCount());
                    if ("logout".equals(message)) {
                        writer.println("logged out");
                        writer.flush();

                        break;
                    } else {
                        writer.println("echo: " + message);
                        writer.flush();
                    }
                }
            } else {
                writer.println("Connection closed. Invalid login message.");
                writer.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
