package geo.geolab.gui;

import geo.geolab.gui.messages.PressedGrawimetrInformacjeData;
import geo.geolab.gui.messages.PressedGrawimetrUsunData;
import geo.geolab.layout.GBC;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class GUIGrawimetrUsun extends JDialog {
	public static final String NAME = "USUN GRAWIMETR";

	private String[] fileInstrument;
	private JList<String> listDziennik;
	private JScrollPane scrollInstrument, scrollInformacje;

	private JLabel stalaGrawimetru;

	private JButton bAnuluj, bUsun;

	// Table
	private JTable table;
	private ModelGrawimetrTablicaReferencyjna customDataModel;

	public GUIGrawimetrUsun(String[] fileInstrument) {
		super(GUI.getInstance(), true);
		this.fileInstrument = fileInstrument;
		zbudujGUI();
		setTitle(NAME);

		setSize(480, 360);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void zbudujGUI() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(zbudujPanelListaObiektow(), new GBC(0, 0).setFill(GBC.BOTH)
				.setWeight(1.0, 1.0));
		add(zbudujPanelPrzyciskiAkcji(), new GBC(1, 0).setFill(GBC.BOTH));
		add(zbudujPanelStalaGrawimetru(),
				new GBC(0, 1, 2, 1).setFill(GBC.HORIZONTAL).setAnchor(GBC.WEST));
		add(zbudujPanelInformacje(),
				new GBC(0, 2, 2, 4).setFill(GBC.HORIZONTAL));
	}

	private JPanel zbudujPanelListaObiektow() {
		listDziennik = new JList<String>(fileInstrument);
		listDziennik.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDziennik.setLayoutOrientation(JList.VERTICAL);
		listDziennik.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				String nazwaGrawimetru = listDziennik.getSelectedValue();
				PressedGrawimetrInformacjeData data = new PressedGrawimetrInformacjeData(
						nazwaGrawimetru);
				GUI.getController().sendInfo(data);

				customDataModel.setData(data.getTablicaReferencyjna());
				customDataModel.fireTableDataChanged();

				if (data.getStalaGrawimetru().equals("null"))
					stalaGrawimetru.setText("brak");
				else
					stalaGrawimetru.setText(data.getStalaGrawimetru());
			}
		});

		scrollInstrument = new JScrollPane(listDziennik);
		scrollInstrument.setPreferredSize(new Dimension(50, 50));

		JPanel panelListaObiektow = new JPanel();
		panelListaObiektow.setLayout(new GridLayout(1, 1));
		panelListaObiektow.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Lista obiektów"));
		panelListaObiektow.add(scrollInstrument);

		return panelListaObiektow;
	}

	private JPanel zbudujPanelPrzyciskiAkcji() {
		JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Obiekt"));
		buttonsPanel.setLayout(new GridLayout(2, 1, 5, 5));
		buttonsPanel.setBounds(310, 0, 100, 80);

		bAnuluj = new JButton("Anuluj");
		bAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonsPanel.add(bAnuluj);

		bUsun = new JButton("Usun");
		bUsun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String value = listDziennik.getSelectedValue();
				dispose();
				GUI.getController().sendInfo(
						new PressedGrawimetrUsunData(value));
			}
		});
		buttonsPanel.add(bUsun);
		return buttonsPanel;
	}

	private JPanel zbudujPanelStalaGrawimetru() {
		JPanel panelStalaGrawimetru = new JPanel();
		panelStalaGrawimetru.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Obiekt"));
		panelStalaGrawimetru.add(new JLabel("Sta³a Grawimetru"));

		stalaGrawimetru = new JLabel("brak");
		panelStalaGrawimetru.add(stalaGrawimetru);
		return panelStalaGrawimetru;
	}

	private JPanel zbudujPanelInformacje() {
		// Create the custom data model
		customDataModel = new ModelGrawimetrTablicaReferencyjna();

		// Create a new table instance
		table = new JTable(customDataModel);

		table.setPreferredScrollableViewportSize(new Dimension(200, 100));
		table.setFillsViewportHeight(true);

		// Configure some of JTable's paramters
		table.setShowHorizontalLines(false);
		table.setRowSelectionAllowed(true);
		table.setColumnSelectionAllowed(true);

		// Change the selection color
		// table.setSelectionForeground(Color.white);
		// table.setSelectionBackground(Color.red);

		scrollInformacje = new JScrollPane(table);
		scrollInformacje.setBounds(0, 0, 300, 80);

		JPanel panelInformacje = new JPanel();
		panelInformacje.setLayout(new GridLayout(1, 1));
		panelInformacje.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Informacje"));
		panelInformacje.add(scrollInformacje);

		return panelInformacje;
	}

}
