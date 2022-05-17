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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

public class Edca extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLblIcon = null;
	private JPanel jPanel1 = null;
	private JPanel jPCwMin = null;
	private JPanel jPCwMax = null;
	private JPanel jPAifs = null;
	private JLabel jLblAc0 = null;
	private JLabel jLblAc1 = null;
	private JLabel jLblAc3 = null;
	private JLabel jLblAc2 = null;
	private JLabel jLblAc01 = null;
	private JLabel jLblAc11 = null;
	private JLabel jLblAc31 = null;
	private JLabel jLblAc21 = null;
	private JLabel jLblAc011 = null;
	private JLabel jLblAc111 = null;
	private JLabel jLblAc311 = null;
	private JLabel jLblAc211 = null;
	private JTextField jTxtCwMin0 = null;
	private JTextField jTxtCwMin1 = null;
	private JTextField jTxtCwMin2 = null;
	private JTextField jTxtCwMin3 = null;
	private JTextField jTxtCwMax0 = null;
	private JTextField jTxtCwMax1 = null;
	private JTextField jTxtCwMax2 = null;
	private JTextField jTxtCwMax3 = null;
	private JTextField jTxtAifs0 = null;
	private JTextField jTxtAifs1 = null;
	private JTextField jTxtAifs2 = null;
	private JTextField jTxtAifs3 = null;
	private JButton jBtnOK = null;
	private JButton jBtnCancel = null;
	private Pamvotis pm=null;
	private MakeNtConf mk=null;
	private String cwmin0=null;
	private String cwmin1=null;
	private String cwmin2=null;
	private String cwmin3=null;
	private String cwmax0=null;
	private String cwmax1=null;
	private String cwmax2=null;
	private String cwmax3=null;
	private String aifs0=null;
	private String aifs1=null;
	private String aifs2=null;
	private String aifs3=null;
	private JPanel jPnIcon = null;
	private JLabel jLabel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel11 = null;
	private JLabel jLabel111 = null;
	


	public Edca(Pamvotis pmarg, MakeNtConf mkarg) {
		super();
		this.pm = pmarg; //Pass the arguement of the parent window
		this.mk = mkarg; //Pass the MakeNtConf arguement of the parent window
		initialize();
	}

	private void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(682, 322);
		this.setResizable(false);
		URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		this.setLocation(new Point((d.width-this.getSize().width)/2, (d.height-this.getSize().height)/2));
		this.setContentPane(getJContentPane());
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); //A listener is defined
		this.setTitle("Pamvotis - 802.11e QoS Configuration");
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				getInitParams(); //Set the values that the window had when it was opened
				Edca.this.setVisible(false); //Close the window
				pm.setEnabled(true); //Open the parent window
				pm.setVisible(true); //Added because otherwise the parent window opens minimized
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLblIcon = new JLabel();
			URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"edca.jpg");
			jLblIcon.setIcon(new ImageIcon(iconURL));
			jLblIcon.setBounds(new Rectangle(2, 2, 89, 83));
			jLblIcon.setText("");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel1(), null);
			jContentPane.add(getJPCwMin(), null);
			jContentPane.add(getJPCwMax(), null);
			jContentPane.add(getJPAifs(), null);
			jContentPane.add(getJBtnOK(), null);
			jContentPane.add(getJBtnCancel(), null);
			jContentPane.add(getJPnIcon(), null);
		}
		return jContentPane;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel111 = new JLabel();
			jLabel111.setBounds(new Rectangle(7, 60, 523, 20));
			jLabel111.setText("Correct parameter specification is at your own risk. See the documentation for details.");
			jLabel111.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel11 = new JLabel();
			jLabel11.setBounds(new Rectangle(7, 40, 517, 20));
			jLabel11.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel11.setText("Be careful to set them correctly so the CwMin/CwMax for each AC to be at least 2.");
			jLabel1 = new JLabel();
			jLabel1.setBounds(new Rectangle(7, 20, 518, 20));
			jLabel1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel1.setText("DCF CwMin may be 16 or 32.DCF CwMax is 1024. Division factors must be powers of 2.");
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(7, 0, 517, 20));
			jLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel.setText("CwMin and CwMax for each AC are set as a submultiple of the original DCF CwMin/Cwax.");
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(new Rectangle(118, 15, 536, 86));
			jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jPanel1.add(jLabel, null);
			jPanel1.add(jLabel1, null);
			jPanel1.add(jLabel11, null);
			jPanel1.add(jLabel111, null);
		}
		return jPanel1;
	}


	private JPanel getJPCwMin() {
		if (jPCwMin == null) {
			jLblAc2 = new JLabel();
			jLblAc2.setText("CwMin(Ac3) = CW_MIN / ");
			jLblAc2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc2.setBounds(new Rectangle(15, 93, 145, 20));
			jLblAc3 = new JLabel();
			jLblAc3.setBounds(new Rectangle(15, 68, 145, 20));
			jLblAc3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc3.setText("CwMin(Ac2) = CW_MIN / ");
			jLblAc1 = new JLabel();
			jLblAc1.setBounds(new Rectangle(15, 43, 145, 20));
			jLblAc1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc1.setText("CwMin(Ac1) = CW_MIN / ");
			jLblAc0 = new JLabel();
			jLblAc0.setBounds(new Rectangle(15, 18, 146, 20));
			jLblAc0.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc0.setText("CwMin(Ac0) = CW_MIN / ");
			jPCwMin = new JPanel();
			jPCwMin.setLayout(null);
			jPCwMin.setBounds(new Rectangle(15, 117, 205, 120));
			jPCwMin.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "CwMin Division Factor", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPCwMin.add(jLblAc0, null);
			jPCwMin.add(jLblAc1, null);
			jPCwMin.add(jLblAc3, null);
			jPCwMin.add(jLblAc2, null);
			jPCwMin.add(getJTxtCwMin0(), null);
			jPCwMin.add(getJTxtCwMin1(), null);
			jPCwMin.add(getJTxtCwMin2(), null);
			jPCwMin.add(getJTxtCwMin3(), null);
		}
		return jPCwMin;
	}

	private JPanel getJPCwMax() {
		if (jPCwMax == null) {
			jLblAc21 = new JLabel();
			jLblAc21.setBounds(new Rectangle(15, 93, 155, 20));
			jLblAc21.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc21.setText("CwMax(Ac3) = CW_MAX / ");
			jLblAc31 = new JLabel();
			jLblAc31.setBounds(new Rectangle(15, 68, 155, 20));
			jLblAc31.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc31.setText("CwMax(Ac2) = CW_MAX / ");
			jLblAc11 = new JLabel();
			jLblAc11.setBounds(new Rectangle(15, 43, 155, 20));
			jLblAc11.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc11.setText("CwMax(Ac1) = CW_MAX / ");
			jLblAc01 = new JLabel();
			jLblAc01.setBounds(new Rectangle(15, 18, 155, 20));
			jLblAc01.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc01.setText("CwMax(Ac0) = CW_MAX / ");
			jPCwMax = new JPanel();
			jPCwMax.setLayout(null);
			jPCwMax.setBounds(new Rectangle(260, 117, 218, 122));
			jPCwMax.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "CwMax Division Factor", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPCwMax.add(jLblAc01, null);
			jPCwMax.add(jLblAc11, null);
			jPCwMax.add(jLblAc31, null);
			jPCwMax.add(jLblAc21, null);
			jPCwMax.add(getJTxtCwMax0(), null);
			jPCwMax.add(getJTxtCwMax1(), null);
			jPCwMax.add(getJTxtCwMax2(), null);
			jPCwMax.add(getJTxtCwMax3(), null);
		}
		return jPCwMax;
	}


	private JPanel getJPAifs() {
		if (jPAifs == null) {
			jLblAc211 = new JLabel();
			jLblAc211.setBounds(new Rectangle(15, 93, 70, 20));
			jLblAc211.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc211.setText("AIFS(AC3):");
			jLblAc311 = new JLabel();
			jLblAc311.setBounds(new Rectangle(15, 68, 70, 20));
			jLblAc311.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc311.setText("AIFS(AC2):");
			jLblAc111 = new JLabel();
			jLblAc111.setBounds(new Rectangle(15, 43, 70, 20));
			jLblAc111.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc111.setText("AIFS(AC1):");
			jLblAc011 = new JLabel();
			jLblAc011.setBounds(new Rectangle(15, 18, 70, 20));
			jLblAc011.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblAc011.setText("AIFS(AC0):");
			jPAifs = new JPanel();
			jPAifs.setLayout(null);
			jPAifs.setBounds(new Rectangle(517, 117, 139, 122));
			jPAifs.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "AIFS", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPAifs.add(jLblAc011, null);
			jPAifs.add(jLblAc111, null);
			jPAifs.add(jLblAc311, null);
			jPAifs.add(jLblAc211, null);
			jPAifs.add(getJTxtAifs0(), null);
			jPAifs.add(getJTxtAifs1(), null);
			jPAifs.add(getJTxtAifs2(), null);
			jPAifs.add(getJTxtAifs3(), null);
		}
		return jPAifs;
	}

	private JTextField getJTxtCwMin0() {
		if (jTxtCwMin0 == null) {
			jTxtCwMin0 = new JTextField();
			jTxtCwMin0.setBounds(new Rectangle(163, 18, 25, 20));
			jTxtCwMin0.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMin0.setText("1");
		}
		return jTxtCwMin0;
	}

	private JTextField getJTxtCwMin1() {
		if (jTxtCwMin1 == null) {
			jTxtCwMin1 = new JTextField();
			jTxtCwMin1.setBounds(new Rectangle(163, 43, 25, 20));
			jTxtCwMin1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMin1.setText("2");
		}
		return jTxtCwMin1;
	}

	private JTextField getJTxtCwMin2() {
		if (jTxtCwMin2 == null) {
			jTxtCwMin2 = new JTextField();
			jTxtCwMin2.setBounds(new Rectangle(163, 68, 25, 20));
			jTxtCwMin2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMin2.setText("4");
		}
		return jTxtCwMin2;
	}

	private JTextField getJTxtCwMin3() {
		if (jTxtCwMin3 == null) {
			jTxtCwMin3 = new JTextField();
			jTxtCwMin3.setBounds(new Rectangle(163, 93, 25, 20));
			jTxtCwMin3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMin3.setText("8");
		}
		return jTxtCwMin3;
	}

	private JTextField getJTxtCwMax0() {
		if (jTxtCwMax0 == null) {
			jTxtCwMax0 = new JTextField();
			jTxtCwMax0.setBounds(new Rectangle(175, 18, 25, 20));
			jTxtCwMax0.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMax0.setText("1");
		}
		return jTxtCwMax0;
	}

	private JTextField getJTxtCwMax1() {
		if (jTxtCwMax1 == null) {
			jTxtCwMax1 = new JTextField();
			jTxtCwMax1.setBounds(new Rectangle(175, 43, 25, 20));
			jTxtCwMax1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMax1.setText("4");
		}
		return jTxtCwMax1;
	}

	private JTextField getJTxtCwMax2() {
		if (jTxtCwMax2 == null) {
			jTxtCwMax2 = new JTextField();
			jTxtCwMax2.setBounds(new Rectangle(175, 68, 25, 20));
			jTxtCwMax2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMax2.setText("8");
		}
		return jTxtCwMax2;
	}

	private JTextField getJTxtCwMax3() {
		if (jTxtCwMax3 == null) {
			jTxtCwMax3 = new JTextField();
			jTxtCwMax3.setBounds(new Rectangle(175, 93, 25, 20));
			jTxtCwMax3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtCwMax3.setText("16");
		}
		return jTxtCwMax3;
	}

	private JTextField getJTxtAifs0() {
		if (jTxtAifs0 == null) {
			jTxtAifs0 = new JTextField();
			jTxtAifs0.setBounds(new Rectangle(90, 18, 25, 20));
			jTxtAifs0.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtAifs0.setText("2");
		}
		return jTxtAifs0;
	}

	private JTextField getJTxtAifs1() {
		if (jTxtAifs1 == null) {
			jTxtAifs1 = new JTextField();
			jTxtAifs1.setBounds(new Rectangle(90, 43, 25, 20));
			jTxtAifs1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtAifs1.setText("2");
		}
		return jTxtAifs1;
	}

	private JTextField getJTxtAifs2() {
		if (jTxtAifs2 == null) {
			jTxtAifs2 = new JTextField();
			jTxtAifs2.setBounds(new Rectangle(90, 68, 25, 20));
			jTxtAifs2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtAifs2.setText("1");
		}
		return jTxtAifs2;
	}

	private JTextField getJTxtAifs3() {
		if (jTxtAifs3 == null) {
			jTxtAifs3 = new JTextField();
			jTxtAifs3.setBounds(new Rectangle(90, 93, 25, 20));
			jTxtAifs3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtAifs3.setText("1");
		}
		return jTxtAifs3;
	}

	private JButton getJBtnOK() {
		if (jBtnOK == null) {
			jBtnOK = new JButton();
			jBtnOK.setBounds(new Rectangle(15, 255, 100, 20));
			jBtnOK.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnOK.setText("OK");
			jBtnOK.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					writeToNtConf();
					Edca.this.setVisible(false);
					pm.setEnabled(true);
					pm.setVisible(true);
				}
			});
		}
		return jBtnOK;
	}

	private JButton getJBtnCancel() {
		if (jBtnCancel == null) {
			jBtnCancel = new JButton();
			jBtnCancel.setBounds(new Rectangle(554, 255, 100, 20));
			jBtnCancel.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnCancel.setText("Cancel");
			jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					getInitParams();
					Edca.this.setVisible(false);
					pm.setEnabled(true);
					pm.setVisible(true);
				}
			});
		}
		return jBtnCancel;
	}
	
