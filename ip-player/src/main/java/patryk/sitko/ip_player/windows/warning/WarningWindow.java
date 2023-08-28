package patryk.sitko.ip_player.windows.warning;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class WarningWindow {
	private JFrame frame;
	private JLabel messageLabel;
	private JPanel emptyPanelNorth;
	private JPanel emptyPanelEast;
	private JPanel emptyPanelWest;
	private JPanel buttonPanelSouth;
	private JPanel labelPanel;
	private JButton closeButton;

	private StringBuilder messageBuilder;

	public WarningWindow(Dimension size, Color foreground, Color background) {
		initComponents();
		layoutComponents();
		initListenets();
		colorComponents(foreground, background);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setSize(size.width, size.height);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
	}

	private void initComponents() {
		frame = new JFrame("\t/!\\Empty field(s) detected/!\\");
		messageLabel = new JLabel();
		emptyPanelNorth = new JPanel();
		emptyPanelEast = new JPanel();
		emptyPanelWest = new JPanel();
		buttonPanelSouth = new JPanel();
		labelPanel = new JPanel();
		closeButton = new JButton("Close");
		messageBuilder = new StringBuilder();
	}

	private void layoutComponents() {
		labelPanel.add(messageLabel);
		frame.add(emptyPanelNorth, BorderLayout.NORTH);
		frame.add(emptyPanelEast, BorderLayout.EAST);
		frame.add(emptyPanelWest, BorderLayout.WEST);
		frame.add(labelPanel, BorderLayout.CENTER);
		frame.add(buttonPanelSouth, BorderLayout.SOUTH);
		buttonPanelSouth.add(closeButton);
	}

	private void colorComponents(Color foreground, Color background) {
		frame.setForeground(background);
		frame.setBackground(background);
		emptyPanelNorth.setBackground(foreground);
		emptyPanelEast.setBackground(foreground);
		emptyPanelWest.setBackground(foreground);
		buttonPanelSouth.setBackground(foreground);
		messageLabel.setForeground(foreground);
		messageLabel.setBackground(background);
		labelPanel.setForeground(foreground);
		labelPanel.setBackground(background);
		closeButton.setForeground(foreground);
		closeButton.setBackground(background);
	}

	private void initListenets() {
		closeButton.addActionListener(actionListener -> {
			frame.dispose();
		});
	}

	public void reset() {
		messageBuilder = new StringBuilder();
	}

	/** Structure text with HTML tags */
	public void concat(String message) {
		messageBuilder.append(message);
	}

	public void warn() {
		messageLabel.setText(messageBuilder.toString());
		frame.setVisible(true);
		SwingUtilities.updateComponentTreeUI(messageLabel);
	}
}
