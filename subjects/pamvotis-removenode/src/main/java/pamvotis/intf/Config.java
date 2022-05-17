package pamvotis.intf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class Config extends JFrame {

	private Pamvotis pm=null; //For enabling the parent window
	private MakeNtConf mk = null; //For writing to the network configuration file
	private ButtonModel initRate=null; //Used for the cancel button
	private ButtonModel initPhy=null; //Used for the cancel button
	private String initNodes = null; //Used for the cancel button
	private String initMixNodes=null; //Used for the cancel button
	private String initPktLngth=null; //Used for the cancel button
	private int initPktDstr=0; //Used for the cancel button
	private String initGen=null; //Used for the cancel button
	private int initGenDstr=0; //Used for the cancel button
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JButton jBtConfOK = null;
	private JButton jBtConfCancel = null;
	private JLabel jLbConfPic = null;
	private JRadioButton jRBPhyS = null;
	private JRadioButton jRBPhyB = null;
	private JRadioButton jRBPhyA = null;
	private JRadioButton jRBPhyG = null;
	private JRadioButton jRBPhyM = null;
	
	//Used for enabling or disabling rates when a physical layer is choosed
	class PhyRadioListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			checkRates(e.getActionCommand()); //Enables and disables rates
		}
	}
	
	private PhyRadioListener phyListen = new PhyRadioListener();
	private ButtonGroup phyGroup = new ButtonGroup();
	
	private JRadioButton jRbRate1 = null;
	private JRadioButton jRbRate2 = null;
	private JRadioButton jRbRate5 = null;
	private JRadioButton jRbRate11 = null;
	private JRadioButton jRbRate6 = null;
	private JRadioButton jRbRate12 = null;
	private JRadioButton jRbRate22 = null;
	private JRadioButton jRbRate9 = null;
	private JRadioButton jRbRate33 = null;
	private JRadioButton jRbRate36 = null;
	private JRadioButton jRbRate48 = null;
	private JRadioButton jRbRate54 = null;
	private JRadioButton jRbRate18 = null;
	private JRadioButton jRbRate24 = null;
	private ButtonGroup rateGroup = new ButtonGroup();
	private JTextField jTxtNodes = null;
	private JTextField jTxtMixNodes = null;
	private JLabel jLblNodes = null;
	private JLabel jLblMixNodes = null;
	private JLabel jLblMixNodes2 = null;
	
	private JComboBox jCmbPkt = null;
	private JComboBox jCmbDstr = null;
	private JLabel jLblPktLngth = null;
	private JLabel jLblPktLngthMean = null;
	private JTextField jTxtPktLngthMean = null;
	private JLabel jLblIntArDstr = null;
	private JLabel jLblPktIntArrMean = null;
	private JTextField jTxtPktGenRateMean = null;
	private JLabel jLblWarn = null;
	private JLabel jLblWarn2 = null;
	private JLabel jLblWarn3 = null;
	private JPanel jPanelPhy = null;
	private JPanel jPanelPkt = null;
	private JPanel jPanelRte = null;
	private JPanel jPanelNodes = null;
	private JPanel jPanelIntArr = null;
	private JPanel jPanelWarn = null;
	
	public Config(Pamvotis pmarg, MakeNtConf mkarg) {
		super();
		this.pm = pmarg; //Pass the arguement of the parent window
		this.mk = mkarg; //Pass the MakeNtConf arguement of the parent window
		initialize();
	}

	private void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(764, 456);
		URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		this.setResizable(false);
		this.setLocation(new Point((d.width-this.getSize().width)/2, (d.height-this.getSize().height)/2));
		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //A listener is defined
		this.setTitle("Pamvotis - Simulation Configuration");
		//Listener for the X button
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				getInitParams(); //Set the values that the window had when it was opened
				Config.this.setVisible(false); //Close the window
				pm.setEnabled(true); //Open the parent window
				pm.setVisible(true); //Added because otherwise the parent window opens minimized
			}
		});
	}


	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLblWarn3 = new JLabel();
			jLblWarn3.setText("Note: All values must be positive integers.");
			jLblWarn3.setLocation(new Point(7, 48));
			jLblWarn3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblWarn3.setSize(new Dimension(378, 18));
			jLblWarn2 = new JLabel();
			jLblWarn2.setText("Use network configuration file to configure position and coverage parameters, specific to each node.");
			jLblWarn2.setLocation(new Point(7, 24));
			jLblWarn2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblWarn2.setSize(new Dimension(578, 17));
			jLblWarn = new JLabel();
			jLblWarn.setText("Note: Global configuration of node parameters assumes that all nodes are in Line of Sight.");
			jLblWarn.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblWarn.setBounds(new Rectangle(7, 0, 550, 19));
			jLblPktIntArrMean = new JLabel();
			jLblPktIntArrMean.setText("Packet Generation Rate Mean (packets/s):");
			jLblPktIntArrMean.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblPktIntArrMean.setBounds(new Rectangle(15, 45, 264, 21));
			jLblIntArDstr = new JLabel();
			jLblIntArDstr.setText("Packet Generation Rate Distribution:");
			jLblIntArDstr.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblIntArDstr.setBounds(new Rectangle(15, 19, 209, 21));
			jLblPktLngthMean = new JLabel();
			jLblPktLngthMean.setText("Packet Length Mean (bits):");
			jLblPktLngthMean.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblPktLngthMean.setBounds(new Rectangle(21, 43, 159, 23));
			jLblPktLngth = new JLabel();
			jLblPktLngth.setText("Packet Length Distribution:");
			jLblPktLngth.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblPktLngth.setBounds(new Rectangle(21, 19, 159, 23));
			jLblMixNodes2 = new JLabel();
			jLblMixNodes2.setEnabled(false);
			jLblMixNodes2.setBounds(new Rectangle(186, 35, 172, 18));
			jLblMixNodes2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblMixNodes2.setText("(mixed 802.11b/g only)");
			jLblMixNodes = new JLabel();
			jLblMixNodes.setEnabled(false);
			jLblMixNodes.setBounds(new Rectangle(186, 18, 162, 21));
			jLblMixNodes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblMixNodes.setText("Number of 802.11b nodes:");
			jLblNodes = new JLabel();
			jLblNodes.setText("Number of nodes:");
			jLblNodes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblNodes.setBounds(new Rectangle(15, 18, 107, 23));
			jLbConfPic = new JLabel();
			jLbConfPic.setBounds(new Rectangle(17, 18, 106, 70));
			URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"nodes.jpg");
			jLbConfPic.setIcon(new ImageIcon(iconURL));
			jLbConfPic.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jLbConfPic.setText("");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJBtConfOK(), null);
			jContentPane.add(getJBtConfCancel(), null);
			jContentPane.add(jLbConfPic, null);
			jContentPane.add(getJPanelPhy(), null);
			jContentPane.add(getJPanelPkt(), null);
			jContentPane.add(getJPanelRte(), null);
			jContentPane.add(getJPanelNodes(), null);
			jContentPane.add(getJPanelIntArr(), null);
			jContentPane.add(getJPanelWarn(), null);
		}
		return jContentPane;
	}


	private JButton getJBtConfOK() {
		if (jBtConfOK == null) {
			jBtConfOK = new JButton();
			jBtConfOK.setText("OK");
			jBtConfOK.setSize(new Dimension(95, 20));
			jBtConfOK.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtConfOK.setLocation(new Point(19, 390));
			jBtConfOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					writeToNtConf();
					Config.this.setVisible(false);
					pm.setEnabled(true);
					pm.setVisible(true);
				}
			});
		}
		return jBtConfOK;
	}


	private JButton getJBtConfCancel() {
		if (jBtConfCancel == null) {
			jBtConfCancel = new JButton();
			jBtConfCancel.setText("Cancel");
			//Same listener as this for the X button
			jBtConfCancel.setBounds(new Rectangle(645, 390, 95, 20));
			jBtConfCancel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtConfCancel.setMnemonic(KeyEvent.VK_UNDEFINED);
			jBtConfCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getInitParams();
					Config.this.setVisible(false);
					pm.setEnabled(true);
					pm.setVisible(true);
				}
			});
		}
		return jBtConfCancel;
	}


	private JRadioButton getJRBPhyS() {
		if (jRBPhyS == null) {
			jRBPhyS = new JRadioButton();
			jRBPhyS.setSelected(true);
			jRBPhyS.setActionCommand("s");
			jRBPhyS.setBounds(new Rectangle(8, 35, 106, 20));
			jRBPhyS.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRBPhyS.setText("IEEE 802.11");
			jRBPhyS.addActionListener(phyListen);
			phyGroup.add(jRBPhyS);
		}
		return jRBPhyS;
	}


	private JRadioButton getJRBPhyB() {
		if (jRBPhyB == null) {
			jRBPhyB = new JRadioButton();
			jRBPhyB.setActionCommand("b");
			jRBPhyB.setBounds(new Rectangle(8, 55, 106, 20));
			jRBPhyB.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRBPhyB.setText("IEEE 802.11b");
			jRBPhyB.addActionListener(phyListen);
			phyGroup.add(jRBPhyB);
		}
		return jRBPhyB;
	}


	private JRadioButton getJRBPhyA() {
		if (jRBPhyA == null) {
			jRBPhyA = new JRadioButton();
			jRBPhyA.setActionCommand("a");
			jRBPhyA.setBounds(new Rectangle(8, 75, 113, 20));
			jRBPhyA.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRBPhyA.setText("IEEE 802.11a");
			jRBPhyA.addActionListener(phyListen);
			phyGroup.add(jRBPhyA);
		}
		return jRBPhyA;
	}


	private JRadioButton getJRBPhyG() {
		if (jRBPhyG == null) {
			jRBPhyG = new JRadioButton();
			jRBPhyG.setActionCommand("g");
			jRBPhyG.setBounds(new Rectangle(8, 95, 105, 20));
			jRBPhyG.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRBPhyG.setText("IEEE 802.11g");
			jRBPhyG.addActionListener(phyListen);
			phyGroup.add(jRBPhyG);
		}
		return jRBPhyG;
	}


	private JRadioButton getJRBPhyM() {
		if (jRBPhyM == null) {
			jRBPhyM = new JRadioButton();
			jRBPhyM.setActionCommand("m");
			jRBPhyM.setBounds(new Rectangle(8, 115, 186, 18));
			jRBPhyM.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRBPhyM.setText("Mixed IEEE 802.11b/g");
			jRBPhyM.addActionListener(phyListen);
			phyGroup.add(jRBPhyM);
		}
		return jRBPhyM;
	}


	private JRadioButton getJRbRate1() {
		if (jRbRate1 == null) {
			jRbRate1 = new JRadioButton();
			jRbRate1.setActionCommand("1000000");
			jRbRate1.setSelected(true);
			jRbRate1.setBounds(new Rectangle(15, 19, 70, 16));
			jRbRate1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate1.setText("1Mb/s");
			rateGroup.add(jRbRate1);
		}
		return jRbRate1;
	}


	private JRadioButton getJRbRate2() {
		if (jRbRate2 == null) {
			jRbRate2 = new JRadioButton();
			jRbRate2.setActionCommand("2000000");
			jRbRate2.setBounds(new Rectangle(15, 39, 70, 15));
			jRbRate2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate2.setText("2Mb/s");
			rateGroup.add(jRbRate2);
		}
		return jRbRate2;
	}

	private JRadioButton getJRbRate5() {
		if (jRbRate5 == null) {
			jRbRate5 = new JRadioButton();
			jRbRate5.setActionCommand("5500000");
			jRbRate5.setEnabled(false);
			jRbRate5.setBounds(new Rectangle(15, 59, 82, 15));
			jRbRate5.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate5.setText("5.5Mb/s");
			rateGroup.add(jRbRate5);
		}
		return jRbRate5;
	}
	
	private JRadioButton getJRbRate6() {
		if (jRbRate6 == null) {
			jRbRate6 = new JRadioButton();
			jRbRate6.setActionCommand("6000000");
			jRbRate6.setEnabled(false);
			jRbRate6.setBounds(new Rectangle(95, 19, 70, 15));
			jRbRate6.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate6.setText("6Mb/s");
			rateGroup.add(jRbRate6);
		}
		return jRbRate6;
	}

	private JRadioButton getJRbRate9() {
		if (jRbRate9 == null) {
			jRbRate9 = new JRadioButton();
			jRbRate9.setActionCommand("9000000");
			jRbRate9.setEnabled(false);
			jRbRate9.setBounds(new Rectangle(95, 39, 70, 15));
			jRbRate9.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate9.setText("9Mb/s");
			rateGroup.add(jRbRate9);
		}
		return jRbRate9;
	}

	private JRadioButton getJRbRate11() {
		if (jRbRate11 == null) {
			jRbRate11 = new JRadioButton();
			jRbRate11.setActionCommand("11000000");
			jRbRate11.setEnabled(false);
			jRbRate11.setBounds(new Rectangle(95, 59, 82, 15));
			jRbRate11.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate11.setText("11Mb/s");
			rateGroup.add(jRbRate11);
		}
		return jRbRate11;
	}

	private JRadioButton getJRbRate12() {
		if (jRbRate12 == null) {
			jRbRate12 = new JRadioButton();
			jRbRate12.setActionCommand("12000000");
			jRbRate12.setEnabled(false);
			jRbRate12.setBounds(new Rectangle(175, 19, 85, 15));
			jRbRate12.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate12.setText("12Mb/s");
			rateGroup.add(jRbRate12);
		}
		return jRbRate12;
	}
	
	private JRadioButton getJRbRate18() {
		if (jRbRate18 == null) {
			jRbRate18 = new JRadioButton();
			jRbRate18.setActionCommand("18000000");
			jRbRate18.setEnabled(false);
			jRbRate18.setBounds(new Rectangle(175, 39, 84, 15));
			jRbRate18.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate18.setText("18Mb/s");
			rateGroup.add(jRbRate18);
		}
		return jRbRate18;
	}

	private JRadioButton getJRbRate22() {
		if (jRbRate22 == null) {
			jRbRate22 = new JRadioButton();
			jRbRate22.setActionCommand("22000000");
			jRbRate22.setEnabled(false);
			jRbRate22.setBounds(new Rectangle(175, 59, 83, 15));
			jRbRate22.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate22.setText("22Mb/s");
			rateGroup.add(jRbRate22);
		}
		return jRbRate22;
	}

	private JRadioButton getJRbRate24() {
		if (jRbRate24 == null) {
			jRbRate24 = new JRadioButton();
			jRbRate24.setActionCommand("24000000");
			jRbRate24.setEnabled(false);
			jRbRate24.setBounds(new Rectangle(255, 19, 81, 15));
			jRbRate24.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate24.setText("24Mb/s");
			rateGroup.add(jRbRate24);
		}
		return jRbRate24;
	}

	private JRadioButton getJRbRate33() {
		if (jRbRate33 == null) {
			jRbRate33 = new JRadioButton();
			jRbRate33.setActionCommand("33000000");
			jRbRate33.setEnabled(false);
			jRbRate33.setBounds(new Rectangle(255, 39, 81, 15));
			jRbRate33.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate33.setText("33Mb/s");
			rateGroup.add(jRbRate33);
		}
		return jRbRate33;
	}

	private JRadioButton getJRbRate36() {
		if (jRbRate36 == null) {
			jRbRate36 = new JRadioButton();
			jRbRate36.setActionCommand("36000000");
			jRbRate36.setEnabled(false);
			jRbRate36.setBounds(new Rectangle(255, 59, 84, 15));
			jRbRate36.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate36.setText("36Mb/s");
			rateGroup.add(jRbRate36);
		}
		return jRbRate36;
	}

	private JRadioButton getJRbRate48() {
		if (jRbRate48 == null) {
			jRbRate48 = new JRadioButton();
			jRbRate48.setActionCommand("48000000");
			jRbRate48.setEnabled(false);
			jRbRate48.setBounds(new Rectangle(335, 19, 73, 15));
			jRbRate48.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate48.setText("48Mb/s");
			rateGroup.add(jRbRate48);
		}
		return jRbRate48;
	}

	private JRadioButton getJRbRate54() {
		if (jRbRate54 == null) {
			jRbRate54 = new JRadioButton();
			jRbRate54.setActionCommand("54000000");
			jRbRate54.setEnabled(false);
			jRbRate54.setBounds(new Rectangle(335, 39, 71, 15));
			jRbRate54.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbRate54.setText("54Mb/s");
			rateGroup.add(jRbRate54);
		}
		return jRbRate54;
	}

	private JTextField getJTxtNodes() {
		if (jTxtNodes == null) {
			jTxtNodes = new JTextField();
			jTxtNodes.setText("10");
			jTxtNodes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtNodes.setBounds(new Rectangle(120, 18, 46, 23));
		}
		return jTxtNodes;
	}

	private JTextField getJTxtMixNodes() {
		if (jTxtMixNodes == null) {
			jTxtMixNodes = new JTextField();
			jTxtMixNodes.setEnabled(false);
			jTxtMixNodes.setBounds(new Rectangle(351, 18, 41, 21));
			jTxtMixNodes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtMixNodes.setText("");
		}
		return jTxtMixNodes;
	}
	
	private JComboBox getJCmbPkt() {
		if (jCmbPkt == null) {
			jCmbPkt = new JComboBox();
			jCmbPkt.setBounds(new Rectangle(180, 19, 94, 23));
			jCmbPkt.setName("");
			jCmbPkt.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCmbPkt.addItem("Constant");
			jCmbPkt.addItem("Uniform");
			jCmbPkt.addItem("Exponential");
		}
		return jCmbPkt;
	}
	
	private JComboBox getJCmbDstr() {
		if (jCmbDstr == null) {
			jCmbDstr = new JComboBox();
			jCmbDstr.setBounds(new Rectangle(320, 19, 79, 21));
			jCmbDstr.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCmbDstr.addItem("Constant");
			jCmbDstr.addItem("Uniform");
			jCmbDstr.addItem("Poisson");
		}
		return jCmbDstr;
	}

	private JTextField getJTxtPktLngthMean() {
		if (jTxtPktLngthMean == null) {
			jTxtPktLngthMean = new JTextField();
			jTxtPktLngthMean.setText("8000");
			jTxtPktLngthMean.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtPktLngthMean.setBounds(new Rectangle(180, 43, 49, 23));
		}
		return jTxtPktLngthMean;
	}

	private JTextField getJTxtPktGenRateMean() {
		if (jTxtPktGenRateMean == null) {
			jTxtPktGenRateMean = new JTextField();
			jTxtPktGenRateMean.setText("8");
			jTxtPktGenRateMean.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtPktGenRateMean.setBounds(new Rectangle(321, 45, 46, 21));
		}
		return jTxtPktGenRateMean;
	}
	
	//Sets the elements  to the values that they had when the window was opened. Used for the cancel button
	private void getInitParams(){
		initRate.setSelected(true); //Sets the selected value for the rate. Init rate is the selected item of the rateGroup
		initPhy.setSelected(true); //Sets the selected value for the physical. Init rate is the selected item of the phyGroup
		jTxtNodes.setText(initNodes);
		jTxtMixNodes.setText(initMixNodes);
		jTxtPktLngthMean.setText(initPktLngth);
		jTxtPktGenRateMean.setText(initGen);
		jCmbPkt.setSelectedIndex(initPktDstr); //Sets the index of the selected item. initPktDstr is the index of the selected item when the window was opened
		jCmbDstr.setSelectedIndex(initGenDstr);
	}
	
	//This is used in the listener of the parent object. Gets the values of all elements and stores it to some 'buffer' objects
	public void setInitParams(){
		initPhy = phyGroup.getSelection(); //Get the selected item and store it to a ComboModel
		checkRates(initPhy.getActionCommand()); //This is the only statement that does the oposite. Enables/disables the rates according to the initial value of phy.
		initRate = rateGroup.getSelection(); //Get the selected item and store it to a ComboModel
		initNodes = jTxtNodes.getText(); //Get the text...
		initMixNodes = jTxtMixNodes.getText();
		initPktLngth = jTxtPktLngthMean.getText();
		initGen = jTxtPktGenRateMean.getText();
		initPktDstr = jCmbPkt.getSelectedIndex(); //Get the index of the selected item
		initGenDstr = jCmbDstr.getSelectedIndex();
	}
	
	//Enables or disables the rates according to the value of the physical layer
	public void checkRates(String str){
		if (str.equals("s")){
			disableRates(true,true,false,false,false,false,false,false,false,false,false,false,false,false);
			jLblMixNodes.setEnabled(false);
			jLblMixNodes2.setEnabled(false);
			jTxtMixNodes.setEnabled(false);
		}
		else if (str.equals("b")){
			disableRates(true,true,true,false,false,true,false,false,false,false,false,false,false,false);
			jLblMixNodes.setEnabled(false);
			jLblMixNodes2.setEnabled(false);
			jTxtMixNodes.setEnabled(false);
		}
		else if (str.equals("a")||str.equals("g")){
			disableRates(false,false,false,true,true,false,true,true,false,true,false,true,true,true);
			jLblMixNodes.setEnabled(false);
			jLblMixNodes2.setEnabled(false);
			jTxtMixNodes.setEnabled(false);
		}
		else{
			disableRates(true,true,true,true,true,true,true,true,true,true,true,true,true,true);
			jLblMixNodes.setEnabled(true);
			jLblMixNodes2.setEnabled(true);
			jTxtMixNodes.setEnabled(true);
		}
	}
	
	//Used in the CheckRates above
	private void disableRates(boolean r1, boolean r2, boolean r5, boolean r6, boolean r9, boolean r11, boolean r12, boolean r18, boolean r22, boolean r24, boolean r33, boolean r36, boolean r48, boolean r54){
		jRbRate1.setEnabled(r1);
		jRbRate2.setEnabled(r2);
		jRbRate5.setEnabled(r5);
		jRbRate6.setEnabled(r6);
		jRbRate9.setEnabled(r9);
		jRbRate11.setEnabled(r11);
		jRbRate12.setEnabled(r12);
		jRbRate18.setEnabled(r18);
		jRbRate22.setEnabled(r22);
		jRbRate24.setEnabled(r24);
		jRbRate33.setEnabled(r33);
		jRbRate36.setEnabled(r36);
		jRbRate48.setEnabled(r48);
		jRbRate54.setEnabled(r54);	
	}
	private void writeToNtConf(){
		mk.setNodes(jTxtNodes.getText());
		mk.setPhyLayer(phyGroup.getSelection().getActionCommand());
		mk.setRate(rateGroup.getSelection().getActionCommand());
		if(jTxtMixNodes.isEnabled()){
			if(jTxtMixNodes.getText()==null || jTxtMixNodes.getText().length()==0)
				mk.setMixNodes("0");
			else
				mk.setMixNodes(jTxtMixNodes.getText());
		}
		else
			mk.setMixNodes("0");
		mk.setPktLngth(jTxtPktLngthMean.getText());
		if(jCmbPkt.getSelectedItem().toString().equals("Constant"))
			mk.setPktDist("c");
		else if(jCmbPkt.getSelectedItem().toString().equals("Uniform"))
			mk.setPktDist("u");
		else mk.setPktDist("e");
		mk.setIntArrTime(jTxtPktGenRateMean.getText());
		if(jCmbDstr.getSelectedItem().toString().equals("Constant"))
			mk.setIntArrDstr("c");
		else if(jCmbDstr.getSelectedItem().toString().equals("Uniform"))
			mk.setIntArrDstr("u");
		else mk.setIntArrDstr("e");
	}

	private JPanel getJPanelPhy() {
		if (jPanelPhy == null) {
			jPanelPhy = new JPanel();
			jPanelPhy.setLayout(null);
			jPanelPhy.setBounds(new Rectangle(18, 110, 290, 166));
			jPanelPhy.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Physical Layer", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelPhy.add(getJRBPhyA(), null);
			jPanelPhy.add(getJRBPhyG(), null);
			jPanelPhy.add(getJRBPhyM(), null);
			jPanelPhy.add(getJRBPhyS(), null);
			jPanelPhy.add(getJRBPhyB(), null);
		}
		return jPanelPhy;
	}

	private JPanel getJPanelPkt() {
		if (jPanelPkt == null) {
			jPanelPkt = new JPanel();
			jPanelPkt.setLayout(null);
			jPanelPkt.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Packet Size", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelPkt.setSize(new Dimension(288, 74));
			jPanelPkt.setLocation(new Point(18, 285));
			jPanelPkt.add(getJCmbPkt(), null);
			jPanelPkt.add(getJTxtPktLngthMean(), null);
			jPanelPkt.add(jLblPktLngth, null);
			jPanelPkt.add(jLblPktLngthMean, null);
		}
		return jPanelPkt;
	}

	private JPanel getJPanelRte() {
		if (jPanelRte == null) {
			jPanelRte = new JPanel();
			jPanelRte.setLayout(null);
			jPanelRte.setBounds(new Rectangle(330, 186, 412, 86));
			jPanelRte.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 1), "Data Rate", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelRte.add(getJRbRate48(), null);
			jPanelRte.add(getJRbRate1(), null);
			jPanelRte.add(getJRbRate11(), null);
			jPanelRte.add(getJRbRate2(), null);
			jPanelRte.add(getJRbRate22(), null);
			jPanelRte.add(getJRbRate36(), null);
			jPanelRte.add(getJRbRate24(), null);
			jPanelRte.add(getJRbRate54(), null);
			jPanelRte.add(getJRbRate6(), null);
			jPanelRte.add(getJRbRate18(), null);
			jPanelRte.add(getJRbRate5(), null);
			jPanelRte.add(getJRbRate33(), null);
			jPanelRte.add(getJRbRate12(), null);
			jPanelRte.add(getJRbRate9(), null);
		}
		return jPanelRte;
	}

	private JPanel getJPanelNodes() {
		if (jPanelNodes == null) {
			jPanelNodes = new JPanel();
			jPanelNodes.setLayout(null);
			jPanelNodes.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Nodes", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelNodes.setSize(new Dimension(412, 72));
			jPanelNodes.setLocation(new Point(330, 110));
			jPanelNodes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jPanelNodes.add(jLblMixNodes, null);
			jPanelNodes.add(jLblMixNodes2, null);
			jPanelNodes.add(jLblNodes, null);
			jPanelNodes.add(getJTxtNodes(), null);
			jPanelNodes.add(getJTxtMixNodes(), null);
		}
		return jPanelNodes;
	}

	private JPanel getJPanelIntArr() {
		if (jPanelIntArr == null) {
			jPanelIntArr = new JPanel();
			jPanelIntArr.setLayout(null);
			jPanelIntArr.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.gray, 1), "Packet Generation Rate", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelIntArr.setSize(new Dimension(408, 74));
			jPanelIntArr.setLocation(new Point(330, 285));
			jPanelIntArr.add(getJCmbDstr(), null);
			jPanelIntArr.add(getJTxtPktGenRateMean(), null);
			jPanelIntArr.add(jLblPktIntArrMean, null);
			jPanelIntArr.add(jLblIntArDstr, null);
		}
		return jPanelIntArr;
	}

	private JPanel getJPanelWarn() {
		if (jPanelWarn == null) {
			jPanelWarn = new JPanel();
			jPanelWarn.setLayout(null);
			jPanelWarn.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jPanelWarn.setSize(new Dimension(605, 69));
			jPanelWarn.setLocation(new Point(133, 18));
			jPanelWarn.setFont(new Font("Dialog", Font.PLAIN, 12));
			jPanelWarn.add(jLblWarn, null);
			jPanelWarn.add(jLblWarn2, null);
			jPanelWarn.add(jLblWarn3, null);
		}
		return jPanelWarn;
	}
} 
