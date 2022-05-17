package pamvotis.core;

import java.util.Vector;

import pamvotis.exceptions.ElementDoesNotExistException;
import pamvotis.exceptions.ElementExistsException;
import pamvotis.sources.Source;


/**
 * Manages the sources of a node. Polls packets from every source and returns them
 * in a vector of packets through the pollPacketsFromSources function.
 * @author Vassilis Zafeiris
 */
public class SourceManager {

	MobileNode _node = null;
	Vector _vActiveSources = new Vector();
	long _lastTimerValue = 0;

	public SourceManager(MobileNode node) {
		_node = node;
	}

	/**
	 * Polls packets from all sources. The interarrival timer of each source
	 * is decreased by  1
	 * 
	 * @return a vector of packets polled from sources
	 */
	public Vector pollPacketsFromSources() {

		Vector newPackets = new Vector();
		Packet tmpPkt = null;
		// Take packets from each available source
		for(int i=0;i<_vActiveSources.size();i++){
			tmpPkt = takePacketFromSource(((Source)_vActiveSources.elementAt(i)), 1);
			if (tmpPkt != null) {
				newPackets.add(tmpPkt);
			}
		}

		return newPackets;
	}

	/**
	 * Add a source to the node's source manager.
	 * 
	 * @param s
	 *            The source instance.
	 * @throws ElementExistsException 
	 */
	public void addSource(Source s) throws ElementExistsException {
		boolean sourceExists=false;
		for(int i=0;i<_vActiveSources.size();i++){
			if(((Source)_vActiveSources.elementAt(i))._sourceId==s._sourceId){
				sourceExists=true;
				break;
			}
		}
		if(sourceExists) throw new ElementExistsException("Source "+s._sourceId+" already exists.");
		else _vActiveSources.add(s);
	}

	/**
	 * Decreases the interarrival timer of a source and creates a new packet if
	 * needed
	 * 
	 * @return A newly generated packet, null otherwise.
	 * @see pamvotis.sources.Source#getNextPacket()
	 */
	private Packet takePacketFromSource(Source s, long elapsedTime) {

		//Decrease the time remaining for the packet to be transmitted
		s.interArTime -= elapsedTime;
		Packet p = null;
		if (s.interArTime <= 0) {
			if (s.packetToBeTransmittedID != -1) {
				p = new Packet();
				p.length = s.pktLength;
				p.generationTime = MobileNode.timer;
				p.id = s.packetToBeTransmittedID;
			}
			s.getNextPacket();
		}
		return p;
	}


	/**
	 * Removes a source with the given session id. 
	 * @param sourceId
	 * @return true if the source exists or false if it doesn't
	 * @throws ElementDoesNotExistException 
	 */
	public boolean removeSource(int sourceId) throws ElementDoesNotExistException{
		int position=-1;
		for(int i=0;i<_vActiveSources.size(); i++){
			if(((Source)_vActiveSources.elementAt(i))._sourceId==sourceId){
				position = i;
				break;
			}
		}
		if(position!=-1){
			_vActiveSources.removeElementAt(position);
			return true;
		}
		else throw new ElementDoesNotExistException("Source "+sourceId+" does not exist.");
	}
	
	/**
	 * Finds a node's active source whose ID is 'id'.
	 * @param id The ID of the source.
	 * @return The source the id of which is 'id'.
	 * @throws ElementDoesNotExistException 
	 */
	public Source getSource(int id) throws ElementDoesNotExistException {
		Source s=null;		
		for(int i=0;i<_vActiveSources.size(); i++){
			if(((Source)_vActiveSources.elementAt(i))._sourceId==id){
				s=((Source)_vActiveSources.elementAt(i));
				break;
			}
		}
		if(s==null) throw new ElementDoesNotExistException("Source"+id+"does not exist.");
		return s;
	}
	
	/**
	 * Removes all active sources.
	 *
	 */
	public void clear(){
		_vActiveSources.clear();
	}
}
