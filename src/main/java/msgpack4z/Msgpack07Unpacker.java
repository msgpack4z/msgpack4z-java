package msgpack4z;

import org.msgpack.core.MessagePack;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.ValueType;
import java.io.IOException;
import java.math.BigInteger;

public class Msgpack07Unpacker implements MsgUnpacker {
    private final MessageUnpacker unpacker;

    public Msgpack07Unpacker(MessageUnpacker unpacker) {
        this.unpacker = unpacker;
    }

    public Msgpack07Unpacker(MessagePack msgpack, byte[] bytes) {
        this(msgpack.newUnpacker(bytes));
    }

    public static MsgUnpacker defaultUnpacker(byte[] bytes) {
        return new Msgpack07Unpacker(MessagePack.DEFAULT, bytes);
    }

    public static MsgType toMsgType(ValueType t) {
        switch (t) {
            case NIL:
                return MsgType.NIL;
            case BOOLEAN:
                return MsgType.BOOLEAN;
            case INTEGER:
                return MsgType.INTEGER;
            case FLOAT:
                return MsgType.FLOAT;
            case STRING:
                return MsgType.STRING;
            case BINARY:
                return MsgType.BINARY;
            case ARRAY:
                return MsgType.ARRAY;
            case MAP:
                return MsgType.MAP;
            case EXTENSION:
                return MsgType.EXTENDED;
            default:
                throw new RuntimeException("impossible");
        }
    }

    @Override
    public MsgType nextType() throws IOException {
        return toMsgType(unpacker.getNextFormat().getValueType());
    }

    @Override
    public byte unpackByte() throws IOException {
        return unpacker.unpackByte();
    }

    @Override
    public short unpackShort() throws IOException {
        return unpacker.unpackShort();
    }

    @Override
    public int unpackInt() throws IOException {
        return unpacker.unpackInt();
    }

    @Override
    public long unpackLong() throws IOException {
        return unpacker.unpackLong();
    }

    @Override
    public BigInteger unpackBigInteger() throws IOException {
        return unpacker.unpackBigInteger();
    }

    @Override
    public double unpackDouble() throws IOException {
        return unpacker.unpackDouble();
    }

    @Override
    public float unpackFloat() throws IOException {
        return unpacker.unpackFloat();
    }

    @Override
    public int unpackArrayHeader() throws IOException {
        return unpacker.unpackArrayHeader();
    }

    @Override
    public void arrayEnd() throws IOException {
        // do nothing
    }

    @Override
    public void mapEnd() throws IOException {
        // do nothing
    }

    @Override
    public int unpackMapHeader() throws IOException {
        return unpacker.unpackMapHeader();
    }

    @Override
    public boolean unpackBoolean() throws IOException {
        return unpacker.unpackBoolean();
    }

    @Override
    public void unpackNil() throws IOException {
        unpacker.unpackNil();
    }

    @Override
    public String unpackString() throws IOException {
        return unpacker.unpackString();
    }

    @Override
    public byte[] unpackBinary() throws IOException {
        final byte[] bytes = new byte[unpacker.unpackBinaryHeader()];
        unpacker.readPayload(bytes);
        return bytes;
    }

    @Override
    public void close() throws IOException {
        unpacker.close();
    }
}
