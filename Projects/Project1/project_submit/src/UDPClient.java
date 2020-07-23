import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
//import java.util.Random;

/**
 * Project 01
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
        System.out.println("The ip address is" + IPAddress);

        byte[] sendData = new byte[UDPConst.SIZE_OF_PACKET];
        byte[] receiveData = new byte[UDPConst.SIZE_OF_PACKET];

        String sentence = "GET TestFile.html HTTP/1.0";
        sendData = sentence.getBytes();

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
        clientSocket.send(sendPacket);

        System.out.println("Now Sending request packet");

        boolean receiveFlag = false;
        DatagramPacket receiveDatagram = new DatagramPacket(receiveData, receiveData.length);

        int receiveNumber = 0;

        ArrayList<Packet> receivedPackets = new ArrayList<>();

        System.out.println("Ready to receive packets");

        //receive data and notify the end
        while (!receiveFlag) {
            clientSocket.receive(receiveDatagram);

            Packet receivePacket = Packet.restorePacket(receiveDatagram);

            receiveNumber++;
            System.out.println("Now received packet's segment number is : " + receiveNumber);
            if (receivePacket.getHeaderData(CommonUtils.HeaderEnum.SEGMENT_NUMBER, receivePacket.getHeader()) == UDPConst.VALUE) {
                receiveFlag = true;
            } else {
                receivedPackets.add(receivePacket);
            }
        }

        // gremlin function
        String probability = "0.0";

        if (args.length == 0) {
            System.out.println("Please enter the probability of damaged packets");
        } else {
            probability = args[0];
        }

        for (Packet packet : receivedPackets) {
            CommonUtils.gremlin(probability, packet);
        }

        // check errors
        if (!CommonUtils.errorDetection(receivedPackets)) {
            System.out.println("Errors Detected in Packet received from Server!");
        }

        // reassemble the file
        byte[] fileBack = CommonUtils.reassemble(receivedPackets);
        String fileStr = new String(fileBack);
        System.out.println("Following is the file got from the server, content :" + fileStr);
        clientSocket.close();
    }
}

