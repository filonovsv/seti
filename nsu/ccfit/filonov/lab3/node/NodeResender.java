package nsu.ccfit.filonov.lab3.node;

import nsu.ccfit.filonov.lab3.message.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;

public class NodeResender extends Thread {

    ArrayBlockingQueue<Message> resendingQueue;
    DatagramSocket socket;
    HashSet<NodeID> neighbors;
    HashSet<Message> confirmReceiving;

    public NodeResender(ArrayBlockingQueue<Message> resendingQueue, DatagramSocket socket,
                        HashSet<NodeID> neighbors, HashSet<Message> confirmReceiving) {
        this.resendingQueue = resendingQueue;
        this.socket = socket;
        this.neighbors = neighbors;
        this.confirmReceiving = confirmReceiving;
    }

    @Override
    public void run() {
        try {
            while (true) {

                Message message = resendingQueue.take();
                if(System.currentTimeMillis() - message.getTime() < 100){
                    Thread.sleep(System.currentTimeMillis() - message.getTime());
                }
                if(confirmReceiving.contains(message)){
                    confirmReceiving.remove(message);
                }else{
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
                    DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length, message.getNodeID().getIP(), message.getNodeID().getPort());
                    socket.send(datagramPacket);
                    resendingQueue.add(new Message(message.getIdentifier(), message.getMessage(), message.getNodeID(), System.currentTimeMillis()));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
