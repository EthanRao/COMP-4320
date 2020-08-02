import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */


/* SR layer */

public class SRRD {
    SegUtils mseg; 
    final int WINSIZ = 8;
    int send_start = 0;
    int win_start = 7;
    public static final int BUFSIZ = 512;
    public static final int maxSeqN = 24; // 32
    int cliPort;
    InetAddress clientIPAddr;

    boolean END = false;
    int lastFragIndex=-1;
    Segment[] segWin = new Segment[maxSeqN];
    String[] AckBuf = new String[maxSeqN];
    EventTime[] timerEvents = new EventTime[8];

    DatagramSocket serverSocket;
    Timer myTimer;

    //Constructor
    /**
     * @param sar 
     * @param client_port of the client
     * @param ipAddress of the client
     * @param trace will print some information
     */
    public SRRD(SegUtils sar,int client_port, InetAddress ipAddress, int srvPort, boolean trace){
        mseg = sar;
        cliPort = client_port;
        clientIPAddr = ipAddress;

        try {
            serverSocket = new DatagramSocket(srvPort);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        for(int i = 0; i<AckBuf.length; i++){
            AckBuf[i]="N"+i;
        }
        fillFrags();
    }

    /**
     * @param pos sequence number of the fragment
     */
    public synchronized void sendSegment(int pos){
        if( segWin[pos] != null){
            byte[] fragmentData = segWin[pos].getSegmentBytes();
            DatagramPacket sendPacket = new DatagramPacket(fragmentData, fragmentData.length, clientIPAddr, cliPort);
            try {
                serverSocket.send(sendPacket);
                timerEvents[pos%8] = new EventTime(pos, 30);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * send file by SR
     */
    public void loopSend() {
        myTimer = new Timer();
        myTimer.schedule(new TimerKeeping(), 1,1);
        for(int i=0 ; i < WINSIZ ; i++){
            sendSegment(i);
        }

        while (!END){
            byte[] recvDat = new byte[BUFSIZ];

            DatagramPacket recvPkt = new DatagramPacket(recvDat, recvDat.length);

            try {
                serverSocket.receive(recvPkt);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String[] ackeds = recvAck(Arrays.copyOf(recvPkt.getData(), recvPkt.getLength()));
            updateAckArray(ackeds);

            if(AckBuf[send_start].startsWith("A")){
                stepWin();
            }
            for(int i = 0; i<WINSIZ; i++){
                if(AckBuf[(send_start+i)%maxSeqN].startsWith("N")){
                    sendSegment((send_start+i)%maxSeqN);
                }
            }

            recvDat = null;
            if (END){
                System.out.println("Connection is Closing...");
            }
        }
        closeConnection();
    }


    /**
     * @param acksArray the string array that is from the client
     */
    public void updateAckArray(String[] acksArray){
        int ackSid;
        boolean isACK = false;
        boolean isLastAcks = false;

        for(int i = 0; i < WINSIZ; i++){
            ackSid = Integer.parseInt(acksArray[i].substring(1));
            isACK = acksArray[i].startsWith("A");

            if(lastFragIndex != -1){
                int seq = segWin[lastFragIndex].getmHeader().getSeqEnd();
                if (seq == 1) isLastAcks = true;
            }

            if(isACK){
                AckBuf[ackSid] = acksArray[i];
                if(ackSid >= send_start && ackSid <=win_start){
                    timerEvents[ackSid%8]=null;
                }

                if(isLastAcks){
                    END = true;
                }
            }
        }
    }

    /**
     * @param acks
     * @return String filled with ackeds that are recieved from the reciever
     */
    public String[] recvAck(byte[] acks){
        ByteArrayInputStream bytein = new ByteArrayInputStream(acks);
        ObjectInputStream objstream=null;
        String[] ackArray = null;

        try {
            objstream = new ObjectInputStream(bytein);
            ackArray = (String[])objstream.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
        }

        try {
            objstream.close();
            bytein.close();
        } catch (IOException e) {
        }

        return ackArray;
    }

    /**
     * fill the window with Segments
     */
    public void fillFrags(){
        for(int i =0; i< WINSIZ; i++){
            if(mseg.hasNext()){
                segWin[i] = mseg.next();
            } else{
                break;
            }
        }
    }

    /**
     * Moves the Window Position over.
     */
    public void stepWin(){
        while(AckBuf[send_start].startsWith("A")){

            send_start = (send_start + 1) % maxSeqN;
            win_start = (win_start + 1) % maxSeqN;
            AckBuf[win_start] = "N"+win_start;

            if(mseg.hasNext()){
                segWin[win_start] = mseg.next();
                int seq = segWin[win_start].getmHeader().getSeqEnd();
                if(seq == 1) {
                    lastFragIndex = win_start;
                }
            }else{
                segWin[win_start] = null;
            }
        }

    }

    /**
     * clear
     */
    public void closeConnection(){
        myTimer.cancel();
        myTimer.purge();
        serverSocket.close();
    }

    /**
     * timer
     */
    class TimerKeeping extends TimerTask {
        @Override
        synchronized public void run() {
            for(int i=0; i<timerEvents.length; i++){
                if(timerEvents[i] !=null){
                    timerEvents[i].decrementExpirationTime();
                    timerEvents[i].decrementExpirationTime();

                    if(timerEvents[i].expirationTime <= 0){
                        int tempID = timerEvents[i].eventID;
                        timerEvents[i] = null;
                        if(AckBuf[tempID].startsWith("N")){
                            sendSegment(tempID);
                        }
                    }
                }
            }
        }
    }
}
