package pamvotis.core;

import java.util.Vector;

import pamvotis.exceptions.ElementDoesNotExistException;
import pamvotis.exceptions.ElementExistsException;
import pamvotis.sources.Source;

/**
 * Simulates a mobile node
 * @author Dimitris El. Vassis - Vassilis Zafeiris
 */
public class MobileNode{
	
	/**
	 * Instantiates a new node and creates a packet buffer on the node. The capacity of the buffer
	 * is the MAX_PKT_QUEUE_LNGTH parameter, specified in the specParams.xml configuration file.
	 */
	MobileNode(){
		_pktBuffer = new PacketBuffer(SpecParams.MAX_PKT_QUEUE_LNGTH);
	}
		
	int backoffCounter=0;	//The backoff counter.

	long collisions=0;	//The total number of collisions a station had.
			//Devided with the number of successful transmissions
			// gives the mean number of retransmission attempts.
	
	long totCollisions=0; //The total number of collisions in the whole simulation.
			//Used for calculating the mean values.
	
	int pktLength=0;	//The length of each packet.
	
	boolean requestTransmit=false; //Represents if a station wants to transmit.
			//Used for finding the number of nodes that want to
			//transmit in a specific slot.
	
	long successfulBits=0;	//The number of successfully transmitted bits.
			//Devided with the total time gives the throughput.
	long totSuccessfulBits=0; //The total number of successful bits in the whole simulation.
			//Used for calculating the mean values.
	
	long successfulTransmissions=0; //The number of successfully
			//transmitted packets.
	
	long totSuccessfulTransmissions=0; //The total succcessful transmisions in the whole simulation.
	//Used for calculating the mean values.
	
	static long timer=0;//The timer. It has the same value for all stations.
			//It is the currentSlot value.
	
	int transmissionDuration=0; //The duration from the first 
			//transmission attempt of a packet until the end of
			//the successful transmission. This variable
			//holds the sum of all transmission durations.
			//At the end it will be devided with the number of
			//transmissions for finding the mean media access delay.
	
	long totTransmissionDurations=0; //The total duration of transmissions in the whole simulation.
	//Used for calculating the mean values. 
	
	long transmissionStart=0; //The starting time of a transmission

	boolean successfullyTransmitting=false; //A flag which shows
			//if a transmission is successful or collision.
	
	int contWind=0;	//The contention window. We initialize it on Simulator class because we don't
	//know the node's AC yet.
	
	boolean havePktToSend= false; // Flag which indicates if a station has a
			//packet in the transmitter.
	
	long queuingDelay=0; //The delay of a packet from the start of
			//its birth until its first transmission attempt.
			//This variable holds the sum of all queuing delays.
			//At the end it will be devided with the number of
			//transmissions for finding the mean queuing delay.
	
	long totQueuingDelay=0; //The total sum of queuing delays in the whole simulation.
			//Used for calculating the mean values.
	
	long queueLength=0; //The sum of the packet queue size in each slot. Used for calculating
		//the average queue length with the following formula: If, in each slot the queue size
		//is e.g. q1, q2, q3... then the average size is (q1+q2+q3)/3. So at the end we devide
		//the sum of all queues with the number of slots.
	long totQueueLength=0; //The sum of queue sizes in the whole simulation. Used for calculating the mean values.
	
	Vector<Packet> pktQueue = new Vector<Packet>(); //The vector that simulates the packet queue
	
	public static java.util.Random generator;
			
	long lastPktTrans=0;	//Indicates the time the last packet was
			//transmitted. Used in the  for the TakePacketFromQueue
			//routine for the implementation of the backoff procedure.

	Params params = new Params(); //The data rate, packet generation parameters, 
			// coordinates and coverage of a node.

	boolean startTransmitting=false;	//Shows if a station initiated a transmission.
			//Used only in the Collision procedure.

	boolean nowTransmitting=false;	//Shows if a station transmits in the
		//current slot. Used globally.

	long jitter=0;	//Variable which holds the sum of the square of
		//the total delay of each packet. At the end it will be
		//devided with the mean total delay to find the delay jitter.
	
	long totJitter=0; //Holds the sum of all jitter (see above) values in the whole simulation.
	//Used for calculating the mean values.
		

	//Initializes the backoff inter. pCw is the contention window value.
	int InitBackoff(int pCw)
	{
		//We consider cw_min as 16, 32 64 etc, meaning that the backoff counter is between 0 and 15,31,63 e.t.c
		return generator.nextInt(pCw);
	}
	
	/*
	 * Manages all packet generation sources of a node. Can be used instead of
	 * source if multiple sources need to generate packets.
	 */
	SourceManager _srcManager = new SourceManager(this);

	
	PacketBuffer _pktBuffer = null; //The packet buffer of a node
	
	
	/**
	 * Returns a source identified by its Id. The Id of a source is the element number at the source manager
	 * @param sourceId The Id of the source
	 * @return The source the id of which is 'sourceId'.
	 * @throws ElementDoesNotExistException 
	 * @see SourceManager#getSource(int)
	 */
	public Source getSource(int sourceId) throws ElementDoesNotExistException {
		return _srcManager.getSource(sourceId);
	}

	/**
	 * Adds a new source to the source manager
	 * @param s The source to be added
	 * @throws ElementExistsException 
	 * @see SourceManager#addSource(Source)
	 */
	public void addSource(Source s) throws ElementExistsException {
		_srcManager.addSource(s);
	}
	
	/**
	 * Removes a source from the source manager
	 * @param sourceId The Id of the source to be removed.
	 * @throws ElementDoesNotExistException 
	 * @see SourceManager#removeSource(int)
	 */
	public void removeSource(int sourceId) throws ElementDoesNotExistException{		
			_srcManager.removeSource(sourceId);
	}


	/**
	 * Calls the corresponding function of the source manager to poll one packet from each source
	 * @return A vector with the packets that are polled from the sources.
	 * @see SourceManager#pollPacketsFromSources()
	 */
	public Vector<Packet> pollPacketsFromSources() {
		return _srcManager.pollPacketsFromSources();
	}

	/**
	 * Gets the packet buffer of the node.
	 * @return The packet buffer of the node.
	 * @see PacketBuffer
	 */
	public PacketBuffer getPacketBuffer() {
		return _pktBuffer;
	}
	
	/**
	 * Removes all the sources of a node.
	 * @see SourceManager#clear()
	 */
	public void removeAllSources(){
		_srcManager.clear();
	}
}
