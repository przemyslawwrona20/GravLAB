package geo.geolab.view;

import geo.geolab.gui.messages.PressedGrawimetrDodajData;
import geo.geolab.gui.messages.PressedRaportDodajData;

public interface View {
	public void grawimetrDodaj();

	public void grawimetrDodaj(PressedGrawimetrDodajData message);

	public void grawimetrUsun(String[] list);

	public void dziennikDodaj();

	public void dziennikImport();

	public void dziennikUsun(String[] fileDziennik);

	public void raport();

	public void raport(PressedRaportDodajData returnMessage);

	public void wyrownanieNowe();

}
