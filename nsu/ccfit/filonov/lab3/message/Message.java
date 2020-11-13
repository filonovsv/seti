package nsu.ccfit.filonov.lab3.message;

import nsu.ccfit.filonov.lab3.node.NodeID;

import java.util.UUID;

public class Message {

  private UUID identifier;
  private String message;
  NodeID nodeID;
  long time;

  public Message(UUID identifier, String message, NodeID nodeID){
    this.identifier = identifier;
    this.message = message;
    this.nodeID = nodeID;
    time = -1;
  }

  public Message(UUID identifier, String message, NodeID nodeID, long time){
    this.identifier = identifier;
    this.message = message;
    this.nodeID = nodeID;
    this.time = time;
  }

  public UUID getIdentifier() {
    return identifier;
  }

  public String getMessage() {
    return message;
  }

  public NodeID getNodeID() {
    return nodeID;
  }

  public long getTime() {
    return time;
  }

  public boolean equals(Object other) {
    if (other instanceof Message) {
      return this.identifier.equals(((Message) other).getIdentifier());
    }
    return false;
  }

  public int hashCode() {
    return identifier.hashCode();
  }

  public String toString()
  {
    return message;
  }
}
