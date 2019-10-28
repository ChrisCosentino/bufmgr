package bufmgr;

import global.PageId;

public class HashTableBufferEntry {
	public HashTableBufferEntry next;
	
	public PageId pageNum = new PageId();
	
	public int frameNum;
	

}
