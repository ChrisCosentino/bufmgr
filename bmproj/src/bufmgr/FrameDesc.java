package bufmgr;

import global.PageId;

public class FrameDesc {
	private int pinCount;
	private PageId pageNum;
	private boolean dirty;
	
	public FrameDesc() {
		this.pinCount = 0;
		this.pageNum = new PageId();
		this.dirty = false;
	}

	public PageId getPageNum() {
		return this.pageNum;
	}
	
	public void setPageNum(PageId n) {
		this.pageNum = n;
	}
	
	public int getPinCount() {
		return this.pinCount;
	}
	
	public void incrementPinCount() {
		this.pinCount = this.pinCount + 1;
	}
	
	public void decrementPinCount() {
		
	}

	public boolean isDirty() {
		return this.dirty;
	}
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
