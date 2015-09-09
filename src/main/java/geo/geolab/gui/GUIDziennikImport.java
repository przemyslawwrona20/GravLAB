package geo.geolab.gui;

import geo.geolab.gui.messages.PressedDziennikImportData;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GUIDziennikImport extends JDialog {
	public static final String NAZWA = "Nazwa";
	// public static final String NAZWA = "Nazwa";
	public static final String OBIEKT = "Obiekt";
	public static final String OBSERWATOR = "Obserwator";
	public static final String SEKRETARZ = "Sekretarz";
	// public static final String NAZWA = "Nazwa";

	public static final String SCIEZKA_DOSTEPU = "Œcie¿ka dostêpu";

	JLabel labelNazwaDziennika, labelObiekt, labelInstrumentPomiarowy,
			labelObserwator, labelSekretarz, labelSciezkaDostepu;
	JTextField tfNazwaDziennika, tfObiekt, tfInstrumentPomiarowy, tfObserwator,
			tfSekretarz, tfSciezka;
	private JButton bAnuluj, bImport;

	// private ModelDziennik modelDziennik;

 	public GUIDziennikImport() {
		super(GUI.getInstance(), true);
		zbudujGUI();
	}

	private void zbudujGUI() {
		setLayout(new GridLayout(6, 2, 5, 5));

		labelNazwaDziennika = new JLabel(NAZWA);
		add(labelNazwaDziennika);
		tfNazwaDziennika = new JTextField();
		add(tfNazwaDziennika);

		labelObiekt = new JLabel(OBIEKT);
		add(labelObiekt);
		tfObiekt = new JTextField();
		add(tfObiekt);

		labelObserwator = new JLabel(OBSERWATOR);
		add(labelObserwator);
		tfObserwator = new JTextField();
		add(tfObserwator);

		labelSekretarz = new JLabel(SEKRETARZ);
		add(labelSekretarz);
		tfSekretarz = new JTextField();
		add(tfSekretarz);

		labelSciezkaDostepu = new JLabel(SCIEZKA_DOSTEPU);
		add(labelSciezkaDostepu);
		tfSciezka = new JTextField();
		tfSciezka.addMouseListener(new MouseListener() {

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
				JFileChooser fileChooser = new JFileChooser();
				int returnValue = fileChooser.showOpenDialog(GUI.getInstance());
				if (returnValue == JFileChooser.APPROVE_OPTION)
					try {
						tfSciezka.setText(fileChooser.getSelectedFile()
								.getCanonicalPath());
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

			}
		});
		add(tfSciezka);

		// --------------------------------------------------------
		bAnuluj = new JButton("Anuluj");
		bAnuluj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		add(bAnuluj);

		bImport = new JButton("Importuj");
		bImport.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String nazwaDziennika = tfNazwaDziennika.getText();
				String obiekt = tfObiekt.getText();
				String obserwator = tfObserwator.getText();
				String sekretarz = tfSekretarz.getText();

				String path = tfSciezka.getText();
				GUI.getController().sendInfo(
						new PressedDziennikImportData(nazwaDziennika, obiekt,
								obserwator, sekretarz, path));
			}
		});
		add(bImport);

		// --------------------------------------------------------

		setSize(400, 200);
		setResizable(true);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

	}
}
