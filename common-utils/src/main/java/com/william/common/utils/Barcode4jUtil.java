package com.william.common.utils;

import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;
import java.awt.image.BufferedImage;
import java.io.OutputStream;

/**
 * barcode4j工具类(条形码, 二维码生成库)
 */
public class Barcode4jUtil {

    // dpi精度
    private static final int DPI = 150;

    /**
     * 生成code128类型的条形码,并输出到输出流中
     * 条形码携带生成的文字信息
     * @param msg
     * @param out
     */
    public static void generate128File(String msg, OutputStream out) {
        try{
            Code128Bean bean = new Code128Bean();
            // module宽度
            bean.setModuleWidth(UnitConv.in2mm(2.0 / DPI));
            bean.doQuietZone(false);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "", DPI, BufferedImage.TYPE_BYTE_BINARY, false, 0);

            // 绘制条形码
            bean.generateBarcode(canvas, msg);
            // 结束绘制
            canvas.finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
