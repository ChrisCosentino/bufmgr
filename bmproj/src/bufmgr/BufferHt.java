//package bufmgr;
//
//import java.util.LinkedList;
//
//import global.PageId;
//
//public class BufferHt {
//	
//	public static final int ARR_SIZE = 128;
//    private LinkedList<HTObject> table = new LinkedList[ARR_SIZE];
// 
//    public BufferHt() {
//        //init vals in array
//        for(int i=0; i < ARR_SIZE; i++){
//            table[i] = null;
//        }
//    }
//    
//    
//    private int hash(PageId pageId) {
//        return (Math.abs(pageId.hashCode())) % table.length;
//    }
//    
//    public void insert(PageId pageId, int frameNum) {
//    	HTObject entry = new HTObject();
//    	int bucket = hash(pageId);
//    	
//    	entry.pageNum.pid = pageId.pid;
//    	entry.frameNum = frameNum;
//    	table.
//    }
//    
//    
//    
//}
