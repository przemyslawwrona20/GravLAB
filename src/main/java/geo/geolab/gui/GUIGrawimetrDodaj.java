package geo.geolab.gui;

import geo.geolab.gui.messages.PressedGrawimetrDodajData;
import geo.geolab.layout.GBC;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class GUIGrawimetrDodaj extends JDialog {
	private final String TITLE = "Dodaj Grawimetr";

	private final String NAZWA_GRAWIMETRU = "Nazwa grawimetru";
	private final String STALA_GRAWIMETRU = "Sta³a grawimetru";
	private final String TABLICA_REFERENCYJNA = "Tablica referencyjna";

	private JRadioButton rbStalaGrawimetru, rbTablicaReferencyjna;
	private JTextField tfNazwaGrawimetruStalaGrawimetru, tfStalaGrawimetru,
			tfNazwaGrawimetruTablicaeferencyjna, tfTablicaReferencyjna;
	private JButton bAnuluj, bDodaj, bWybierzTabliceReferencyjna;
	private JLabel labelNazwaGrawimetruStalaGrawimetru, labelStalaGawimetru,
			labelNazwaGrawimetruTablicaReferencyjna, labelTablicaReferencyjna;

	public GUIGrawimetrDodaj() {
		super(GUI.getInstance(), true);
		zbudujGUI();
		setParameters();
	}

	public GUIGrawimetrDodaj(PressedGrawimetrDodajData data) {

		super(GUI.getInstance(), true);
		System.out.println(data.getName());
		zbudujGUI();

		if (data.isStala()) {
			rbStalaGrawimetru.setSelected(true);
			tfNazwaGrawimetruStalaGrawimetru.setText(data.getName());
			tfStalaGrawimetru.setText(data.getPath());
		} else if (data.isPath()) {
			rbTablicaReferencyjna.setSelected(true);
			tfNazwaGrawimetruTablicaeferencyjna.setText(data.getName());
			tfTablicaReferencyjna.setText("");
		}
		setParameters();
	}

	public void setParameters() {
		setEnabledComponent(true);
		setTitle(TITLE);
		setSize(480, 240);

		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void zbudujGUI() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(zbudujPanelRadioButtons(), new GBC(0, 0, 2, 1).setFill(GBC.BOTH));
		add(zbudujPanelStalaGrawimetru(), new GBC(0, 1).setFill(GBC.BOTH));
		add(zbudujPanelTablicaReferencyjna(), new GBC(1, 1).setFill(GBC.BOTH));
		add(zbudujPanelButtons(), new GBC(0, 2, 2, 1).setFill(GBC.BOTH));
	}

	private JPanel zbudujPanelRadioButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 5, 5));

		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Rodzaj dziennika"));

		rbStalaGrawimetru = new JRadioButton(STALA_GRAWIMETRU);
		rbStalaGrawimetru.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				setEnabledComponent(true);
			}
		});
		rbStalaGrawimetru.setSelected(true);

		rbTablicaReferencyjna = new JRadioButton(TABLICA_REFERENCYJNA);
		rbTablicaReferencyjna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clear();
				setEnabledComponent(false);
			}
		});

		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(rbStalaGrawimetru);
		buttonGroup.add(rbTablicaReferencyjna);

		panel.add(rbStalaGrawimetru);
		panel.add(rbTablicaReferencyjna);

		return panel;
	}

	private JPanel zbudujPanelStalaGrawimetru() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 5, 5));

		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Sta³a grawimetru"));

		labelNazwaGrawimetruStalaGrawimetru = new JLabel(NAZWA_GRAWIMETRU);
		panel.add(labelNazwaGrawimetruStalaGrawimetru);

		tfNazwaGrawimetruStalaGrawimetru = new JTextField();
		panel.add(tfNazwaGrawimetruStalaGrawimetru);

		labelStalaGawimetru = new JLabel(STALA_GRAWIMETRU);
		panel.add(labelStalaGawimetru);

		tfStalaGrawimetru = new JTextField();
		panel.add(tfStalaGrawimetru);

		return panel;

	}

	private JPanel zbudujPanelTablicaReferencyjna() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 2, 5, 5));

		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Sta³a referencyjna"));

		labelNazwaGrawimetruTablicaReferencyjna = new JLabel(NAZWA_GRAWIMETRU);
		panel.add(labelNazwaGrawimetruTablicaReferencyjna);

		tfNazwaGrawimetruTablicaeferencyjna = new JTextField();
		panel.add(tfNazwaGrawimetruTablicaeferencyjna);

		labelTablicaReferencyjna = new JLabel(TABLICA_REFERENCYJNA);
		panel.add(labelTablicaReferencyjna);

		tfTablicaReferencyjna = new JTextField();
		panel.add(tfTablicaReferencyjna);

		panel.add(new JPanel());

		bWybierzTabliceReferencyjna = new JButton("Wybierz");
		bWybierzTabliceReferencyjna.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (rbTablicaReferencyjna.isSelected()) {
					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new File("."));
					chooser.setFileFilter(new FileNameExtensionFilter(
							"Pliki tekstu", "txt"));
					int returnValue = chooser.showOpenDialog(null);

					if (returnValue == JFileChooser.APPROVE_OPTION) {
						tfTablicaReferencyjna.setText(chooser.getSelectedFile()
								.getAbsolutePath());
					}
				}

			}
		});
		panel.add(bWybierzTabliceReferencyjna);

		return panel;
	}

	private JPanel zbudujPanelButtons() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1, 2, 5, 5));

		bAnuluj = new JButton("ANULUJ");
		bAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(bAnuluj);

		bDodaj = new JButton("DODAJ");
		bDodaj.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				boolean isStala = rbStalaGrawimetru.isSelected();
				boolean isPath = rbTablicaReferencyjna.isSelected();
				String name = null;
				String value = null;

				if (isStala) {
					name = tfNazwaGrawimetruStalaGrawimetru.getText();
					value = tfStalaGrawimetru.getText();
				} else if (isPath) {
					name = tfNazwaGrawimetruTablicaeferencyjna.getText();
					value = tfTablicaReferencyjna.getText();
				}

				PressedGrawimetrDodajData addGravData = new PressedGrawimetrDodajData(
						isStala, isPath, name, value);
				GUI.getController().sendInfo(addGravData);
				dispose();
			}
		});
		panel.add(bDodaj);

		return panel;
	}

	private void setEnabledComponent(boolean b) {
		tfNazwaGrawimetruStalaGrawimetru.setEnabled(b);
		tfStalaGrawimetru.setEnabled(b);

		tfNazwaGrawimetruTablicaeferencyjna.setEnabled(!b);
		tfTablicaReferencyjna.setEnabled(!b);
		bWybierzTabliceReferencyjna.setEnabled(!b);
	}

	private void clear() {
		tfNazwaGrawimetruStalaGrawimetru.setText(null);
		tfStalaGrawimetru.setText(null);

		tfNazwaGrawimetruTablicaeferencyjna.setText(null);
		tfTablicaReferencyjna.setText(null);
	}
}
