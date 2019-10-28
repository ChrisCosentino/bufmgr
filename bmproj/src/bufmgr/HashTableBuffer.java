package bufmgr;

import global.GlobalConst;
import global.PageId;

public class HashTableBuffer implements GlobalConst {
	
	private HashTableBufferEntry hashTable[];
	private int numFrames;
	
	public HashTableBuffer(int numFrames) {
		this.numFrames = numFrames;
		this.hashTable = new HashTableBufferEntry[numFrames];
		for(int i = 0; i < numFrames; i++) {
			hashTable[i] = null;
		}
	}
	
	private int hash(PageId pageNum) {
		return (pageNum.pid % numFrames);
	}
	
	public boolean add(PageId pageNum, int frameNum) {
		
		HashTableBufferEntry entry = new HashTableBufferEntry();
		
		int index = hash(pageNum);
		
		
		return false;
		
	}
}
