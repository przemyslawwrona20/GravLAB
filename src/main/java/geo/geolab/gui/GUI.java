package geo.geolab.gui;

import java.awt.Color;

import geo.geolab.gui.messages.PressedGrawimetrDodajData;
import geo.geolab.gui.messages.PressedRaportDodajData;
import geo.geolab.view.View;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.geolab.controller.Controller;

@SuppressWarnings("serial")
public class GUI extends JFrame implements View {
	private static GUI instance = null;

	/** Referencja do obiektu kontrolera DISPACHER CONTROLLER */
	private static Controller controller = null;

	private GUI() {
		try {
			UIManager
					.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(this);
		setJMenuBar(GUIMenuBar.getInstance());
		GUIMenuBar.getInstance();
		// setUndecorated(true);
		setIconImage(new ImageIcon("./icon.png").getImage());
		getContentPane().setBackground(Color.WHITE);
		setSize(400, 400);
		setLocation(100, 100);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void setController(Controller controller) {
		GUI.controller = controller;
	}

	public static Controller getController() {
		return controller;
	}

	public static GUI getInstance() {
		if (instance == null) {
			instance = new GUI();
		}
		return instance;
	}

	// GRAWIMETR
	public void grawimetrDodaj() {
		new GUIGrawimetrDodaj();
	}

	public void grawimetrDodaj(PressedGrawimetrDodajData message) {
		new GUIGrawimetrDodaj(message);
	}

	// DZIENNIK
	public void dziennikImport() {
		new GUIDziennikImport();
	}

	@Override
	public void grawimetrUsun(String[] list) {
		new GUIGrawimetrUsun(list);
	}

	@Override
	public void dziennikDodaj() {
		new GUIDziennikDodaj();
	}

	@Override
	public void dziennikUsun(String[] fileDziennik) {
		new GUIDziennikUsun(fileDziennik);

	}

	// RAPORT
	public void raport() {
		new GUIRaportDodaj();

	}

	public void raport(PressedRaportDodajData returnMessage) {
		new GUIRaportDodaj(returnMessage);

	}

	// WYROWNANIE
	public void wyrownanieNowe() {
		new GUIWyrownanieDodaj();
	}
}
