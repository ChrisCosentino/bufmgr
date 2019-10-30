/* ... */

package bufmgr;

import java.io.*;
import java.util.*;
import diskmgr.*;
import global.*;

public class BufMgr implements GlobalConst{
	

	private Hashtable<PageId, Integer> pageFrameMap;
	private byte[][] bufPool;
	private FrameDesc[] frameDesc;
	private int numBufs;
	private Replacer replacer;

  /**
   * Create the BufMgr object.
   * Allocate pages (frames) for the buffer pool in main memory and
   * make the buffer manage aware that the replacement policy is
   * specified by replacerArg (i.e. Clock, LRU, MRU etc.).
   *
   * @param numbufs number of buffers in the buffer pool.
   * @param replacerArg name of the buffer replacement policy.
   */

  public BufMgr(int numbufs, String replacerArg) {
	  this.pageFrameMap = new Hashtable<>();
	  this.numBufs = numbufs;
	  this.bufPool = new byte[numBufs][MINIBASE_PAGESIZE];
	  this.frameDesc = new FrameDesc[numbufs];
	  this.replacer = new LRUReplacer(this);
  }


  /** 
   * Pin a page.
   * First check if this page is already in the buffer pool.  
   * If it is, increment the pin_count and return a pointer to this 
   * page.  If the pin_count was 0 before the call, the page was a 
   * replacement candidate, but is no longer a candidate.
   * If the page is not in the pool, choose a frame (from the 
   * set of replacement candidates) to hold this page, read the 
   * page (using the appropriate method from {\em diskmgr} package) and pin it.
   * Also, must write out the old page in chosen frame if it is dirty 
   * before reading new page.  (You can assume that emptyPage==false for
   * this assignment.)
   *
   * @param Page_Id_in_a_DB page number in the minibase.
   * @param page the pointer point to the page.
   * @param emptyPage true (empty page); false (non-empty page)
 * @throws Exception 
   */

  public void pinPage(PageId pin_pgid, Page page, boolean emptyPage) throws Exception {
	  
	  if(pageFrameMap.contains(pin_pgid)) {
		  
		  FrameDesc currentFrame = frameDesc[pageFrameMap.get(pin_pgid)];
//		  if (currentFrame.getPinCount() == 0) {
//			  this.replacer.remove(currentFrame); //remove from replacement candidates.
//		  }
		  currentFrame.incrementPinCount();
		
	  } else {
		  FrameDesc victimFrame = new FrameDesc() ;
		  victimFrame = replacer.chooseVictim();
		  
		  int i = Arrays.asList(frameDesc).indexOf(victimFrame);
		  if (i == -1) {
			  frameDesc[frameDesc.length+1] = victimFrame;
			  i = frameDesc.length;
		  }
		  if(victimFrame.isDirty()) {
			  flushPage(victimFrame.getPageNum());
		  }
		  
		  victimFrame.setPageNum(pin_pgid);
		  victimFrame.incrementPinCount();
		  victimFrame.setDirty(false);
		  pageFrameMap.put(pin_pgid, i);
	  }
  }


  /**
   * Unpin a page specified by a pageId.
   * This method should be called with dirty==true if the client has
   * modified the page.  If so, this call should set the dirty bit 
   * for this frame.  Further, if pin_count&gt;0, this method should 
   * decrement it. If pin_count=0 before this call, throw an exception
   * to report error.  (For testing purposes, we ask you to throw
   * an exception named PageUnpinnedException in case of error.)
   *
   * @param globalPageId_in_a_DB page number in the minibase.
   * @param dirty the dirty bit of the frame
 * @throws HashEntryNotFoundException 
 * @throws PageUnpinnedException 
   */

  public void unpinPage(PageId PageId_in_a_DB, boolean dirty) throws HashEntryNotFoundException, PageUnpinnedException {
	  
	  if(pageFrameMap.containsKey(PageId_in_a_DB)) {
		  int frameID = pageFrameMap.get(PageId_in_a_DB);
		  
		  if(frameDesc[frameID].getPinCount() == 0) {
			  throw new PageUnpinnedException(null, "");
		  }
		  
		  frameDesc[frameID].decrementPinCount();
		  frameDesc[frameID].setDirty(dirty);
		  
		  if (frameDesc[frameID].getPinCount() == 0) {
			  this.replacer.insert(frameDesc[frameID]);
		  }
	  }else {
		  throw new HashEntryNotFoundException(null, "");
	  }
  }


