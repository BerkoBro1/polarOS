package apps.sys64.imageViewer;

import java.io.*;

import javax.imageio.ImageIO;
import javax.swing.*;

import sys64.CreateFrame;

public class ImageViewer {
	public static void start(JDesktopPane pane, String dir) {
		String[] nameA = dir.split("\\\\");
		String name = nameA[nameA.length - 1];
		ImageIcon image = new ImageIcon(dir);
		JInternalFrame frame = CreateFrame.create(image.getIconWidth() + 70, image.getIconHeight() + 70, 240, 240, name, pane, true, false, -1, false, null, false, null, null);
		JPanel panel = new JPanel();
		JLabel imageLabel = new JLabel(image);
		panel.add(imageLabel);
		JScrollPane jsp = new JScrollPane(panel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(jsp);
	}
}
