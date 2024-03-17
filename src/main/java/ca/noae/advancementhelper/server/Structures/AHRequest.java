package ca.noae.advancementhelper.server.Structures;

import java.io.Serializable;

public class AHRequest implements Serializable {
  static final long serialVersionUID = 1L;

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
    this.requestLength = 10;
    this.requestPayload = rP;
  }

  public char getRequestChar() { return this.requestChar; }

  public int getRequestLength() { return this.requestLength; }

  public Payload getPayload() { return this.requestPayload; }
}
