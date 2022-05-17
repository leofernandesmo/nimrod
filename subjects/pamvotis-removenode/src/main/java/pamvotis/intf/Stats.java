package pamvotis.intf;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class Stats extends JFrame {
	
	private Pamvotis pm=null;
	private MakeNtConf mk = null;
	private String tmpStr = "_";
	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLblPict = null;
	private JCheckBox jChkThrBt = null;
	private JCheckBox jChkThrPkt = null;
	private JCheckBox jChkMean = null;
	private JCheckBox jChkUtil = null;
	private JCheckBox jChkMdel = null;
	private JCheckBox jChkQDel = null;
	private JCheckBox jChkTDel = null;
	private JCheckBox jChkJit = null;
	private JCheckBox jChkQlngth = null;
	private JCheckBox jChkRatts = null;
	private JButton jBtOK = null;
	private JButton jBtCancel = null;
	private JPanel jPanel = null;

	public Stats(Pamvotis pmarg, MakeNtConf mkarg) {
		super();
		pm=pmarg;
		mk = mkarg;
		initialize();
	}

	private void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(423, 354);
		URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		this.setResizable(false);
		this.setLocation(new Point((d.width-this.getSize().width)/2, (d.height-this.getSize().height)/2));
		this.setContentPane(getJContentPane());
		this.setTitle("Pamvotis - Statistics Configuration");
		//Listener for the X button
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				getInitParams(); //Set the values that the window had when it was opened
				Stats.this.setVisible(false); //Close the window
				pm.setEnabled(true); //Open the parent window
				pm.setVisible(true); //Added because otherwise the parent window opens minimized
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLblPict = new JLabel();
			jLblPict.setBounds(new Rectangle(78, 12, 233, 63));
			jLblPict.setText("");
			URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"stats.jpg");
			jLblPict.setIcon(new ImageIcon(iconURL));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(jLblPict, null);
			jContentPane.add(getJBtOK(), null);
			jContentPane.add(getJBtCancel(), null);
			jContentPane.add(getJPanel(), null);
		}
		return jContentPane;
	}

	private JCheckBox getJChkThrBt() {
		if (jChkThrBt == null) {
			jChkThrBt = new JCheckBox();
			jChkThrBt.setText("Throughput (Kb/s)");
			jChkThrBt.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkThrBt.setBounds(new Rectangle(13, 60, 169, 20));
		}
		return jChkThrBt;
	}

	private JCheckBox getJChkThrPkt() {
		if (jChkThrPkt == null) {
			jChkThrPkt = new JCheckBox();
			jChkThrPkt.setText("Throughput (pkts/s)");
			jChkThrPkt.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkThrPkt.setBounds(new Rectangle(13, 90, 166, 21));
		}
		return jChkThrPkt;
	}

	private JCheckBox getJChkMean() {
		if (jChkMean == null) {
			jChkMean = new JCheckBox();
			jChkMean.setEnabled(false);
			jChkMean.setSelected(true);
			jChkMean.setBounds(new Rectangle(13, 30, 105, 23));
			jChkMean.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkMean.setText("Mean Values");
		}
		return jChkMean;
	}

	private JCheckBox getJChkUtil() {
		if (jChkUtil == null) {
			jChkUtil = new JCheckBox();
			jChkUtil.setText("Utilization");
			jChkUtil.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkUtil.setBounds(new Rectangle(13, 120, 129, 26));
		}
		return jChkUtil;
	}

	private JCheckBox getJChkMdel() {
		if (jChkMdel == null) {
			jChkMdel = new JCheckBox();
			jChkMdel.setText("Media Access Delay (ms)");
			jChkMdel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkMdel.setBounds(new Rectangle(13, 150, 170, 26));
		}
		return jChkMdel;
	}

	private JCheckBox getJChkQDel() {
		if (jChkQDel == null) {
			jChkQDel = new JCheckBox();
			jChkQDel.setText("Queuing Delay (ms)");
			jChkQDel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkQDel.setBounds(new Rectangle(198, 29, 157, 31));
		}
		return jChkQDel;
	}

	private JCheckBox getJChkTDel() {
		if (jChkTDel == null) {
			jChkTDel = new JCheckBox();
			jChkTDel.setText("Total Packet Delay (ms)");
			jChkTDel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkTDel.setBounds(new Rectangle(198, 60, 163, 28));
		}
		return jChkTDel;
	}

	private JCheckBox getJChkJit() {
		if (jChkJit == null) {
			jChkJit = new JCheckBox();
			jChkJit.setText("Delay Jitter (ms)");
			jChkJit.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkJit.setBounds(new Rectangle(198, 90, 146, 29));
		}
		return jChkJit;
	}

	private JCheckBox getJChkQlngth() {
		if (jChkQlngth == null) {
			jChkQlngth = new JCheckBox();
			jChkQlngth.setText("Packet Queue Length");
			jChkQlngth.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkQlngth.setBounds(new Rectangle(198, 120, 159, 25));
		}
		return jChkQlngth;
	}

	private JCheckBox getJChkRatts() {
		if (jChkRatts == null) {
			jChkRatts = new JCheckBox();
			jChkRatts.setText("Retransmission Attempts");
			jChkRatts.setFont(new Font("Dialog", Font.PLAIN, 12));
			jChkRatts.setBounds(new Rectangle(198, 150, 173, 23));
		}
		return jChkRatts;
	}

	private JButton getJBtOK() {
		if (jBtOK == null) {
			jBtOK = new JButton();
			jBtOK.setBounds(new Rectangle(14, 283, 82, 21));
			jBtOK.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtOK.setText("OK");
			jBtOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					writeToNtConf();
					Stats.this.setVisible(false); //Close the window
					pm.setEnabled(true); //Open the parent window
					pm.setVisible(true); //Added because otherwise the parent window opens minimized
				}
			});
		}
		return jBtOK;
	}

	private JButton getJBtCancel() {
		if (jBtCancel == null) {
			jBtCancel = new JButton();
			jBtCancel.setBounds(new Rectangle(313, 283, 82, 21));
			jBtCancel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtCancel.setText("Cancel");
			jBtCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getInitParams(); //Set the values that the window had when it was opened
					Stats.this.setVisible(false); //Close the window
					pm.setEnabled(true); //Open the parent window
					pm.setVisible(true); //Added because otherwise the parent window opens minimized
				}
			});
		}
		return jBtCancel;
	}
	
	public void setInitParams(){
		tmpStr="_";
		if (jChkThrBt.isSelected()) tmpStr+= "tb";
		if (jChkThrPkt.isSelected()) tmpStr+= "tp";
		if (jChkUtil.isSelected()) tmpStr+= "ut";
		if (jChkMdel.isSelected()) tmpStr+= "md";
		if (jChkQDel.isSelected()) tmpStr+= "qd";
		if (jChkTDel.isSelected()) tmpStr+= "td";
		if (jChkJit.isSelected()) tmpStr+= "dj";
		if (jChkQlngth.isSelected()) tmpStr+= "ql";
		if (jChkRatts.isSelected()) tmpStr+= "ra";
	}
	
	private void getInitParams(){
		if(tmpStr.contains("tb")) jChkThrBt.setSelected(true); else jChkThrBt.setSelected(false);
		if(tmpStr.contains("tp")) jChkThrPkt.setSelected(true); else jChkThrPkt.setSelected(false);
		if(tmpStr.contains("ut")) jChkUtil.setSelected(true); else jChkUtil.setSelected(false);
		if(tmpStr.contains("md")) jChkMdel.setSelected(true); else jChkMdel.setSelected(false);
		if(tmpStr.contains("qd")) jChkQDel.setSelected(true); else jChkQDel.setSelected(false);
		if(tmpStr.contains("td")) jChkTDel.setSelected(true); else jChkTDel.setSelected(false);
		if(tmpStr.contains("dj")) jChkJit.setSelected(true); else jChkJit.setSelected(false);
		if(tmpStr.contains("ql")) jChkQlngth.setSelected(true); else jChkQlngth.setSelected(false);
		if(tmpStr.contains("ra")) jChkRatts.setSelected(true); else jChkRatts.setSelected(false);
	}
	
	//Passes the selected values to the arguements of the network configuration file object 
	private void writeToNtConf(){
		String str="_";
		if (jChkThrBt.isSelected()) str+= "tb";
		if (jChkThrPkt.isSelected()) str+= "tp";
		if (jChkUtil.isSelected()) str+= "ut";
		if (jChkMdel.isSelected()) str+= "md";
		if (jChkQDel.isSelected()) str+= "qd";
		if (jChkTDel.isSelected()) str+= "td";
		if (jChkJit.isSelected()) str+= "dj";
		if (jChkQlngth.isSelected()) str+= "ql";
		if (jChkRatts.isSelected()) str+= "ra";
		mk.setOutResults(str);
	}

	/**
	 * This method initializes jPanel	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel() {
		if (jPanel == null) {
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(14, 82, 383, 190));
			jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Available Statistic Results", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanel.add(getJChkJit(), null);
			jPanel.add(getJChkQlngth(), null);
			jPanel.add(getJChkMean(), null);
			jPanel.add(getJChkThrBt(), null);
			jPanel.add(getJChkTDel(), null);
			jPanel.add(getJChkQDel(), null);
			jPanel.add(getJChkMdel(), null);
			jPanel.add(getJChkThrPkt(), null);
			jPanel.add(getJChkRatts(), null);
			jPanel.add(getJChkUtil(), null);
		}
		return jPanel;
	}
}
