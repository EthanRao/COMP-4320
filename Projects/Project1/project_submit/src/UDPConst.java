public class UDPConst {

    /**
     * Project 01
     * @author Xi Rao
     * @author Qiang Wu
     * @author Zikai Zhang
     */

    /**
     * as mentioned in pdf, 256-byte packet
     */

    public static final int SIZE_OF_PACKET = 256;

    /**
     * packet header
     * includes segment_number and check_sum and may include dest and source port
     */

    public static final int SIZE_OF_HEADER = 4;

    /**
     * actual data size in packet
     */

    public static final int SIZE_OF_DATA = SIZE_OF_PACKET - SIZE_OF_HEADER;

    /**
     * null character as the end of file
     */

    public static final String NULL_CHARACTER = "\0";

    /**
     * indicate special case
     */

    public static final short VALUE = -1;

    public static final double PROB1 = 0.5;

    public static final double PROB2 = 0.8;

    public static final String ZERO = "0.0";

}
