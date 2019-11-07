package createZoneDialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import client.Arduino;
import client.ArduinoCheckBoxes;
import client.LEDStrip;
import client.StripCheckBoxes;
import client.Zone;

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
import structures.treeSelectionStructure;

import java.awt.ScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.JLabel;
import javax.swing.JTree;

public class CreateZoneDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private JTextField txtZoneName;
	
	private JTree tree;

	/**
	 * Create the dialog.
	 */
	public CreateZoneDialog(ArrayList<Arduino> arduinos) {
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
			contentPanel.setLayout(new MigLayout("", "[141px,grow]", "[][][218px,grow]"));

			
//			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Devices");
//			
//			for(Arduino current : arduinos) {
//				DefaultMutableTreeNode deviceNode = new DefaultMutableTreeNode(current);						
//				for(LEDStrip strip : current.getStrips()) {
//					deviceNode.add(new DefaultMutableTreeNode(strip));
//				}
//				rootNode.add(deviceNode);
//			}
			
			tree = new treeSelectionStructure(arduinos, "Devices");
			JScrollPane scrollPane = new JScrollPane(tree);
			contentPanel.add(scrollPane, "cell 0 2,grow");
			
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
				
		
	}
	
	/*
	 * 
	 * Buttons Triggers
	 * 
	 */
	private void okButtonTrigger() {
		
	}
	
	private void cancelButtonTrigger() {
		
	}
	
	/*
	 * 
	 * End Button Triggers
	 * 
	 */
}
