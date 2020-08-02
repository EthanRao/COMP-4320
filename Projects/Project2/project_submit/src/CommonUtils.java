import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Random;

/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */

/**
 * implement utils
 * includes segmentation re-assembly
 * checksum
 */
public class CommonUtils {

    public enum HeaderEnum {
        /**
         * header segment_number and checksum
         */
        SEGMENT_NUMBER("segment_number", 0),
        CHECKSUM("check_sum", 1);

        private String type;
        private Integer index;

        HeaderEnum(String type, Integer index) {
            this.type = type;
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }
    }

    /**
     * segmentation of messages
     * set header and data
     *
     * @param message
     * @return
     */
    public static ArrayList<Packet> segmentation(byte[] message) {

        ArrayList<Packet> result = new ArrayList<>();

        int length = message.length;
        if (length > 0) {
            int pos = 0;
            Integer segmentNumber = 0;
            while (pos < length) {
                Packet packet = new Packet();
                ArrayList<Short> header = new ArrayList<>(2);
                header.add(UDPConst.VALUE);
                header.add(UDPConst.VALUE);
                byte[] packetData = new byte[UDPConst.SIZE_OF_DATA];
                int packetDataSize = UDPConst.SIZE_OF_DATA;
                if (length - pos < UDPConst.SIZE_OF_DATA) {
                    packetDataSize = length - pos;
                }

                for (int i = 0; i < packetDataSize; i++) {
                    packetData[i] = message[pos];
                    pos++;
                }

                packet.setData(packetData);
                packet.setHeaderData(HeaderEnum.SEGMENT_NUMBER, segmentNumber.shortValue(), header);
                short checkSum = getCheckSum(packetData);
                packet.setHeaderData(HeaderEnum.CHECKSUM, checkSum, header);
                packet.setHeader(header);

                segmentNumber++;

                result.add(packet);
            }
        }

        return result;
    }

    /**
     * reassemble of file
     *
     * @param packetArrayList
     * @return
     */
    public static byte[] reassemble(ArrayList<Packet> packetArrayList) {

        int totalSize = 0;
        for (Packet packet : packetArrayList) {
            totalSize += packet.getDataSize();
        }
        byte[] result = new byte[totalSize];
        int pos = 0;
        for (int i = 0; i < packetArrayList.size(); ++i) {
            for (Packet packet : packetArrayList) {
                if (packet.getHeaderData(HeaderEnum.SEGMENT_NUMBER, packet.getHeader()) == i) {
                    int dataSize = packet.getDataSize();
                    for (int j = 0; j < dataSize; ++j) {
                        result[pos + j] = packet.getData(j);
                    }
                    pos += dataSize;
                    break;
                }
            }
        }

        return result;

    }

    /**
     * The checksum is calculated by simply summing all the bytes in the packet
     *
     * @param message
     * @return
     */
    public static short getCheckSum(byte[] message) {
        Integer sum = 0;
        for (byte i : message) {
            sum += i;
        }
        return sum.shortValue();
    }

    /**
     * error detection
     *
     * @param packetArrayList
     * @return
     */
    public static boolean errorDetection(ArrayList<Packet> packetArrayList) {
        boolean result = true;
        for (Packet packet : packetArrayList) {
            ArrayList<Short> header = packet.getHeader();
            short checkSumByServer = packet.getHeaderData(HeaderEnum.CHECKSUM, header);
            short checkSumByClient = getCheckSum(packet.getData());
            System.out.println("checkSumByServer : " + checkSumByServer);
            System.out.println("checkSumByClient : " + checkSumByClient);
            if (checkSumByServer != checkSumByClient) {
                result = false;
                System.out.println("Error ! Checksum not the same , SegmentNumber is :" + packet.getHeaderData(HeaderEnum.SEGMENT_NUMBER, header));
            }
        }
        return result;
    }

    /**
     * If the packet is to be damaged, the probability of
     * changing one byte is .5, the probability of changing two bytes is .3, and the probability of
     * changing 3 bytes is .2.
     * @param probability
     * @param packet
     */
    public static void gremlin(String probability, Packet packet) {

        if (probability.equals(UDPConst.ZERO)) {
            return;
        }

        Random rand = new Random();
        double probability1 = rand.nextDouble();
        double probability2 = rand.nextDouble();
        int changeNumber;

        if (probability2 <= UDPConst.PROB1) {
            changeNumber = 1;
        } else if (probability2 <= UDPConst.PROB2) {
            changeNumber = 2;
        } else {
            changeNumber = 3;
        }

        if (probability1 <= Double.parseDouble(probability)) {
            System.out.println("gremlin function works! It may cause different checkSum.");
            for (int i = 0; i < changeNumber; i++) {
                byte[] data = packet.getData();
                int byteNum = rand.nextInt(packet.getDataSize());
                data[byteNum] = (byte) ~data[byteNum];
            }
        }
    }

    /**
     * prepare final packet to classify the end of file
     *
     * @param message
     * @param i
     * @param port
     * @return
     */
    public static DatagramPacket finalPacket(byte[] message, InetAddress i, int port) {

        byte[] returnData = ByteBuffer.allocate(UDPConst.SIZE_OF_PACKET)
                .putShort(UDPConst.VALUE)
                .putShort(UDPConst.VALUE)
                .put(message)
                .array();

        return new DatagramPacket(returnData, returnData.length, i, port);
    }

}

