package client;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import javax.swing.JButton;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import hardware.LEDPort;
import hardware.LEDStrip;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.ActionEvent;
import java.awt.Font;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.TitledBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.JColorChooser;
import javax.swing.JScrollPane;
import java.awt.event.MouseAdapter;


@SuppressWarnings("serial")
public class Zone extends JInternalFrame {
	
	//Attributes
	private String name;
	
<<<<<<< HEAD
	private Set<LEDPort> ports;
	private ArrayList<LEDStrip> strips;
	private JColorChooser colorChooser;
=======
	private Set<LEDPort> zonePorts;
>>>>>>> master
	
	private ColorChangeListener colorChangeListener = new ColorChangeListener();
	private JPanel scrollViewport;
	
	private Component parent;
	
	
	public Zone(Component parent, String name, ArrayList<LEDStrip> strips) {
		super(name);
<<<<<<< HEAD
		this.name = name;
		this.strips = strips;
		this.ports = new HashSet<LEDPort>();
		this.parent = parent;
		for(LEDStrip strip : this.strips) {
			ports.add(strip.getParentArduino().getPort());
		}
		
		int length = 20;
		UIManager.put("ColorChooser.swatchesRecentSwatchSize", new Dimension(length, length));
		UIManager.put("ColorChooser.swatchesSwatchSize", new Dimension(length, length));
=======
		strips = new ArrayList<>();
		zonePorts = new HashSet<>();
		zoneName = name;
		this.strips = strips;
		for(Object strip : strips) {
			zonePorts.add(strip);
		}
>>>>>>> master
		GuiInit();
	}
	
	public String toString() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		this.setTitle(name);
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<LEDStrip> getStrips(){
		return strips;
	}
	
	/**
	 * Create the frame.
	 * @return 
	 */
	
	private void GuiInit() {		
		setBounds(100, 100, 992, 606);
		
		JPanel customColorBox = new JPanel();
		getContentPane().setLayout(new MigLayout("", "[30][276px,center][:30:30][506px,grow][30]", "[15][17px][162px,grow][19px]"));
		
		colorChooser = new JColorChooser();
		colorChooser.setPreviewPanel(new JPanel());
		colorChooser.getSelectionModel().addChangeListener(colorChangeListener);
		AbstractColorChooserPanel[] panels = colorChooser.getChooserPanels();
		colorChooser.removeChooserPanel(panels[1]);
		try {
			removeTransparencySlider(colorChooser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		getContentPane().add(colorChooser, "cell 3 2,grow");
		
		customColorBox.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(customColorBox, "cell 1 2,grow");
		customColorBox.setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		JScrollPane scrollPane = new JScrollPane();
		customColorBox.add(scrollPane, "cell 0 0,grow");
		
		scrollViewport = new JPanel();
		scrollPane.setViewportView(scrollViewport);
		scrollViewport.setLayout(new MigLayout("", "[grow,fill]", "[]"));
		
		JPanel panel = new JPanel();
		customColorBox.add(panel, "cell 0 1,growx");
		
		JButton btnSaveColor = new JButton("Save Color");
		
		btnSaveColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String input = JOptionPane.showInputDialog(colorChooser,"Enter Color Name", "Add Custom Color", JOptionPane.DEFAULT_OPTION);
				scrollViewport.add(new ColorButton(input, colorChooser.getColor()), "wrap");
				repaint();
				revalidate();
			}
		});

		panel.add(btnSaveColor);
		
		JLabel lblCustomColors = new JLabel("Custom Colors");
		getContentPane().add(lblCustomColors, "cell 1 1,growx,aligny top");
		lblCustomColors.setHorizontalAlignment(SwingConstants.CENTER);
		lblCustomColors.setFont(new Font("Tahoma", Font.PLAIN, 14));
	}
	
	private class ColorChangeListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			for(LEDStrip strip : strips) {
				LEDPort currentPort = strip.getParentArduino().getPort();
				currentPort.sendData(strip.getPin(), 80, colorChooser.getColor().getRed(), colorChooser.getColor().getGreen(), colorChooser.getColor().getBlue());
			}
		}
	}
	
	private static void removeTransparencySlider(JColorChooser jc) throws Exception {

	    AbstractColorChooserPanel[] colorPanels = jc.getChooserPanels();
	    for (int i = 1; i < colorPanels.length; i++) {
	        AbstractColorChooserPanel cp = colorPanels[i];

	        Field f = cp.getClass().getDeclaredField("panel");
	        f.setAccessible(true);

	        Object colorPanel = f.get(cp);
	        Field f2 = colorPanel.getClass().getDeclaredField("spinners");
	        f2.setAccessible(true);
	        Object spinners = f2.get(colorPanel);

	        Object transpSlispinner = Array.get(spinners, 3);
	        if (i == colorPanels.length - 1) {
	            transpSlispinner = Array.get(spinners, 4);
	        }
	        Field f3 = transpSlispinner.getClass().getDeclaredField("slider");
	        f3.setAccessible(true);
	        JSlider slider = (JSlider) f3.get(transpSlispinner);
	        slider.setEnabled(false);
	        Field f4 = transpSlispinner.getClass().getDeclaredField("spinner");
	        f4.setAccessible(true);
	        JSpinner spinner = (JSpinner) f4.get(transpSlispinner);
	        spinner.setEnabled(false);
	    }
	}
	
	private class ColorButton extends JButton {
		
		private Color userObject;
		private JButton currentButton = this;
		
		public ColorButton(String name, Color c) {
			super(name);
			super.setBackground(c);
			super.setForeground(Color.BLACK);
			float luminance = c.getRed() + c.getGreen() + c.getBlue();
			System.out.println(luminance);
			if(luminance <= 350) {
				super.setForeground(Color.WHITE);
			}
			userObject = c;
			JPopupMenu rClick = new JPopupMenu();
			JMenuItem remove = new JMenuItem("Remove");
			remove.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					scrollViewport.remove(currentButton);
					parent.repaint();
					parent.revalidate();
				}
			});
			rClick.add(remove);
			super.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					if(e.getButton()  == MouseEvent.BUTTON1) {
						colorChooser.setColor(userObject);
					} else if(e.getButton() == MouseEvent.BUTTON3) {
						System.out.println("Right");
						rClick.show(currentButton, e.getX(), e.getY());
					}
				}
			});
		}
		
	}
}