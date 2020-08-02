import java.util.ArrayList;
import java.util.Arrays;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */


/* Segments the object into segments */

public class SegUtils {

    ArrayList<Segment> segList = new ArrayList<Segment>();
    byte[] mData;

    public SegUtils(byte[] totalData){
        mData = totalData;
        if(mData !=null && mData.length != 0){
            segmentFile();
        }
    }

    public SegUtils() {
    }

    Segment genChecksum(Segment segment){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			int datalen = segment.getDataBytes().length;
			byte[] byteHash = Arrays.copyOf(segment.getDataBytes(), datalen+2);
			byteHash[datalen] = segment.getmHeader().getSequenceID();
			byteHash[datalen+1] = segment.getmHeader().getSeqEnd();
			md.update(byteHash);
			byte[] MD5bytes = md.digest();
			segment.getmHeader().setCheckSum(MD5bytes);
		} catch (NoSuchAlgorithmException e) {
		}
		
		return segment;
	}

    /**
     * Using the Error Detector this separates the file into segments with headers and stores them
     * in the Servers Segment List
     */
    public void segmentFile(){
        int start = 0;
        int segsize = SRRD.BUFSIZ-18;
        int end = segsize;
        for(int i =0; i<(double)mData.length/segsize; i++){
            Segment nseg = new Segment(Arrays.copyOfRange(mData, start, end));
            nseg.getmHeader().setSequenceID((byte)(i%SRRD.maxSeqN));
            if((i+1)<(double)mData.length/segsize){
                nseg.getmHeader().setSeqEnd((byte)0);
            } else{
                nseg.getmHeader().setSeqEnd((byte)1);
            }
            appendSegs(genChecksum(nseg));
            start = end;
            end = end+segsize;
        }
    }

    /**
     * @return
     */
    public boolean hasNext(){
        return segList.size() != 0;
    }

    /**
     * @return
     */
    public Segment next(){
        return segList.remove(0);
    }

    /**
     * @param segment
     */
    public void appendSegs(Segment segment){
        segList.add(segment);
    }

    /**
	 * @return mData is the all the data recompiled from the segments
	 */
	public byte[] getData() {
		return mData;
	}

    /**
	 * @param fragList
	 */
	public void setSegmentList(ArrayList<Segment> fragList){
		segList = fragList;
	}

	/**
	 * converts the arraylist of segments into an array of bytes  
	 */
	public void unsegmentFile(){
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		while(!segList.isEmpty()){
			Segment temp = segList.remove(0);
			try {
				outputStream.write(temp.getDataBytes());
			} catch (IOException e) {
			}
		}

		mData = outputStream.toByteArray();
		try {
			outputStream.close();
		} catch (IOException e) {
		}
	}
}
