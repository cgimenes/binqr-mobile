package pi.binqr.binqr;


import java.util.Arrays;

public class Metadata {
    /*
    QR Code metadata (290 bytes)
    1 byte - number
    1 byte - quantity
    1 byte - filename length
    255 bytes - filename
    32 bytes - md5
    */
    private final String checksum;
    private final int number;
    private final int quantity;
    private final int filenameLength;
    private final String filename;

    private Metadata(int number, int quantity, int filenameLength, String filename, String checksum) {

        this.number = number;
        this.quantity = quantity;
        this.filenameLength = filenameLength;
        this.filename = filename;
        this.checksum = checksum;
    }

    public static Metadata fromBytes(byte[] metadataBytes) {
        int number = metadataBytes[0];
        int quantity = metadataBytes[1];
        int filenameLength = metadataBytes[2];
        String filename = new String(Arrays.copyOfRange(metadataBytes, 3, filenameLength + 3));
        String checksum = new String(Arrays.copyOfRange(metadataBytes, 258, 289));

        return new Metadata(number, quantity, filenameLength, filename, checksum);
    }

    public String getChecksum() {
        return checksum;
    }

    public String getFilename() {
        return filename;
    }

    public int getFilenameLength() {
        return filenameLength;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getNumber() {
        return number;
    }
}
