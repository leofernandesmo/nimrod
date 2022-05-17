package pamvotis.intf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pamvotis.core.Simulator;

public class Run extends JFrame {
	
	Pamvotis pm = null;
	SimThread st = null;
	private Task task;
	private static final long serialVersionUID = 1L;
	private JPanel jCPaneRun = null;
	private JTextArea jTxtArRun = null;
	private JLabel jLabel = null;
	private JProgressBar jProgressBar = null;
	private JLabel jLabel2 = null;
	private JButton jBtRun = null;
	private JButton jBtPause = null;
	private JLabel jLblSim = null;
	private JLabel jLblEl = null;


	public Run(Pamvotis pmArg) {
		super();
		pm = pmArg;
	}

	public void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(263, 390);
		URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		this.setLocation(new Point((d.width-this.getSize().width)/2, (d.height-this.getSize().height)/2));
		this.setResizable(false);
		this.setContentPane(getJCPaneRun());
		this.setTitle("Pamvotis - Simulation Run");
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		st = new SimThread();
		st.start();
		task = new Task();
		task.start();
	}
	
	private JPanel getJCPaneRun() {
			jLblEl = new JLabel();
			jLblEl.setBounds(new Rectangle(9, 287, 233, 21));
			jLblEl.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblEl.setText("");
			jLblSim = new JLabel();
			jLblSim.setBounds(new Rectangle(9, 262, 233, 21));
			jLblSim.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblSim.setText("");
			jLabel2 = new JLabel();
			jLabel2.setBounds(new Rectangle(58, 208, 124, 16));
			jLabel2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel2.setText("Simulation Progress");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(92, 27, 67, 14));
			jLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel.setText("Event Log");
			jCPaneRun = new JPanel();
			jCPaneRun.setLayout(null);
			jCPaneRun.setSize(new Dimension(192, 357));
			jCPaneRun.add(getJTxtArRun(), null);
			jCPaneRun.add(jLabel, null);
			jCPaneRun.add(getJProgressBar(), null);
			jCPaneRun.add(jLabel2, null);
			jCPaneRun.add(getJBtRun(), null);
			jCPaneRun.add(getJBtPause(), null);
			jCPaneRun.add(jLblSim, null);
			jCPaneRun.add(jLblEl, null);
		return jCPaneRun;
	}
	
	private JTextArea getJTxtArRun() {
			jTxtArRun = new JTextArea();
			jTxtArRun.setBounds(new Rectangle(35, 46, 190, 145));
			jTxtArRun.setWrapStyleWord(true);
			jTxtArRun.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtArRun.setEditable(false);
			jTxtArRun.setForeground(Color.blue);
			jTxtArRun.setText("");
			jTxtArRun.setLineWrap(true);
		return jTxtArRun;
	}

	private JProgressBar getJProgressBar() {
			jProgressBar = new JProgressBar();
			jProgressBar.setBounds(new Rectangle(9, 231, 233, 21));
			jProgressBar.setString("");
			jProgressBar.setFont(new Font("Dialog", Font.PLAIN, 12));
			jProgressBar.setStringPainted(true);
		return jProgressBar;
	}

	private JButton getJBtRun() {
			jBtRun = new JButton();
			jBtRun.setBounds(new Rectangle(154, 321, 87, 25));
			jBtRun.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtRun.setText("Stop");
			jBtRun.addActionListener(new java.awt.event.ActionListener() {

				@SuppressWarnings("deprecation")
				public void actionPerformed(java.awt.event.ActionEvent e) {
					if(jBtRun.getText().equals("Stop")){
						task.stop();
						st.stop();
						jBtRun.setText("Close");
						jTxtArRun.setText(jTxtArRun.getText()+ "Simulation interrupted.\n");
						jBtPause.setEnabled(false);
					}
					else{
						Run.this.setVisible(false);
						pm.setVisible(true);
					}
				}
			});
		return jBtRun;
	}

	private JButton getJBtPause() {
			jBtPause = new JButton();
			jBtPause.setBounds(new Rectangle(15, 322, 88, 24));
			jBtPause.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtPause.setText("Pause");
			jBtPause.addActionListener(new java.awt.event.ActionListener() {   
				@SuppressWarnings("deprecation")
				public void actionPerformed(java.awt.event.ActionEvent e) {    
					if(jBtPause.getText().equals("Pause")){
						st.suspend();
						task.suspend();
						jBtPause.setText("Resume");
						jTxtArRun.setText(jTxtArRun.getText()+ "Simulation Paused.\n");
					}
					else{
						st.resume();
						task.resume();
						jBtPause.setText("Pause");
						jTxtArRun.setText(jTxtArRun.getText()+ "Simulation Resumed.\n");
					}
				}
			
			});
		return jBtPause;
	}

    class Task extends Thread {

        public void run() {
        	long start = System.currentTimeMillis();
        	long elapsedTimeMillis = 0;
        	int elapsedTimeSec  = 0;
        	int elapsedTimeMin = 0;
        	while(st.getSimProgress()<100){
				try {
					Thread.sleep(10);
					jProgressBar.setValue(st.getSimProgress());
					jProgressBar.setString(st.getSimProgress()+"%");
					jLblSim.setText("Time Simulated: " + st.getSimTime() + " seconds.");
					
					elapsedTimeMillis = System.currentTimeMillis()-start;
					elapsedTimeSec = (int)((elapsedTimeMillis/1000F)%60);
					elapsedTimeMin = (int)(elapsedTimeMillis/(60*1000F));
					jLblEl.setText("Time Elapsed: "+ elapsedTimeMin + ":" + elapsedTimeSec+" seconds");
					if(jBtRun.getText().equals("Close")){
						
						break;
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
        	if(!jBtRun.getText().equals("Close")){
        		jBtRun.setText("Close");
        		jTxtArRun.setText(jTxtArRun.getText()+ "Simulation finished.\n");
        	}
        	jBtPause.setEnabled(false);
        }
    }
	
    class SimThread extends Thread {
    	
    	private Simulator sim = new Simulator();

    	public int getSimTime(){
    		return (int) sim.getTime();
    	}
    	
    	public int getSimProgress(){
    		return sim.getProgress();
    	}
    	
    	//This function makes all the job.
    	public void run() {
    		
    		int simTime=0;int values=0; //Read from the xml file.
    		
    		jTxtArRun.setText(jTxtArRun.getText()+ "Simulation Started.\n");
    		
    		//Read the total duration and the number of collected values from the xml file
    		try {
				String str=null;
				File file = new File("config"+File.separator+"NtConf.xml");
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(file);
				NodeList ndlst;
				Element elmnt;
				
				ndlst = doc.getElementsByTagName("duration");
				elmnt = (Element) ndlst.item(0);
				ndlst = elmnt.getChildNodes();
				str = ((Node) ndlst.item(0)).getNodeValue();
				simTime = Integer.parseInt(str);
				
				ndlst = doc.getElementsByTagName("values");
				elmnt = (Element) ndlst.item(0);
				ndlst = elmnt.getChildNodes();
				str = ((Node) ndlst.item(0)).getNodeValue();
				values = Integer.parseInt(str);
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
			
			//We separate the total time in a number of intervals equal to the values the
			//user chose to be collected. At the end of each interval a statistic value will
			//be printed to a corresponding file (see the PrintStats method of the
			//simulator class for more details). We also transform the total time to
			//milliseconds for more accuracy.
			int interval= simTime*1000/values;
			
			//Some times the division is not exact and a modular arises. We should simulate this too!
			int modInterval=(simTime*1000)%values;
			int j=0;
			
			//First configure the parameters of the simulation.
    		sim.confParams();
    		
    		//Then print the headers of the statistic files.
    		sim.printHeaders();
    		
    		//Call the simulator to simulate the time intervals we defined
    		for(int i=1;i<=values;i++){
    			
    			//If we are not in the last interval
    			if(i!=values) sim.simulate(j+1,j+interval);
    			
    			//If we are in the last interval we should simulate the modular too.
    			else{
    				
    				//If there is not a modular...
    				if(modInterval==0) sim.simulate(j+1, j+interval);
    				
    				//If there is...
    				else sim.simulate(j+1, j+interval+modInterval);
    			}
    			
    			//Increase the bounds of the next simulation interval
    			j+=interval;
    		}
    		
    		//Print the mean values to a file.
    		sim.printMeanValues();
    	}		
    }

}