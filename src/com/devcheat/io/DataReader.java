package com.devcheat.io;

import sun.org.mozilla.javascript.internal.ObjToIntMap;

/**
 * Created with IntelliJ IDEA.
 * User: Vincent
 * Date: 1/03/13
 * Time: 13:15
 * To change this template use File | Settings | File Templates.
 */
public class DataReader {
    int pos = 0;
    byte[] b;

    public int getPos() {
        return pos;
    }

    public byte[] getData() {
        return b;
    }

    public DataReader(byte[] data) {
        this.b = data;
    }

    public DataReader(int startPos, byte[] data) {
        this.pos = startPos;
        this.b = data;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int readUnsignedByte() {
        return b[pos++] & 0xFF;
    }

    public int readUnsignedShort() {
        byte[] b = this.b;
        return ((b[pos++] & 0xFF) << 8) | (b[pos++] & 0xFF);
    }

    public short readShort() {
        byte[] b = this.b;
        return (short) (((b[pos++] & 0xFF) << 8) | (b[pos++] & 0xFF));
    }

    public int readInt() {
        byte[] b = this.b;
        return ((b[pos++] & 0xFF) << 24) | ((b[pos++] & 0xFF) << 16)
                | ((b[pos++] & 0xFF) << 8) | (b[pos++] & 0xFF);
    }

    public long readLong() {
        long l1 = readInt();
        long l0 = readInt() & 0xFFFFFFFFL;
        return (l1 << 32) | l0;
    }

    public String readUTF() {
        int utfLen = readUnsignedShort();
        int endIndex = pos + utfLen;
        char[] buf = new char[utfLen];
        byte[] b = this.b;
        int strLen = 0;
        int c;
        int st = 0;
        char cc = 0;
        while (pos < endIndex) {
            c = b[pos++];
            switch (st) {
                case 0:
                    c = c & 0xFF;
                    if (c < 0x80) { // 0xxxxxxx
                        buf[strLen++] = (char) c;
                    } else if (c < 0xE0 && c > 0xBF) { // 110x xxxx 10xx xxxx
                        cc = (char) (c & 0x1F);
                        st = 1;
                    } else { // 1110 xxxx 10xx xxxx 10xx xxxx
                        cc = (char) (c & 0x0F);
                        st = 2;
                    }
                    break;
                case 1: // byte 2 of 2-byte char or byte 3 of 3-byte char
                    buf[strLen++] = (char) ((cc << 6) | (c & 0x3F));
                    st = 0;
                    break;
                case 2: // byte 2 of 3-byte char
                    cc = (char) ((cc << 6) | (c & 0x3F));
                    st = 1;
                    break;
            }
        }
        return new String(buf, 0, strLen);
    }


    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }
}
