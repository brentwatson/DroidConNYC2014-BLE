package com.droidcon.nyc.ble.part4.utils;

import java.security.*;
import java.io.*;

public abstract class HashCode
{
    private static final char[] hexDigits;

    static {
        hexDigits = "0123456789abcdef".toCharArray();
    }

    public abstract int asInt();

    public abstract byte[] asBytes();

    public static HashCode fromBytes(final byte[] bytes) {
        Preconditions.checkArgument(bytes.length >= 1, (Object)"A HashCode must contain at least 1 byte.");
        return fromBytesNoCopy(bytes.clone());
    }
    
    static HashCode fromBytesNoCopy(final byte[] bytes) {
        return new BytesHashCode(bytes);
    }
    
    public abstract int bits();
    
    @Override
    public final boolean equals(final Object object) {
        if (object instanceof HashCode) {
            final HashCode that = (HashCode)object;
            return MessageDigest.isEqual(this.asBytes(), that.asBytes());
        }
        return false;
    }
    
    @Override
    public final int hashCode() {
        if (this.bits() >= 32) {
            return this.asInt();
        }
        final byte[] bytes = this.asBytes();
        int val = bytes[0] & 0xFF;
        for (int i = 1; i < bytes.length; ++i) {
            val |= (bytes[i] & 0xFF) << i * 8;
        }
        return val;
    }
    
    @Override
    public final String toString() {
        final byte[] bytes = this.asBytes();
        final StringBuilder sb = new StringBuilder(2 * bytes.length);
        for (final byte b : bytes) {
            sb.append(HashCode.hexDigits[b >> 4 & 0xF]).append(HashCode.hexDigits[b & 0xF]);
        }
        return sb.toString();
    }
    
    private static final class BytesHashCode extends HashCode implements Serializable
    {
        final byte[] bytes;
        private static final long serialVersionUID = 0L;
        
        BytesHashCode(final byte[] bytes) {
            super();
            this.bytes = Preconditions.checkNotNull(bytes);
        }
        
        @Override
        public int bits() {
            return this.bytes.length * 8;
        }
        
        @Override
        public byte[] asBytes() {
            return this.bytes.clone();
        }
        
        @Override
        public int asInt() {
            return (this.bytes[0] & 0xFF) | (this.bytes[1] & 0xFF) << 8 | (this.bytes[2] & 0xFF) << 16 | (this.bytes[3] & 0xFF) << 24;
        }

    }
}
