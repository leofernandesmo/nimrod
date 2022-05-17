package pamvotis.intf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class About extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLabelIcon = null;
	private JPanel jPanel = null;
	private JLabel jLabel1 = null;
	private JLabel jLabel11 = null;
	private JLabel jLabel111 = null;
	private JLabel jLabel2 = null;
	private JLabel jLabel21 = null;
	private JLabel jLabel15 = null;
	private JLabel jLabel151 = null;
	private JButton jButton = null;
	Pamvotis pm=null;
	private JPanel jPanel1 = null;
	private JLabel jLabel12 = null;
	private JLabel jLabel112 = null;
	private JLabel jLabel211 = null;
	private JLabel jLabel22 = null;
	private JLabel jLabel = null;
	public About(Pamvotis pmarg) {
		super();
		pm=pmarg;
		initialize();
	}

	private void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(544, 408);
		URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		this.setResizable(false);
		this.setLocation(new Point((d.width-this.getSize().width)/2, (d.height-this.getSize().height)/2));
		this.setContentPane(getJContentPane());
		this.setTitle("About Pamvotis");
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				About.this.setVisible(false);
				pm.setEnabled(true);
				pm.setVisible(true);
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLabel151 = new JLabel();
			jLabel151.setBounds(new Rectangle(179, 312, 132, 21));
			jLabel151.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel151.setForeground(Color.blue);
			jLabel151.setText("support@pamvotis.org");
			jLabel15 = new JLabel();
			jLabel15.setBounds(new Rectangle(35, 285, 474, 20));
			jLabel15.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel15.setText("For any other comment, well intentioned or not, please contact the author:");
			jLabel21 = new JLabel();
			jLabel21.setText("and Vassilis Zafeiris.");
			jLabel21.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel21.setBounds(new Rectangle(84, 54, 309, 20));
			jLabel2 = new JLabel();
			jLabel2.setText("");
			jLabel2.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel2.setBounds(new Rectangle(84, 73, 304, 20));
			jLabel111 = new JLabel();
			jLabel111.setText("Copyright@ 2008 - Dimitris El. Vassis - Vassilis Zafeiris");
			jLabel111.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel111.setBounds(new Rectangle(15, 93, 334, 20));
			jLabel11 = new JLabel();
			jLabel11.setText("The application was created by Dimitris El. Vassis,");
			jLabel11.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel11.setBounds(new Rectangle(85, 32, 305, 20));
			jLabel1 = new JLabel();
			jLabel1.setText("Pamvotis 1.1 - WLAN 802.11a/b/g Simulator.");
			jLabel1.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel1.setBounds(new Rectangle(15, 11, 285, 20));
			jLabelIcon = new JLabel();
			jLabelIcon.setText("");
			URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
			jLabelIcon.setIcon(new ImageIcon(iconURL));
			jLabelIcon.setBounds(new Rectangle(15, 30, 66, 66));
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(jLabel15, null);
			jContentPane.add(jLabel151, null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJPanel1(), null);
		}
		return jContentPane;
	}

	private JPanel getJPanel() {
		if (jPanel == null) {
			jLabel = new JLabel();
			jLabel.setBounds(new Rectangle(418, 11, 86, 102));
			URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"pamvotis2.jpg");
			jLabel.setIcon(new ImageIcon(iconURL));
			jLabel.setText("");
			jPanel = new JPanel();
			jPanel.setLayout(null);
			jPanel.setBounds(new Rectangle(15, 20, 512, 125));
			jPanel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
			jPanel.add(jLabelIcon, null);
			jPanel.add(jLabel1, null);
			jPanel.add(jLabel11, null);
			jPanel.add(jLabel21, null);
			jPanel.add(jLabel2, null);
			jPanel.add(jLabel111, null);
			jPanel.add(jLabel, null);
		}
		return jPanel;
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(193, 340, 104, 23));
			jButton.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButton.setText("OK");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					About.this.setVisible(false);
					pm.setEnabled(true);
					pm.setVisible(true);
				}
			});
		}
		return jButton;
	}

	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jLabel22 = new JLabel();
			jLabel22.setBounds(new Rectangle(13, 75, 485, 20));
			jLabel22.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel22.setText("The logo of the appliation (the P letter) depicts the lake.");
			jLabel211 = new JLabel();
			jLabel211.setBounds(new Rectangle(13, 55, 466, 20));
			jLabel211.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel211.setText("world that has an island with permanent residents.");
			jLabel112 = new JLabel();
			jLabel112.setBounds(new Rectangle(13, 35, 463, 20));
			jLabel112.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel112.setText("which the creator of the simulator comes from. Pamvotis is the only lake in the");
			jLabel12 = new JLabel();
			jLabel12.setBounds(new Rectangle(13, 15, 466, 20));
			jLabel12.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLabel12.setText("Pamvotis took its name from a lake in Ioannina city of Greece, the city from ");
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(new Rectangle(14, 164, 513, 107));
			jPanel1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
			jPanel1.add(jLabel12, null);
			jPanel1.add(jLabel112, null);
			jPanel1.add(jLabel211, null);
			jPanel1.add(jLabel22, null);
		}
		return jPanel1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10" 
