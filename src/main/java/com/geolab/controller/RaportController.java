package com.geolab.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Scanner;

import com.geolab.comunicat.RaportComunicat;

import geo.geolab.gravimetry.GravDziennik;
import geo.geolab.gravimetry.GravInstrument;
import geo.geolab.gravimetry.RaportABBAAB;
import geo.geolab.gravimetry.RaportABCDEA;
import geo.geolab.gui.GUIRaportDodaj;
import geo.geolab.gui.messages.PressedRaportDodajData;
import geo.geolab.gui.messages.PressedRaportDodajHint;
import geo.geolab.gui.messages.PressedRaportUsunInfo;

public class RaportController {
	Controller parentController;

	public RaportController(Controller parentController) {
		super();
		this.parentController = parentController;
	}

	public void dodajRaport() {
		new GUIRaportDodaj();
	}

	public void dodajRaport(PressedRaportDodajData data) {
		String gravPath = Controller.GRAV_PATH + data.getInstrumentPomiarowy()
				+ ".txt";
		String dziennikPath = Controller.DZIENNIK_PATH
				+ data.getDziennikPomiarowy() + ".txt";
		// String raportPath = Controller.REPORT_PATH + data.getNazwaRaportu()
		// + ".rtf";
		String raportPath = data.getPathToSave();

		GravInstrument grawimetr = new GravInstrument(gravPath);
		GravDziennik dziennik = new GravDziennik();
		dziennik.setGrawimetr(grawimetr);
		dziennik.openFromTXT(dziennikPath);

		if (dziennik.rozpoznajSchemat() == GravDziennik.ABBAAB) {
			RaportABBAAB raport = new RaportABBAAB(dziennik,
					data.getTypPoprawki(), 1.18);
			raport.oblicz();
			raport.export(raportPath);

		} else if (dziennik.rozpoznajSchemat() == GravDziennik.ABCDEA) {
			RaportABCDEA raport = new RaportABCDEA(dziennik,
					data.getTypPoprawki(), 1.18);
			raport.oblicz();
			raport.export(raportPath);

		} else {
			RaportComunicat.bladRozpoznaniaSchematuPomiaru();
		}
	}

	public static String[] getAllRaport() {
		File directory = new File(Controller.REPORT_PATH);
		String[] myFiles = directory.list(new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".rtf");
			}
		});

		for (int i = 0; i < myFiles.length; i++) {
			String name = myFiles[i];
			myFiles[i] = name.replaceAll(".rtf", "");
		}

		return myFiles;
	}

	public void getInformacjeFromDziennik(PressedRaportDodajHint data) {
		String path = Controller.DZIENNIK_PATH + data.getNazwaDziennika()
				+ ".txt";
		File file = new File(path);
		Scanner scanner = null;

		try {
			scanner = new Scanner(file);
			String nazwaRaportu = null;
			String obiekt = null;
			String obserwator = null;
			String sekretarz = null;

			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.contains(DziennikController.NAZWA_RAPORTU))
					nazwaRaportu = line.replace(
							DziennikController.NAZWA_RAPORTU, "").trim();
				else if (line.contains(DziennikController.OBIEKT))
					obiekt = line.replace(DziennikController.OBIEKT, "").trim();
				else if (line.contains(DziennikController.OBSERWATOR))
					obserwator = line
							.replace(DziennikController.OBSERWATOR, "").trim();
				else if (line.contains(DziennikController.SEKRETARZ))
					sekretarz = line.replace(DziennikController.SEKRETARZ, "")
							.trim();
			}

			data.setNazwaRaportu(nazwaRaportu);
			data.setObiekt(obiekt);
			data.setObserwator(obserwator);
			data.setSekretarz(sekretarz);

			// PressedRaportDodajData returnMessage = new
			// PressedRaportDodajData(
			// data.getNazwaDziennika(), "", nazwaRaportu, obiekt,
			// obserwator, sekretarz);
			//
			// parentController.getView().raport(returnMessage);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

	}

	public void getInformacjeFromRaport(PressedRaportUsunInfo data) {

		String filePath = Controller.REPORT_PATH + data.getNazwaRaportu()
				+ ".rtf";

		File file = new File(filePath);
		data.getTaInformacje().setText(null);
		ArrayList<String> returnValue = new ArrayList<String>();

		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
			while (scanner.hasNext()) {
				String line = scanner.nextLine();
				data.getTaInformacje().append(line + "\n");
				returnValue.add(line);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			scanner.close();
		}

		data.getTaInformacje().setEditable(false);
	}
}
