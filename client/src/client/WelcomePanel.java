package client;

import javax.swing.JPanel;

import client.AppWindow.LoadLayoutListener;
import net.miginfocom.swing.MigLayout;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;



public class WelcomePanel extends JPanel {
	
	/**
	 * Create the panel.
	 */
	public WelcomePanel(ActionListener conf, ActionListener add, ActionListener load) {
		setLayout(new MigLayout("", "[][400,right][10][growprio 1000,grow,left]", "[234.00][70px][][15px][][15][]"));
		
		JLabel nameLabel = new JLabel("LED Control Panel");
		nameLabel.setFont(new Font("OCR A Extended", Font.BOLD, 50));
		add(nameLabel, "cell 0 0 4 1,alignx center");
		
		JLabel lblStartByAdding = new JLabel("Start by adding your hardware");
		lblStartByAdding.setFont(new Font("Swis721 Cn BT", Font.PLAIN, 21));
		add(lblStartByAdding, "cell 1 2,alignx right");
		
		JButton btnConfigureHardware = new JButton("Configure Hardware");
		btnConfigureHardware.setFont(new Font("Swis721 Cn BT", Font.PLAIN, 21));
		add(btnConfigureHardware, "cell 3 2");
		btnConfigureHardware.addActionListener(conf);
		
		JLabel lblThenCreateLighting = new JLabel("Then create lighting zones");
		lblThenCreateLighting.setFont(new Font("Swis721 Cn BT", Font.PLAIN, 21));
		add(lblThenCreateLighting, "cell 1 4,alignx right");
		
		JButton btnCreateZone = new JButton("Create Zone");
		btnCreateZone.setFont(new Font("Swis721 Cn BT", Font.PLAIN, 21));
		add(btnCreateZone, "cell 3 4");
		btnCreateZone.addActionListener(add);
		
		JLabel lblOrOpenAn = new JLabel("Or open an existing layout");
		lblOrOpenAn.setFont(new Font("Swis721 Cn BT", Font.PLAIN, 21));
		add(lblOrOpenAn, "cell 1 6,alignx right");
		
		JButton btnOpenLayout = new JButton("Open Layout");
		btnOpenLayout.setFont(new Font("Swis721 Cn BT", Font.PLAIN, 21));
		add(btnOpenLayout, "cell 3 6");
		btnOpenLayout.addActionListener(load);

	}

}
