package nsu.ccfit.filonov.lab3.node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Set;

public class NodeConnector extends Thread {

    DatagramSocket socket;
    NodeID targetID;
    Set neighbors;

    public NodeConnector(DatagramSocket socket, NodeID targetID, Set neighbors) {
        this.socket = socket;
        this.targetID = targetID;
        this.neighbors = neighbors;

    }

    @Override
    public void run() {


        try {

            byte[] request = new byte[1];

            request[0] = Node.JOIN;

            DatagramPacket datagramPacket = new DatagramPacket(request, request.length, targetID.getIP(), targetID.getPort());

            socket.send(datagramPacket);

            socket.receive(datagramPacket);

            neighbors.add(new NodeID(datagramPacket.getAddress(), datagramPacket.getPort()));

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
