import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.OS;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefDisplayHandlerAdapter;
import org.cef.handler.CefFocusHandlerAdapter;
import org.cef.handler.CefPrintHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ReviewerMain {

	private static boolean browserHasFocus = false;

	public static void buildFrame(){

		// Define the CEF app and browser
		CefApp.addAppHandler((new CefAppHandlerAdapter(null){
			@Override
			public void stateHasChanged(CefAppState state) {
				if (state == CefAppState.TERMINATED){
					System.exit(0);
				}
			}
		}));

		final CefSettings settings = new CefSettings();
		settings.windowless_rendering_enabled = false;
		final CefApp cefApp = CefApp.getInstance(settings);
		final CefClient client = cefApp.createClient();
		final CefBrowser browser = client.createBrowser("http://www.google.com", false, false);
		final Component browserUI = browser.getUIComponent();
		client.addFocusHandler(new CefFocusHandlerAdapter(){
			@Override
			public void onGotFocus(CefBrowser browser) {
				if (browserHasFocus) return;
				browserHasFocus = true;
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
				browser.setFocus(true);
				System.out.println("Client got focus");
			}

			@Override
			public void onTakeFocus(CefBrowser browser, boolean next) {
				browserHasFocus = false;
			}
		});

		// Define the addressbox
		final JTextField addressBox = new JTextField("Hello", 20);
		final Font currentAddressBoxFont = addressBox.getFont();
		final Font newAddressBoxFont = currentAddressBoxFont.deriveFont((float)16);
		addressBox.setMargin(new Insets(5,5,5,5));
		addressBox.setFont(newAddressBoxFont);
		addressBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// The enter key fires this
				browser.loadURL("https://www.lumenshield.com");
			}
		});
		addressBox.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				if (!browserHasFocus) return;
				browserHasFocus = false;
				KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
				addressBox.grabFocus(); // Allows typing into the box
				System.out.println("Address box got focus");
			}
		});

		// Define the frames
		final JFrame frameContainer = new JFrame("Footbridge Media Site Launch Inspector");
		final JPanel panel = new JPanel(new GridBagLayout());
		final GridBagConstraints gc = new GridBagConstraints();

		// The topPanel receives the browser
		browserUI.setPreferredSize(new Dimension(500,500));

		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		panel.add(browserUI, gc);

		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 2;
		panel.add(addressBox, gc);


		frameContainer.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frameContainer.setSize(800, 600);
		frameContainer.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				frameContainer.dispose();
				cefApp.dispose();
				// Alternative: CefApp.getInstance().dispose();
			}
		});

		frameContainer.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				System.out.println("Resized");
				System.out.println(frameContainer.getSize().toString());

				panel.setSize(frameContainer.getSize());
				frameContainer.repaint();
				frameContainer.revalidate();
			}
		});

		frameContainer.add(panel, BorderLayout.CENTER);
		frameContainer.pack();
		frameContainer.setEnabled(true);
		frameContainer.setVisible(true);

		final Component[] components = panel.getComponents();

		System.out.println("Init");
		System.out.println(components.length);
	}

	public static void main(String[] args){
		buildFrame();
	}

}
