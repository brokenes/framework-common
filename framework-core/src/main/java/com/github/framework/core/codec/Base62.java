package com.github.framework.core.codec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Base62 编码&解码工具类 
 *
 */
public class Base62 {

    private final static String BASE62_CODING_SPACE = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static String BASE62_ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";


    private Base62() {}

    /**
     * 生成base62编码字符串
     * @param original
     * @return
     * @throws IOException
     */
    public static String decode(final byte[] original) throws IOException {
        final StringBuilder sb = new StringBuilder();
        final BitInputStream stream = new BitInputStream(original);
        // only read 6-bit at a time
        final byte[] read = new byte[1];

        while (stream.hasMore()) {
            // try to read 5 bits
            final int length = stream.readBits(read, 0, 6);
            if (length <= 0) {
                break;
            }
            // first 5-bit is 11111
            if (read[0] >> 1 == 0x1f) {
                sb.append(BASE62_CODING_SPACE.charAt(61));
                stream.seekBit(-1);
            } else if (read[0] >> 1 == 0x1e) {
                // first 5-bit is 11110
                sb.append(BASE62_CODING_SPACE.charAt(60));
                stream.seekBit(-1);
            } else {
                sb.append(BASE62_CODING_SPACE.charAt(read[0]));
            }
        }
        return sb.toString();
    }

    /**
     * 从base62字符串中解码
     * @param base62
     * @return
     * @throws IOException
     */
    public static byte[] encode(final String base62) throws IOException {
        final ByteArrayOutputStream tempOutputStream = new ByteArrayOutputStream();
        final BitOutputStream out = new BitOutputStream(tempOutputStream);

        for (int i = 0; i < base62.length(); i++) {
            final int index = BASE62_CODING_SPACE.indexOf(base62.charAt(i));

            if (i == base62.length() - 1) {
                final int mod = out.getiBitCount() % 8;
                if (mod != 0) {
                    out.writeBits(index, 8 - mod);
                }
            } else {
                if (index == 60) {
                    out.writeBits(0x1e, 5);
                } else if (index == 61) {
                    out.writeBits(0xf8, 5);
                } else {
                    out.writeBits(index, 6);
                }
            }
        }
        tempOutputStream.flush();
        return tempOutputStream.toByteArray();
    }

}
