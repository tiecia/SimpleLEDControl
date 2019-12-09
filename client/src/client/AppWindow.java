package client;

import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JTabbedPane;

import createZoneDialog.CreateZoneDialog;
import hardware.Arduino;
import hardware.LEDStrip;
import hardwareConfigurator.HardwareConfigurator;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.awt.event.ActionEvent;

public class AppWindow {

	private JFrame frame;

	private JTabbedPane tabbedPane = null;
	private WelcomePanel welcomeScreen = null;

	private JMenuItem mntmRenameZone = new JMenuItem();
	private JMenuItem mntmSaveLayout = new JMenuItem();
	private JMenuItem mntmDeleteZone = new JMenuItem();
	
	private HardwareConfigurator conf = new HardwareConfigurator();
	
	private ArrayList<Zone> zones = new ArrayList<Zone>();
	
	//Actions
	private AddZoneAction addZoneAction = new AddZoneAction();
	private RenameZoneAction renameZoneAction = new RenameZoneAction();
	private RemoveZoneAction removeZoneAction = new RemoveZoneAction();
	
	//Action Listeners
	private LoadLayoutListener loadLayoutListener = new LoadLayoutListener();
	private ConfigureHardwareListener configureHardwareListener = new ConfigureHardwareListener();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppWindow window = new AppWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */

