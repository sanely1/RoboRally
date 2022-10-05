package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains the Message and all necessary members
 */

public class Message {

    private Integer sender;
    private List<Integer> receiver = new ArrayList<>();
    private String message;
    private boolean isPrivate = false;
    private TypeName.MSG_TYPE msgType = TypeName.MSG_TYPE.MSG_TYPE_INFO;

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    /**
     * returns the receiver of the message
     */
    public List<Integer> getReceiver() {
        return receiver;
    }

    /**
     * sets the receiver of the message
     */
    public void setReceiver(List<Integer> receiver) {
        this.receiver = receiver;
    }


    /**
     * adds receivers
     */
    public void addReceiver(Integer receiver) {
        if (!this.receiver.contains(receiver))
            this.receiver.add(receiver);
    }

    /**
     * returns the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * sets the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    public TypeName.MSG_TYPE getMsgType() {
        return msgType;
    }

    public void setMsgType(TypeName.MSG_TYPE msgType) {
        this.msgType = msgType;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}


