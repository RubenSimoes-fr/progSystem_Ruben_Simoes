public class Utils {

    public static int writeInt(byte[] memory, int offset, int value) {
        // TODO: Écrire les 4 octets de 'value' dans 'memory'
        memory[offset]= (byte)((value >> 8) & 0xFF);
        memory[offset+1]= (byte)((value >> 16) & 0xFF);
        memory[offset+2]= (byte)((value >> 24) & 0xFF);
        memory[offset+3]= (byte)((value >> 32) & 0xFF);
        // à partir de 'offset', en big-endian.
        return 4;
    }

    public static int readInt(byte[] memory, int offset) {
        int value;
        value = memory[0];
        value <<= 8;
        value += memory[1];
        value <<= 8;
        value += memory[2];
        value <<= 8;
        value += memory[3];
        value <<= 8;
        return value;
    }

    public static int writeShort(byte[] memory, int offset, short value) {
        // TODO: Écrire les 2 octets de 'value'.
        memory[offset]= (byte)((value >> 8) & 0xFF);
        memory[offset+1]= (byte)((value >> 8) & 0xFF);
        return 2;
    }

    public static short readShort(byte[] memory, int offset) {
        // TODO: Lire le short sur 2 octets.
        return 0;
    }
}