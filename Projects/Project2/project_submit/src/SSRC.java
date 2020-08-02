import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * client receive by SR
*/
public class SSRC {
    final int maxSeqN = 24;
    final int WINSIZ = 8;
    public static final int BUFSIZ = 512; // 128

    String[] ackedBuffer = new String[maxSeqN];
    ArrayList<Segment> fragList = new ArrayList<Segment>();
    Segment[] fragDWin = new Segment[maxSeqN];
    int recv_start = 0;
    int win_start = 7;

    int localPort;
    int srvPort;
    InetAddress serverIP;

    boolean lastRecv = false;
    DatagramSocket clientSocket;
    Gremlin gremlin;
    boolean trace = false;

    /**
     * @param local_Port - Port on client Side
     * @param server_IP - The Server IP
     * @param remote_Port - Port on Server side
     * @param grem is the Gremlin Used inside SR
     */
    public SSRC(int local_Port,InetAddress server_IP, int remote_Port, Gremlin grem, boolean traceOPT){
        gremlin = grem;

        for(int i =0; i<ackedBuffer.length; i++){
            ackedBuffer[i] = "N"+i;
        }
        localPort = local_Port;
        srvPort = remote_Port;
        serverIP = server_IP;
        try {
            clientSocket = new DatagramSocket(localPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        trace = traceOPT;
    }

    /**
     * @return
     */
    public ArrayList<Segment> getfragList(){
        return fragList;
    }

    public Segment packetToSegment(DatagramPacket datagram){
        if(datagram != null){
            byte[] data = datagram.getData();
            Segment frag = new Segment(Arrays.copyOfRange(data, 18, data.length));
            frag.getmHeader().setCheckSum(Arrays.copyOfRange(data, 0, 16));
            frag.getmHeader().setSequenceID(data[16]);
            frag.getmHeader().setSeqEnd(data[17]);
            return frag;
        }
        return null;
    }

    boolean validateChecksum(Segment segment){
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
			int datalen = segment.getDataBytes().length;
			byte[] byteHash = Arrays.copyOf(segment.getDataBytes(), datalen+2);
			byteHash[datalen] = segment.getmHeader().getSequenceID();
			byteHash[datalen+1] = segment.getmHeader().getSeqEnd();
			md.update(byteHash);
			byte[] ExpectedMD5Bytes = md.digest();
			byte[] ActualMD5Bytes = segment.getmHeader().getCheckSum();
			return MessageDigest.isEqual(ExpectedMD5Bytes, ActualMD5Bytes);
		} catch (NoSuchAlgorithmException e) {
		}
		return false;
	}


    /**
     * loop for receive
     */
    public void loopRecv(){
        while(!lastRecv){

            //System.out.println(Arrays.toString(ackedBuffer));
            byte[] receiveData = new byte[BUFSIZ];

            DatagramPacket recvPkt = new DatagramPacket(receiveData, receiveData.length);
            recvPkt = new DatagramPacket(receiveData, receiveData.length);
            try {
                clientSocket.receive(recvPkt);
            } catch (IOException e) {
                e.printStackTrace();
            }

            recvPkt = gremlin.filter(recvPkt);
            if(recvPkt != null){
                Segment temp = packetToSegment(recvPkt);
                int seqID = temp.getmHeader().getSequenceID();

                System.out.println("SeqNo to Checked: "+seqID);

                boolean rcvbase = (seqID >=(recv_start-WINSIZ)%maxSeqN 
                        && seqID <= (recv_start-1)%maxSeqN); 
                boolean inwin =(seqID >= recv_start 
                        && seqID <=win_start);
                boolean inmodo = (seqID <= (maxSeqN-1) 
                        && recv_start>win_start);

                boolean validfrag = validateChecksum(temp);
                if (validfrag) {
                    if (rcvbase || inwin || inmodo){
                        ackedBuffer[seqID]= "A"+seqID;
                        fragDWin[seqID] = temp;
                        sendAckeds();
                        if(seqID == recv_start){
                            stepDWin();
                        }
                    } else {
                        sendAckeds();
                    }
                }
            }
        }
    }

    /**
     * Transmits a vector of ACks and NAKs back to the server     
     */
    public void sendAckeds(){
        ArrayList<String> ackVector = new ArrayList<String>();
        for (int i = 0; i < WINSIZ; i++) {
            ackVector.add(ackedBuffer[(recv_start + i)%maxSeqN]);
        }

        byte[] btes = null;

        ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
        ObjectOutputStream outputstream;

        String[] tmpAckVec = new String[ackVector.size()];
        tmpAckVec = ackVector.toArray(tmpAckVec);

        try {
            outputstream = new ObjectOutputStream(bytestream);
            outputstream.writeObject(tmpAckVec);
            outputstream.flush();
            outputstream.close();
        } catch (IOException e) {
        }

        btes = bytestream.toByteArray();
        try {
            bytestream.flush();
            bytestream.close();
        } catch (IOException e1) {
        }

        DatagramPacket sendPacket = new DatagramPacket(btes, btes.length, serverIP, srvPort);

        try {
            clientSocket.send(sendPacket);
        } catch (IOException e) {
        }
    }

    /**
     * Moves the receive window
     */
    public void stepDWin() {		
        while(ackedBuffer[recv_start].startsWith("A")){
            int seq = fragDWin[recv_start].getmHeader().getSeqEnd();
            if(seq == 1){
                lastRecv = true;
                System.out.println("send over");
            }

            fragList.add(fragDWin[recv_start]);
            recv_start = (recv_start + 1) % maxSeqN;
            win_start = (win_start + 1) % maxSeqN;

            ackedBuffer[win_start] = "N"+win_start;
            fragDWin[win_start] = null;
        }
    }
}