  /** 
   * Allocate new pages.
   * Call DB object to allocate a run of new pages and 
   * find a frame in the buffer pool for the first page
   * and pin it. (This call allows a client of the Buffer Manager
   * to allocate pages on disk.) If buffer is full, i.e., you 
   * can't find a frame for the first page, ask DB to deallocate 
   * all these pages, and return null.
   *
   * @param firstpage the address of the first page.
   * @param howmany total number of allocated new pages.
   *
   * @return the first page id of the new pages.  null, if error.
 * @throws IOException 
 * @throws DiskMgrException 
 * @throws FileIOException 
 * @throws InvalidPageNumberException 
 * @throws InvalidRunSizeException 
   */

  public PageId newPage(Page firstpage, int howmany) throws InvalidRunSizeException, InvalidPageNumberException, FileIOException, DiskMgrException, IOException {
	  
	  PageId pageId = new PageId();
	  try {
		  SystemDefs.JavabaseDB.allocate_page(pageId, howmany);
		  pinPage(pageId, firstpage, true);
		  
	  }catch(Exception e) {
			  SystemDefs.JavabaseDB.deallocate_page(pageId);
			  e.printStackTrace();
	  }
	  return pageId;
  }

        
  /**
   * This method should be called to delete a page that is on disk.
   * This routine must call the method in diskmgr package to 
   * deallocate the page. 
   *
   * @param globalPageId the page number in the data base.
 * @throws PagePinnedException 
 * @throws PageUnpinnedException 
 * @throws HashEntryNotFoundException 
   */

  public void freePage(PageId globalPageId) throws PagePinnedException, HashEntryNotFoundException, PageUnpinnedException {
	  int frameNum = pageFrameMap.get(globalPageId);
	  
	  if (!pageFrameMap.contains(globalPageId)) {
		  return;
	  }
	  
	  if (frameDesc[frameNum].getPinCount() > 0) {
		  throw new PagePinnedException(null, "");
	  }
	  
	  try {
			if (frameDesc[frameNum].getPinCount() == 1) {
				this.unpinPage(globalPageId, false);
			}
				

			SystemDefs.JavabaseDB.deallocate_page(globalPageId);
		} catch (InvalidRunSizeException | InvalidPageNumberException
				| FileIOException | DiskMgrException | IOException e) {
			e.printStackTrace();
		}
  }


  /**
   * Used to flush a particular page of the buffer pool to disk.
   * This method calls the write_page method of the diskmgr package.
   *
   * @param pageid the page number in the database.
   */

  public void flushPage(PageId pageid) {
	  if (!pageFrameMap.contains(pageid)) {
		  return;
	  }
	  frameDesc[pageFrameMap.get(pageid)].setDirty(false);
	  try {
		SystemDefs.JavabaseDB.write_page(pageid, new Page(bufPool[pageFrameMap.get(pageid)]));
	  } catch (InvalidPageNumberException | FileIOException | IOException e) {
		e.printStackTrace();
	}
	  
  }
  

  /** Flushes all pages of the buffer pool to disk
   */

  public void flushAllPages() {
	  for(int i = 0; i < this.numBufs; i++) {
		  if(frameDesc[i].isDirty()) {
			  flushPage(frameDesc[i].getPageNum());
		  }
	  }
  }


  /** Gets the total number of buffers.
   *
   * @return total number of buffer frames.
   */

  public int getNumBuffers() {
	  return this.numBufs;
  }


  /** Gets the total number of unpinned buffer frames.
   *
   * @return total number of unpinned buffer frames.
   */

  public int getNumUnpinnedBuffers() {
	  int j = 0;
	  for(int i = 0; i < this.numBufs; i++) {
		  if (frameDesc[i].getPinCount() == 0) {
			  j++;
		  }
	  }
	  return j;
  }
}

