package ca.noae.advancementhelper.server.Structures;

import ca.noae.advancementhelper.server.Structures.Payload;

import java.io.Serializable;

/*
 * AHRequest
 */
public class AHRequest implements Serializable {
    transient requestType requestType;
    char requestChar;
    
    int requestLength;
    Payload requestPayload;

    public AHRequest(requestType rT, int rL, Payload rP) {
        this.requestType = rT;
        this.requestChar = rT.CHAR;
        this.requestLength = rL;
        this.requestPayload = rP;
    }

    public AHRequest(requestType rT, Payload rP) {
        this.requestType = rT;
        this.requestChar = rT.CHAR;
        this.requestLength = rP.size;
        this.requestPayload = rP;
    }
}

