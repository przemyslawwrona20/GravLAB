package geo.geolab.gui;

import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
class ModelWyrownaniePunktyStale extends AbstractTableModel {
	private static String[] columnNames = { "Nazwa", "Wartoœæ" };
	private Object[][] data = {};

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		try {
			return data.length;
		} catch (ArrayIndexOutOfBoundsException ex) {
			return 0;
		} catch (NullPointerException ex) {
			return 0;
		}
	}

	public String getColumnName(int col) {
		return columnNames[col];
	}

	public Object getValueAt(int row, int col) {
		try {
			return data[row][col];
		} catch (ArrayIndexOutOfBoundsException ex) {
			return "";
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}

	/*
	 * Don't need to implement this method unless your table's editable.
	 */
	public boolean isCellEditable(int row, int col) {
		return true;
	}

	/*
	 * Don't need to implement this method unless your table's data can change.
	 */
	public void setValueAt(Object value, int row, int col) {
		if (col >= getColumnCount())
			return;
		else if (row < getRowCount() && col < getColumnCount())
			data[row][col] = value;
		else if (row == getRowCount()) {
			rewrite();
			data[row][col] = value;
		} else
			return;

		fireTableCellUpdated(row, col);
	}

	public void setData(Object[][] data) {
		this.data = data;
	}

	private void rewrite() {
		Object[][] varData = new Object[getRowCount() + 1][getColumnCount()];

		for (int i = 0; i < getRowCount(); i++)
			for (int j = 0; j < getColumnCount(); j++) {
				varData[i][j] = getValueAt(i, j);
			}
		data = varData;
	}

}