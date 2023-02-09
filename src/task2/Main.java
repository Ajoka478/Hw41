package task2;

import task2.EchoClient;

public class Main {
    public static void main(String[] args)  {
        EchoClient.connectTo(8788).run();

    }
}


