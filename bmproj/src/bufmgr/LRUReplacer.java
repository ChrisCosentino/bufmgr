package bufmgr;

import java.util.LinkedList;
import java.util.Queue;


public class LRUReplacer extends Replacer{

	private BufMgr bufMgr;
	private Queue<FrameDesc> candidates;
	private int frameCount;
	
	public LRUReplacer(BufMgr bufmgr) {
		this.bufMgr = bufmgr;
		this.candidates = new LinkedList<>();
		this.frameCount = 0;
	}

	@Override
	public void insert(FrameDesc frame) {
		this.candidates.add(frame);
	}

	@Override
	public void remove(FrameDesc frame) {
		Queue<FrameDesc> tempQueue = new LinkedList<>();
		for (int i = 0; i < this.candidates.size(); i++) {
			if (this.candidates.peek() == frame){
				this.candidates.remove();
				continue;
			}
			tempQueue.add(this.candidates.remove());
		}
		this.candidates = tempQueue;
	}

	//returns the index of the frame for the replacement policy / only run when page is NOT in the buffer pool
	@Override
	public FrameDesc chooseVictim() throws Exception {
		int numBuffers = 2;
				//this.bufMgr.getNumBuffers();
		int frameCount = this.getFrameCount();

		if (frameCount < numBuffers) {
			FrameDesc newFrame = new FrameDesc();
			this.frameCount++;
			return newFrame;
		}
		else {
			if (candidates.peek() == null) {
				throw new BufferPoolExceededException(null, "BufMgr: Error");
			}
			return candidates.remove();
		}
		
	}

	public int getFrameCount(){
		return this.frameCount;
	}

}