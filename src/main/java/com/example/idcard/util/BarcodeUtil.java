package com.example.idcard.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.EAN13Writer;

import java.io.ByteArrayOutputStream;

public class BarcodeUtil {

    public static byte[] generateCode128(String text, int width, int height) throws Exception {
        Code128Writer writer = new Code128Writer();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.CODE_128, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }

    public static byte[] generateEAN13(String text, int width, int height) throws Exception {
        EAN13Writer writer = new EAN13Writer();
        BitMatrix matrix = writer.encode(text, BarcodeFormat.EAN_13, width, height);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(matrix, "PNG", out);
        return out.toByteArray();
    }
}