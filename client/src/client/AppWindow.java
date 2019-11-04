package client;

import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import java.awt.GridLayout;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class AppWindow {

	private JFrame frame;

	private int numOfTabs = 0;

	private JTabbedPane tabbedPane = null;



	private JMenuItem mntmRenameZone = new JMenuItem("Rename Zone");
	private JMenuItem mntmSaveLayout = new JMenuItem("Save Layout");
	private JMenuItem mntmDeleteZone = new JMenuItem("Delete Zone");
	
	private HardwareConfigurator conf = new HardwareConfigurator();
	
	private ArrayList<Zone> zones = new ArrayList<Zone>();
	

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
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		/*
		 * Frame
		 */
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 807, 705);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

		/*
		 * Tabbed Pane
		 */

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frame.getContentPane().add(tabbedPane);

		/*
		 * Menu Bar
		 */

		// Bar
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		JSeparator separator = new JSeparator();

			// File Button
			JMenu mnFile = new JMenu("File");
			menuBar.add(mnFile);
			
				// Rename Zone
				mntmRenameZone.setEnabled(false);
				mntmRenameZone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int currentIndex = tabbedPane.getSelectedIndex();
						String currentName = tabbedPane.getTitleAt(currentIndex);
						String newName = JOptionPane.showInputDialog(frame, "Rename Zone", currentName);
						if (newName != null) {
							zones.get(currentIndex).setZoneName(newName);
							tabbedPane.setTitleAt(currentIndex, newName);
						}
					}
				});
	
				// Save Layout
				mntmSaveLayout.setEnabled(false);
				mntmSaveLayout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
	
				// Delete Zone
				mntmDeleteZone.setEnabled(false);
				mntmDeleteZone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
	
				//Add Zone
				JMenuItem mntmAddZone = new JMenuItem("Add Zone");
				mntmAddZone.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						int workingIndex = tabbedPane.getTabCount();
						CreateZoneDialog dialog = new CreateZoneDialog(conf.getArduinos(), zones);
						dialog.setModal(true);
						dialog.setBounds(200, 200, 300, 300);
						dialog.setVisible(true);
						
						
						System.out.println("Zones" + zones);
						if(zones.size() != 0) {
							numOfTabs++;
							mntmRenameZone.setEnabled(true);
							mntmDeleteZone.setEnabled(true);
							mntmSaveLayout.setEnabled(true);
							System.out.println("Zone Name: " + zones.get(workingIndex).getName());
							tabbedPane.addTab(zones.get(workingIndex).getName(), zones.get(workingIndex));
							System.out.println("Zones" + zones);
						} else {
							System.out.println("Is Empty");
						}
					}
				});
	
				// Load Layout
				JMenuItem mntmLoadLayout = new JMenuItem("Load Layout");
				mntmLoadLayout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
	
	
				//Set Default Layout
				JMenuItem mntmSetDefaultLayout = new JMenuItem("Set Default Layout");
				mntmSetDefaultLayout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});
	
				//Reset Default Layout
				JMenuItem mntmResetDefaultLayout = new JMenuItem("Reset Default Layout");
				mntmResetDefaultLayout.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						
					}
				});


		
		// Add all the menu items to the menu.
		mnFile.add(mntmAddZone);
		mnFile.add(mntmRenameZone);
		mnFile.add(mntmDeleteZone);
		mnFile.add(separator);
		mnFile.add(mntmLoadLayout);
		mnFile.add(mntmSaveLayout);
		mnFile.add(mntmSetDefaultLayout);
		
		JMenuItem mntmConfigureHardware = new JMenuItem("Configure Hardware");
		mntmConfigureHardware.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				configureHardware();
			}
		});
		mnFile.add(mntmConfigureHardware);
	}
	
	private void configureHardware() { //Creates and launches the HardwareConfigurator
		conf.setBounds(200, 200, 450, 300);
		conf.setVisible(true);
	}
}