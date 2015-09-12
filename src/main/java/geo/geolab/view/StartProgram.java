package geo.geolab.view;

import geo.geolab.controller.Controller;
import geo.geolab.gui.GUI;
import geo.geolab.gui.GUIStart;

public class StartProgram {
	Controller controller;
	View view;

	StartProgram() {
		controller = new Controller();
		view = GUI.getInstance();

		GUI.setController(controller);
		controller.setView(view);
	}

	public static void main(String[] args) {
		GUIStart start = new GUIStart();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		start.dispose();

		new StartProgram();
	}

}
