import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;

/**
 * Project 01
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */

class UDPServer {
    public static void main(String args[]) throws Exception {
        //use socket port 9876
        DatagramSocket serverSocket = new DatagramSocket(9876);

        Scanner scanner;

        byte[] receiveData = new byte[UDPConst.SIZE_OF_PACKET];
        byte[] sendData = new byte[UDPConst.SIZE_OF_PACKET];
        byte[] fileEnd = UDPConst.NULL_CHARACTER.getBytes();

        while (true) {
            //receive data from client
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            System.out.println("Now UDP-Server Received a request packet");

            //parse the name of file
            String sentence = new String(receivePacket.getData());
            scanner = new Scanner(sentence);
            scanner.next();
            sentence = scanner.next();
            scanner.close();

            InetAddress IPAddress = receivePacket.getAddress();
            int port = receivePacket.getPort();

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

            //segment big file to small packets
            ArrayList<Packet> packetArrayList = CommonUtils.segmentation(response.getBytes());

            //send packets and indicate file end
            int packetNumber = 0;
            System.out.println("Send start");
            for (Packet packet : packetArrayList) {
                DatagramPacket sendPacket = packet.getDatagramPacket(IPAddress, port);
                serverSocket.send(sendPacket);
                packetNumber++;
                System.out.println("This is " + packetNumber + " packet, total packet size is " + packetArrayList.size());
            }

            DatagramPacket finalPacket = CommonUtils.finalPacket(fileEnd, IPAddress, port);
            serverSocket.send(finalPacket);
            System.out.println("Now send the last packet to indicate the end of the file");
            System.out.println("Send over");
        }
    }
}

