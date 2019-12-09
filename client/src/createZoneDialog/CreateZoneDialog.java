package createZoneDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import client.Zone;
import hardware.Arduino;
import net.miginfocom.swing.MigLayout;
import structures.HardwareTree;

public class CreateZoneDialog extends JDialog {
	private static final long serialVersionUID = 1367918462768972665L;

	private final JPanel contentPanel = new JPanel();
	
	private JTextField txtZoneName;
	
	private HardwareTree tree;
	private Zone createdZone;
	
	@SuppressWarnings("unused")
	private Component parent;

	public CreateZoneDialog(Component parent, ArrayList<Arduino> arduinos) {
		this.parent = parent;
		setBounds(100, 100, 420, 300);
		getContentPane().setLayout(new BorderLayout());
		
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[141px,grow]", "[][][218px,grow]"));
		
		tree = new HardwareTree(arduinos, "Devices");
		
		JScrollPane scrollPane = new JScrollPane(tree);
		contentPanel.add(scrollPane, "cell 0 2,grow");
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
	
		//Name Text Field
		txtZoneName = new JTextField();
		txtZoneName.setText("Zone Name");
		buttonPane.add(txtZoneName);
		txtZoneName.setColumns(10);
	
		//OK Button
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Selected strips in CreateZoneDialog: " + tree.getSelectedStrips());
				createdZone = new Zone(parent, txtZoneName.getText(), tree.getSelectedStrips());
				setVisible(false);
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	
		//Cancel Button
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);	
	}
	
	public Zone getZone() {
		return createdZone;
	}
}