package patryk.sitko.ip_player.windows.parser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Observable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import patryk.sitko.ip_player.windows.warning.WarningWindow;

public class ParserWindow extends Observable implements Runnable {
	private WarningWindow warningWindow;

	private JFrame frame;
	Container container;

	private JPanel emptyPanelNorth;
	private JPanel emptyPanelEast;
	private JPanel emptyPanelWest;
	private JPanel buttonPanelSouth;
	private JPanel loginPanel;
	private JPanel passwordPanel;
	private JPanel cameraIpAddressAndPortPanel;
	private JPanel customMRLpanel;

	private JLabel loginLabel;
	private JLabel passwordLabel;
	private JLabel cameraIpAddressLabel;
	private JLabel portLabel;
	private JLabel customMRLlabel;

	private JTextField loginTextField;
	private JPasswordField passwordField;
	private JTextField cameraIpAddressTextField1;
	private JTextField cameraIpAddressTextField2;
	private JTextField cameraIpAddressTextField3;
	private JTextField cameraIpAddressTextField4;
	private JTextField portTextField;
	private JTextField customMRLtextField;

	private JButton connectButton;
	private JButton cancelButton;

	private Color foregroundColor;
	private Color backgroundColor;

	public ParserWindow(boolean usedWithThread, Color foreground, Color background) {
		if (!usedWithThread) {
			foregroundColor = foreground;
			backgroundColor = background;
			initComponents();
			colorComponents(foreground, background);
			layoutComponents();
			initListeners();
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(300, 191);
			frame.setResizable(false);
			frame.setLocationRelativeTo(null);
		}
	}

	private void initComponents() {
		warningWindow = new WarningWindow(new Dimension(300, 200), Color.RED, Color.WHITE);
		frame = new JFrame("New Connection");
		frame.setUndecorated(true);
		container = new Container();
		emptyPanelNorth = new JPanel();
		emptyPanelEast = new JPanel();
		emptyPanelWest = new JPanel();
		buttonPanelSouth = new JPanel();
		loginPanel = new JPanel();
		passwordPanel = new JPanel();
		cameraIpAddressAndPortPanel = new JPanel();
		customMRLpanel = new JPanel();
		loginLabel = new JLabel("Login: ");
		passwordLabel = new JLabel("Password:");
		cameraIpAddressLabel = new JLabel("Address:");
		portLabel = new JLabel("Port:");
		customMRLlabel = new JLabel("Custom MRL:");
		loginTextField = new JTextField(20);
		passwordField = new JPasswordField(18);
		cameraIpAddressTextField1 = new JTextField(2);
		cameraIpAddressTextField2 = new JTextField(2);
		cameraIpAddressTextField3 = new JTextField(2);
		cameraIpAddressTextField4 = new JTextField(2);
		portTextField = new JTextField(3);
		customMRLtextField = new JTextField(17);
		connectButton = new JButton("Connect");
		cancelButton = new JButton("Cancel");
	}

	private void layoutComponents() {
		container.setLayout(new FlowLayout());
		loginPanel.add(loginLabel);
		loginPanel.add(loginTextField);
		passwordPanel.add(passwordLabel);
		passwordPanel.add(passwordField);
		cameraIpAddressAndPortPanel.add(cameraIpAddressLabel);
		cameraIpAddressAndPortPanel.add(cameraIpAddressTextField1);
		cameraIpAddressAndPortPanel.add(new JLabel("."));
		cameraIpAddressAndPortPanel.add(cameraIpAddressTextField2);
		cameraIpAddressAndPortPanel.add(new JLabel("."));
		cameraIpAddressAndPortPanel.add(cameraIpAddressTextField3);
		cameraIpAddressAndPortPanel.add(new JLabel("."));
		cameraIpAddressAndPortPanel.add(cameraIpAddressTextField4);
		cameraIpAddressAndPortPanel.add(portLabel);
		cameraIpAddressAndPortPanel.add(portTextField);
		customMRLpanel.add(customMRLlabel);
		customMRLpanel.add(customMRLtextField);
		buttonPanelSouth.setLayout(new FlowLayout());
		buttonPanelSouth.add(connectButton);
		buttonPanelSouth.add(cancelButton);
		container.add(loginPanel);
		container.add(passwordPanel);
		container.add(cameraIpAddressAndPortPanel);
		container.add(customMRLpanel);
		frame.add(emptyPanelNorth, BorderLayout.NORTH);
		frame.add(emptyPanelEast, BorderLayout.EAST);
		frame.add(emptyPanelWest, BorderLayout.WEST);
		frame.add(buttonPanelSouth, BorderLayout.SOUTH);
		frame.add(container);
	}

