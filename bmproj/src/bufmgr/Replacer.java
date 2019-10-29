package bufmgr;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import global.PageId;

public abstract class Replacer {
	private FrameDesc[] frameDesc;
	private Queue<FrameDesc> bufferFrames = new LinkedList();

	public abstract void insert(FrameDesc frame); //inserts frame
	public abstract void update(FrameDesc frame); //moves frame to back of queue
	public abstract FrameDesc chooseVictim() throws Exception; //chooses frame from replacement policy
	
	public void removeFromCandidates(PageId pageId) {
		bufferFrames.remove(frameDesc[directory.get(pageId.pid)]);
	}
	public void setFrameDesc(FrameDesc[] frameDesc) {
		this.frameDesc = frameDesc;
	}
}
