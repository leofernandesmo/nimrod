package pamvotis.intf;
////////////////////////////////////////////////////////////////////////////////////////////////////////
//This class reads the parameters from the GUI or the network configuration file and makes an xml file
//which is used by the Simulator class.
////////////////////////////////////////////////////////////////////////////////////////////////////////

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

//import com.sun.org.apache.xerces.internal.dom.DocumentImpl;
//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
//import com.sun.org.apache.xml.internal.serialize.XMLSerializer;


public class MakeNtConf {
	
	//These variables are initialized with set methods from the GUI. Their values are written to the xml file
	private String seed = "69";
	private String duration="300";
	private String values = "100";
	private String nodes="10";
	private String pktLngth = "8000";
	private String pktDist = "c";
	private String intArrTime="8";
	private String intArrDstr = "c";
	private String rate = "1000000";
	private String phyLayer = "s";
	private String mixNodes = "0";
	private String rtsThr = "99999";
	private String ctsToSelf = "n";
	private String ntConfPath = "";
	private String resultsPath = new File(".").getAbsolutePath();
	private String outResults= "_";
	
	private String cwmin0="1";
	private String cwmin1="2";
	private String cwmin2="4";
	private String cwmin3="8";
	private String cwmax0="1";
	private String cwmax1="4";
	private String cwmax2="8";
	private String cwmax3="16";
	private String aifs0="2";
	private String aifs1="2";
	private String aifs2="1";
	private String aifs3="1";
	 
	 //The above four values are used to determine the physical layer and number of 802.11b nodes
	 //in mixed mode. They are used only if network configuration file is used.
	 private int sLayer = 0;
	 private int bLayer = 0;
	 private int aLayer = 0;
	 private int gLayer = 0;
	 private int nd = 0;
	 
