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
		CefApp.addAppHandler((new CefAppHandlerAdapter(null){
			@Override
			public void stateHasChanged(CefAppState state) {
				if (state == CefAppState.TERMINATED){
					System.exit(0);
				}
			}
		}));

		final CefSettings settings = new CefSettings();
		settings.windowless_rendering_enabled = false; // Setting to true will disable interaction

		final CefApp cefApp = CefApp.getInstance(settings);
		final CefClient client = cefApp.createClient();
		final CefBrowser browser = client.createBrowser("http://www.google.com", OS.isLinux(), false);
		final Component browserUI = browser.getUIComponent();


		final JFrame mainFrame = new JFrame("Footbridge Media Site Launch Inspector");
		final JTextField addressBox = new JTextField("Hello", 100);
		final Font currentAddressBoxFont = addressBox.getFont();
		final Font newAddressBoxFont = currentAddressBoxFont.deriveFont((float)16);
		addressBox.setMargin(new Insets(5,5,5,5));
		addressBox.setFont(newAddressBoxFont);
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		mainFrame.getContentPane().add(addressBox, BorderLayout.SOUTH);
		mainFrame.getContentPane().add(browserUI, BorderLayout.CENTER);
		mainFrame.setEnabled(true);
		mainFrame.setSize(800, 600);
		mainFrame.setVisible(true);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override public void windowClosing(WindowEvent e) {
				mainFrame.dispose();
				cefApp.dispose();
				// Alternative: CefApp.getInstance().dispose();
			}
		});

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

		System.out.println("Init");
	}

	public static void main(String[] args){
		buildFrame();
	}

}
