public class Inode {

    private MemoryManager memoryManager;
    private int inodeNumber;

    public static final int INODE_SIZE = 128;
    public static final int DIRECT_POINTERS = 10;

    public Inode(
            MemoryManager memoryManager,
            int inodeNumber) {

        this.memoryManager = memoryManager;
        this.inodeNumber = inodeNumber;
    }

    public int getInodeOffset() {
        return memoryManager.INODE_TABLE_OFFSET + inodeNumber * INODE_SIZE;
    }

    public int getFileType() {
        return Utils.readInt(memoryManager.getFilesystemMemory(), getInodeOffset()+4);
    }

    public int getFileSize() {
        return Utils.readInt(memoryManager.getFilesystemMemory(), getInodeOffset()+8);
    }

    public int[] getDirectPointers() {

        byte[] memory =
                memoryManager.getFilesystemMemory();

        int[] pointers =
                new int[DIRECT_POINTERS];

        for (int i=0; i<10; i++) {
            pointers[i] = Utils.readInt(memoryManager.getFilesystemMemory(), getInodeOffset()+ 28 + i*4);
        }

        return pointers;
    }

    public void writeToMemory(
            int fileType,
            int fileSize,
            long creationTime,
            long modificationTime,
            int[] directPointers,
            int indirectPointer,
            short permissions,
            int nombreLiens) {

        byte[] memory = memoryManager.getFilesystemMemory();
        int offset = getInodeOffset();

        offset += Utils.writeInt(memory, offset, inodeNumber);

        offset += Utils.writeInt(memory, offset, fileType);

        offset += Utils.writeInt(memory, offset, fileSize);

        offset += Utils.writeLong(memory, offset, creationTime);

        offset += Utils.writeLong(memory, offset, modificationTime);

        for (int i = 0; i < DIRECT_POINTERS; i++) {
            int ptr = (directPointers != null && i < directPointers.length) ? directPointers[i] : 0;
            offset += Utils.writeInt(memory, offset, ptr);
        }

        offset += Utils.writeInt(memory, offset, indirectPointer);

        offset += Utils.writeShort(memory, offset, permissions);

        offset += Utils.writeInt(memory, offset, nombreLiens);
    }
}