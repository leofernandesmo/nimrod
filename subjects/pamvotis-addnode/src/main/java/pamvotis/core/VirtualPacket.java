package pamvotis.core;

/**
 * Simulates a packet generated from a session. Be careful, this packet is not yet generated. It is just a prediction
 * of what the source will generate. More specifically, when we want to generate a session of packets, then a
 * row of virtual packets is generated, each one having a field called interarrival. When this time expires, then the
 * real packet is generated and added to the packet queue through the Source.getNextPacket fuction.
 * @see pamvotis.sources.Source#getNextPacket()
 * @author Dimitris El. Vassis
 */
public class VirtualPacket {
	/**
	 * The packet length in bits.
	 */
	public int length;
	/**
	 * The interarrival time of a packet. When this time expires, the real packet will be generated.
	 */
	public int interarrival;
	/**
	 * The ID of the packet that will be generated.
	 */
	public int id;
	
	/**
	 * Creates a virtual packet.
	 * @param length The packet length in bits.
	 * @param interarrival The interarrival time of a packet. When this time expires, the real packet will be generated.
	 * @param id The ID of the packet that will be generated.
	 */
	public VirtualPacket(int length, int interarrival, int id){
		this.length=length;
		this.interarrival = interarrival;
		this.id=id;
	}
}
