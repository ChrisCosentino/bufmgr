package bufmgr;

public abstract class Replacer {

	public abstract void insert(FrameDesc frame); //inserts frame
	public abstract void remove(FrameDesc frame); //moves frame to back of queue
	public abstract FrameDesc chooseVictim() throws Exception; //chooses frame from replacement policy

}