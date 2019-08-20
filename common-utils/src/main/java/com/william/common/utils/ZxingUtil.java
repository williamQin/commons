package com.william.common.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Zxing工具类
 * 1.生成条形码(生成的二维码不携带文字信息) & 解析条形码
 * 2.生成二维码 & 解析二维码
 */
public class ZxingUtil {

    /**
     * generateCode 根据code生成相应的条形码
     *
     * @param out    条形码输出流
     * @param code   条形码内容
     * @param width  条形码图片宽度
     * @param height 条形码图片高度
     */
    public static void generate128Barcode(OutputStream out, String code, int width, int height) {
        //定义位图矩阵BitMatrix
        try {
            // 使用code_128格式进行编码生成100*25的条形码
            MultiFormatWriter writer = new MultiFormatWriter();
            BitMatrix matrix = writer.encode(code, BarcodeFormat.CODE_128, width, height, null);
            ImageIO.write(MatrixToImageWriter.toBufferedImage(matrix), "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取条形码输入流并解析包含的内容
     *
     * @param in 输入流
     * @return
     */
    public static String readBarcode(InputStream in) {
        try {
            BufferedImage image = ImageIO.read(in);
            if (image == null) {
                return null;
            }
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "GBK");
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);

            Result result = new MultiFormatReader().decode(bitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码
     *
     * @param content 内容
     * @param width   图片宽度
     * @param height  图片高度
     * @param out     输出流
     */
    public static void generateQRCode(String content, int width, int height, OutputStream out) {
        //定义二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");//设置编码
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);//设置容错等级
        hints.put(EncodeHintType.MARGIN, 2);//设置边距默认是5

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取二维码内容
     * @param in 输入流
     * @return
     */
    public static String readQRCode(InputStream in) {
        try {
            BufferedImage image = ImageIO.read(in);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            Map<DecodeHintType, Object> hints = new HashMap<>();
            hints.put(DecodeHintType.CHARACTER_SET, "utf-8");//设置编码
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
