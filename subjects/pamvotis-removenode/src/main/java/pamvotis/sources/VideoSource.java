package pamvotis.sources;

import pamvotis.core.VirtualPacket;

/**
 * Represents a source generating video traffic according to 3GPP TR 25.892
 * V6.0.0
 * 
 * @author Dimitris El. Vassis
 */
public class VideoSource extends Source {
	int _frameIntArr = 20;
	int _packetsPerFrame = 8;
	float _pktSize = 50 * 8;
	float _pktSizeMax = 125 * 8;
	int _pktIntArr = (int) (0.006 / slot);
	int _pktIntArrMax = (int) (0.125 / slot);
	int intArrCounter = 0;
	static long timer;
	long _lastFrameCreation = 0;

	/**
	 * Instantiates a video source according to specified parameters. See 3GPP
	 * TR 25.892 V6.0.0 for more details.
	 * 
	 * @param sourceId
	 *            The ID of the source
	 * @param frameRate
	 *            The video frame rate in frames/sec
	 * @param packetsPerFrame
	 *            The packets of a frame
	 * @param pktSize
	 *            The averaga size of each packet in bits
	 * @param pktSizeMax
	 *            The maximum size of each packet in bits
	 * @param pktIntArr
	 *            The average packet interarrival time in seconds (can be float
	 *            as well)
	 * @param pktIntArrMax
	 *            The maximum packet interarival interval in seconds (can be
	 *            float as well)
	 */
	public VideoSource(int sourceId, int frameRate, int packetsPerFrame,
			float pktSize, float pktSizeMax, float pktIntArr, float pktIntArrMax) {
		super(); // Call superclass constructor to assign an ID to pktSize
					// source
		_sourceId = sourceId;
		_frameIntArr = (int) (1f / frameRate / slot);
		_packetsPerFrame = packetsPerFrame;
		_pktSize = pktSize;
		_pktSizeMax = pktSizeMax; // Note: At the moment, this parameter is not
									// used, as pareto distrubution
		// is adopted, instead of truncated pareto recommended by 3GPP.
		_pktIntArr = (int) (pktIntArr / slot);
		_pktIntArrMax = (int) (pktIntArrMax / slot); // Note: At the moment,
														// this parameter is not
														// used, as pareto
														// distrubution
		// is adopted, instead of truncated pareto recommended by 3GPP.
		intArrCounter = _frameIntArr;
		_lastFrameCreation = Source.timer;
		createSession();
	}

	/**
	 * Creates a video session, according to the parameters of the constructor
	 */
	private void createSession() {
		RandomGenerator random = RandomGenerator.getInstance();
		for (int i = 0; i < _packetsPerFrame; i++) {
			session.addElement(new VirtualPacket((int) random.genRv(RandomGenerator.PARETO_DISTR, _pktSize,
					_pktSizeMax, -1), (int) random.genRv(RandomGenerator.PARETO_DISTR, _pktIntArr,
					_pktIntArrMax), i + 1));
		}
		intArrCounter = _frameIntArr;
	}

	/**
	 * If the session is not empty, it takes the first packet of the session in
	 * order to be born when its interarrival timer expires. If the session is
	 * empty, it just adds dummy packets, that actually represent an off
	 * interval.
	 */
	public void getNextPacket() {
		if (!session.isEmpty()) {
			interArTime = session.firstElement().interarrival;
			pktLength = session.firstElement().length;
			packetToBeTransmittedID = session.firstElement().id;
			session.removeElementAt(0);
		} else {
			interArTime = 1;
			pktLength = 1;
			packetToBeTransmittedID = -1;
		}
	}

	/**
	 * When the frame interarrival timer expires, a new session is created,
	 * which is actually a new frame.
	 */
	public void synchronize() {
		if (Source.timer - _lastFrameCreation >= _frameIntArr) {
			_lastFrameCreation = Source.timer;
			createSession();
		}
	}
}
