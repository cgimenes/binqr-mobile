package com.binqr.client;


import java.util.Arrays;

public class Metadata {
    /*
    QR Code metadata (290 bytes)
    1 byte - number
    1 byte - quantity
    1 byte - filename length
    255 bytes - filename
    */
    private final int number;
    private final int quantity;
    private final String filename;

    private Metadata(int number, int quantity, String filename) {

        this.number = number;
        this.quantity = quantity;
        this.filename = filename;
    }

    public static Metadata fromBytes(byte[] metadataBytes) {
        int number = metadataBytes[0];
        int quantity = metadataBytes[1];
        int filenameLength = metadataBytes[2];
        String filename = new String(Arrays.copyOfRange(metadataBytes, 3, filenameLength + 3));

        return new Metadata(number, quantity, filename);
    }

    public String getFilename() {
        return filename;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getNumber() {
        return number;
    }
}
