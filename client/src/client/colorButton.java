package client;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class colorButton extends JButton {

	public colorButton(String name, int brightness, int red, int green, int blue) {
		JButton button = new JButton(name);
		add(button);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(name);
			}
		});
	}
}
