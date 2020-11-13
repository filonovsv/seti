package nsu.ccfit.filonov.lab3.node;

import nsu.ccfit.filonov.lab3.message.Message;

import java.net.InetAddress;

public class NodeID {
    private InetAddress IP;
    private int port;

    public NodeID(InetAddress IP, int port){
        this.IP = IP;
        this.port = port;
    }

    public InetAddress getIP() {
        return IP;
    }

    public int getPort() {
        return port;
    }

    public boolean equals(Object other) {
        if (other instanceof NodeID) {
            NodeID otherNodeID = (NodeID) other;
            return this.IP.equals(otherNodeID.getIP()) && (this.port == otherNodeID.getPort());
        }
        return false;
    }
    public int hashCode() {
        return IP.hashCode()/2 + Integer.hashCode(port)/2 + (IP.hashCode()%2 +Integer.hashCode(port)%2)%2;
    }
    public String toString()
    {
        return "IP + port";
    }
}
