package structures;

import java.beans.PropertyChangeListener;

import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.RowMapper;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class HardwareTreeSelectionModel extends DefaultTreeSelectionModel implements TreeSelectionModel {
	
	public void addSelectionPath() {
		System.out.println("new selection model");
	}
}
