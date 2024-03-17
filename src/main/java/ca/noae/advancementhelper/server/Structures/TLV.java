package ca.noae.advancementhelper.server.Structures;

import ca.noae.advancementhelper.server.Structures.Payload;

import java.io.Serializable;


/*
 * AHRequest
 */
public class AHRequest implements Serializable {
    transient requestType requestType;
    char requestChar;
    
    int requestLenght;
    Payload requestPayload;

    public AHRequest(requestType rT, int rL, Payload rP) {
        this.requestType = rT;
        this.requestChar = rT.CHAR;
        this.requestLenght = rL;
        this.requestPayload = rP;
    }

    public AHRequest(requestType rT, Payload rP) {
        this.requestType = rT;
        this.requestChar = rT.CHAR;
        this.requestLenght = rP.
        this.requestPayload = rP;
    }
}

public enum requestType {
    CREATE("Q"),
    READ("S"),
    UPDATE("U"),
    DELETE("D");


    public final String CHAR;

    private requestType(String CHAR) {
        this.CHAR = CHAR;
    }
}