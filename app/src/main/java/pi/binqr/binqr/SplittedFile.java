package pi.binqr.binqr;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;

public class SplittedFile {

    private QRCode[] pieces;
    private String fileName;

    public QRCode addPart(byte[] rawBytes) {
        QRCode qrCode = QRCode.fromBytes(rawBytes);
        if (pieces == null) {
            pieces = new QRCode[qrCode.getMetadata().getQuantity()];
        }
        pieces[qrCode.getMetadata().getNumber()-1] = qrCode;
        if (fileName == null) {
            fileName = qrCode.getMetadata().getFilename();
        }
        return qrCode;
    }

    public boolean isPartAdded(byte[] rawBytes) {
        return pieces != null && pieces[rawBytes[0]-1] != null;
    }

    public boolean isCompleted() {
        for (QRCode piece : pieces) {
            if (piece == null) {
                return false;
            }
        }

        return true;
    }

    public int getPiecesQuantity() {
        return pieces.length;
    }

    private byte[] mergeFile() {
         byte[][] dataPieces = new byte[pieces.length][];

        for (int i = 0; i < pieces.length; i++) {
            dataPieces[i] = pieces[i].getData();
        }

        return joinArray(dataPieces);
    }

    private byte[] joinArray(byte[][] arrays) {
        int length = 0;
        for (byte[] array : arrays) {
            length += array.length;
        }

        final byte[] result = new byte[length];

        int offset = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    public void save(File directory) {
        File file = new File(directory, fileName);

        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fos=new FileOutputStream(file.getPath());

            byte[] mergedFile = mergeFile();

            fos.write(mergedFile);
            fos.close();
        }
        catch (java.io.IOException e) {
            Log.e("PictureDemo", "Exception in photoCallback", e);
        }
    }
}