//	This is used in the listener of the parent object. Gets the values of all elements and stores it to some 'buffer' objects
	public void setInitParams(){
		cwmin0=jTxtCwMin0.getText();
		cwmin1=jTxtCwMin1.getText();
		cwmin2=jTxtCwMin2.getText();
		cwmin3=jTxtCwMin3.getText();
		cwmax0=jTxtCwMax0.getText();
		cwmax1=jTxtCwMax1.getText();
		cwmax2=jTxtCwMax2.getText();
		cwmax3=jTxtCwMax3.getText();
		aifs0=jTxtAifs0.getText();
		aifs1=jTxtAifs1.getText();
		aifs2=jTxtAifs2.getText();
		aifs3=jTxtAifs3.getText();
	}
	
	//Sets the elements  to the values that they had when the window was opened. Used for the cancel button
	private void getInitParams(){
		jTxtCwMin0.setText(cwmin0);
		jTxtCwMin1.setText(cwmin1);
		jTxtCwMin2.setText(cwmin2);
		jTxtCwMin3.setText(cwmin3);
		jTxtCwMax0.setText(cwmax0);
		jTxtCwMax1.setText(cwmax1);
		jTxtCwMax2.setText(cwmax2);
		jTxtCwMax3.setText(cwmax3);
		jTxtAifs0.setText(aifs0);
		jTxtAifs1.setText(aifs1);
		jTxtAifs2.setText(aifs2);
		jTxtAifs3.setText(aifs3);
	}
	
	//Writes the parameters to the MakeNtConf instance arguements
	private void writeToNtConf(){
		mk.setCwmin0(jTxtCwMin0.getText());
		mk.setCwmin1(jTxtCwMin1.getText());
		mk.setCwmin2(jTxtCwMin2.getText());
		mk.setCwmin3(jTxtCwMin3.getText());
		mk.setCwmax0(jTxtCwMax0.getText());
		mk.setCwmax1(jTxtCwMax1.getText());
		mk.setCwmax2(jTxtCwMax2.getText());
		mk.setCwmax3(jTxtCwMax3.getText());
		mk.setAifs0(jTxtAifs0.getText());
		mk.setAifs1(jTxtAifs1.getText());
		mk.setAifs2(jTxtAifs2.getText());
		mk.setAifs3(jTxtAifs3.getText());
	}

	/**
	 * This method initializes jPnIcon	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPnIcon() {
		if (jPnIcon == null) {
			jPnIcon = new JPanel();
			jPnIcon.setLayout(null);
			jPnIcon.setBounds(new Rectangle(15, 15, 91, 87));
			jPnIcon.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jPnIcon.add(jLblIcon, null);
		}
		return jPnIcon;
	}
}  //  @jve:decl-index=0:visual-constraint="10,10" 
