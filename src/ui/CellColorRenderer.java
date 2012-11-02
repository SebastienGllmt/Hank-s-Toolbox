package ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

public class CellColorRenderer extends DefaultListCellRenderer {
	public CellColorRenderer() {
		setOpaque(true);
	}

	public void setBackground(Color colr) {
	}

	public void setMyBackground(Color col) {
		super.setBackground(col);
	}

	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		setText("       ");
		setMyBackground((Color) value);
		setForeground((Color) value);
		return this;
	}
}