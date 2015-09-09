package geo.geolab.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import geo.geolab.gui.messages.Message;
import geo.geolab.gui.messages.PressedDziennikDodaj;
import geo.geolab.gui.messages.PressedGrawimetrDodaj;
import geo.geolab.gui.messages.PressedDziennikImport;
import geo.geolab.gui.messages.PressedRaportDodaj;
import geo.geolab.gui.messages.PressedDziennikUsun;
import geo.geolab.gui.messages.PressedGrawimetrUsun;
import geo.geolab.gui.messages.PressedWyrownanieNowe;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class GUIMenuBar extends JMenuBar {
	private static GUIMenuBar instanceMenuBar = null;

	private GUIMenuBar() {

	}

	private void createPanelsInMenu() {
		GravPanel gravPanel = new GravPanel(new ItemActionListener());
		DziennikPanel dziennikPanel = new DziennikPanel(
				new ItemActionListener());
		ObliczeniaPanel obliczeniaPanel = new ObliczeniaPanel(
				new ItemActionListener());
		WyrownaniePanel wyrownaniePanel = new WyrownaniePanel(
				new ItemActionListener());

		instanceMenuBar.add(gravPanel);
		instanceMenuBar.add(dziennikPanel);
		instanceMenuBar.add(obliczeniaPanel);
		instanceMenuBar.add(wyrownaniePanel);

	}

	private class GravPanel extends JMenu {
		public GravPanel(ItemActionListener itemActionListener) {
			super("GRAWIMETR");
			add(new Item(new PressedGrawimetrDodaj(), itemActionListener));
			add(new Item(new PressedGrawimetrUsun(), itemActionListener));
		}
	}

	private class DziennikPanel extends JMenu {

		public DziennikPanel(ItemActionListener itemActionListener) {
			super("DZIENNIK");
			add(new Item(new PressedDziennikDodaj(), itemActionListener));
			add(new Item(new PressedDziennikUsun(), itemActionListener));
			addSeparator();
			add(new Item(new PressedDziennikImport(), itemActionListener));

		}
	}

	private class ObliczeniaPanel extends JMenu {

		public ObliczeniaPanel(ItemActionListener itemActionListener) {
			super("RAPORT");
			add(new Item(new PressedRaportDodaj(), itemActionListener));
		}
	}

	private class WyrownaniePanel extends JMenu {
		public WyrownaniePanel(ItemActionListener itemActionListener) {
			super("WYROWNANIE");
			add(new Item(new PressedWyrownanieNowe(), itemActionListener));
		}
	}

	/** Klasa wewnêtrzna bêd¹ca szablonem przycisku */
	class Item extends JMenuItem {

		/** Referencja obiektu zawierajacego imformacjê o wciœnienym przycisku */
		private Message message;

		public Item(Message message, ItemActionListener itemActionListener) {
			super(message.getName());
			this.message = message;
			addActionListener(itemActionListener);
		}

		public Message getMessage() {
			return message;
		}
	}

	/** KLasa wewnêtrzna bêd¹ca szablonem obs³ugi akcji wciœniêcia przycisku */
	private class ItemActionListener implements ActionListener {

		/**
		 * Akcja obs³uguj¹ca akcjiê wciœniêcia przycisku po przez wys³anie
		 * obiektu Message do kontrolera
		 */
		public void actionPerformed(ActionEvent event) {
			Item item = (Item) event.getSource();
			GUI.getController().sendInfo(item.getMessage());
			// controller.sendInfo(item.getMessage());
		}
	}

	public static GUIMenuBar getInstance() {
		if (instanceMenuBar == null) {
			instanceMenuBar = new GUIMenuBar();
			instanceMenuBar.createPanelsInMenu();
		}

		return instanceMenuBar;
	}

}
