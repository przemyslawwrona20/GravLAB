package geo.geolab.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JWindow;

@SuppressWarnings("serial")
public class GUIStart extends JWindow {
	private BufferedImage image;
	private int DEFAULT_WIDTH = 800;
	private int DEFAULT_HEIGHT = 400;

	public GUIStart() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();

		File imageFile = new File("./gravLab.png");
		try {
			image = ImageIO.read(imageFile);
		} catch (IOException e) {
			System.err.println("Blad odczytu obrazka");
			e.printStackTrace();
		}

		setLocation((width - DEFAULT_WIDTH) / 2, (height - DEFAULT_HEIGHT) / 2);
		setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setVisible(true);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(image, 0, 0, this);
	}

}
