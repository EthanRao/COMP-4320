import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */


public class Segment {
    private SegHeader mHeader;	
    private byte[] mData;

    public Segment(byte[] data){
        mData = data;
        mHeader = new SegHeader();
    }

    public SegHeader getmHeader() {
        return mHeader;
    }

    public void setDataBytes(byte[] Data) {
        mData = Data;
    }

    public byte[] getDataBytes(){
        return mData;
    }

    public byte[] getSegmentBytes(){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            outputStream.write(getmHeader().getHeaderBytes());
            outputStream.write(getDataBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }

    public class SegHeader{
        private byte[] mCheckSum;
        private byte seqId;
        private byte seqEnd;

        public byte getSeqEnd() {
            return seqEnd;
        }
        public void setSeqEnd(byte endOfSequence) {
            seqEnd = endOfSequence;
        }
        public byte[] getCheckSum() {
            return mCheckSum;
        }
        public void setCheckSum(byte[] checkSum) {
            mCheckSum = checkSum;
        }
        public byte getSequenceID() {
            return seqId;
        }
        public void setSequenceID(byte sequenceID) {
            seqId = sequenceID;
        }

        public byte[] getHeaderBytes(){
            byte[] headerByteArray = Arrays.copyOf(mCheckSum, mCheckSum.length + 2);
            headerByteArray[mCheckSum.length] = seqId;
            headerByteArray[mCheckSum.length+1] = seqEnd;
            return headerByteArray;
        }
    }
}
