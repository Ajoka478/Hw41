package task1;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {
    private final int port;

    private EchoServer(int port){
        this.port = port;
    }

    static EchoServer bindToPort(int port){
        return new EchoServer(port);
    }
    public void run(){
        try (ServerSocket server = new ServerSocket(port)){
            try (var clientSocket = server.accept()){
                handle(clientSocket);
            }
        }catch (IOException e){
            String fmtMsg = "Port %s is busy.%n";
            System.out.printf(fmtMsg, port);
            e.printStackTrace();
        }
    }
    private void handle(Socket socket) throws IOException {
        var input = socket.getInputStream();
        var isr = new InputStreamReader(input, "UTF-8");
        var scanner = new Scanner(isr);

        try (scanner) {
            while (true) {
                var message = scanner.nextLine().strip();
                System.out.printf("Got: %s%n", message);
                String reversedMessage = new StringBuilder(message).reverse().toString();
                PrintWriter out = new PrintWriter(socket.getOutputStream().toString());
                out.println(reversedMessage);
                out.write(reversedMessage);
                out.write(System.lineSeparator());
                out.flush();
                if ("bye".equalsIgnoreCase(message)) {
                    System.out.printf("Bye, bye!%n");
                    return;
                }        }
        } catch (NoSuchElementException e) {
            System.out.printf("Client dropped the connection! %n");
        }
    }
}