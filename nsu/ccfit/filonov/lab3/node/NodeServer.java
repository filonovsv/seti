package nsu.ccfit.filonov.lab3.node;

import nsu.ccfit.filonov.lab3.message.Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;

public class NodeServer extends Thread {

    final int UUIDToBytesLength = 36;
    DatagramSocket socket;
    HashSet<Message> confirmReceiving;
    ArrayBlockingQueue<Message> sendingQueue;
    HashSet<NodeID> neighbors;

    public NodeServer(DatagramSocket socket, HashSet<Message> confirmReceiving,
                      ArrayBlockingQueue<Message> sendingQueue, HashSet<NodeID> neighbors) {
        this.socket = socket;
        this.confirmReceiving = confirmReceiving;
        this.sendingQueue = sendingQueue;
        this.neighbors = neighbors;
    }

    @Override
    public void run() {

        byte[] buffer = new byte[1024];

        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        try {
            while (true) {

                socket.receive(packet);
                if(packet.getData()[0] == Node.MESSAGE){
                    byte[] uuid = new byte[UUIDToBytesLength];
                    byte[] data = new byte[packet.getData().length - UUIDToBytesLength -1];
                    for (int i = 1; i < UUIDToBytesLength+1; i++) {
                        uuid[i-1] = packet.getData()[i];
                    }
                    for (int i = UUIDToBytesLength+1; i < packet.getData().length; i++) {
                        data[i - UUIDToBytesLength-1] = packet.getData()[i];
                    }
                    System.out.println(new String(data));
                    sendingQueue.add(new Message(UUID.fromString(new String(uuid)), new String(data),
                            new NodeID(packet.getAddress(), packet.getPort())));

                    byte[] buf = new byte[1024];
                    buf[0] = Node.CONFIRMATION;

                    for (int i = 1; i < uuid.length+1; i++) {
                        buf[i] = uuid[i-1];
                    }

                    socket.send(new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort()));

                }else if(packet.getData()[0] == Node.CONFIRMATION){
                    byte[] buf = new byte[UUIDToBytesLength];
                    for(int i =1; i < UUIDToBytesLength+1; i++){
                        buf[i-1] = packet.getData()[i];
                    }
                    confirmReceiving.add(new Message(UUID.fromString(new String(buf)), "",
                            new NodeID(packet.getAddress(), packet.getPort())));
                }else if(packet.getData()[0] == Node.JOIN){
                    byte[] buf = new byte[1];
                    buf[0] = Node.JOIN;
                    socket.send(new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort()));
                    neighbors.add(new NodeID(packet.getAddress(), packet.getPort()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}