package msgpack4z;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

public class Msgpack07Packer implements MsgPacker {
    private final MessagePacker self;
    private final ByteArrayOutputStream out;

    public Msgpack07Packer() {
        this(MessagePack.DEFAULT);
    }

    public Msgpack07Packer(MessagePack msgpack) {
        this.out = new ByteArrayOutputStream();
        this.self = msgpack.newPacker(out);
    }

    @Override
    public void packByte(byte a) throws IOException {
        self.packByte(a);
    }

    @Override
    public void packShort(short a) throws IOException {
        self.packShort(a);
    }

    @Override
    public void packInt(int a) throws IOException {
        self.packInt(a);
    }

    @Override
    public void packLong(long a) throws IOException {
        self.packLong(a);
    }

    @Override
    public void packDouble(double a) throws IOException {
        self.packDouble(a);
    }

    @Override
    public void packFloat(float a) throws IOException {
        self.packFloat(a);
    }

    @Override
    public void packBigInteger(BigInteger a) throws IOException {
        self.packBigInteger(a);
    }

    @Override
    public void packArrayHeader(int a) throws IOException {
        self.packArrayHeader(a);
    }

    @Override
    public void arrayEnd() throws IOException {
        // do nothing
    }

    @Override
    public void packMapHeader(int a) throws IOException {
        self.packMapHeader(a);
    }

    @Override
    public void mapEnd() throws IOException {
        // do nothing
    }

    @Override
    public void packBoolean(boolean a) throws IOException {
        self.packBoolean(a);
    }

    @Override
    public void packNil() throws IOException {
        self.packNil();
    }

    @Override
    public void packString(String a) throws IOException {
        self.packString(a);
    }

    @Override
    public void packBinary(byte[] a) throws IOException {
        self.packBinaryHeader(a.length);
        self.writePayload(a);
    }

    @Override
    public byte[] result() throws IOException {
        self.close();
        return out.toByteArray();
    }

    @Override
    public void packExtTypeHeader(byte extType, int payloadLen) throws IOException {
        self.packExtensionTypeHeader(extType, payloadLen);
    }

    @Override
    public void writePayload(byte[] a) throws IOException {
        self.writePayload(a);
    }
}
