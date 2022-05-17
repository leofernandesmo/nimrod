package pamvotis.intf;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;


public class Pamvotis extends JFrame {

	private static final long serialVersionUID = 1L;
	private Run rn = null;
	private Config cf = null;
	private Stats st = null;
	private MakeNtConf mk = null;
	private About ab = null;
	private Lisence lc=null;
	private Edca ed=null;
	
	private JPanel jContentPane = null;
	
	private JButton jBtnRun = null;

	private JButton jButton3 = null;
	private JButton jBtConf = null;
	private JButton jBtnStats = null;
	private JButton jBtnNtConf = null;
	private ButtonGroup ntGroup = new ButtonGroup();  //  @jve:decl-index=0:
	
	private JFileChooser jFcNtConf=new JFileChooser();
	private JFileChooser jFcSvRes = new JFileChooser();
	private JFileChooser jFcView = new JFileChooser();
	
	//Filter xls files
	class NtConfFilter extends FileFilter {

	    public boolean accept(File f) {
	        if (f.isDirectory()) return true;
	        String extension = f.getName();
	        if (extension != null) {
	            if(extension.endsWith("xls")||extension.endsWith("txt"))return true;
	            else return false;
	        }
	        return false;
	    }
	    
	    public String getDescription(){return "Network Configuration Filtering";}
	}

	//Filter txt files
	class ViewFilter extends FileFilter {

	    public boolean accept(File f) {
	        if (f.isDirectory()) return true;
	        String extension = f.getName();
	        if (extension != null) {
	            if (extension.endsWith("txt"))return true;
	            else return false;
	        }
	        return false;
	    }
	    
	    public String getDescription(){return "Network Configuration Filtering";}
	}
	
	private JLabel jLblNtConf = null;
	private JButton jBtnSvRes = null;
	private JRadioButton jRbGlobal = null;
	private JRadioButton jRbNtConf = null;
	private JTextField jTxtValues = null;
	private JLabel jLblValues = null;
	private JLabel jLblResults = null;
	private JLabel jLblSvRes = null;
	private JTextField jTxtSeed = null;
	private JLabel jLblSeed = null;
	private JLabel jLblTime = null;
	private JTextField jTxtTime = null;
	private JPanel jPanelRts = null;
	private JComboBox jCmbRts = null;
	private JTextField jTxtThr = null;
	private JLabel jLblThr = null;
	private JLabel jLblProt = null;
	
	private JButton getJBtnRun() {
		if (jBtnRun == null) {
			jBtnRun = new JButton();
			jBtnRun.setBounds(new Rectangle(15, 282, 119, 20));
			jBtnRun.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnRun.setText("Run Simulation");
			jBtnRun.addActionListener(new java.awt.event.ActionListener() {   
				public void actionPerformed(java.awt.event.ActionEvent e) {
					CheckAndStart();
				}
			
			});
		}
		return jBtnRun;
	}

