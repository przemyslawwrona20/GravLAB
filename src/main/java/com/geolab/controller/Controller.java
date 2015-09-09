package com.geolab.controller;

import geo.geolab.gui.messages.Message;
import geo.geolab.gui.messages.PressedDziennikDodajTableData;
import geo.geolab.gui.messages.PressedDziennikInformacjeData;
import geo.geolab.gui.messages.PressedDziennikUsunData;
import geo.geolab.gui.messages.PressedGrawimetrDodajData;
import geo.geolab.gui.messages.PressedGrawimetrInformacjeData;
import geo.geolab.gui.messages.PressedRaportDodajData;
import geo.geolab.gui.messages.PressedDziennikImportData;
import geo.geolab.gui.messages.PressedGrawimetrUsunData;
import geo.geolab.gui.messages.PressedRaportDodajHint;
import geo.geolab.gui.messages.PressedRaportUsunInfo;
import geo.geolab.gui.messages.PressedWyrownanieDataZapisz;
import geo.geolab.view.View;

public class Controller implements ControllerInterface {
	public static final String GRAV_PATH = "./grav/";
	public static final String REPORT_PATH = "./report/";
	public static final String LUNI_PATH = "./luni/";
	public static final String DZIENNIK_PATH = "./dziennik/";
	public static final String WYROWNANIE_PATH = "./wyrownanie/";

	private View view;

	/** Kontroler obs³uguj¹cy zdarzenia grawimetru */
	GravimetrController gravimetrController = new GravimetrController(this);

	/** Kontroler obs³uguj¹cy zdarzenia dziennika */
	DziennikController dziennikController = new DziennikController(this);

	/** Dziennik obs³uguj¹cy obliczenia */
	RaportController raportController = new RaportController(this);

	/** Kontroler obs³uguj¹cy wyrównywanie obserwacji */
	WyrownanieController wyrownanieController = new WyrownanieController(this);

	public Controller() {
	}

	public void sendInfo(Message message) {

		String nameClass = message.getClass().getSimpleName();

		switch (nameClass) {
		// GRAWIMETR
		case "PressedGrawimetrDodaj":
			gravimetrController.dodajGrawimetr();
			break;

		case "PressedGrawimetrDodajData":
			gravimetrController
					.dodajGrawimetr((PressedGrawimetrDodajData) message);
			break;

		case "PressedGrawimetrUsun":
			gravimetrController.usunGrawimetr();
			break;

		case "PressedGrawimetrUsunData":
			gravimetrController
					.usunGrawimetr((PressedGrawimetrUsunData) message);
			break;
		case "PressedGrawimetrInformacjeData":
			gravimetrController
					.getInformacjeFromGrawimetr((PressedGrawimetrInformacjeData) message);
			break;

		// DZIENNIK
		case "PressedDziennikDodaj":
			dziennikController.dziennikDodaj();
			break;
		case "PressedDziennikUsun":
			dziennikController.dziennikUsun();
			break;
		case "PressedDziennikUsunData":
			dziennikController.dziennikUsun((PressedDziennikUsunData) message);
			break;
		case "PressedDziennikImport":
			dziennikController.importujDziennik();
			break;

		case "PressedDziennikDodajTableData":
			dziennikController
					.dodajDziennik((PressedDziennikDodajTableData) message);
			break;

		case "PressedDziennikImportData":
			dziennikController
					.importujDziennik((PressedDziennikImportData) message);
			break;

		case "PressedDziennikInformacjeData":
			dziennikController
					.setInformacje((PressedDziennikInformacjeData) message);
			break;

		// RAPORTY
		case "PressedRaportDodaj":
			raportController.dodajRaport();
			break;
		case "PressedRaportDodajData":
			raportController.dodajRaport((PressedRaportDodajData) message);
			break;
		case "PressedRaportDodajHint":
			raportController
					.getInformacjeFromDziennik((PressedRaportDodajHint) message);
			break;
		case "PressedRaportUsunInfo":
			raportController
					.getInformacjeFromRaport((PressedRaportUsunInfo) message);
			break;

		// WYRÓWNANIE
		case "PressedWyrownanieNowe":
			wyrownanieController.wyrownaj();
			break;

		case "PressedWyrownanieDataZapisz":
			wyrownanieController
					.wyrownajZapiszRaport((PressedWyrownanieDataZapisz) message);
			break;

		default:
			System.out.println("Nie wim co zrobiæ");
			break;
		}

	}

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}
}
