import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Random;

/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */

public class UDPClient {
    public static void main(String args[]) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        DatagramSocket clientSocket = new DatagramSocket();
        //own ip address
        InetAddress IPAddress = InetAddress.getByName("104.145.92.29");
        //InetAddress IPAddress = InetAddress.getByName("127.0.0.1");
        System.out.println("The ip address is" + IPAddress);

        byte[] sendData = new byte[UDPConst.SIZE_OF_PACKET];
        byte[] receiveData = new byte[UDPConst.SIZE_OF_PACKET];

        String sentence = "GET TestFile.html HTTP/1.0";
        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);

        System.out.println("Now Sending request packet");

        int localPort = clientSocket.getLocalPort();
        clientSocket.close();

        Gremlin gremlin = new Gremlin(0.1, 0.3);
        SSRC sr = new SSRC(localPort, IPAddress, 9876, gremlin, false);
        sr.loopRecv();

        SegUtils seg = new SegUtils();
        seg.setSegmentList(sr.getfragList());
        seg.unsegmentFile();

        byte[] temp = seg.getData();
        String receivedSegData = new String(temp);
        System.out.println(receivedSegData);
    }
}

