package geo.geolab.gui;

import geo.geolab.gravimetry.GravPrzewyzszenie;
import geo.geolab.gravimetry.GravPunkt;
import geo.geolab.gravimetry.Wyrownanie;
import geo.geolab.gui.messages.PressedWyrownanieDataZapisz;
import geo.geolab.layout.GBC;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUIWyrownanieDodaj extends JDialog {
	private JTextField tfPuntyNazwa, tfPunktWartosc, tfPoczatek, tfKoniec,
			tfObserwacjaWartosc, tfOberwacjaBlad;

	private JScrollPane scrollPunktyStale, scrollObserwacjie;

	private ModelWyrownaniePunktyStale modelWyrownaniePunktyStale;
	private ModelWyrownanieObserwacjie modelWyrownanieObserwacjie;
	private JTable tablePunktyStale, tableObserwacjie;

	private Wyrownanie wyrownanie;

	public GUIWyrownanieDodaj() {
		super(GUI.getInstance(), true);

		zbudujGUI();

		setSize(400, 500);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	private void zbudujGUI() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(zbudujPanelStalePunkty(), new GBC(0, 0, 1, 2).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));
		add(zbudujPanelObserwacjie(), new GBC(0, 2, 1, 2).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));
		add(zbudujPanelAkcjie(), new GBC(0, 4, 1, 1).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));
	}

	private JPanel zbudujPanelStalePunkty() {
		// Create the custom data model
		modelWyrownaniePunktyStale = new ModelWyrownaniePunktyStale();

		// Create a new table instance
		tablePunktyStale = new JTable(modelWyrownaniePunktyStale);

		tablePunktyStale.setPreferredScrollableViewportSize(new Dimension(200,
				100));
		tablePunktyStale.setFillsViewportHeight(true);

		// Configure some of JTable's paramters
		tablePunktyStale.setShowHorizontalLines(false);
		tablePunktyStale.setRowSelectionAllowed(false);
		tablePunktyStale.setColumnSelectionAllowed(true);

		scrollPunktyStale = new JScrollPane(tablePunktyStale);
		scrollPunktyStale.setBounds(0, 0, 300, 80);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Punkty sta³e"));
		// panel.add(scrollPunktyStale);

		panel.add(zbudujPanelDodajPunkt(),
				new GBC(0, 0, 1, 1).setWeight(1.0, 1.0).setFill(GBC.BOTH));
		panel.add(scrollPunktyStale, new GBC(0, 1, 1, 2).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));

		return panel;
	}

	private JPanel zbudujPanelDodajPunkt() {
		JPanel panelDodajPunkt = new JPanel();
		panelDodajPunkt.setLayout(new GridLayout(2, 3));
		panelDodajPunkt.add(new JLabel("Nazwa"));
		panelDodajPunkt.add(new JLabel("Wartoœæ"));
		panelDodajPunkt.add(new JLabel());

		panelDodajPunkt.add(tfPuntyNazwa = new JTextField());
		panelDodajPunkt.add(tfPunktWartosc = new JTextField());

		JButton bDodajPunkt = new JButton("Dodaj");
		bDodajPunkt.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int row = modelWyrownaniePunktyStale.getRowCount();
				modelWyrownaniePunktyStale.setValueAt(tfPuntyNazwa.getText()
						.trim(), row, 0);
				modelWyrownaniePunktyStale.setValueAt(tfPunktWartosc.getText()
						.replace(",", ".").trim(), row, 1);
				modelWyrownaniePunktyStale.fireTableDataChanged();

				tfPuntyNazwa.setText("");
				tfPunktWartosc.setText("");
			}
		});
		panelDodajPunkt.add(bDodajPunkt);

		return panelDodajPunkt;
	}

	private JPanel zbudujPanelObserwacjie() {
		// Create the custom data model
		modelWyrownanieObserwacjie = new ModelWyrownanieObserwacjie();

		// Create a new table instance
		tableObserwacjie = new JTable(modelWyrownanieObserwacjie);

		tableObserwacjie.setPreferredScrollableViewportSize(new Dimension(200,
				100));
		tableObserwacjie.setFillsViewportHeight(true);

		// Configure some of JTable's paramters
		tableObserwacjie.setShowHorizontalLines(false);
		tableObserwacjie.setRowSelectionAllowed(false);
		tableObserwacjie.setColumnSelectionAllowed(true);

		scrollObserwacjie = new JScrollPane(tableObserwacjie);
		scrollObserwacjie.setBounds(0, 0, 300, 80);

		JPanel panel = new JPanel();
		panel.setLayout(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Obserwacjie"));
		panel.add(scrollObserwacjie);

		panel.add(zbudujPanelDodajObserwacje(),
				new GBC(0, 0, 1, 1).setWeight(1.0, 1.0).setFill(GBC.BOTH));
		panel.add(scrollObserwacjie, new GBC(0, 1, 1, 2).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));

		return panel;
	}

	private JPanel zbudujPanelDodajObserwacje() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2, 5));

		panel.add(new JLabel("Pocz¹tek"));
		panel.add(new JLabel("Koniec"));
		panel.add(new JLabel("Wartoœæ"));
		panel.add(new JLabel("B³¹d"));
		panel.add(new JLabel());

		panel.add(tfPoczatek = new JTextField());
		panel.add(tfKoniec = new JTextField());
		panel.add(tfObserwacjaWartosc = new JTextField());
		panel.add(tfOberwacjaBlad = new JTextField());

		JButton bDodajObserwacje = new JButton("Dodaj");
		bDodajObserwacje.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				int row = modelWyrownanieObserwacjie.getRowCount();
				modelWyrownanieObserwacjie.setValueAt(tfPoczatek.getText()
						.trim(), row, 0);
				modelWyrownanieObserwacjie.setValueAt(
						tfKoniec.getText().trim(), row, 1);
				modelWyrownanieObserwacjie.setValueAt(tfObserwacjaWartosc
						.getText().trim().replace(",", "."), row, 2);
				modelWyrownanieObserwacjie.setValueAt(tfOberwacjaBlad.getText()
						.trim().replace(",", "."), row, 3);
				modelWyrownaniePunktyStale.fireTableDataChanged();

				tfPoczatek.setText("");
				tfKoniec.setText("");
				tfObserwacjaWartosc.setText("");
				tfOberwacjaBlad.setText("");

			}
		});
		panel.add(bDodajObserwacje);

		return panel;

	}

	private JPanel zbudujPanelAkcjie() {
		JPanel panel = new JPanel();

		JButton bAnuluj = new JButton("Anuluj");
		bAnuluj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		panel.add(bAnuluj);

		JButton bWyrownaj = new JButton("Wyrownaj");
		bWyrownaj.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				ArrayList<GravPunkt> punktyStale = new ArrayList<GravPunkt>();
				for (int i = 0; i < modelWyrownaniePunktyStale.getRowCount(); i++) {
					GravPunkt gravPunkt = new GravPunkt(
							modelWyrownaniePunktyStale.getValueAt(i, 0)
									.toString(), new Double(
									modelWyrownaniePunktyStale.getValueAt(i, 1)
											.toString()));
					punktyStale.add(gravPunkt);
				}

				ArrayList<GravPrzewyzszenie> przewyzszenia = new ArrayList<GravPrzewyzszenie>();
				for (int i = 0; i < modelWyrownanieObserwacjie.getRowCount(); i++) {
					GravPrzewyzszenie gravPrzewyzszenie = new GravPrzewyzszenie(
							modelWyrownanieObserwacjie.getValueAt(i, 0)
									.toString(), modelWyrownanieObserwacjie
									.getValueAt(i, 1).toString(), new Double(
									modelWyrownanieObserwacjie.getValueAt(i, 2)
											.toString()), new Double(
									modelWyrownanieObserwacjie.getValueAt(i, 3)
											.toString()));
					przewyzszenia.add(gravPrzewyzszenie);
				}

				wyrownanie = new Wyrownanie(punktyStale, przewyzszenia);
				wyrownanie.wyrownaj();

				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showSaveDialog(fileChooser);
				if (returnValue != JFileChooser.APPROVE_OPTION)
					return;

				String pathToSave = fileChooser.getSelectedFile()
						.getAbsolutePath() + ".rtf";

				PressedWyrownanieDataZapisz message = new PressedWyrownanieDataZapisz(
						pathToSave, wyrownanie);
				GUI.getController().sendInfo(message);
			}
		});
		panel.add(bWyrownaj);

		return panel;
	}
}
