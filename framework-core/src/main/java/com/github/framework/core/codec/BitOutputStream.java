package com.github.framework.core.codec;

import java.io.IOException;
import java.io.OutputStream;

/** 
 * 
 * Base62工具类用到的针对bit操作的OutputStream
 *
 */
public class BitOutputStream {
    public static final int CUR_BIT = 31;
    public static final int INT_8 = 8;
    /**
     * The Java OutputStream that is used to write completed bytes.
     */
    private OutputStream outputStream;

    /**
     * The temponary buffer containing the individual bits until a byte has been
     * completed and can be commited to the output stream.
     */
    private int buffer;

    /**
     * Counts how many bits have been cached up to now.
     */
    private int bitCount;

    /**
     * Create a new bit output stream based on an existing Java OutputSTream.
     * 
     * @param outputStream
     *            the output stream this class should use.
     */
    public BitOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    /**
     * Write a single bit to the stream. It will only be flushed to the
     * underlying OutputStream when a byte has been completed or when flush()
     * manually.
     * 
     * @param bit
     *            1 if the bit should be set, 0 if not
     * @throws IOException
     */
    synchronized public void writeBit(final int bit) throws IOException {
        if (outputStream == null) {
            throw new IOException("Already closed");
        }

        if (bit != 0 && bit != 1) {
            throw new IOException(bit + " is not a bit");
        }

        buffer = buffer << 1 | bit;
        bitCount++;

        if (bitCount == INT_8) {
            flush();
        }
    }

    /**
     * 
     * @return
     */
    public int getiBitCount() {
        return bitCount;
    }

    /**
     * Write the current cache to the stream and reset the buffer.
     * 
     * @throws IOException
     */
    public void flush() throws IOException {
        if (bitCount > 0) {
            outputStream.write((byte) buffer);
            bitCount = 0;
            buffer = 0;
        }
    }

    /**
     * Flush the data and close the underlying output stream.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        flush();
        outputStream.close();
        outputStream = null;
    }


    synchronized public void writeBits(final int value, final int numBits) throws IOException {
        for (int i = numBits - 1; i >= 0; i--) {
            writeBit(value >> i & 0x01);
        }
    }


    public static int getRequiredNumOfBits(final int maxValue) {
        // 0 still requires 1 bit to write it.
        if (maxValue == 0) {
            return 1;
        }

        // Go from left to right and search for the first 1
        // 00011010
        // |---- First 1
        int curBit;
        for (curBit = CUR_BIT; curBit >= 0; curBit--) {
            if ((maxValue & 0x01 << curBit) > 0) {
                // Found first bit that is not null - max. value
                break;
            }
        }
        return curBit + 1;
    }
}
