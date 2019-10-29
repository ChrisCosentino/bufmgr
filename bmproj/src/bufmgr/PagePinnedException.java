package bufmgr;

import chainexception.ChainException;

public class PagePinnedException extends ChainException{
	public PagePinnedException(Exception ex, String name) {
		super(ex, name);
	}
}
