package pamvotis.sources;

import pamvotis.core.VirtualPacket;


/**
 * This class represents an FTP source according 3GPP TR 25.892 V6.0.0.
 * @author Dimitris El. Vassis
 */
public class FTPSource extends Source {

	float _ftpFSizeMean=2000000*8; //Ftp traffic parameters
	float _ftpFSizeStdDev=722000*8; //Ftp traffic parameters
	float _ftpFSizeMax=5000000*8; //Ftp traffic parameters
	float _ftpReadingTime=180; //Ftp traffic parameters
	int _pktSize = 8000;
	int generatedRTime=0; //The reading time (off period) between the current and the next session
	int lastPacketID = 0; //The ID of the last packet of the session
	

	/**
	 * Instantiates an FTP source as described in 3GPP TR 25.892 V6.0.0.
	 * @param sourceId The ID of the source
	 * @param pktSize The packet size in bits.
	 * @param ftpFSizeMean The mean file size in Bytes.
	 * @param ftpFSizeStdDev The standard deviation of the file size In bytesi
	 * @param ftpFSizeMax The maximum file size in Bytes.
	 * @param ftpReadingTime The reading time in seconds.
	 */
	public FTPSource(int sourceId, int pktSize, float ftpFSizeMean, float ftpFSizeStdDev, float ftpFSizeMax,
			float ftpReadingTime){
		super(); //Calls the constructor of Source to initialize the source ID
		_sourceId = sourceId;
		_pktSize = pktSize;
		_ftpFSizeMean = ftpFSizeMean * 8;
		_ftpFSizeStdDev = ftpFSizeStdDev * 8;
		_ftpFSizeMax = 	ftpFSizeMax * 8;
		_ftpReadingTime = (int) (((double) ftpReadingTime) / slot);
		getNextPacket();
	}
	
	/**
	 * Creates an FTP sesion.
	 */
	private void createSession() {
		RandomGenerator random = RandomGenerator.getInstance();
		//If the user changed the default packet size (1500Bytes) set a flag. See start of the for loop.
		boolean packetSizeChanged=false; if(_pktSize!=12000) packetSizeChanged=true;
		// set file size
		int file = (int) random.genRv(RandomGenerator.LOGNORMAL_DISTR, _ftpFSizeMean, _ftpFSizeStdDev,_ftpFSizeMax,-1);
		// set reading time
		generatedRTime = (int) random.genRv(RandomGenerator.EXPONENTIAL_DISTR, _ftpReadingTime);
		// Calculate the packets to be transmitted
		//According to 3GPP, 74% of packets use 1500Bytes MSDU while 24% use 576Butes MSDU.
		//If the user did not change the default packet size, set the packet size randomly, according to the above. 
		int pktSize=0; int pktSizeRand = 0; int pkts = 0;
		if(packetSizeChanged) pkts = (int) (file / _pktSize);
		else pkts = (int) (file/(0.24*1500*8+0.76*576*8));

		// Add the packets to the session queue and mark their ids.
		for (int i = 0; i < pkts; i++) {
			if(packetSizeChanged) pktSize = _pktSize;
			else{
				pktSizeRand = (int) RandomGenerator.generator.nextFloat();
				if(pktSizeRand>0.24f) pktSize = 1500*8;
				else pktSize = 576*8;
			}
			session.addElement(new VirtualPacket(pktSize, 1, i + 1));
		}
		// Mark the id of the last packet to be transmitted. When this will
		// be transmitted, off session will be generated
		lastPacketID = pkts;
		// This is a dummy packet.
		// Ihterarrival is a very long dummy interval. Will be changed by
		// the setReadingTime
		// ID is -1 in order to check it and not add it to the packet queue
		// for transmission
		session.addElement(new VirtualPacket(1, generatedRTime, -1));
	}


	/**
	 * If a session is empty, a new one is created. Then the first packet of the session is picked up
	 * in order to be born when its interarrival counter expires.
	 */
	public void getNextPacket() {		
		if (session.isEmpty()){
			createSession();
		}
		interArTime = session.firstElement().interarrival;
		pktLength = session.firstElement().length;
		packetToBeTransmittedID = session.firstElement().id;
		session.removeElementAt(0);
	}
	
	
	/**
	 * This function is not needed for the case of FTP.
	 */
	public void synchronize(){}
}
