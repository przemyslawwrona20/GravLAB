package geo.geolab.gui;

import geo.geolab.gui.messages.PressedDziennikInformacjeData;
import geo.geolab.gui.messages.PressedDziennikUsunData;
import geo.geolab.layout.GBC;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

@SuppressWarnings("serial")
public class GUIDziennikUsun extends JDialog {
	private static final String TITLE = "NOWY RAPORT";
	private static final int HEIGHT = 370;
	private static final int WIDTH = 1000;

	private String[] fileDziennik;
	private JList<String> listDziennik;
	private JScrollPane scrollInstrument, scrollDziennik;

	private JLabel labelObiekt = new JLabel(), labelObserwator = new JLabel(),
			labelSekretarz = new JLabel();

	private JButton bAnuluj, bUsun;

	private ModelDziennik customDataModel;
	private JTable table;

	public GUIDziennikUsun(String[] fileInstrument) {
		super(GUI.getInstance(), true);
		this.fileDziennik = fileInstrument;
		setLayout(new FlowLayout());
		zbudujGUI();

		setTitle(TITLE);
		setSize(WIDTH, HEIGHT);
		setVisible(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	public void zbudujGUI() {
		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(zbudujPanelObiekty(),
				new GBC(0, 0).setWeight(1.0, 1.0).setFill(GBC.BOTH));
		add(zbudujPanelPzyciskiAkcji(), new GBC(1, 0));

		add(zbudujPanelInformacjiePodstawowe(),
				new GBC(0, 1, 2, 1).setFill(GBC.HORIZONTAL));

		add(zbudujPanelDziennik(), new GBC(0, 2, 2, 1).setFill(GBC.HORIZONTAL));
	}

	private JPanel zbudujPanelObiekty() {
		listDziennik = new JList<String>(fileDziennik);
		listDziennik.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listDziennik.setLayoutOrientation(JList.VERTICAL);
		listDziennik.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				String selectedValue = listDziennik.getSelectedValue();
				PressedDziennikInformacjeData message = new PressedDziennikInformacjeData(
						selectedValue);
				GUI.getController().sendInfo(message);

				labelObiekt.setText(message.getNazwaObiektu());
				labelObserwator.setText(message.getObserwator());
				labelSekretarz.setText(message.getSekretarz());

				customDataModel.setData(message.getDziennikTablica());
				customDataModel.fireTableDataChanged();
			}
		});

		scrollInstrument = new JScrollPane(listDziennik);
		scrollInstrument.setBounds(0, 0, 300, 80);

		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(1, 1));
		panel.setPreferredSize(new Dimension(400, 80));
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Obiekty"));

		panel.add(scrollInstrument);

		return panel;
	}

	private JPanel zbudujPanelPzyciskiAkcji() {
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(2, 1, 5, 5));
		panel.setBounds(310, 0, 100, 80);
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Akcje"));

		bAnuluj = new JButton("Anuluj");
		bAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		panel.add(bAnuluj);

		bUsun = new JButton("Usun");
		bUsun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String value = listDziennik.getSelectedValue();
				setVisible(false);
				GUI.getController()
						.sendInfo(new PressedDziennikUsunData(value));
				dispose();
			}
		});
		panel.add(bUsun);

		return panel;
	}

	private JPanel zbudujPanelInformacjiePodstawowe() {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Informacje"));

		panel.setLayout(new GridLayout(3, 2, 5, 5));

		panel.add(new JLabel("Mierzony obiekt"));
		panel.add(labelObiekt);

		panel.add(new JLabel("Obserwator"));
		panel.add(labelObserwator);

		panel.add(new JLabel("Sekretarz"));
		panel.add(labelSekretarz);

		return panel;
	}

	private JPanel zbudujPanelDziennik() {
		// Create the custom data model
		customDataModel = new ModelDziennik();

		// Create a new table instance
		table = new JTable(customDataModel);

		table.setPreferredScrollableViewportSize(new Dimension(200, 100));
		table.setFillsViewportHeight(true);

		// Configure some of JTable's paramters
		table.setShowHorizontalLines(false);
		table.setRowSelectionAllowed(false);
		table.setColumnSelectionAllowed(true);

		// Change the selection color
		// table.setSelectionForeground(Color.white);
		// table.setSelectionBackground(Color.red);

		scrollDziennik = new JScrollPane(table);
		scrollDziennik.setBounds(0, 0, 300, 80);

		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Dziennik"));
		panel.setLayout(new GridLayout(1, 1));
		panel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), "Dziennik pomiarowy"));
		panel.add(scrollDziennik);

		return panel;
	}
}
