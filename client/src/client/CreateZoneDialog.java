package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import java.awt.GridLayout;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import net.miginfocom.swing.MigLayout;
import java.awt.ScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;

public class CreateZoneDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private Arduino[] stagedArduinos;
	private ArduinoCheckBoxes[] arduinoBoxes;
	private StripCheckBoxes[] stripBoxes;
	private JTextField txtZoneName;
	
	private ArrayList<Zone> zones;
	private Zone newZone;

	/**
	 * Create the dialog.
	 */
	public CreateZoneDialog(ArrayList<Arduino> arduinoList, ArrayList<Zone> zones) {
		this.zones = zones;
		stagedArduinos = new Arduino[arduinoList.size()];
		/*
		 * 
		 * Content Pane
		 * 
		 */
		setBounds(100, 100, 420, 300);
		getContentPane().setLayout(new BorderLayout());
		
			/*
			 * Content Panel
			 */
			contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
			getContentPane().add(contentPanel, BorderLayout.CENTER);
			contentPanel.setLayout(new MigLayout("", "[141px,grow][141px,grow][141px,grow]", "[][][218px,grow]"));
			
				/*
				 * Content Panel Contents
				 */
		
				//Arduinos Label
				JLabel lblArduinos = new JLabel("Arduinos");
				contentPanel.add(lblArduinos, "cell 0 1,alignx center,aligny center");
				
				//LED Strips Label
				JLabel lblLedStrips = new JLabel("LED Strips");
				contentPanel.add(lblLedStrips, "cell 1 1,alignx center");
				
				//LED Strip Sections Label
				JLabel lblLedStripSections = new JLabel("LED Strip Sections");
				contentPanel.add(lblLedStripSections, "cell 2 1,alignx center");
				
				//Arduinos Panel
				JPanel arduinoPanel = new JPanel();
				arduinoPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				contentPanel.add(arduinoPanel, "cell 0 2,grow");
				
				//Strip Panel
				JPanel stripPanel = new JPanel();
				stripPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				contentPanel.add(stripPanel, "cell 1 2,grow");
				
				//Strip Section Panel
				JPanel stripSectionPanel = new JPanel();
				stripSectionPanel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
				contentPanel.add(stripSectionPanel, "cell 2 2,grow");
			/*
			 * End Content Panel
			 */
			
			/*
			 * Buttons Pane
			 */
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
				
				/*
				 * Button Pane Content
				 */
			
				//Name Text Field
				txtZoneName = new JTextField();
				txtZoneName.setText("Zone Name");
				buttonPane.add(txtZoneName);
				txtZoneName.setColumns(10);
			
				//OK Button
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						okButtonTrigger();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			
				//Cancel Button
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						cancelButtonTrigger();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				
			/*
			 * End Button Pane
			 */
		/*
		 * 
		 * End Content Pane
		 * 
		 */
				
		
		
		//Fill CheckBox Panes with Data
		arduinoBoxes = new ArduinoCheckBoxes[arduinoList.size()];
		for(int i = 0; i<arduinoList.size(); i++) {
			arduinoBoxes[i] = new ArduinoCheckBoxes(arduinoList.get(i));
			arduinoPanel.add(arduinoBoxes[i]);
		}
		
		
		///LED Strip Code (Future Addition)
		
//		stripBoxes = new StripCheckBoxes[stripList.size()];
//		for(int i = 0; i<stripList.size(); i++) {
//			stripBoxes[i] = new StripCheckBoxes(stripList.get(i));
//			stripPanel.add(stripBoxes[i]);
//		}
	}
	
	/*
	 * 
	 * Buttons Triggers
	 * 
	 */
	private void okButtonTrigger() {
		ArrayList<Arduino> selectedArduinos = new ArrayList<Arduino>();
		for(int i = 0; i<arduinoBoxes.length; i++) {
			if(arduinoBoxes[i].getState()) {
				selectedArduinos.add(arduinoBoxes[i].getArduino());
			}
		}
		newZone = new Zone(txtZoneName.getText(), selectedArduinos);
		System.out.println("addingArduinos" + Arrays.toString(stagedArduinos));
		zones.add(newZone);
		setVisible(false);
	}
	
	private void cancelButtonTrigger() {
		
	}
	
	/*
	 * 
	 * End Button Triggers
	 * 
	 */
}
