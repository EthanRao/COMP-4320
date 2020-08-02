import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */

class UDPServer {
    public static void main(String args[]) throws Exception {
        //use socket port 9876
        int srvport = 9876;
        DatagramSocket serverSocket = new DatagramSocket(srvport);

        Scanner scanner;

        byte[] receiveData = new byte[UDPConst.SIZE_OF_PACKET];
        byte[] sendData = new byte[UDPConst.SIZE_OF_PACKET];
        byte[] fileEnd = UDPConst.NULL_CHARACTER.getBytes();

        while (true) {
            //receive data from client
            DatagramPacket recvPkt = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(recvPkt);
            System.out.println("Now UDP-Server Received a request packet");

            //parse the name of file
            String sentence = new String(recvPkt.getData());
            scanner = new Scanner(sentence);
            scanner.next();
            sentence = scanner.next();
            scanner.close();

            InetAddress IPAddress = recvPkt.getAddress();
            int port = recvPkt.getPort();

            //open the file
            scanner = new Scanner(new File(sentence));
            String fileContent = "";
            while (scanner.hasNext()) {
                fileContent += scanner.nextLine();
            }
            System.out.println("Followings are file content :" + fileContent);
            scanner.close();

            //add headlines
            String headerLines = "HTTP/1.0 200 Document Follows\r\n"
                    + "Content-Type: text/plain\r\n"
                    + "Content-Length: " + fileContent.length() + "\r\n"
                    + "\r\n";
            String response = headerLines + fileContent;
            serverSocket.close();

            SegUtils seg = new SegUtils(response.getBytes());
            SRRD srd = new SRRD(seg, port, IPAddress, srvport, false);
            srd.loopSend();

            break;
        }
    }
}

