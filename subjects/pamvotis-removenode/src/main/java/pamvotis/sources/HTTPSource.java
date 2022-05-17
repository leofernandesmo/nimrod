package pamvotis.sources;

import pamvotis.core.VirtualPacket;

/**
 * This class represents an HTTP source according to 3GPP TR 25.892 V6.0.0.
 * @author Dimitris El. Vassis
 *
 */
public class HTTPSource extends Source{
	int _pktSize = 12000;
	float _mainObjectMean = 10710*8;
	float _mainObjectStDev = 25032*8;
	float _mainObjectMin = 100*8;
	float _mainObjectMax = 2000000*8;
	float _embObjectMean = 7758*8;
	float _embObjectStDev = 126168*8;
	float _embObjectMin = 50*8;
	float _embObjectMax = 2000000*8;
	float _NumOfEmbObjectsMean = 5.64f;
	float _NumOfEmbObjectsMax = 53;
	float _readingTime = 30f/slot;
	float _parsingTime = 0.13f/slot;
	int id=0;
	
	
	/**
	 * Creates an HTTP (WWW) source according to 3GPP TR 25.892 V6.0.0.
	 * @param sourceId The ID of the new source
	 * @param pktSize The packet size mean value in bits.
	 * @param mainObjectMean The mean size of the main object in Bytes.
	 * @param mainObjectStDev The standard deviation of the size of the main object in Bytes.
	 * @param mainObjectMin The minimum size of the main object in Bytes.
	 * @param mainObjectMax The maximum size of the main object in Bytes.
	 * @param embObjectMean The mean size of an embedded object in Bytes.
	 * @param embObjectStDev The standard deviation of the size of an embedded object in Bytes.
	 * @param embObjectMin The minimum size of an embedded object in Bytes.
	 * @param embObjectMax The maximum size of an embedded object in Bytes.
	 * @param NumOfEmbObjectsMean The mean value of the number of embedded objects.
	 * @param NumOfEmbObjectsMax The maximum number of embedded objects.
	 * @param readingTime The mean value of the reading time in seconds. Can be float as well.
	 * @param parsingTime The mean value of the parsing time in seconds. Can be float as well.
	 */
	public HTTPSource(int sourceId,int pktSize,float mainObjectMean,float mainObjectStDev,float mainObjectMin,float mainObjectMax,
			float embObjectMean,float embObjectStDev,float embObjectMin,float embObjectMax,float NumOfEmbObjectsMean,
			float NumOfEmbObjectsMax,float readingTime,float parsingTime){
		
		super();
		_sourceId = sourceId;
		_pktSize = pktSize;
		_mainObjectMean = mainObjectMean*8;
		_mainObjectStDev = mainObjectStDev*8;
		_mainObjectMin = mainObjectMin*8;
		_mainObjectMax = mainObjectMax*8;
		_embObjectMean = embObjectMean*8;
		_embObjectStDev = embObjectStDev*8;
		_embObjectMin = embObjectMin*8;
		_embObjectMax = embObjectMax*8;
		_NumOfEmbObjectsMean = NumOfEmbObjectsMean;
		_NumOfEmbObjectsMax = NumOfEmbObjectsMax;
		_readingTime = readingTime/slot;
		_parsingTime = parsingTime/slot;
		getNextPacket();
	}
	
	/**
	 * Creates a new HTTP session
	 */
	private void createSession(){
		RandomGenerator random = RandomGenerator.getInstance();
		id=0; //The counter that marks the id of each packet
		//Generate the number of embedded objects
		int nd = (int) random.genRv(RandomGenerator.PARETO_DISTR,_NumOfEmbObjectsMean,_NumOfEmbObjectsMax,-1);
		
		//Generate a main object file and add it to the session 
		addFile('m');
		
		//Add a dummy packet to simulate the parsing time
		int pTime = (int)random.genRv(RandomGenerator.EXPONENTIAL_DISTR,_parsingTime,Float.MAX_VALUE,1);
		session.addElement(new VirtualPacket(_pktSize, pTime, -1));
		
		//generate nd embedded object files and add them to the session
		for(int i=0;i<nd;i++){
			addFile('e');
		}
		//Add a dummy packet that simulates the reading time
		int rTime = (int)random.genRv(RandomGenerator.EXPONENTIAL_DISTR,_readingTime,Float.MAX_VALUE,1);
		session.addElement(new VirtualPacket(_pktSize, rTime, -1));
	}
	
	/**
	 * Helper function that generates a file, splits it to packets and adds it to the session
	 */
	private void addFile(char type){
		RandomGenerator random = RandomGenerator.getInstance();
		int file=0;
		//If it is a main object...
		if(type=='m') file = (int) random.genRv(RandomGenerator.LOGNORMAL_DISTR,_mainObjectMean,_mainObjectStDev,_mainObjectMax,_mainObjectMin);
		//If it is an embedded object...
		else file = (int) random.genRv(RandomGenerator.LOGNORMAL_DISTR,_embObjectMean,_embObjectStDev,_embObjectMax,_embObjectMin);
		
		//If the user changed the default packet size (1500Bytes) set a flag. See start of the for loop.
		boolean packetSizeChanged=false; if(_pktSize!=12000) packetSizeChanged=true;
		
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
			session.addElement(new VirtualPacket(pktSize, 1, ++id));
		}
	}
	
	/**
	 * If a session is empty, a new one is created. Then the first packet of the session is picked up and
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
	 * This function is not needed for the case of HTTP.
	 */
	public void synchronize(){}

}
