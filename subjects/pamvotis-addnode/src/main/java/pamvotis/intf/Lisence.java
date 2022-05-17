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
import javax.swing.JTextPane;
import javax.swing.border.BevelBorder;

public class Lisence extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel jContentPane = null;
	private JLabel jLblIcon = null;
	private JTextPane jTextPane = null;
	private JButton jButton = null;
	private Pamvotis pm=null;
	private JPanel jPanel = null;
	private JPanel jPanel1 = null;

	public Lisence(Pamvotis pmarg) {
		super();
		pm=pmarg;
		initialize();
	}


	private void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(660, 207);
		URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		this.setResizable(false);
		this.setLocation(new Point((d.width-this.getSize().width)/2, (d.height-this.getSize().height)/2));
		this.setContentPane(getJContentPane());
		this.setTitle("Pamvotis - Lisence Information");
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent e) {
				Lisence.this.setVisible(false);
				pm.setEnabled(true);
				pm.setVisible(true);
			}
		});
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLblIcon = new JLabel();
			URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"cuffs.jpg");
			jLblIcon.setIcon(new ImageIcon(iconURL));
			jLblIcon.setBounds(new Rectangle(4, 3, 120, 80));
			jLblIcon.setText("");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.add(getJButton(), null);
			jContentPane.add(getJPanel(), null);
			jContentPane.add(getJPanel1(), null);
		}
		return jContentPane;
	}

	private JTextPane getJTextPane() {
		if (jTextPane == null) {
			jTextPane = new JTextPane();
			jTextPane.setText("Pamvotis was designed for academic purposes only. It is not a commercial application. The application and the source code may be distributed or modified under the terms of the GNU General Public Lisence. See the Lisence.txt file located in the doc folder of the application directory for more information.");
			jTextPane.setBackground(new Color(153, 255, 153));
			jTextPane.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTextPane.setForeground(new Color(0, 0, 153));
			jTextPane.setBounds(new Rectangle(2, 2, 477, 83));
			jTextPane.setEditable(false);
		}
		return jTextPane;
	}

	private JButton getJButton() {
		if (jButton == null) {
			jButton = new JButton();
			jButton.setBounds(new Rectangle(261, 135, 124, 23));
			jButton.setText("OK");
			jButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Lisence.this.setVisible(false);
					pm.setEnabled(true);
					pm.setVisible(true);
				}
			});
		}
		return jButton;
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
			jPanel.setBounds(new Rectangle(148, 22, 480, 85));
			jPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jPanel.add(getJTextPane(), null);
		}
		return jPanel;
	}


	/**
	 * This method initializes jPanel1	
	 * 	
	 * @return javax.swing.JPanel	
	 */
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new JPanel();
			jPanel1.setLayout(null);
			jPanel1.setBounds(new Rectangle(11, 22, 127, 84));
			jPanel1.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			jPanel1.add(jLblIcon, null);
		}
		return jPanel1;
	}

}  //  @jve:decl-index=0:visual-constraint="10,10"
