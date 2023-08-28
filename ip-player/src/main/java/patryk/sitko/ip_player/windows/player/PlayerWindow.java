package patryk.sitko.ip_player.windows.player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import patryk.sitko.ip_player.codec.loader.CodecLoader;
import patryk.sitko.ip_player.windows.parser.ParserWindow;
import uk.co.caprica.vlcj.player.embedded.windows.Win32FullScreenStrategy;

public class PlayerWindow extends JFrame implements Observer, Serializable {

	private int streams;
	private boolean writingAllowed;
	private ParserWindow parserWindow;
	private JButton newConnectionButton;
	private ArrayList<String> listOfMRLs;
	private Color foregroundColor;
	private Color backgroundColor;
	private Container container;
	private File mrlFile;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PlayerWindow(Color foreground, Color background) {
		super("DVPlayer - Contact: patryk.sitko.algemeen@gmail.com all rights reserved");
		foregroundColor = foreground;
		backgroundColor = background;
		initComponents();
		colorComponents(foreground, background);
		layoutComponents();
		initListeners();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(600, 800));
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setVisible(true);
		restoreSessions();
	}

	private void initComponents() {
		try {
			streams = 0;
			container = new Container();
			newConnectionButton = new JButton("New Connection");
			parserWindow = new ParserWindow(false, foregroundColor, backgroundColor);
			mrlFile = new File("lib/Session/MRLs.txt");
			mrlFile.createNewFile();
			parserWindow.addObserver(this);
		} catch (IOException e) {
		}
	}

	private void colorComponents(Color foreground, Color background) {
		getContentPane().setBackground(background);
		newConnectionButton.setForeground(foreground);
		newConnectionButton.setBackground(background);
	}

	private void layoutComponents() {
		setIconImage(new ImageIcon("lib/Images/FrameIcon.png").getImage());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(container, BorderLayout.CENTER);
		getContentPane().add(newConnectionButton, BorderLayout.SOUTH);
	}

	private void initListeners() {
		newConnectionButton.addActionListener(actionListener -> {
			parserWindow.setVisible(true);
		});

	}

	public void addNewStream(String MRL) {
		if (writingAllowed)
			writeMRL(MRL);
		++streams;
		JPanel vlcPanel = new JPanel();
		CodecLoader vlc = new CodecLoader(new Win32FullScreenStrategy(this));
		JButton button = new JButton("End Connection");
		button.addActionListener(actionListener -> {
			--streams;
			removeMRL(MRL);
			JPanel panel = vlcPanel;
			vlc.stopPlaying();
			container.remove(panel);
			refreshLayout();
		});

		vlcPanel.setLayout(new BorderLayout());
		vlcPanel.setBackground(backgroundColor);
		button.setForeground(foregroundColor);
		button.setBackground(backgroundColor);
		vlcPanel.add(vlc, BorderLayout.CENTER);
		vlcPanel.add(button, BorderLayout.SOUTH);
		container.add(vlcPanel);
		vlc.startPlaying(MRL);
		refreshLayout();
	}

	private void writeMRL(Object MRL) {
		try (PrintWriter printWriter = new PrintWriter(new FileOutputStream(mrlFile, true))) {
			printWriter.println(MRL);
			printWriter.flush();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private void removeMRL(String MRL) {
		writingAllowed = false;
		listOfMRLs = new ArrayList<>();
		try (Scanner fileScanner = new Scanner(mrlFile)) {
			while (true) {
				if (fileScanner.hasNextLine()) {
					listOfMRLs.add(fileScanner.nextLine());
				} else
					break;
			}
		} catch (IOException ex) {
			ex.printStackTrace();
			writingAllowed = true;
		}
		listOfMRLs.remove(MRL);
		try {
			Files.delete(mrlFile.toPath());
			mrlFile.createNewFile();
			writingAllowed = true;
			for (String _MRL : listOfMRLs)
				writeMRL(_MRL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void restoreSessions() {
		writingAllowed = false;
		try (Scanner fileScanner = new Scanner(mrlFile)) {
			while (true) {
				if (fileScanner.hasNextLine()) {
					addNewStream(fileScanner.nextLine());
				} else
					break;
			}
			writingAllowed = true;
		} catch (IOException ex) {
			ex.printStackTrace();
			writingAllowed = true;
		}
	}

	private void refreshLayout() {
		if (streams < 2)
			container.setLayout(new GridLayout(1, 1));
		else if (streams < 7)
			container.setLayout(new GridLayout(2, 1));
		else if (streams < 10)
			container.setLayout(new GridLayout(3, 2));
		else if (streams < 17)
			container.setLayout(new GridLayout(4, 2));
		else if (streams < 26)
			container.setLayout(new GridLayout(5, 2));
		else
			container.setLayout(new GridLayout(6, 1));
		SwingUtilities.updateComponentTreeUI(container);

	}

	@Override
	public void update(Observable o, Object MRL) {
		if (o instanceof ParserWindow) {
			addNewStream((String) MRL);
		}
	}

}
