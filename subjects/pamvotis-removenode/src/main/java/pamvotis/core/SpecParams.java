package pamvotis.core;
import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * This class reads the Standard's parameters from the NtConf.xml file and stores them to the static variables.
 * Other classes that need these parameters can simply access the variables.
 * @author Dimitris El. Vassis
 */
public class SpecParams {

	/**
	 * The time slot value in sec for pure 802.11a/g networks
	 */
	public static float SLOT_ERP;
	/**
	 * The time slot value in sec for 802.11/802.11b networks or mixed 802.11g/802.11b networks
	 */
	public static float SLOT_NON_ERP;
	/**
	 * The SIFS duration in sec for 802.11a networks
	 */
	public static float SIFS_A;
	/**
	 * The SIFS duration for 802.11b/g networks.
	 */
	public static float SIFS_G;
	/**
	 * The long physical layer duration in sec. Used for simple 802.11 networks.
	 */
	public static float LONG_PHY;
	/**
	 * The short physical layer duration in sec. Used for 802.11b networks.
	 */
	public static float SHORT_PHY;
	/**
	 * The OFDM physical layer duration in sec. Used for 802.11a/g networks.
	 */
	public static float OFDM_PHY;
	/**
	 * The value of the minimum contention window for DSSS (802.11b networks)
	 */
	public static int CW_MIN_DSSS;
	/**
	 * The value of the minimum contention window for OFDM (802.11a/g networks)
	 */
	public static int CW_MIN_OFDM;
	/**
	 * The value of the maximum contention window
	 */
	public static int CW_MAX;
	/**
	 * The length of the ACK in bits.
	 */
	public static int ACK;
	/**
	 * The length of the RTS in bits.
	 */
	public static int RTS;
	/**
	 * The length of the CTS in bits.
	 */
	public static int CTS;
	/**
	 * The length of the MAC header in bits.
	 */
	public static int MAC;
	/**
	 * The maximum packet (MSDU) size in bits.
	 */
	public static int MAX_PKT;
	/**
	 * Th maximum packet buffer size in bits.
	 */
	public static int MAX_PKT_QUEUE_LNGTH;
	
	//
	/**
	 * Reads the parameters of the SpecParams.xml file and stores them to the static members.
	 */
	public static void ReadParameters(){
		
		String str=null;
		
		//Open the file
		try {
			File file = new File("config"+File.separator+"SpecParams.xml");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document doc;
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file);
			NodeList ndlst;
			Element elmnt;
			
			//Read each element seperately
			ndlst = doc.getElementsByTagName("SLOT_ERP");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			SLOT_ERP = Float.valueOf(str).floatValue();
			
			ndlst = doc.getElementsByTagName("SLOT_NON_ERP");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			SLOT_NON_ERP = Float.valueOf(str).floatValue();

			ndlst = doc.getElementsByTagName("SIFS_A");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			SIFS_A = Float.valueOf(str).floatValue();
			
			ndlst = doc.getElementsByTagName("SIFS_G");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			SIFS_G = Float.valueOf(str).floatValue();
			
			ndlst = doc.getElementsByTagName("LONG_PHY");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			LONG_PHY = Float.valueOf(str).floatValue();
			
			ndlst = doc.getElementsByTagName("SHORT_PHY");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			SHORT_PHY = Float.valueOf(str).floatValue();
			
			ndlst = doc.getElementsByTagName("OFDM_PHY");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			OFDM_PHY = Float.valueOf(str).floatValue();
			
			ndlst = doc.getElementsByTagName("CW_MIN_DSSS");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			CW_MIN_DSSS = Integer.parseInt(str);
			
			ndlst = doc.getElementsByTagName("CW_MIN_OFDM");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			CW_MIN_OFDM = Integer.parseInt(str);
		
			ndlst = doc.getElementsByTagName("CW_MAX");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			CW_MAX = Integer.parseInt(str);
			
			ndlst = doc.getElementsByTagName("ACK");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			ACK = Integer.parseInt(str);
			
			ndlst = doc.getElementsByTagName("RTS");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			RTS = Integer.parseInt(str);
			
			ndlst = doc.getElementsByTagName("CTS");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			CTS = Integer.parseInt(str);
			
			ndlst = doc.getElementsByTagName("MAC");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			MAC = Integer.parseInt(str);
			
			ndlst = doc.getElementsByTagName("MAX_PKT");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			MAX_PKT = Integer.parseInt(str);
			
			ndlst = doc.getElementsByTagName("MAX_PKT_QUEUE_LNGTH");
			elmnt = (Element) ndlst.item(0);
			ndlst = elmnt.getChildNodes();
			str = ((Node) ndlst.item(0)).getNodeValue();
			MAX_PKT_QUEUE_LNGTH = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (DOMException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
