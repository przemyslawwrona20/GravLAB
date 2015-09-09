package geo.geolab.gui;

import geo.geolab.gui.messages.PressedDziennikDodajTableData;
import geo.geolab.layout.GBC;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.*;

@SuppressWarnings("serial")
public class GUIDziennikDodaj extends JDialog {
	private static final String TITLE = "NOWY DZIENNIK";
	private static final int HEIGHT = 400;
	private static final int WIDTH = 1000;

	private JTextField tfNazwa, tfObiekt, tfObserwator, tfSekretarz,
			tfNazwaPunktu, tfB, tfL, tfH, tfData, tfCzas, tfOdczyt, tfWysokosc,
			tfBlad;
	private JScrollPane scrollObserwacjie;

	private ModelDziennik modelDziennik;
	private JTable tableObserwacjie;

	private String delims = " ,.;/";

	public GUIDziennikDodaj() {
		super(GUI.getInstance(), true);

		zbudujGUI();

		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	private void zbudujGUI() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);
		add(zbudujPanelInformacjie(), new GBC(0, 1, 1, 1).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));
		add(zbudujPanelObserwacjie(), new GBC(0, 2, 1, 2).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));
		add(zbudujPanelAkcjie(), new GBC(0, 5, 1, 1).setWeight(1.0, 1.0)
				.setFill(GBC.BOTH));
	}

	private JPanel zbudujPanelInformacjie() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Informacjie podstawowe"));
		panel.setLayout(new GridLayout(2, 4));

		panel.add(new JLabel("Nazwa"));
		panel.add(new JLabel("Obiekt"));
		panel.add(new JLabel("Obserwator"));
		panel.add(new JLabel("Sekretarz"));

		panel.add(tfNazwa = new JTextField());
		panel.add(tfObiekt = new JTextField());
		panel.add(tfObserwator = new JTextField());
		panel.add(tfSekretarz = new JTextField());

		return panel;
	}

	private JPanel zbudujPanelObserwacjie() {
		// Create the custom data model
		modelDziennik = new ModelDziennik();

		// Create a new table instance
		tableObserwacjie = new JTable(modelDziennik);

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
		panel.setLayout(new GridLayout(2, 11));

		panel.add(new JLabel("Nazwa"));
		panel.add(new JLabel("B"));
		panel.add(new JLabel("L"));
		panel.add(new JLabel("H"));
		panel.add(new JLabel("Data"));
		panel.add(new JLabel("Godzina"));
		panel.add(new JLabel("Odczyt"));
		panel.add(new JLabel("Wysokoœæ"));
		panel.add(new JLabel("B³¹d"));
		panel.add(new JLabel());

		panel.add(tfNazwaPunktu = new JTextField());
		panel.add(tfB = new JTextField());
		panel.add(tfL = new JTextField());
		panel.add(tfH = new JTextField());
		panel.add(tfData = new JTextField());
		panel.add(tfCzas = new JTextField());
		panel.add(tfOdczyt = new JTextField());
		panel.add(tfWysokosc = new JTextField());
		panel.add(tfBlad = new JTextField());

		JButton bDodajObserwacje = new JButton("Dodaj");
		bDodajObserwacje.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!isCorrect())
					return;

				int row = modelDziennik.getRowCount();
				modelDziennik
						.setValueAt(tfNazwaPunktu.getText().trim(), row, 0);
				modelDziennik.setValueAt(tfB.getText(), row, 1);
				modelDziennik.setValueAt(tfL.getText(), row, 2);
				modelDziennik.setValueAt(tfH.getText(), row, 3);

				StringTokenizer tokensData = new StringTokenizer(tfData
						.getText(), delims);
				modelDziennik.setValueAt(tokensData.nextToken().trim(), row, 4);
				modelDziennik.setValueAt(tokensData.nextToken().trim(), row, 5);
				modelDziennik.setValueAt(tokensData.nextToken().trim(), row, 6);

				StringTokenizer tokensTime = new StringTokenizer(tfCzas
						.getText(), delims);
				modelDziennik.setValueAt(tokensTime.nextToken().trim(), row, 7);
				modelDziennik.setValueAt(tokensTime.nextToken().trim(), row, 8);
				modelDziennik.setValueAt(tokensTime.nextToken().trim(), row, 9);

				modelDziennik.setValueAt(
						tfOdczyt.getText().trim().replace(",", "."), row, 10);
				modelDziennik.setValueAt(
						tfWysokosc.getText().trim().replace(",", "."), row, 11);
				modelDziennik.setValueAt(
						tfBlad.getText().trim().replace(",", "."), row, 12);
				// modelWyrownaniePunktyStale.fireTableDataChanged();

				tfNazwaPunktu.setText("");
				tfB.setText("");
				tfL.setText("");
				tfH.setText("");
				tfData.setText("");
				tfCzas.setText("");
				tfOdczyt.setText("");
				tfWysokosc.setText("");
				tfBlad.setText("");
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

		JButton bDodajDziennik = new JButton("Dodaj dziennik");
		bDodajDziennik.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				GUI.getController().sendInfo(
						new PressedDziennikDodajTableData(tfNazwa.getText(),
								tfObiekt.getText(), tfObserwator.getText(),
								tfSekretarz.getText(), modelDziennik
										.getStringData()));
			}
		});

		panel.add(bDodajDziennik);

		return panel;
	}

	private boolean isCorrect() {
		StringTokenizer tokensData = new StringTokenizer(tfData.getText(),
				delims);

		if (tokensData.countTokens() != 3) {
			return false;
		}

		StringTokenizer tokensTime = new StringTokenizer(tfCzas.getText(),
				delims);
		if (tokensTime.countTokens() != 3) {
			return false;

		}

		return true;
	}
}
