public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
        memory[offset]= (byte)((value >> 24) & 0xFF);
        memory[offset+1]= (byte)((value >> 16) & 0xFF);
        memory[offset+2]= (byte)((value >> 8) & 0xFF);
        memory[offset+3]= (byte)(value & 0xFF);
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        int value = memory[offset] & 0xFF;
        value = (value << 8) | (memory[offset + 1] & 0xFF);
        value = (value << 8) | (memory[offset + 2] & 0xFF);
        value = (value << 8) | (memory[offset + 3] & 0xFF);
        return value;
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        memory[offset]= (byte)((value >> 8) & 0xFF);
        memory[offset+1]= (byte)(value & 0xFF);
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        int value = memory[offset] & 0xFF;
        value = (value << 8) | (memory[offset + 1] & 0xFF);
        return (short) value;
    }
}