package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import net.miginfocom.swing.MigLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class AddStripDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textPort;
	private JTextField textName;
	
	private boolean canceled = false;
	
	
	private JComboBox comboBox;
	private JTextField textPIN;

	/**
	 * Create the dialog.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AddStripDialog(ArrayList<Arduino> arduinos) {
		comboBox = new JComboBox(arduinos.toArray());
		
		setModal(true);
		setBounds(100, 100, 290, 184);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][230px,grow]", "[][27px][27px][][]"));
		
		JLabel lblPin = new JLabel("PIN");
		contentPanel.add(lblPin, "cell 0 0,alignx center");
		
		textPIN = new JTextField();
		contentPanel.add(textPIN, "cell 2 0,growx");
		textPIN.setColumns(10);
		
		JLabel lblNumOfLEDs = new JLabel("# of LEDs");
		contentPanel.add(lblNumOfLEDs, "cell 0 1,grow");
		lblNumOfLEDs.setHorizontalAlignment(SwingConstants.CENTER);
		
		textPort = new JTextField();
		contentPanel.add(textPort, "cell 2 1,grow");
		textPort.setColumns(10);
		
		JLabel lblName = new JLabel("Name");
		contentPanel.add(lblName, "cell 0 2,grow");
		lblName.setHorizontalAlignment(SwingConstants.CENTER);
		
		textName = new JTextField();
		contentPanel.add(textName, "cell 2 2,grow");
		textName.setColumns(10);
		
		JLabel lblParentDevice = new JLabel("Parent Device");
		contentPanel.add(lblParentDevice, "cell 0 3");
		
		contentPanel.add(comboBox, "cell 2 3,growx");
		
		//Button Pane
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		//OK Button
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
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
				canceled = true;
				setVisible(false);
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
	}
	
	public int getNUM() {
		return Integer.parseInt(textPort.getText());
	}
	
	public String getName() {
		return textName.getText();
	}
	
	public Arduino getArduino() {
		return (Arduino) comboBox.getSelectedItem();
	}
	
	public int getPIN() {
		return Integer.parseInt(textPIN.getText());
	}
	
	public boolean isCanceled() {
		return canceled;
	}
}
