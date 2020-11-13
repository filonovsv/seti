package nsu.ccfit.filonov.lab3.node;

import nsu.ccfit.filonov.lab3.message.Message;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class NodeReader extends Thread {

    NodeID myID;
    ArrayBlockingQueue<Message> sendingQueue;

    public NodeReader(NodeID myID, ArrayBlockingQueue<Message> sendingQueue) {
        this.myID = myID;
        this.sendingQueue = sendingQueue;
    }

    @Override
    public void run(){
        while(true) {
            byte[] bytes = new byte[1024];
            try {
                System.in.read(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            sendingQueue.add(new Message(UUID.randomUUID(), new String(bytes).strip(), myID));
        }
    }
}
