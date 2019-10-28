package bufmgr;

import java.util.Queue;

import global.PageId;

public class LRUReplacer extends Replacer{
	private Queue<FrameDesc> candidates;
	
	private BufMgr bufmgr;
	
	public LRUReplacer(BufMgr bufmgr) {
		this.bufmgr = bufmgr;
	}

	@Override
	public int chooseVictim() throws Exception {
	
		return 0;
	}

	@Override
	public void updateCandidatesPinned(PageId pageId) {
		
		
	}

	@Override
	public void updateCandidatesUnpinned(PageId pageId) {
		
		
	}

}