	private JButton getJButton3() {
		if (jButton3 == null) {
			jButton3 = new JButton();
			jButton3.setBounds(new Rectangle(692, 281, 119, 20));
			jButton3.setFont(new Font("Dialog", Font.PLAIN, 12));
			jButton3.setText("Exit");
			jButton3.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return jButton3;
	}

	private JButton getJBtConf() {
		if (jBtConf == null) {
			jBtConf = new JButton();
			jBtConf.setText("Configure");
			jBtConf.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtConf.setBounds(new Rectangle(190, 25, 91, 22));
			jBtConf.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					cf.setVisible(true);
					cf.setInitParams();
					Pamvotis.this.setEnabled(false);
				}
			});
		}
		return jBtConf;
	}

	private JButton getJBtnStats() {
		if (jBtnStats == null) {
			jBtnStats = new JButton();
			jBtnStats.setText("Choose Statistics");
			jBtnStats.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnStats.setBounds(new Rectangle(255, 25, 137, 20));
			jBtnStats.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					st.setVisible(true);
					st.setInitParams();
					Pamvotis.this.setEnabled(false);
				}
			});
		}
		return jBtnStats;
	}

	private JButton getJBtnNtConf() {
		if (jBtnNtConf == null) {
			jBtnNtConf = new JButton();
			jBtnNtConf.setEnabled(false);
			jBtnNtConf.setBounds(new Rectangle(240, 58, 34, 20));
			jBtnNtConf.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnNtConf.setText("...");
			jBtnNtConf.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					int returnVal = jFcNtConf.showOpenDialog(Pamvotis.this);
					if (returnVal == JFileChooser.APPROVE_OPTION){
						String str = jFcNtConf.getSelectedFile().getAbsolutePath();
						jFcNtConf.setCurrentDirectory(jFcNtConf.getSelectedFile());
						mk.setNtConfPath(str);
						jLblNtConf.setText(str);
					}
				}
			});
		}
		return jBtnNtConf;
	}

	private JButton getJBtnSvRes() {
		if (jBtnSvRes == null) {
			jBtnSvRes = new JButton();
			jBtnSvRes.setText("...");
			jBtnSvRes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnSvRes.setBounds(new Rectangle(205, 58, 40, 20));
			jBtnSvRes.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jFcSvRes.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					int returnVal = jFcSvRes.showSaveDialog(Pamvotis.this);
					if (returnVal == JFileChooser.APPROVE_OPTION){
						String str = jFcSvRes.getSelectedFile().getAbsolutePath();
						jFcSvRes.setCurrentDirectory(jFcSvRes.getSelectedFile());
						//Remove the file separator if exists
						if(str.endsWith("\\")||str.endsWith("/")){
							str = str.substring(0, str.length()-1);
						}
						mk.setResultsPath(str);
						jLblSvRes.setText(str);
					}
				    
				}
			});
		}
		return jBtnSvRes;
	}

	private JRadioButton getJRbGlobal() {
		if (jRbGlobal == null) {
			jRbGlobal = new JRadioButton();
			jRbGlobal.setSelected(true);
			jRbGlobal.setBounds(new Rectangle(15, 25, 171, 20));
			jRbGlobal.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbGlobal.setText("Globally Configure Nodes");
			jRbGlobal.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jBtnNtConf.setEnabled(false);
					jBtConf.setEnabled(true);
				}
			});
			ntGroup.add(jRbGlobal);
		}
		return jRbGlobal;
	}

	private JRadioButton getJRbNtConf() {
		if (jRbNtConf == null) {
			jRbNtConf = new JRadioButton();
			jRbNtConf.setText("Specify Network Configuration File");
			jRbNtConf.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbNtConf.setBounds(new Rectangle(15, 58, 220, 20));
			jRbNtConf.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jBtnNtConf.setEnabled(true);
					jBtConf.setEnabled(false);
				}
			});
			ntGroup.add(jRbNtConf);
		}
		return jRbNtConf;
	}

	private JTextField getJTxtValues() {
		if (jTxtValues == null) {
			jTxtValues = new JTextField();
			jTxtValues.setText("100");
			jTxtValues.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtValues.setBounds(new Rectangle(205, 25, 40, 20));
		}
		return jTxtValues;
	}

	private JTextField getJTxtSeed() {
		if (jTxtSeed == null) {
			jTxtSeed = new JTextField();
			jTxtSeed.setText("69");
			jTxtSeed.setBounds(new Rectangle(158, 25, 40, 20));
			jTxtSeed.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtSeed.setToolTipText("");
		}
		return jTxtSeed;
	}

	private JTextField getJTxtTime() {
		if (jTxtTime == null) {
			jTxtTime = new JTextField();
			jTxtTime.setText("300");
			jTxtTime.setFont(new Font("Dialog", Font.PLAIN, 12));
			jTxtTime.setBounds(new Rectangle(158, 58, 40, 20));
		}
		return jTxtTime;
	}

	private JPanel getJPanelRts() {
		if (jPanelRts == null) {
			jLblProt = new JLabel();
			jLblProt.setBounds(new Rectangle(15, 25, 135, 20));
			jLblProt.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblProt.setText("Access Mechanism:");
			jLblThr = new JLabel();
			jLblThr.setBounds(new Rectangle(13, 59, 126, 20));
			jLblThr.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblThr.setText("RTS Threshold (bits):");
			jPanelRts = new JPanel();
			jPanelRts.setLayout(null);
			jPanelRts.setBounds(new Rectangle(15, 130, 279, 123));
			jPanelRts.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Mac Layer Configuration", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelRts.add(getJCmbRts(), null);
			jPanelRts.add(getJTxtThr(), null);
			jPanelRts.add(jLblThr, null);
			jPanelRts.add(jLblProt, null);
			jPanelRts.add(getJBtnEDCA(), null);
		}
		return jPanelRts;
	}

	private JComboBox getJCmbRts() {
		if (jCmbRts == null) {
			jCmbRts = new JComboBox();
			jCmbRts.setBounds(new Rectangle(150, 25, 113, 21));
			jCmbRts.setSelectedIndex(-1);
			jCmbRts.setFont(new Font("Dialog", Font.PLAIN, 12));
			jCmbRts.setMaximumRowCount(3);
			jCmbRts.addItem("Basic Access");
			jCmbRts.addItem("RTS/CTS");
			jCmbRts.addItem("CTS-to-Self");
			jCmbRts.addActionListener(listen);
		}
		return jCmbRts;
	}
	private class RTSListener implements ActionListener{
		public void actionPerformed(java.awt.event.ActionEvent e) {
			if(((JComboBox)e.getSource()).getSelectedIndex()==0){
				jTxtThr.setEnabled(false);
				mk.setCtsToSelf("n");
			}
			else if(((JComboBox)e.getSource()).getSelectedIndex()==1){
				jTxtThr.setEnabled(true);
				mk.setCtsToSelf("n");
			}
			else {
				jTxtThr.setEnabled(true);
				mk.setCtsToSelf("y");
			}
		}
	}
	
	private RTSListener listen = new RTSListener();  //  @jve:decl-index=0:
	private JPanel jPanelEvents = null;
	private JPanel jPanelStats = null;
	private JPanel jPanelNtConf = null;
	private JMenuBar jJMenuBar = null;
	private JMenu jMenuFile = null;
	private JMenu jMenuHelp = null;
	private JMenuItem jMenuItemExit = null;
	private JMenuItem jMenuItemOnline = null;
	private JMenuItem jMenuItemAbout = null;
	private JLabel jLblPamv = null;
	private JButton jBtnView = null;
	private JRadioButton jRbXml = null;
	private JMenuItem jMILisence = null;
	private JButton jBtnEDCA = null;
	private JTextField getJTxtThr() {
		if (jTxtThr == null) {
			jTxtThr = new JTextField();
			jTxtThr.setBounds(new Rectangle(149, 59, 40, 20));
			jTxtThr.setEnabled(false);
		}
		return jTxtThr;
	}

	private JPanel getJPanelEvents() {
		if (jPanelEvents == null) {
			jPanelEvents = new JPanel();
			jPanelEvents.setLayout(null);
			jPanelEvents.setBounds(new Rectangle(160, 15, 216, 95));
			jPanelEvents.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Events Configuration", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelEvents.add(getJTxtSeed(), null);
			jPanelEvents.add(jLblSeed, null);
			jPanelEvents.add(jLblTime, null);
			jPanelEvents.add(getJTxtTime(), null);
		}
		return jPanelEvents;
	}

	private JPanel getJPanelStats() {
		if (jPanelStats == null) {
			jPanelStats = new JPanel();
			jPanelStats.setLayout(null);
			jPanelStats.setBounds(new Rectangle(400, 15, 413, 95));
			jPanelStats.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Statistic Results Configuration", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelStats.add(jLblValues, null);
			jPanelStats.add(getJTxtValues(), null);
			jPanelStats.add(jLblResults, null);
			jPanelStats.add(getJBtnSvRes(), null);
			jPanelStats.add(getJBtnStats(), null);
			jPanelStats.add(jLblSvRes, null);
		}
		return jPanelStats;
	}

	private JPanel getJPanelNtConf() {
		if (jPanelNtConf == null) {
			jPanelNtConf = new JPanel();
			jPanelNtConf.setLayout(null);
			jPanelNtConf.setBounds(new Rectangle(312, 130, 502, 123));
			jPanelNtConf.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(SystemColor.controlDkShadow, 1), "Nodes Configuration", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION, new Font("Dialog", Font.BOLD, 12), SystemColor.controlDkShadow));
			jPanelNtConf.add(getJBtConf(), null);
			jPanelNtConf.add(jLblNtConf, null);
			jPanelNtConf.add(getJRbNtConf(), null);
			jPanelNtConf.add(getJBtnNtConf(), null);
			jPanelNtConf.add(getJRbGlobal(), null);
			jPanelNtConf.add(getJRbXml(), null);
		}
		return jPanelNtConf;
	}

	private JMenuBar getJJMenuBar() {
		if (jJMenuBar == null) {
			jJMenuBar = new JMenuBar();
			jJMenuBar.add(getJMenuFile());
			jJMenuBar.add(getJMenuHelp());
		}
		return jJMenuBar;
	}

