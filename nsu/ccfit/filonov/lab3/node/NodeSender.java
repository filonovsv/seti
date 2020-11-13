package nsu.ccfit.filonov.lab3.node;

import nsu.ccfit.filonov.lab3.message.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

public class NodeSender extends Thread {

    DatagramSocket socket;
    HashSet<NodeID> neighbors;
    ArrayBlockingQueue<Message> sendingQueue;
    ArrayBlockingQueue<Message> resendingQueue;

    public NodeSender(DatagramSocket socket, HashSet<NodeID> neighbors, ArrayBlockingQueue<Message> sendingQueue,
                      ArrayBlockingQueue<Message> resendingQueue){

        this.socket = socket;
        this.neighbors = neighbors;
        this.sendingQueue = sendingQueue;
        this.resendingQueue = resendingQueue;
    }

    @Override
    public void run(){

        try {
            while (true) {
                Message message = sendingQueue.take();
                byte[] uuid = message.getIdentifier().toString().getBytes();
                byte[] data = message.getMessage().getBytes();
                byte[] buf = new byte[uuid.length + data.length+1];

                buf[0] = Node.MESSAGE;

                for (int i = 1; i < uuid.length+1; i++) {
                    buf[i] = uuid[i-1];
                }
                for (int i = uuid.length+1; i < uuid.length + data.length+1; i++) {
                    buf[i] = data[i - uuid.length-1];
                }

                for (NodeID id : neighbors) {
                    if(!id.equals(message.getNodeID())) {
                        DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, id.getIP(), id.getPort());
                        socket.send(datagramPacket);
                        resendingQueue.add(new Message(message.getIdentifier(), message.getMessage(), id, System.currentTimeMillis()));
                    }
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
