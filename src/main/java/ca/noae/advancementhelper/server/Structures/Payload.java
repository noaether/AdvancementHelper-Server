package ca.noae.advancementhelper.server.Structures;

import java.io.Serializable;

public class Payload implements Serializable {
    int size;

    String userToken;

    public int getSize() {
        return size;
    }
};