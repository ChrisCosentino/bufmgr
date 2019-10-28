package bufmgr;

import java.util.HashMap;
import java.util.List;

import global.PageId;

public abstract class Replacer {
	private FrameDesc[] frameDesc;
	private HashMap<Integer, Integer> directory;
	private List<FrameDesc> candidates;
	
	public abstract int chooseVictim() throws Exception;
	public abstract void updateCandidatesPinned(PageId pageId);
	public abstract void updateCandidatesUnpinned(PageId pageId);
	
	public void removeFromCandidates(PageId pageId) {
		candidates.remove(frameDesc[directory.get(pageId.pid)]);
	}
	public void setFrameDesc(FrameDesc[] frameDesc) {
		this.frameDesc = frameDesc;
	}
}
