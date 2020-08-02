import java.net.DatagramPacket;
import java.util.Random;

/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */


public class Gremlin {
	Random rand;
	double mLossPr;
	double mCorruptPr;
	double mPassPr;
	int numCorrupt = 0;
	int numLoss = 0;
	int numPass = 0;
	DatagramPacket packet;

	/**
	 * @param lostP can not exceed 1
	 * @param damP can not exceed 1
	 */
	public Gremlin(double lostP, double damP){
		rand = new Random();
		mLossPr = lostP;
		mCorruptPr = damP;
		mPassPr = 1 - (lostP + damP);
	}

	/**
	 * @param datagram
	 */
	private void loadDatagramPacket(DatagramPacket datagram){
		packet = datagram;
	}

	/**
	 * @return
	 */
	public DatagramPacket getDatagramPacket(){
		return packet;
	}

	/**
	 * @return DatagramPacket with the corrupted data
	 */
	private DatagramPacket packetDamaged(){
		double sample = rand.nextDouble();
		byte[] data = getDatagramPacket().getData();
        int changeNum = 0;
		
		if(sample <=.5){
            changeNum = 1;
		} else if (sample <= .8) {
            changeNum = 2;
        } else {
            changeNum = 3;
        }

        for(int i = 0; i<changeNum; i++){
			int randomIndex = rand.nextInt(getDatagramPacket().getData().length);
            data[randomIndex]=(byte)~data[randomIndex];
        }

		getDatagramPacket().setData(data);
		return getDatagramPacket();
	}

	/**
	 * @return
	 */
	public DatagramPacket packetLost(){
		return null;
	}

	/**
	 * @return
	 */
	public DatagramPacket packetValid(){
		return packet;
	}

	/**
	 * @param datagram Uncorrupted Datagram packet
	 * @return DatagramPacket of the corrupted Data in the Datagram packet
	 */
	public DatagramPacket filter(DatagramPacket datagram){
		loadDatagramPacket(datagram);
		double sample = rand.nextDouble();
		if(sample <= getmPassPr()){
			numPass++;
			return packetValid();
		} else if( sample <= getmPassPr() + getmCorruptPr()){
			numCorrupt++;
			return packetDamaged();
		} else{
			numLoss++;
			return packetLost();
		}
	}

	/**
	 * @return
	 */
	public double getmLossPr() {
		return mLossPr;
	}

	/**
	 * @param mLossPr
	 */
	public void setmLossPr(double mLossPr) {
		this.mLossPr = mLossPr;
	}

	/**
	 * @return
	 */
	public double getmCorruptPr() {
				
		return mCorruptPr;
	}

	/**
	 * @param mCorruptPr
	 */
	public void setmCorruptPr(double mCorruptPr) {
		this.mCorruptPr = mCorruptPr;
	}

	/**
	 * @return
	 */
	public double getmPassPr() {
		return mPassPr;
	}

	/**
	 * @param mPassPr
	 */
	public void setmPassPr(double mPassPr) {
		this.mPassPr = mPassPr;
	}

}
