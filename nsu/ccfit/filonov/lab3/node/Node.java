package nsu.ccfit.filonov.lab3.node;

import nsu.ccfit.filonov.lab3.message.Message;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class Node extends Thread{

    public static final byte MESSAGE = 1;
    public static final byte CONFIRMATION = 2;
    public static final byte JOIN = 3;

    private final int QueueCapacity = 1000;

    NodeID myID;
    HashSet<NodeID> neighbors;
    HashSet<Message> confirmReceiving;
    ArrayBlockingQueue<Message> sendingQueue;
    ArrayBlockingQueue<Message> resendingQueue;
    int lossPercent;
    DatagramSocket socket;

    public Node(String[] args) {

        neighbors = new HashSet<>();
        confirmReceiving = new HashSet<>();

        sendingQueue = new ArrayBlockingQueue<>(QueueCapacity);
        resendingQueue = new ArrayBlockingQueue<>(QueueCapacity);
        try {
            myID = new NodeID(InetAddress.getLocalHost(), Integer.parseInt(args[1]));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        try {
            socket = new DatagramSocket(myID.getPort());
        } catch (SocketException e) {
            e.printStackTrace();
        }
        lossPercent = Integer.parseInt(args[2]);

         if (args.length == 5) {
             try {
                 NodeConnector nodeConnector = new NodeConnector(socket, new NodeID(InetAddress.getByName(args[3]),
                         Integer.parseInt(args[4])), neighbors);

                 nodeConnector.start();
                 nodeConnector.join();
             }catch (UnknownHostException e){
                 e.printStackTrace();
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
         }
    }

    @Override
    public void run() {

        NodeServer nodeServer = new  NodeServer(socket,  confirmReceiving,  sendingQueue, neighbors);
        NodeReader nodeReader = new NodeReader(myID, sendingQueue);
        NodeSender nodeSender = new NodeSender(socket, neighbors, sendingQueue,resendingQueue);
        NodeResender nodeResender = new NodeResender(resendingQueue ,socket ,neighbors , confirmReceiving);

        nodeServer.start();
        nodeReader.start();
        nodeSender.start();
        nodeResender.start();

        try {
            nodeServer.join();
            nodeReader.join();
            nodeSender.join();
            nodeResender.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
