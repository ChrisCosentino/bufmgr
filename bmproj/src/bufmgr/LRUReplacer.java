package bufmgr;

import java.util.LinkedList;
import java.util.Queue;

import global.PageId;

public class LRUReplacer extends Replacer{

	private BufMgr bufMgr;
	private Queue<FrameDesc> bufferFrames = new LinkedList<>();
	
	public LRUReplacer(BufMgr bufmgr) {
		this.bufMgr = bufmgr;
	}

	@Override
	public void insert(FrameDesc frame) {
		this.bufferFrames.add(frame);
	}

	@Override
	public void remove(FrameDesc frame) {
		Queue<FrameDesc> tempQueue = new LinkedList<>();
		for (int i = 0; i < this.bufferFrames.size(); i++) {
			if (this.bufferFrames.peek() == frame){
				this.bufferFrames.remove();
				continue;
			}
			tempQueue.add(this.bufferFrames.remove());
		}
		this.bufferFrames = tempQueue;
	}

	//returns the index of the frame for the replacement policy / only run when page is NOT in the buffer pool
	@Override
	public FrameDesc chooseVictim() throws Exception {
		int numBuffers = this.bufMgr.getNumBuffers();
		int frameCount = this.getFrameCount();

		if (frameCount < numBuffers) {
			FrameDesc newFrame = new FrameDesc();
			this.bufferFrames.add(newFrame);
			return newFrame;
		}

		return bufferFrames.remove();
	}

	public int getFrameCount(){
		return this.bufferFrames.size();
	}

}
