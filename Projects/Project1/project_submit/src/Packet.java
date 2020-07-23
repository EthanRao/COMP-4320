import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

    /**
    * Project 01
    * @author Xi Rao
    * @author Qiang Wu
    * @author Zikai Zhang
    */

    /**
    * Packet Class
    */

public class Packet {

    /**
     * header segment_number and check_sum
     */

    private ArrayList<Short> header;

    /**
     * data byte array
     */

    private byte[] data;

    public Packet() {

        header = new ArrayList<>();

        data = new byte[UDPConst.SIZE_OF_PACKET];

    }

    public ArrayList<Short> getHeader() {
        return header;
    }

    public void setHeader(ArrayList<Short> header) {
        this.header = header;
    }

    public void setHeaderData(CommonUtils.HeaderEnum headerEnum, Short value, ArrayList<Short> header) {

        Integer index = headerEnum.getIndex();

        header.set(index, value);

    }

    public Short getHeaderData(CommonUtils.HeaderEnum headerEnum, ArrayList<Short> header) {

        Integer index = headerEnum.getIndex();

        return header.get(index);

    }

    public void setData(byte[] toSetData) throws IllegalArgumentException {

        int dataSize = toSetData.length;
        if (dataSize > 0 && dataSize <= UDPConst.SIZE_OF_DATA) {
            data = new byte[dataSize];
            for (int i = 0; i < dataSize; ++i) {
                data[i] = toSetData[i];
            }
        } else {
            throw new IllegalArgumentException(
                    "Error, size = " + dataSize);
        }

    }

    public byte getData(int index) {

        if (index >= 0 && index < data.length) {
            return data[index];
        }

        throw new IllegalArgumentException(
                "Error, index = " + index);

    }

    public byte[] getData() {
        return data;
    }

    public int getDataSize() {
        int length = 0;
        for (byte b : data) {
            if (b != 0) {
                length++;
            }
        }
        return length;
    }


    public DatagramPacket getDatagramPacket(InetAddress i, int port) {

        byte[] returnData = ByteBuffer.allocate(UDPConst.SIZE_OF_PACKET)
                .putShort(getHeaderData(CommonUtils.HeaderEnum.SEGMENT_NUMBER, header))
                .putShort(getHeaderData(CommonUtils.HeaderEnum.CHECKSUM, header))
                .put(data)
                .array();

        return new DatagramPacket(returnData, returnData.length, i, port);
    }

    public static Packet restorePacket(DatagramPacket in) {

        Packet packet = new Packet();
        ByteBuffer byteBuffer = ByteBuffer.wrap(in.getData());
        ArrayList<Short> header = new ArrayList<>();
        header.add(byteBuffer.getShort());
        header.add(byteBuffer.getShort());
        packet.setHeader(header);

        byte[] data = in.getData();
        byte[] otherData = new byte[data.length - byteBuffer.position()];
        for (int i = 0; i < otherData.length; i++) {
            otherData[i] = data[i + byteBuffer.position()];
        }

        packet.setData(otherData);
        return packet;

    }
}

