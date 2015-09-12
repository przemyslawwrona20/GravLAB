package geo.geolab.gui;

import geo.geolab.controller.DziennikController;
import geo.geolab.controller.GravimetrController;
import geo.geolab.gui.messages.PressedRaportDodajData;
import geo.geolab.gui.messages.PressedRaportDodajHint;
import geo.geolab.layout.GBC;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

import com.geolab.comunicat.RaportComunicat;

@SuppressWarnings("serial")
public class GUIRaportDodaj extends JDialog {
	private static final String TITLE = "NOWY RAPORT";
	private static final int HEIGHT = 500;
	private static final int WIDTH = 400;

	private String[] fileDziennik, fileInstrument;

	private JLabel lWybierzDziennik, lInstrument, lNazwaRaportu, lObiekt,
			lObserwator, lSekretarz;

	private JTextField tfNazwaRaportu, tfObiekt, tfObserwator, tfSekretarz;
	private JList<String> listDziennik, listInstrument;
	private JScrollPane scrollDziennik, scrollInstrument;
	private JButton bAnuluj, bDodaj;
	private JRadioButton rbBasic, rbLongmana, rbWenzla;

	public GUIRaportDodaj() {
		super(GUI.getInstance(), true);

		fileDziennik = DziennikController.getAllName();
		fileInstrument = GravimetrController.getAllName();

		zbudujGUI();

		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	}

	public GUIRaportDodaj(PressedRaportDodajData returnMessage) {
		super(GUI.getInstance(), true);

		fileDziennik = DziennikController.getAllName();
		fileInstrument = GravimetrController.getAllName();

		zbudujGUI();

		listDziennik.setSelectedValue(returnMessage.getNazwaRaportu(), true);
		listInstrument.setSelectedValue(returnMessage.getInstrumentPomiarowy(),
				true);

		tfNazwaRaportu.setText(returnMessage.getNazwaRaportu());
		tfObiekt.setText(returnMessage.getObiekt());
		tfObserwator.setText(returnMessage.getObserwator());
		tfSekretarz.setText(returnMessage.getSekretarz());

		setSize(400, 400);
		setLayout(new GridLayout(2, 1, 5, 5));
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}

	public int whichIsSelected() {
		if (rbBasic.isSelected())
			return 0;
		else if (rbLongmana.isSelected())
			return 1;
		else if (rbWenzla.isSelected())
			return 2;
		else
			return -1;
	}

	private void zbudujGUI() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(zbudujPanelDziennikInstrument(),
				new GBC(0, 0, 1, 2).setWeight(1.0, 1.0).setFill(GBC.BOTH));
		add(zbudujPanelTypPoprawki(), new GBC(0, 2).setFill(GBC.BOTH));
		add(zbudujPanelDodatkoweInformacjie(), new GBC(0, 3).setFill(GBC.BOTH));

		add(zbudujPanelPrzyciskiAkcji(), new GBC(0, 4).setFill(GBC.HORIZONTAL));

	}

	private JPanel zbudujPanelDziennikInstrument() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Dziennik & Instrument"));
		panel.setLayout(new GridLayout(2, 1, 5, 5));

		lWybierzDziennik = new JLabel("Dziennik pomiarowy");
		panel.add(lWybierzDziennik);

		listDziennik = new JList<String>(fileDziennik);
		listDziennik.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDziennik.setLayoutOrientation(JList.VERTICAL);
		listDziennik.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent arg0) {
			}

			public void mousePressed(MouseEvent arg0) {
			}

			public void mouseExited(MouseEvent arg0) {
			}

			public void mouseEntered(MouseEvent arg0) {
			}

			public void mouseClicked(MouseEvent arg0) {
				String selectedValue = listDziennik.getSelectedValue();
				PressedRaportDodajHint message = new PressedRaportDodajHint(
						selectedValue);
				GUI.getController().sendInfo(message);

				tfNazwaRaportu.setText(message.getNazwaRaportu());
				tfObiekt.setText(message.getObiekt());
				tfObserwator.setText(message.getObserwator());
				tfSekretarz.setText(message.getSekretarz());
			}
		});
		scrollDziennik = new JScrollPane(listDziennik);
		panel.add(scrollDziennik);

		lInstrument = new JLabel("Instrument");
		panel.add(lInstrument);

		listInstrument = new JList<String>(fileInstrument);
		listInstrument.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listInstrument.setLayoutOrientation(JList.VERTICAL);
		scrollInstrument = new JScrollPane(listInstrument);
		panel.add(scrollInstrument);

		return panel;
	}

	private JPanel zbudujPanelTypPoprawki() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 1, 5, 5));
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Typ poprawki"));

		rbBasic = new JRadioButton("Poprawka podstawowa");
		rbLongmana = new JRadioButton("Poprawka Longmana");
		rbWenzla = new JRadioButton("Poprawka Wenzla", true);

		ButtonGroup group = new ButtonGroup();
		group.add(rbBasic);
		group.add(rbLongmana);
		group.add(rbWenzla);

		panel.add(rbBasic);
		panel.add(rbLongmana);
		panel.add(rbWenzla);

		return panel;
	}

	private JPanel zbudujPanelDodatkoweInformacjie() {

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Dodatkowe informacjie"));
		panel.setLayout(new GridLayout(5, 2, 5, 5));

		lNazwaRaportu = new JLabel("Nazwa Raportu");
		panel.add(lNazwaRaportu);
		tfNazwaRaportu = new JTextField();
		panel.add(tfNazwaRaportu);

		lObiekt = new JLabel("Obiekt");
		panel.add(lObiekt);
		tfObiekt = new JTextField();
		panel.add(tfObiekt);

		lObserwator = new JLabel("Obserwator");
		panel.add(lObserwator);
		tfObserwator = new JTextField();
		panel.add(tfObserwator);

		lSekretarz = new JLabel("Sekretarz");
		panel.add(lSekretarz);
		tfSekretarz = new JTextField();
		panel.add(tfSekretarz);

		return panel;
	}

	public JPanel zbudujPanelPrzyciskiAkcji() {
		JPanel panel = new JPanel();

		bAnuluj = new JButton("Anuluj");
		bAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(bAnuluj);

		bDodaj = new JButton("Oblicz i Zapisz");
		bDodaj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showSaveDialog(fileChooser);
				if (returnValue != JFileChooser.APPROVE_OPTION)
					return;

				String pathToSave = fileChooser.getSelectedFile()
						.getAbsolutePath() + ".rtf";

				String dziennikPomiarowy = (String) listDziennik
						.getSelectedValue();
				String instrumentPomiarowy = (String) listInstrument
						.getSelectedValue();
				int typPoprawki = whichIsSelected();
				String nazwaRaportu = tfNazwaRaportu.getText();
				String obiekt = tfObiekt.getText();
				String obserwator = tfObserwator.getText();
				String sekretarz = tfSekretarz.getText();

				if (dziennikPomiarowy == null) {
					RaportComunicat.bladNieZaznaczonoDziennika();
					return;
				} else if (instrumentPomiarowy == null) {
					RaportComunicat.bladNieZaznaczonoInstrumentu();
					return;
				}

				PressedRaportDodajData pressedAddRaportData = new PressedRaportDodajData(
						pathToSave, dziennikPomiarowy, instrumentPomiarowy,
						typPoprawki, nazwaRaportu, obiekt, obserwator,
						sekretarz);
				dispose();
				GUI.getController().sendInfo(pressedAddRaportData);
			}
		});
		panel.add(bDodaj);

		return panel;
	}

}
