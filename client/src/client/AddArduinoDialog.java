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
import java.awt.event.ActionEvent;

public class AddArduinoDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textPort;
	private JTextField textName;
	
	private boolean canceled = false;


	//Constructor
	public AddArduinoDialog() {
		/*
		 * 
		 * Content Pane
		 * 
		 */
		setModal(true);
		setBounds(100, 100, 270, 150);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new MigLayout("", "[][][230px]", "[27px][27px]"));
		
			/*
			 * Content Pane Contents
			 */
			
			//COM Port Label
			JLabel lblComPort = new JLabel("COM Port");
			contentPanel.add(lblComPort, "cell 0 0,grow");
			lblComPort.setHorizontalAlignment(SwingConstants.CENTER);
			
			//COM Port Input Field
			textPort = new JTextField();
			contentPanel.add(textPort, "cell 2 0,grow");
			textPort.setColumns(10);
			
			//Name Label
			JLabel lblName = new JLabel("Name");
			contentPanel.add(lblName, "cell 0 1,grow");
			lblName.setHorizontalAlignment(SwingConstants.CENTER);
			
			//Name Input Field
			textName = new JTextField();
			contentPanel.add(textName, "cell 2 1,grow");
			textName.setColumns(10);
			
			/*
			 * End Content Pane 
			 */
			
			
		/*
		 * 
		 * Button Pane
		 * 
		 */
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
			/*
			 * Button Pane Contents
			 */
		
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
			
			/*
			 * End Button Pane Contents
			 */
	}
	
	public String getPort() {
		return textPort.getText();
	}
	
	public String getName() {
		return textName.getText();
	}
	
	public boolean isCanceled() {
		return canceled;
	}
}