	 //This method is called when configuration from GUI is choosed
	 public void configureGlobally(){
		 Element e = null;
		 Element s = null;
		Element tr=null;
		Node n = null;

		Document xmldoc= new DocumentImpl();

		Element root = xmldoc.createElement("scenario");
		e = xmldoc.createElementNS(null, "seed");
		n = (Node) xmldoc.createTextNode(seed);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "duration");
		n = (Node) xmldoc.createTextNode(duration);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "values");
		n = (Node) xmldoc.createTextNode(values);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		//For each node...
		for (int i=0;i<Integer.parseInt(nodes);i++)
		{
			e = xmldoc.createElementNS(null, "node");
			e.setAttributeNS(null, "number", Integer.toString(i+1));
			tr= xmldoc.createElementNS(null, "rate");
			n = (Node)xmldoc.createTextNode(rate);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			//Remember that if global configation is used, the coverage and position parameters have no mean.
			tr= xmldoc.createElementNS(null, "coverage");
			n = (Node)xmldoc.createTextNode("17");
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "xPosition");
			n = (Node)xmldoc.createTextNode("17");
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "yPosition");
			n = (Node)xmldoc.createTextNode("17");
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "AC");
			n = (Node)xmldoc.createTextNode("0");
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			//Create a default source. This is expected to change  in later versions.
			s = xmldoc.createElementNS(null, "source");
			s.setAttributeNS(null, "id", "1");
			s.setAttributeNS(null, "type", "generic");
			
			tr= xmldoc.createElementNS(null, "pktLngth");
			n = (Node)xmldoc.createTextNode(pktLngth);
			tr.appendChild((Node) n);
			s.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "pktDist");
			n = (Node)xmldoc.createTextNode(pktDist);
			tr.appendChild((Node) n);
			s.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "intArrTime");
			n = (Node)xmldoc.createTextNode(intArrTime);
			tr.appendChild((Node) n);
			s.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "intArrDstr");
			n = (Node)xmldoc.createTextNode(intArrDstr);
			tr.appendChild((Node) n);
			s.appendChild(tr);
			
			e.appendChild(s);
			
			root.appendChild(e);			
		}
		e = xmldoc.createElementNS(null, "nodes");
		n = (Node) xmldoc.createTextNode(nodes);
		e.appendChild((Node) n);
		root.appendChild(e);
		e = xmldoc.createElementNS(null, "mixNodes");
		n = (Node) xmldoc.createTextNode(mixNodes);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "phyLayer");
		n = (Node) xmldoc.createTextNode(phyLayer);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e= xmldoc.createElementNS(null, "RTSThr");
		n = (Node) xmldoc.createTextNode(rtsThr);
		e.appendChild((Node) n);
		root.appendChild(e);

		e= xmldoc.createElementNS(null, "ctsToSelf");
		n = (Node) xmldoc.createTextNode(ctsToSelf);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "EDCA");
		
		tr= xmldoc.createElementNS(null, "cwMinFact0");
		n = (Node)xmldoc.createTextNode(cwmin0);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMinFact1");
		n = (Node)xmldoc.createTextNode(cwmin1);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMinFact2");
		n = (Node)xmldoc.createTextNode(cwmin2);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMinFact3");
		n = (Node)xmldoc.createTextNode(cwmin3);
		tr.appendChild((Node) n);
		e.appendChild(tr);

		tr= xmldoc.createElementNS(null, "cwMaxFact0");
		n = (Node)xmldoc.createTextNode(cwmax0);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMaxFact1");
		n = (Node)xmldoc.createTextNode(cwmax1);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMaxFact2");
		n = (Node)xmldoc.createTextNode(cwmax2);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMaxFact3");
		n = (Node)xmldoc.createTextNode(cwmax3);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs0");
		n = (Node)xmldoc.createTextNode(aifs0);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs1");
		n = (Node)xmldoc.createTextNode(aifs1);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs2");
		n = (Node)xmldoc.createTextNode(aifs2);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs3");
		n = (Node)xmldoc.createTextNode(aifs3);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		root.appendChild(e);
		
		e= xmldoc.createElementNS(null, "resultsPath");
		n = (Node) xmldoc.createTextNode(resultsPath);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		//Out results is a string determining the results that the user choosed.
		e= xmldoc.createElementNS(null, "outResults");
		n = (Node) xmldoc.createTextNode(outResults);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		xmldoc.appendChild(root);
			
		try {
			
			//The xml configuration file is called NtConf.xml
			FileOutputStream fos = new FileOutputStream("config"+File.separator+"NtConf.xml");

			OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
			of.setIndent(1);
			of.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(fos,of);

			serializer.asDOMSerializer();
			serializer.serialize( xmldoc.getDocumentElement() );
			fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	 
	//Makes the xml configuration file according to an excel network configuration file (windows users) 
	public void configWithFile(){ 
		Element e = null;
		Element s = null;
		Element tr=null;
		Node n = null;

		Document xmldoc= new DocumentImpl();

		Element root = xmldoc.createElement("scenario");
		e = xmldoc.createElementNS(null, "seed");
		n = (Node) xmldoc.createTextNode(seed);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "duration");
		n = (Node) xmldoc.createTextNode(duration);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "values");
		n = (Node) xmldoc.createTextNode(values);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		Connection c = null;
		Statement stmnt = null;
		String str=null;
		
		try {
			Class.forName( "sun.jdbc.odbc.JdbcOdbcDriver" );
			c = DriverManager.getConnection("jdbc:odbc:Driver={Microsoft Excel Driver (*.xls)};DBQ=" + ntConfPath);
			stmnt = c.createStatement();
			String query = "Select * from [NetConf$]" ;
			ResultSet rs = stmnt.executeQuery( query );

			 while(rs.next()) {
				 str=rs.getString(1);
				 if(str!=null && str.length()!=0){
					e = xmldoc.createElementNS(null, "node");
					e.setAttributeNS(null, "number", Integer.toString(nd+1));

					tr= xmldoc.createElementNS(null, "rate");
					str = rs.getString(2);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					e.appendChild(tr);
					
					tr= xmldoc.createElementNS(null, "xPosition");
					str = rs.getString(7);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					e.appendChild(tr);
					
					tr= xmldoc.createElementNS(null, "yPosition");
					str = rs.getString(8);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					e.appendChild(tr);
					
					tr= xmldoc.createElementNS(null, "coverage");
					str = rs.getString(9);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					e.appendChild(tr);
					
					tr= xmldoc.createElementNS(null, "AC");					
					str=rs.getString(11);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					e.appendChild(tr);
					
					//Create a default source. This is expected to change  in later versions.
					s = xmldoc.createElementNS(null, "source");
					s.setAttributeNS(null, "id", "1");
					s.setAttributeNS(null, "type", "generic");
					
					tr= xmldoc.createElementNS(null, "pktLngth");
					str = rs.getString(3);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					s.appendChild(tr);
						
					tr= xmldoc.createElementNS(null, "pktDist");
					str = rs.getString(4);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					s.appendChild(tr);
						
					tr= xmldoc.createElementNS(null, "intArrTime");
					str = rs.getString(5);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					s.appendChild(tr);
						
					tr= xmldoc.createElementNS(null, "intArrDstr");
					str = rs.getString(6);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					n = (Node)xmldoc.createTextNode(str);
					tr.appendChild((Node) n);
					s.appendChild(tr);
					
					e.appendChild(s);
					root.appendChild(e);
						
					str = rs.getString(10);
					if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
					//According to the value of the physical layer increase a counter, in order to know
					//the number of b nodes in mixed mode.
					if(str.equals("s")) sLayer++;
					else if (str.equals("b")) sLayer++;
					else if (str.equals("g")) gLayer++;
					else if (str.equals("a")) aLayer++;
					else sLayer++;
					
					nd++; //nd is the number of nodes
				}
			}
		} catch (DOMException e2) {
			e2.printStackTrace();
		} catch (SQLException e2) {
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			e2.printStackTrace();
		}
		//According to the counters determine the physical layer. nd is the number of nodes
		if(nd==sLayer) phyLayer = "s";
		else if(nd==bLayer) phyLayer = "b";
		else if(nd==aLayer) phyLayer = "a";
		else if(nd==gLayer) phyLayer = "g";
		else phyLayer = "m";
		e = xmldoc.createElementNS(null, "phyLayer");
		n = (Node) xmldoc.createTextNode(phyLayer);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "nodes");
		n = (Node) xmldoc.createTextNode(Integer.toString(nd));
		e.appendChild((Node) n);
		root.appendChild(e);
		
		if(phyLayer=="m")mixNodes = Integer.toString(nd-gLayer);
		else mixNodes="0";
		e = xmldoc.createElementNS(null, "mixNodes");
		n = (Node) xmldoc.createTextNode(mixNodes);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e= xmldoc.createElementNS(null, "RTSThr");
		n = (Node) xmldoc.createTextNode(rtsThr);
		e.appendChild((Node) n);
		root.appendChild(e);

		e= xmldoc.createElementNS(null, "ctsToSelf");
		n = (Node) xmldoc.createTextNode(ctsToSelf);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "EDCA");
		
		tr= xmldoc.createElementNS(null, "cwMinFact0");
		n = (Node)xmldoc.createTextNode(cwmin0);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMinFact1");
		n = (Node)xmldoc.createTextNode(cwmin1);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMinFact2");
		n = (Node)xmldoc.createTextNode(cwmin2);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMinFact3");
		n = (Node)xmldoc.createTextNode(cwmin3);
		tr.appendChild((Node) n);
		e.appendChild(tr);

		tr= xmldoc.createElementNS(null, "cwMaxFact0");
		n = (Node)xmldoc.createTextNode(cwmax0);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMaxFact1");
		n = (Node)xmldoc.createTextNode(cwmax1);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMaxFact2");
		n = (Node)xmldoc.createTextNode(cwmax2);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "cwMaxFact3");
		n = (Node)xmldoc.createTextNode(cwmax3);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs0");
		n = (Node)xmldoc.createTextNode(aifs0);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs1");
		n = (Node)xmldoc.createTextNode(aifs1);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs2");
		n = (Node)xmldoc.createTextNode(aifs2);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		tr= xmldoc.createElementNS(null, "aifs3");
		n = (Node)xmldoc.createTextNode(aifs3);
		tr.appendChild((Node) n);
		e.appendChild(tr);
		
		root.appendChild(e);
		
		e= xmldoc.createElementNS(null, "resultsPath");
		n = (Node) xmldoc.createTextNode(resultsPath);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e= xmldoc.createElementNS(null, "outResults");
		n = (Node) xmldoc.createTextNode(outResults);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		xmldoc.appendChild(root);
			
		try {
			FileOutputStream fos = new FileOutputStream("config"+File.separator+"NtConf.xml");

			OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
			of.setIndent(1);
			of.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(fos,of);

			serializer.asDOMSerializer();
			serializer.serialize( xmldoc.getDocumentElement() );
			fos.close();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	//Makes the xml configuration file according to a text based network configuration file (for linux users)
	public void configInLinux(){ 
		Element e = null;
		Element tr=null;
		Node n = null;

		Document xmldoc= new DocumentImpl();

		Element root = xmldoc.createElement("scenario");
		e = xmldoc.createElementNS(null, "seed");
		n = (Node) xmldoc.createTextNode(seed);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "duration");
		n = (Node) xmldoc.createTextNode(duration);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		e = xmldoc.createElementNS(null, "values");
		n = (Node) xmldoc.createTextNode(values);
		e.appendChild((Node) n);
		root.appendChild(e);
		
		String str=null;
		String line=null;
		
		try {
			BufferedReader buf = new BufferedReader(new FileReader(ntConfPath));
			StringTokenizer st;
			//Ignore the explanation lines
			//for(int i=0;i<41;i++) buf.readLine();
			while(!buf.readLine().startsWith("Node")){}
			buf.readLine();
			 while((line=buf.readLine())!=null) {
				 //Check for blank lines at the end...
				 if(!line.startsWith("1")&&!line.startsWith("2")&&!line.startsWith("3")&&!line.startsWith("4")&&!line.startsWith("5")&&!line.startsWith("6")&&!line.startsWith("7")&&!line.startsWith("8")&&!line.startsWith("9"))
					 break;
				 st = new StringTokenizer(line);
				 str=st.nextToken();
				e = xmldoc.createElementNS(null, "node");
				e.setAttributeNS(null, "number", Integer.toString(nd+1));

				tr= xmldoc.createElementNS(null, "rate");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);
					
				tr= xmldoc.createElementNS(null, "pktLngth");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);
						
				tr= xmldoc.createElementNS(null, "pktDist");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);
						
				tr= xmldoc.createElementNS(null, "intArrTime");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);
						
				tr= xmldoc.createElementNS(null, "intArrDstr");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);

				tr= xmldoc.createElementNS(null, "xPosition");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);

				tr= xmldoc.createElementNS(null, "yPosition");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);

				tr= xmldoc.createElementNS(null, "coverage");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);

				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				//According to the value of the physical layer increase a counter, in order to know
				//the number of b nodes in mixed mode.
				if(str.equals("s")) sLayer++;
				else if (str.equals("b")) sLayer++;
				else if (str.equals("g")) gLayer++;
				else if (str.equals("a")) aLayer++;
				else sLayer++;
				
				tr= xmldoc.createElementNS(null, "AC");
				str=st.nextToken();
				if(str.endsWith(".0")) str=str.substring(0,str.length()-2);
				n = (Node)xmldoc.createTextNode(str);
				tr.appendChild((Node) n);
				e.appendChild(tr);
				
				root.appendChild(e);
				
				nd++; //nd is the number of nodes
			}

			//According to the counters determine the physical layer. nd is the number of nodes
			if(nd==sLayer) phyLayer = "s";
			else if(nd==bLayer) phyLayer = "b";
			else if(nd==aLayer) phyLayer = "a";
			else if(nd==gLayer) phyLayer = "g";
			else phyLayer = "m";
			e = xmldoc.createElementNS(null, "phyLayer");
			n = (Node) xmldoc.createTextNode(phyLayer);
			e.appendChild((Node) n);
			root.appendChild(e);
		
			e = xmldoc.createElementNS(null, "nodes");
			n = (Node) xmldoc.createTextNode(Integer.toString(nd));
			e.appendChild((Node) n);
			root.appendChild(e);
		
			if(phyLayer=="m")mixNodes = Integer.toString(nd-gLayer);
			else mixNodes="0";
			e = xmldoc.createElementNS(null, "mixNodes");
			n = (Node) xmldoc.createTextNode(mixNodes);
			e.appendChild((Node) n);
			root.appendChild(e);
		
			e= xmldoc.createElementNS(null, "RTSThr");
			n = (Node) xmldoc.createTextNode(rtsThr);
			e.appendChild((Node) n);
			root.appendChild(e);

			e= xmldoc.createElementNS(null, "ctsToSelf");
			n = (Node) xmldoc.createTextNode(ctsToSelf);
			e.appendChild((Node) n);
			root.appendChild(e);
			
			e = xmldoc.createElementNS(null, "EDCA");
			
			tr= xmldoc.createElementNS(null, "cwMinFact0");
			n = (Node)xmldoc.createTextNode(cwmin0);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "cwMinFact1");
			n = (Node)xmldoc.createTextNode(cwmin1);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "cwMinFact2");
			n = (Node)xmldoc.createTextNode(cwmin2);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "cwMinFact3");
			n = (Node)xmldoc.createTextNode(cwmin3);
			tr.appendChild((Node) n);
			e.appendChild(tr);

			tr= xmldoc.createElementNS(null, "cwMaxFact0");
			n = (Node)xmldoc.createTextNode(cwmax0);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "cwMaxFact1");
			n = (Node)xmldoc.createTextNode(cwmax1);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "cwMaxFact2");
			n = (Node)xmldoc.createTextNode(cwmax2);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "cwMaxFact3");
			n = (Node)xmldoc.createTextNode(cwmax3);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "aifs0");
			n = (Node)xmldoc.createTextNode(aifs0);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "aifs1");
			n = (Node)xmldoc.createTextNode(aifs1);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "aifs2");
			n = (Node)xmldoc.createTextNode(aifs2);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			tr= xmldoc.createElementNS(null, "aifs3");
			n = (Node)xmldoc.createTextNode(aifs3);
			tr.appendChild((Node) n);
			e.appendChild(tr);
			
			root.appendChild(e);
			
			e= xmldoc.createElementNS(null, "resultsPath");
			n = (Node) xmldoc.createTextNode(resultsPath);
			e.appendChild((Node) n);
			root.appendChild(e);
		
			e= xmldoc.createElementNS(null, "outResults");
			n = (Node) xmldoc.createTextNode(outResults);
			e.appendChild((Node) n);
			root.appendChild(e);
		
			xmldoc.appendChild(root);
			
			FileOutputStream fos = new FileOutputStream("config"+File.separator+"NtConf.xml");

			OutputFormat of = new OutputFormat("XML","ISO-8859-1",true);
			of.setIndent(1);
			of.setIndenting(true);
			XMLSerializer serializer = new XMLSerializer(fos,of);

			serializer.asDOMSerializer();
			serializer.serialize( xmldoc.getDocumentElement() );
			fos.close();
		} catch (DOMException e2) {
			e2.printStackTrace();
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	
	//Set method follow...
	
	public void setCtsToSelf(String ctsToSelf) {
		this.ctsToSelf = ctsToSelf;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public void setMixNodes(String mixNodes) {
		this.mixNodes = mixNodes;
	}

	public void setNodes(String nodes) {
		this.nodes = nodes;
	}

	public void setNtConfPath(String ntConfPath) {
		this.ntConfPath = ntConfPath;
	}

	public void setOutResults(String outResults) {
		this.outResults = outResults;
	}

	public void setResultsPath(String resultsPath) {
		this.resultsPath = resultsPath;
	}

	public void setRtsThr(String rtsThr) {
		this.rtsThr = rtsThr;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public void setValues(String values) {
		this.values = values;
	}
	
	public void setIntArrDstr(String intArrDstr) {
		this.intArrDstr = intArrDstr;
	}
	public void setIntArrTime(String intArrTime) {
		this.intArrTime = intArrTime;
	}
	public void setPhyLayer(String phyLayer) {
		this.phyLayer = phyLayer;
	}
	public void setPktDist(String pktDist) {
		this.pktDist = pktDist;
	}
	public void setPktLngth(String pktLngth) {
		this.pktLngth = pktLngth;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}

	public void setAifs0(String aifs0) {
		this.aifs0 = aifs0;
	}

	public void setAifs1(String aifs1) {
		this.aifs1 = aifs1;
	}

	public void setAifs2(String aifs2) {
		this.aifs2 = aifs2;
	}

	public void setAifs3(String aifs3) {
		this.aifs3 = aifs3;
	}

	public void setALayer(int layer) {
		aLayer = layer;
	}

	public void setBLayer(int layer) {
		bLayer = layer;
	}

	public void setCwmax0(String cwmax0) {
		this.cwmax0 = cwmax0;
	}

	public void setCwmax1(String cwmax1) {
		this.cwmax1 = cwmax1;
	}

	public void setCwmax2(String cwmax2) {
		this.cwmax2 = cwmax2;
	}

	public void setCwmax3(String cwmax3) {
		this.cwmax3 = cwmax3;
	}

	public void setCwmin0(String cwmin0) {
		this.cwmin0 = cwmin0;
	}

	public void setCwmin1(String cwmin1) {
		this.cwmin1 = cwmin1;
	}

	public void setCwmin2(String cwmin2) {
		this.cwmin2 = cwmin2;
	}

	public void setCwmin3(String cwmin3) {
		this.cwmin3 = cwmin3;
	}

	public void setGLayer(int layer) {
		gLayer = layer;
	}

	public void setNd(int nd) {
		this.nd = nd;
	}

	public void setSLayer(int layer) {
		sLayer = layer;
	}
}