	private void initListeners() {
		connectButton.addActionListener(actionListener -> {
			String login = loginTextField.getText();
			String password = String.copyValueOf(passwordField.getPassword());
			String ipAddress = cameraIpAddressTextField1.getText() + "." + cameraIpAddressTextField2.getText() + "."
					+ cameraIpAddressTextField3.getText() + "." + cameraIpAddressTextField4.getText();
			String port = portTextField.getText();
			String customMRL = customMRLtextField.getText();
			if (customMRL.length() > 0) {
				setChanged();
				notifyObservers(customMRL);
				customMRLtextField.setText("");
				frame.dispose();
			} else if (cameraIpAddressTextField1.getText().length() > 0
					&& cameraIpAddressTextField2.getText().length() > 0
					&& cameraIpAddressTextField3.getText().length() > 0
					&& cameraIpAddressTextField4.getText().length() > 0 && portTextField.getText().length() > 0) {
				setChanged();
				if (login.length() > 0 || password.length() > 0) {
					notifyObservers("rtsp://" + login + ":" + password + "@" + ipAddress + ":" + port + "/h264");
					passwordField.setText("");
					frame.dispose();
				} else {
					notifyObservers("rtp://@" + ipAddress + ":" + port);
					frame.dispose();
				}
			} else {
				warningWindow.reset();
				warningWindow.concat("<html><h1>|__/!\\_REQUIRED_/!\\__|</h1>");
				if (cameraIpAddressTextField1.getText().length() == 0)
					warningWindow.concat("<p>IP-Address: Field 1 is empty.<br>");
				if (cameraIpAddressTextField2.getText().length() == 0)
					warningWindow.concat("IP-Address: Field 2 is empty.<br>");
				if (cameraIpAddressTextField3.getText().length() == 0)
					warningWindow.concat("IP-Address: Field 3 is empty.<br>");
				if (cameraIpAddressTextField4.getText().length() == 0)
					warningWindow.concat("IP-Address: Field 4 is empty.<br>");
				if (portTextField.getText().length() == 0)
					warningWindow.concat("Port: Field is emty.<br>");
				warningWindow.concat("(Or you can enter a custom MRL)</p>");
				warningWindow.warn();
			}
		});
		cancelButton.addActionListener(actionListener -> {
			frame.dispose();
		});
		cameraIpAddressTextField1.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent event) {
				consumeNonNumeric(cameraIpAddressTextField1, event, 3);
			}
		});
		cameraIpAddressTextField2.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent event) {
				consumeNonNumeric(cameraIpAddressTextField2, event, 3);
			}
		});
		cameraIpAddressTextField3.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent event) {
				consumeNonNumeric(cameraIpAddressTextField3, event, 3);
			}
		});
		cameraIpAddressTextField4.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent event) {
				consumeNonNumeric(cameraIpAddressTextField4, event, 3);
			}
		});
		portTextField.addKeyListener(new KeyAdapter() {

			public void keyTyped(KeyEvent event) {
				consumeNonNumeric(portTextField, event, 4);
			}
		});
	}

	public void setVisible(boolean b) {
		frame.setVisible(b);
	}

	private void consumeNonNumeric(JTextField field, KeyEvent e, int maxLength) {
		if (field.getText().length() >= maxLength) {
			e.consume();
		}
		switch (e.getKeyChar()) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			break;
		default:
			e.consume();
		}
	}

	private void colorComponents(Color foreground, Color background) {
		frame.getContentPane().setBackground(background);
		container.setBackground(background);
		emptyPanelNorth.setBackground(foreground);
		emptyPanelEast.setBackground(foreground);
		emptyPanelWest.setBackground(foreground);
		buttonPanelSouth.setBackground(foreground);
		loginPanel.setBackground(background);
		passwordPanel.setBackground(background);
		cameraIpAddressAndPortPanel.setBackground(background);
		customMRLpanel.setBackground(background);
		loginLabel.setForeground(foreground);
		loginLabel.setBackground(background);
		passwordLabel.setForeground(foreground);
		passwordLabel.setBackground(background);
		cameraIpAddressLabel.setForeground(foreground);
		cameraIpAddressLabel.setBackground(background);
		portLabel.setForeground(foreground);
		portLabel.setBackground(background);
		customMRLlabel.setForeground(foreground);
		customMRLlabel.setBackground(background);
		loginTextField.setForeground(foreground);
		loginTextField.setBackground(background);
		passwordField.setForeground(foreground);
		passwordField.setBackground(background);
		cameraIpAddressTextField1.setForeground(foreground);
		cameraIpAddressTextField1.setBackground(background);
		cameraIpAddressTextField2.setForeground(foreground);
		cameraIpAddressTextField2.setBackground(background);
		cameraIpAddressTextField3.setForeground(foreground);
		cameraIpAddressTextField3.setBackground(background);
		cameraIpAddressTextField4.setForeground(foreground);
		cameraIpAddressTextField4.setBackground(background);
		portTextField.setForeground(foreground);
		portTextField.setBackground(background);
		customMRLtextField.setForeground(foreground);
		customMRLtextField.setBackground(background);
		connectButton.setForeground(foreground);
		connectButton.setBackground(background);
		cancelButton.setForeground(foreground);
		cancelButton.setBackground(background);
	}

	@Override
	public void run() {
		new ParserWindow(false, foregroundColor, backgroundColor);
	}
}
