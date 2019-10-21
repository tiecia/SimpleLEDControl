package client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.Choice;
import net.miginfocom.swing.MigLayout;
import javax.swing.JSplitPane;

public class HardwareConfigurator extends JDialog {


	public final JPanel panel = new JPanel();
	
	private ArrayList<Arduino> arduinos = new ArrayList<Arduino>();
	private ArrayList<Arduino> strips = new ArrayList<Arduino>();
	private JPanel contentPanel;
	private JPanel confirmPanel;
	private JButton btnOK;
	

	
	public HardwareConfigurator() {
		super();
		GuiInit();
	}
	
	public void GuiInit() {
		/*
		 * 
		 * ContentPane
		 * 
		 */
		setModal(true);
		panel.setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
			/*
			 * Main Content Panel 
			 */
			contentPanel = new JPanel();
			getContentPane().add(contentPanel);
			contentPanel.setLayout(new MigLayout("", "[128.00][150,grow]", "[214.00px,grow]"));
			
			confirmPanel = new JPanel();
			getContentPane().add(confirmPanel, BorderLayout.SOUTH);
			confirmPanel.setLayout(new MigLayout("", "[141.00px][][][][][][][][]", "[23px]"));
			
			btnOK = new JButton("OK");
			confirmPanel.add(btnOK, "cell 0 0,alignx left,aligny center");
			//AddArduino Button
			JButton btnAddArduino = new JButton("Add Arduino");
			confirmPanel.add(btnAddArduino, "cell 8 0");
			btnAddArduino.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					createArduino();
				}
			});
	}
	
	private void createArduino() {
		AddArduinoDialog add = new AddArduinoDialog();
		add.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e) {
				System.out.println("Closed");
			}
			
		});
		add.setVisible(true);
		
		if(!add.isCanceled()) {
			int workingIndex = 0;
			arduinos.add(new Arduino(add.getName(), add.getPort()));
			if(arduinos.get(arduinos.size()-1).isOpen()) {
				contentPanel.add(new JButton());
			} else {
				JOptionPane.showMessageDialog(panel, "Failed to connect to Arduino \"" + arduinos.get(arduinos.size()).getName() + "\"", "Error", JOptionPane.ERROR_MESSAGE);
				arduinos.remove(arduinos.size());
			}
		}
	}
	
	public ArrayList<Arduino> getArduinos() {
		return arduinos;
	}
	
	public ArrayList<Arduino> getStrips() {
		return strips;
	}
}
