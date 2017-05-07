package com.nianticlabs.nia.network;

import java.nio.ByteBuffer;

public class NiaNet {
    private static final int CHUNK_SIZE = 32768;
    static ThreadLocal<ByteBuffer> readBuffer;
    private static final ThreadLocal<byte[]> threadChunk;

    /* renamed from: com.nianticlabs.nia.network.NiaNet.1 */
    static class C09631 extends ThreadLocal<byte[]> {
        C09631() {
        }

        protected byte[] initialValue() {
            return new byte[NiaNet.CHUNK_SIZE];
        }
    }

    /* renamed from: com.nianticlabs.nia.network.NiaNet.2 */
    static class C09642 extends ThreadLocal<ByteBuffer> {
        C09642() {
        }

        protected ByteBuffer initialValue() {
            return ByteBuffer.allocateDirect(NiaNet.CHUNK_SIZE);
        }
    }

    static {
        threadChunk = new C09631();
        readBuffer = new C09642();
    }

    private NiaNet() {
    }
}
