package org.lolhens.test.bytearray;

import org.lolhens.primitive.PrimitiveUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by LolHens on 15.10.2014.
 */
public class ByteArray {
    public static void main(String[] args) {
        byte[] byteArray1 = new byte[4];
        byte[] byteArray2 = null;

        long time, all;

        all = 0;
        for (int i = 0; i < 10; i++) {
            time = System.nanoTime();
            byteArray2 = PrimitiveUtil.FLOAT.toByteArray(4f, ByteOrder.BIG_ENDIAN);
            all += System.nanoTime() - time;
        }
        System.out.println(all / 10);

        all = 0;
        for (int i = 0; i < 10; i++) {
            time = System.nanoTime();
            ByteBuffer buffer = ByteBuffer.wrap(byteArray1).order(ByteOrder.BIG_ENDIAN);
            buffer.putFloat(4f);
            all += System.nanoTime() - time;
        }
        System.out.println(all / 10);

        for (byte b : byteArray1) System.out.print(b + " ");
        System.out.println();
        for (byte b : byteArray2) System.out.print(b + " ");
    }
}