	public AppWindow() {
		welcomeScreen =  new WelcomePanel(configureHardwareListener, addZoneAction, loadLayoutListener);
		
		frame = new JFrame();
		frame.setBounds(100, 100, 822, 705);
		frame.setMinimumSize(new Dimension(1240, 700));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				conf.closeAllDevices();
			}
		});


		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		// Bar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnZone = new JMenu("Zone");
		menuBar.add(mnZone);
		
		//Add Zone
		JMenuItem mntmAddZone = new JMenuItem("Add Zone");
		mnZone.add(mntmAddZone);
		mntmAddZone.setAction(addZoneAction);
		mnZone.add(mntmRenameZone);
		
		//Rename Zone
		mntmRenameZone.setAction(renameZoneAction);
		mnZone.add(mntmDeleteZone);

		// Delete Zone
		mntmDeleteZone.setAction(removeZoneAction);
		
		JMenu mnLayout = new JMenu("Layout");
		menuBar.add(mnLayout);
		
		JMenuItem mntmNewLayout = new JMenuItem("New Layout");
		mnLayout.add(mntmNewLayout);
		mnLayout.add(mntmSaveLayout);
		mntmSaveLayout.setText("Save Layout");
		
				// Load Layout
				JMenuItem mntmLoadLayout = new JMenuItem("Load Layout");
				mnLayout.add(mntmLoadLayout);
				mntmLoadLayout.addActionListener(loadLayoutListener);
		mntmSaveLayout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File defaultLayoutPath = new File("C:\\git\\SimpleLEDControl\\layouts");
				defaultLayoutPath.mkdirs();
				
				JFileChooser saveDialog = new JFileChooser("C:\\git\\SimpleLEDControl\\layouts");
				int userInput = saveDialog.showSaveDialog(frame);
				//Wait for user
				
				File newLayoutFile;
				if(userInput == JFileChooser.APPROVE_OPTION) {
					String layoutPathAdress = saveDialog.getCurrentDirectory().getPath();
					newLayoutFile = new File(layoutPathAdress + "\\" + saveDialog.getSelectedFile().getName() + ".txt");
					int overrideUserInput = JOptionPane.OK_OPTION;
					if(newLayoutFile.exists()) { //Asks for file override
						overrideUserInput = JOptionPane.showConfirmDialog(tabbedPane,"Are you sure you want to override \"" + saveDialog.getSelectedFile().getName() + ".txt\"?", "Save Layout", JOptionPane.YES_NO_OPTION);
					}
					if(overrideUserInput == JOptionPane.OK_OPTION) { //If user is ok with overriding or no file is there to override
						try {
							newLayoutFile.createNewFile();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
						saveLayoutToFile(newLayoutFile);
					}
				}
			}
		});
		mntmNewLayout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int userInput = JOptionPane.showConfirmDialog(tabbedPane,"Are you sure you want to create a new layout?\r\nAny unsaved changes to this layout will NOT be saved." , "Create New Layout", JOptionPane.YES_NO_OPTION);
				if(userInput == JOptionPane.YES_OPTION) {
					newLayout();
				}
			}
		});
		
		JMenu mnHardware = new JMenu("Hardware");
		menuBar.add(mnHardware);
		
		JMenuItem mntmConfigureHardware = new JMenuItem("Configure Hardware");
		mnHardware.add(mntmConfigureHardware);
		mntmConfigureHardware.addActionListener(configureHardwareListener);
		updateZones();
	}
	
	
	private void newLayout() {
		conf.closeAllDevices();
		conf = new HardwareConfigurator();
		zones.clear();
		updateZones();
	}
	
	private void saveLayoutToFile(File file) {
		PrintStream out = null;
		try {
			out = new PrintStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		out.println("layout");
		out.println("hardware");
		out.println(conf.getArduinos().size());
		for(Arduino ard : conf.getArduinos()) {
			out.println(ard.getName());
			out.println(ard.getPort().getPortName());
			out.println(ard.getStrips().size());
			for(LEDStrip strip : ard.getStrips()) {
				out.println(strip.getName());
				out.println(strip.getPin());
				out.println(strip.getNumOfLEDs());
			}
		}
		out.println("zones");
		out.println(zones.size());
		for(Zone zone : zones) {
			out.println(zone.getName());
			out.println(zone.getStrips().size());
			for(LEDStrip strip : zone.getStrips()) {
				out.println(strip.getName());
				out.println(strip.getParentArduino().getPort().getPortName());
				out.println(strip.getPin());
				out.println(strip.getNumOfLEDs());
			}
		}
	}
	
	public void openLayoutFromFile(File file) {
		newLayout();
		Scanner s = null;
		try {
			s = new Scanner(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String next = null;
		try {
			//Check for Layout file
			next = s.nextLine();
			if(!next.equals("layout")) {
				invalidFile();
				return;
			} 
			
			//Load Hardware
			next = s.nextLine();
			if(!next.equals("hardware")) {
				invalidFile();
				return;
			}
			int numOfDevices = s.nextInt();
			s.nextLine();
			ArrayList<Arduino> devices = new ArrayList<Arduino>();
			for(int i = 0; i<numOfDevices; i++) {
				String ardName = s.nextLine();
				String ardPortName = s.nextLine();
				int numOfStrips = s.nextInt();
				s.nextLine();
				ArrayList<LEDStrip> strips = new ArrayList<LEDStrip>();
				for(int j = 0; j<numOfStrips; j++) {
					String stripName = s.nextLine();
					int stripPin = s.nextInt();
					int stripNum = s.nextInt();
					s.nextLine();
					strips.add(new LEDStrip(stripName, stripNum, stripPin));
				}
				devices.add(new Arduino(ardName, ardPortName, strips));
				if(!devices.get(devices.size()-1).isOpen()) {
					failedToConnect();
					return;
				}
				conf.setDevices(devices);
			}
			
			//Load Zones
			next = s.nextLine();
			int numOfZones = s.nextInt();
			s.nextLine();
			ArrayList<Zone> zones = new ArrayList<Zone>();
			for(int j = 0; j<numOfZones; j++) {
				String zoneName = s.nextLine();
				int numOfStrips = s.nextInt();
				s.nextLine();
				ArrayList<LEDStrip> strips = new ArrayList<LEDStrip>();
				for(int i = 0; i<numOfStrips; i++) {
					String stripName = s.nextLine();
					String portName = s.nextLine();
					int pin = s.nextInt();
					int numOfLEDs = s.nextInt();
					s.nextLine();
					LEDStrip correspondingStrip = conf.getCorrespondingStrip(stripName, portName, numOfLEDs, pin);
					if(correspondingStrip != null) {
						strips.add(correspondingStrip);
					} else {
						invalidFile();
						return;
					}
				}
				zones.add(new Zone(frame, zoneName, strips));
			}
			this.zones = zones;
			updateZones();
		} catch (NoSuchElementException e) {
			invalidFile();
			return;
		}
	}
	
	private void failedToConnect() {
		newLayout();
		JOptionPane.showMessageDialog(tabbedPane,"Failed to connect to one or more devices" , "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	private void invalidFile() {
		newLayout();
		JOptionPane.showMessageDialog(tabbedPane,"Invalid Layout File" , "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	//Updates the tabbedPane using the zones ArrayList as a model
	public void updateZones() {
		tabbedPane.removeAll();
		if(zones.size() > 0) {
			frame.remove(welcomeScreen);
			frame.getContentPane().add(tabbedPane);
			for(Zone zone : zones) {
				tabbedPane.add(zone);
			}
			renameZoneAction.checkState();
			removeZoneAction.checkState();
		} else {
			frame.getContentPane().remove(tabbedPane);
			frame.getContentPane().add(welcomeScreen);
		}
		frame.repaint();
		frame.revalidate();
	}
	
	
	//ACTIONS
	private class AddZoneAction extends AbstractAction {
		private static final long serialVersionUID = 1L;

		public AddZoneAction() {
			super("Add Zone");
		}
		
		public void actionPerformed(ActionEvent e) {
			System.out.println("New Zone");
			CreateZoneDialog dialog = new CreateZoneDialog(frame, conf.getArduinos());
			dialog.setModal(true);
			dialog.setBounds(200, 200, 300, 300);
			dialog.setVisible(true);
			//Wait for user
			
			if(dialog.getZone() != null) {
				zones.add(dialog.getZone());
				updateZones();
				if(zones.size() > 0) {
					renameZoneAction.setEnabled(true);
					removeZoneAction.setEnabled(true);
				}
			}
		}
	}
	
	private class RenameZoneAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public RenameZoneAction() {
			super("Rename Zone");
			if(zones.size() < 1) {
				this.setEnabled(false);
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			Zone currentZone = (Zone) tabbedPane.getSelectedComponent();
			String newName = JOptionPane.showInputDialog(frame, "Rename Zone", currentZone.getName());
			//Wait for user
			if(newName != null) {
				currentZone.setName(newName);
			}
			updateZones();
		}
		public void checkState() {
			if(zones.size() < 1) {
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
		}
	}
	
	private class RemoveZoneAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		public RemoveZoneAction() {
			super("Remove Zone");
			if(zones.size() < 1) {
				this.setEnabled(false);
			}
		}
		
		public void actionPerformed(ActionEvent e) {
			int currentZone = tabbedPane.getSelectedIndex();
			zones.remove(currentZone);
			updateZones();
			if(zones.size() < 1) {
				this.setEnabled(false);
				renameZoneAction.setEnabled(false);
			}
		}
		public void checkState() {
			if(zones.size() < 1) {
				this.setEnabled(false);
			} else {
				this.setEnabled(true);
			}
		}
	}
	
	//Action Listeners
	public class LoadLayoutListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			File defaultLayoutPath = new File("C:\\git\\SimpleLEDControl\\layouts");
			defaultLayoutPath.mkdirs();
			
			JFileChooser openDialog = new JFileChooser("C:\\git\\SimpleLEDControl\\layouts");
			int userInput = JFileChooser.CANCEL_OPTION;
			userInput = openDialog.showOpenDialog(frame);
			//Wait for user
			
			File selectedFile;
			if(userInput == JFileChooser.APPROVE_OPTION) {
				selectedFile = openDialog.getSelectedFile();
				openLayoutFromFile(selectedFile);
			}
		}
	}
	
	private class ConfigureHardwareListener implements ActionListener {
		public void actionPerformed(ActionEvent arg0) {
			conf.setVisible(true);
		}
	}
}