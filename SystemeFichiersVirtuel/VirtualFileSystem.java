
public class VirtualFileSystem {

    private MemoryManager memoryManager;

    public VirtualFileSystem() {
        this.memoryManager = new MemoryManager();
    }

    private int allocateInode() {
        
        byte[] memory = memoryManager.getFilesystemMemory();

        for (int i = 0; i <= (MemoryManager.MAX_INODES - 1) ; i++) {
            int offset = MemoryManager.INODE_TABLE_OFFSET + (i * Inode.INODE_SIZE);
            int fileType = Utils.readInt(memory, offset + 4);

            if (fileType == 0) {
                return i;
            }
        }

        return -1;
    }

    public boolean createFile(String directory, String filename) {
        int inodeNum = allocateInode();

        if (inodeNum == -1) {
            return false;
        }

        Inode inode = new Inode(memoryManager, inodeNum);
        
        long currentTime = System.currentTimeMillis();
        int[] directPointers = new int[Inode.DIRECT_POINTERS];

        inode.writeToMemory(
            1,
            0,
            currentTime,
            currentTime,
            directPointers,
            0,
            (short) 0644,
            1 
        );

        return true;
    }

    public MemoryManager getMemoryManager() {
        return memoryManager;
    }

    public boolean writeFile(int inodeNum, byte[] data) {
        int blocksNeeded = (data.length + MemoryManager.BLOCK_SIZE - 1) / MemoryManager.BLOCK_SIZE;

        if (blocksNeeded > Inode.DIRECT_POINTERS) {
            return false;
        }

        int[] blockPointers = new int[Inode.DIRECT_POINTERS];

        // Allocation des blocs nécessaires
        for (int i = 0; i < blocksNeeded; i++) {
            int block = memoryManager.allocateBlock();
            if (block == -1) {
                return false;
            }
            blockPointers[i] = block;
        }

        byte[] memory = memoryManager.getFilesystemMemory();
        int bytesRemaining = data.length;
        int dataSrcOffset = 0;

        for (int i = 0; i < blocksNeeded; i++) {
            int bytesToCopy = Math.min(MemoryManager.BLOCK_SIZE, bytesRemaining);
            int blockNum = blockPointers[i];
            int physicalOffset = blockNum * MemoryManager.BLOCK_SIZE;

            System.arraycopy(data, dataSrcOffset, memory, physicalOffset, bytesToCopy);

            dataSrcOffset += bytesToCopy;
            bytesRemaining -= bytesToCopy;
        }

        Inode inode = new Inode(memoryManager, inodeNum);
        long now = System.currentTimeMillis();
        inode.writeToMemory(
                1,
                data.length,
                now,
                now,
                blockPointers,
                0,
                (short) 0644,
                1
        );

        return true;
    }

    public byte[] readFile(int inodeNum) {
        Inode inode = new Inode(memoryManager, inodeNum);
        int fileSize = inode.getFileSize();

        if (fileSize == 0) {
            return new byte[0];
        }

        byte[] fileData = new byte[fileSize];
        byte[] memory = memoryManager.getFilesystemMemory();
        int[] blockPointers = inode.getDirectPointers();

        int bytesRemaining = fileSize;
        int fileDestOffset = 0;
        int blocksToRead = (fileSize + MemoryManager.BLOCK_SIZE - 1) / MemoryManager.BLOCK_SIZE;

        for (int i = 0; i < blocksToRead; i++) {
            int bytesToCopy = Math.min(MemoryManager.BLOCK_SIZE, bytesRemaining);
            int blockNum = blockPointers[i];
            int physicalOffset = blockNum * MemoryManager.BLOCK_SIZE;

            System.arraycopy(memory, physicalOffset, fileData, fileDestOffset, bytesToCopy);

            fileDestOffset += bytesToCopy;
            bytesRemaining -= bytesToCopy;
        }

        return fileData;
    }
}