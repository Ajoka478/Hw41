package task2;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
public class EchoClient {
    private final int port;
    private final  String host;

    public EchoClient( String host, int port) {
        this.port = port;
        this.host = host;    }

    public static  EchoClient connectTo(int port){
        var localhost = "127.0.0.1";
        return new EchoClient(localhost, port);    }

    public void run() {
        System.out.println("Type smth and press 'Enter':");
        try (Socket socket = new Socket(host, port)) {
            Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            try (scanner; writer) {
                while (true) {
                    String message = scanner.nextLine();
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                    if ("bye".equalsIgnoreCase(message)) {
                        return;
                    }
                    var input = socket.getInputStream();
                    var isr = new InputStreamReader(input);
                    try(Scanner scanner1 = new Scanner(isr)){
                        var messageFromServer = scanner1.nextLine().strip();
                        System.out.printf("Server: %s%n",messageFromServer);}
                }
            }

        }catch (NoSuchElementException e) {
            System.out.printf("Connection dropped!%n");
        } catch (IOException e) {
            System.out.printf("Can't connect to %s:%s!%n", host, port);
            e.printStackTrace();
        }
    }
}

    