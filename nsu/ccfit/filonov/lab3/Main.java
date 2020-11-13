package nsu.ccfit.filonov.lab3;

import nsu.ccfit.filonov.lab3.node.Node;

public class Main {

    public static void main(String[] args) {

        Node node = new Node(args);
        node.start();
        try {
            node.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
