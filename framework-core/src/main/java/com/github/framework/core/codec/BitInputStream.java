package com.github.framework.core.codec;

/**
 * Base62工具类用到的针对bit操作的InputStream
 *
 */
public class BitInputStream {

    public static final int INT_8 = 8;
    public static final int INT_0 = 0;
    public static final int INT_32 = 32;

    private final byte[] buffer;
    private final int length;
    private int byteOffset;
    private int bitOffset;


    public BitInputStream(final byte[] bytes) {
        this.buffer = bytes;
        this.length = bytes.length;
        this.byteOffset = INT_0;
        this.bitOffset = INT_0;
    }


    synchronized public boolean hasMore() {
        return byteOffset * 8 + bitOffset < length * 8;
    }


    synchronized public int seekBit(final int pos) {
        final int nbyte = (bitOffset + pos) / INT_8;
        final int nbit = (bitOffset + pos) % INT_8;

        if (nbit < INT_0 || byteOffset + nbyte < INT_0 || (byteOffset + nbyte) * INT_8 + nbit > length * INT_8) {
            throw new IndexOutOfBoundsException();
        }

        bitOffset = nbit;
        byteOffset += nbyte;

        return pos > INT_0 ? pos : pos * -1;
    }


    synchronized public int readBits(final byte[] out, final int offset, final int length) {
        if (offset < INT_0 || (out.length - offset) * INT_8 < length || out.length * INT_8 < length || length >= INT_32) {
            throw new IndexOutOfBoundsException();
        }

        int bitCount = INT_0;
        int byteIndex = offset;

        for (int i = length - 1; i >= INT_0;) {
            if (!hasMore()) {
                return bitCount;
            }

            int count = INT_0;
            int value = INT_0;

            do {
                final int bit = readBit();
                if (bit == -1) {
                    break;
                }
                value |= bit << count;
                count++;
                bitCount++;
                i--;
            }
            while (i >= INT_0 && count < INT_8);

            out[byteIndex++] = (byte) (value & 0xff);
        }

        return bitCount;
    }


    synchronized public int readBit() {
        if (!hasMore()) {
            return -1;
        }
        if (bitOffset == INT_8) {
            byteOffset++;
            bitOffset = INT_0;
        }
        final int bit = buffer[byteOffset] & 1 << bitOffset++;
        return bit > INT_0 ? 1 : INT_0;
    }
}
