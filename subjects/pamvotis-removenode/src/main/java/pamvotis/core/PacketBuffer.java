package pamvotis.core;

import java.util.Vector;

/**
 * Simulates the hardware packet buffer of a noder
 * @author Vassilis Zafeiris
 * 
 */
public class PacketBuffer {

	int _maxSize = 0; // Buffer capacity in packets. Will be defined in the
						// constructor

	Vector<Packet> _vPacketQueue = null;

	/**
	 * Creates a packet buffer, which is actually called _vPacketQueue
	 * 
	 * @param size The buffer capacity in packets.
	 */
	public PacketBuffer(int size) {

		_maxSize = size;
		_vPacketQueue = new Vector<Packet>(_maxSize);
	}

	/**
	 * Gets all the packets that the source manager polled from each source and
	 * adds them to the packet buffer. The source manager, at each slot, it
	 * polls one packet from each source
	 * 
	 * @param newPackets A vector that contains all the packets polled from sources
	 */
	public void enqueue(Vector<Packet> newPackets) {
		for (Packet p : newPackets) {
			if (_vPacketQueue.size() < _maxSize)
				_vPacketQueue.add(p);
			else
				break;
		}
	}

	/**
	 * @return The first element (packet) of the packet buffer
	 * @see Packet
	 */
	public Packet firstPacket() {
		return _vPacketQueue.firstElement();
	}

	/**
	 * Removes the first element of the packet buffer.
	 */
	public void dequeue() {
		_vPacketQueue.removeElementAt(0);
	}

	/**
	 * Checks if a packet buffer is empty.
	 * 
	 * @return True if it is empty or false if not.
	 */
	public boolean isEmpty() {
		return _vPacketQueue.isEmpty();
	}

	/**
	 * @return The size of the packet buffer.
	 */
	public int size() {
		return _vPacketQueue.size();
	}
}