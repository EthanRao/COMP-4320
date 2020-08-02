/**
 * Project 02
 * @author Xi Rao
 * @author Qiang Wu
 * @author Zikai Zhang
 */

public class EventTime {

	double expirationTime;
	int eventID;


	/**
	 * @param event_ID sequence ID
	 * @param expiration_Time in ms
	 */
	public EventTime(int event_ID, double expiration_Time){
		expirationTime = expiration_Time;
		eventID = event_ID;
	}

	/**
	 * @return
	 */
	public double getExpirationTime() {
		return expirationTime;
	}

	/**
	 * @param expiration_Time
	 */
	public void setExpirationTime(double expiration_Time) {
		expirationTime = expiration_Time;
	}

	public void decrementExpirationTime(){
	expirationTime--;	
	}
	
	/**
	 * @return
	 */
	public int getEventID() {
		return eventID;
	}

	/**
	 * @param event_ID
	 */
	public void setEventID(int event_ID) {
		eventID = event_ID;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return "Event ID: "+eventID + "\t"+"Event has: " +expirationTime;
	}
}