private JMenu getJMenuFile() {
	if (jMenuFile == null) {
		jMenuFile = new JMenu();
		jMenuFile.setPreferredSize(new Dimension(60, 20));
		jMenuFile.setText("File");
		jMenuFile.setFont(new Font("Dialog", Font.PLAIN, 12));
		jMenuFile.setMnemonic(KeyEvent.VK_UNDEFINED);
		jMenuFile.setBounds(new Rectangle(0, 0, 40, 20));
		jMenuFile.add(getJMILisence());
		jMenuFile.addSeparator();
		jMenuFile.add(getJMenuItemExit());
	}
	return jMenuFile;
}

	private JMenu getJMenuHelp() {
		if (jMenuHelp == null) {
			jMenuHelp = new JMenu();
			jMenuHelp.setPreferredSize(new Dimension(40, 20));
			jMenuHelp.setText("Help");
			jMenuHelp.setFont(new Font("Dialog", Font.PLAIN, 12));
			jMenuHelp.add(getJMenuItemOnline());
			jMenuHelp.addSeparator();
			jMenuHelp.add(getJMenuItemAbout());
		}
		return jMenuHelp;
	}

	private JMenuItem getJMenuItemExit() {
		if (jMenuItemExit == null) {
			jMenuItemExit = new JMenuItem();
			jMenuItemExit.setPreferredSize(new Dimension(40, 20));
			jMenuItemExit.setFont(new Font("Dialog", Font.PLAIN, 12));
			jMenuItemExit.setText("Exit");
			jMenuItemExit.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					System.exit(0);
				}
			});
		}
		return jMenuItemExit;
	}

	private JMenuItem getJMenuItemOnline() {
		if (jMenuItemOnline == null) {
			jMenuItemOnline = new JMenuItem();
			jMenuItemOnline.setPreferredSize(new Dimension(80, 20));
			jMenuItemOnline.setFont(new Font("Dialog", Font.PLAIN, 12));
			jMenuItemOnline.setText("Online Documentation");
			jMenuItemOnline.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//new Opener().openUrl(new File("doc"+File.separator+"Online_doc.html").getAbsolutePath());
					new Opener('u',new File("doc"+File.separator+"Online_doc.html").getAbsolutePath()).start();
				}
			});
		}
		return jMenuItemOnline;
	}

	private JMenuItem getJMenuItemAbout() {
		if (jMenuItemAbout == null) {
			jMenuItemAbout = new JMenuItem();
			jMenuItemAbout.setPreferredSize(new Dimension(140, 20));
			jMenuItemAbout.setFont(new Font("Dialog", Font.PLAIN, 12));
			jMenuItemAbout.setText("About Pamvotis");
			jMenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Pamvotis.this.setEnabled(false);
					ab.setVisible(true);
				}
			});
		}
		return jMenuItemAbout;
	}

	private JButton getJBtnView() {
		if (jBtnView == null) {
			jBtnView = new JButton();
			jBtnView.setBounds(new Rectangle(343, 283, 118, 19));
			jBtnView.setEnabled(false);
			jBtnView.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnView.setText("View Results");
			jBtnView.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					//System.out.println(jFcSvRes.get);
					jFcView.setCurrentDirectory(new File(jLblSvRes.getText()));
					int returnVal = jFcView.showOpenDialog(Pamvotis.this);
					if (returnVal == JFileChooser.APPROVE_OPTION){
						jFcView.setCurrentDirectory(jFcSvRes.getSelectedFile());
						new Opener('t',jFcView.getSelectedFile().getAbsolutePath()).start();
					}
				}
			});
		}
		return jBtnView;
	}

	private JRadioButton getJRbXml() {
		if (jRbXml == null) {
			jRbXml = new JRadioButton();
			jRbXml.setBounds(new Rectangle(15, 91, 229, 16));
			jRbXml.setFont(new Font("Dialog", Font.PLAIN, 12));
			jRbXml.setText("Use the Application's XML File");
			jRbXml.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jBtConf.setEnabled(false);
					jBtnNtConf.setEnabled(false);
					jLblNtConf.setEnabled(false);
				}
			});
			ntGroup.add(jRbXml);
		}
		return jRbXml;
	}

	private JMenuItem getJMILisence() {
		if (jMILisence == null) {
			jMILisence = new JMenuItem();
			jMILisence.setName("Lisence");
			jMILisence.setActionCommand("Lisence");
			jMILisence.setText("Lisence");
			jMILisence.setPreferredSize(new Dimension(60, 20));
			jMILisence.setFont(new Font("Dialog", Font.PLAIN, 12));
			jMILisence.setMnemonic(KeyEvent.VK_UNDEFINED);
			jMILisence.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					Pamvotis.this.setEnabled(false);
					lc.setVisible(true);
				}
			});
		}
		return jMILisence;
	}

	/**
	 * This method initializes jBtnEDCA	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getJBtnEDCA() {
		if (jBtnEDCA == null) {
			jBtnEDCA = new JButton();
			jBtnEDCA.setBounds(new Rectangle(13, 90, 250, 20));
			jBtnEDCA.setFont(new Font("Dialog", Font.PLAIN, 12));
			jBtnEDCA.setText("IEEE 802.11e EDCA Configuration");
			jBtnEDCA.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					ed.setVisible(true);
					ed.setInitParams();
					Pamvotis.this.setEnabled(false);
				}
			});
		}
		return jBtnEDCA;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Pamvotis thisClass = new Pamvotis();
				thisClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				thisClass.setVisible(true);
			}
		});
	}
	
	public Pamvotis() {
		super();
		initialize();
	}

	private void initialize() {
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(834, 386);
		URL iconURL = getClass().getResource("/resources" + File.separator + "icons"+File.separator+"logo.jpg");
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(iconURL));
		this.setResizable(false);
		this.setLocation(new Point((d.width-this.getSize().width)/2, (d.height-this.getSize().height)/2));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setJMenuBar(getJJMenuBar());
		this.setContentPane(getJContentPane());
		this.setTitle("Pamvotis 1.1 - WLAN Simulator");
		//this.setJMenuBar(new Menu());
		jFcNtConf.setCurrentDirectory(new File("config"));
		jFcNtConf.setFileFilter(new NtConfFilter());
		jFcSvRes.setCurrentDirectory(new File("."));
		jFcView.setCurrentDirectory(new File("."));
		jFcView.setFileFilter(new ViewFilter());
		rn = new Run(this);
		mk = new MakeNtConf();
		cf = new Config(this, mk);
		st=new Stats(this, mk);
		ab = new About(this);
		lc = new Lisence(this);
		ed=new Edca(this,mk);
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jLblPamv = new JLabel();
			jLblPamv.setBounds(new Rectangle(15, 15, 123, 95));
			jLblPamv.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			URL iconURL = getClass().getResource("resources" + File.separator + "icons"+File.separator+"pamvotis.jpg");
			jLblPamv.setIcon(new ImageIcon(iconURL));
			jLblPamv.setText("");
			jLblTime = new JLabel();
			jLblTime.setText("Simulation Time (sec):");
			jLblTime.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblTime.setBounds(new Rectangle(20, 58, 128, 20));
			jLblSeed = new JLabel();
			jLblSeed.setText("Seed:");
			jLblSeed.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblSeed.setBounds(new Rectangle(105, 25, 40, 20));
			jLblSvRes = new JLabel();
			jLblSvRes.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
			jLblSvRes.setBounds(new Rectangle(258, 58, 135, 20));
			jLblSvRes.setDisplayedMnemonic(KeyEvent.VK_UNDEFINED);
			jLblSvRes.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblSvRes.setText(new File(".").getAbsolutePath());
			jLblResults = new JLabel();
			jLblResults.setText("Directory to save results:");
			jLblResults.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblResults.setBounds(new Rectangle(21, 58, 152, 20));
			jLblValues = new JLabel();
			jLblValues.setText("Number of values per statistic:");
			jLblValues.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblValues.setBounds(new Rectangle(20, 25, 180, 20));
			jLblNtConf = new JLabel();
			jLblNtConf.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.black));
			jLblNtConf.setBounds(new Rectangle(280, 58, 201, 20));
			jLblNtConf.setFont(new Font("Dialog", Font.PLAIN, 12));
			jLblNtConf.setText("");
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setName("");
			jContentPane.add(getJBtnRun(), null);
			jContentPane.add(getJButton3(), null);
			jContentPane.add(getJPanelRts(), null);
			jContentPane.add(getJPanelEvents(), null);
			jContentPane.add(getJPanelStats(), null);
			jContentPane.add(getJPanelNtConf(), null);
			jContentPane.add(jLblPamv, null);
			jContentPane.add(getJBtnView(), null);
		}
		return jContentPane;
	}
	
	private void CheckAndStart(){
		if(jTxtSeed.getText().length()==0)
			JOptionPane.showMessageDialog(Pamvotis.this,"Seed must be a positive integer.","Error - Seed",JOptionPane.ERROR_MESSAGE);
		else if (jTxtValues.getText().length()==0)
			JOptionPane.showMessageDialog(Pamvotis.this,"Number of collected values must be a positive integer.","Error - Values",JOptionPane.ERROR_MESSAGE);
		else if (jTxtTime.getText().length()==0)
			JOptionPane.showMessageDialog(Pamvotis.this,"Simulation time must be a positive integer.","Error - Simulation Time",JOptionPane.ERROR_MESSAGE);
		else if (jTxtThr.isEnabled()&&(jTxtThr.getText().length()==0))
			JOptionPane.showMessageDialog(Pamvotis.this,"RTS Threshold must be a positive integer.","Error - RTS Threshold",JOptionPane.ERROR_MESSAGE);
		else if(jBtnNtConf.isEnabled()&&(jLblNtConf.getText()==null||jLblNtConf.getText().length()==0))
			JOptionPane.showMessageDialog(Pamvotis.this,"Specify a network configuration file.","Error - Network Configuration",JOptionPane.ERROR_MESSAGE);
		else{
			mk.setSeed(jTxtSeed.getText());
			mk.setDuration(jTxtTime.getText());
			mk.setValues(jTxtValues.getText());
			if(jTxtThr.isEnabled())
				mk.setRtsThr(jTxtThr.getText());
			else
				mk.setRtsThr("999999");
			if(jBtnNtConf.isEnabled()){
				if(jLblNtConf.getText().endsWith("xls"))
					mk.configWithFile();
				else
					mk.configInLinux();
			}
			if(jBtConf.isEnabled())
				mk.configureGlobally();
			rn.initialize();
			rn.setVisible(true);
			Pamvotis.this.setVisible(false);
			jBtnView.setEnabled(true);
		}
	}
}  //  @jve:decl-index=0:visual-constraint="10,10" 
