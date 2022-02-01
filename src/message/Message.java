package message;

import java.io.Serializable;

/**
 *
 * @author ribeiro
 */
public class Message implements Serializable {

    private static final long serialVersionUID = -88888L;

    /**
     * specifies the message type (notification, ack)
     */
    private final String messageType;
    
    /**
     * specifies the header of the message (method name)
     */
    private final String msgID;
    
    /**
     * specifies the arguments to be sent (Object, Object[])
     */
    private final Object msgContent;
    
    /**
     * specifies the sender Type (craftsman, workshop)
     */
    private final String senderType;
    
    /**
     * specifies the thread id (if needed) of the requester
     */
    private final int senderID;
    
    /**
     * specifies the receiver type (customer, shop)
     */
    private final String receiverType;
    
    /**
     * specifies the thread id (if needed) of the requester
     */
    private final int receiverID;

    public Message(String messageType, String msgID, Object msg, String senderType,
            int senderID, String receiverType, int receiverID) {

        this.messageType = messageType;
        this.msgID = msgID;
        this.msgContent = msg;
        this.senderType = senderType;
        this.senderID = senderID;
        this.receiverType = receiverType;
        this.receiverID = receiverID;
    }

    public String getMessageType() {
        return messageType;
    }

    public String getMessageID() {
        return msgID;
    }

    public Object getMessageContent() {
        return msgContent;
    }

    public String getSenderType() {
        return senderType;
    }

    public int getSenderID() {
        return senderID;
    }

    public String getReceiverType() {
        return receiverType;
    }

    public int getReceiverID() {
        return receiverID;
    }

    @Override
    public String toString() {
        return "Message{" + "msgID=" + msgID + " msgContent=" + msgContent + " msgType=" + messageType + " senderType=" + senderType + " senderID=" + senderID +" receiverType="+receiverType+" receiverID="+receiverID+ '}';
    }

}
