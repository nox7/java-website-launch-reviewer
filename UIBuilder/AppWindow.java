package UIBuilder;

import javax.swing.*;
import org.cef.CefClient;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefFocusHandlerAdapter;

import java.awt.*;
import java.awt.event.*;

public class AppWindow extends JFrame{

	private boolean browserHasFocus = false;
	public JPanel panel;
	public BrowserWrapper bWrapper;
	public JTextField addressBox;

	public AppWindow(String frameTitle){
		super(frameTitle);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(800, 600);
	}

	/**
	 * Creates a new JPanel and sets it to the main class property panel
	 *
	 * @return void
	 */
	public void createPanel(){
		panel = new JPanel(new GridBagLayout());
	}

	/**
	 * Creates the address text box and sets the main class property for it
	 *
	 * @return void
	 */
	public JTextField createAddressBox(){
		final GridBagConstraints gc = new GridBagConstraints();
		addressBox = new JTextField("Hello", 20);
		final Font currentAddressBoxFont = addressBox.getFont();
		final Font newAddressBoxFont = currentAddressBoxFont.deriveFont((float)16);
		addressBox.setMargin(new Insets(5,5,5,5));
		addressBox.setFont(newAddressBoxFont);
		gc.fill = GridBagConstraints.HORIZONTAL;
		gc.weightx = 1;
		gc.weighty = 0;
		gc.gridx = 0;
		gc.gridy = 1;
		gc.gridwidth = 2;
		panel.add(addressBox, gc);

		return addressBox;
	}
	/**
	 * Appends a created Browser to the panel
	 *
	 * @return void
	 */
	public void appendBrowser(){
		final GridBagConstraints gc = new GridBagConstraints();
		gc.fill = GridBagConstraints.BOTH;
		gc.weightx = 1;
		gc.weighty = 1;
		gc.gridx = 0;
		gc.gridy = 0;
		panel.add(bWrapper.ui, gc);
	}

	/**
	 * Connects necessary events to a browser's client
	 *
	 * @return void
	 */
	public void connectBrowserClientEvents(CefClient client){
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
	}

	/**
	 * Connects events to the address box
	 *
	 * @return void
	 */
	public void connectAddressBoxEvents(){
		addressBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// The enter key fires this
				bWrapper.browser.loadURL("https://www.lumenshield.com");
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
	}
	/**
	 * Connects events to the super class (JFrame)
	 *
	 * @return void
	 */
	public void connectFrameEvents(){
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				bWrapper.app.dispose();
				// Alternative: CefApp.getInstance().dispose();
			}
		});

		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {

			}
		});
	}

	public void build(){
		createPanel();

		bWrapper = new BrowserWrapper("https://www.google.com");
		bWrapper.ui.setPreferredSize(new Dimension(1024,720));

		appendBrowser();
		createAddressBox();
		connectBrowserClientEvents(bWrapper.client);
		connectFrameEvents();

		add(panel, BorderLayout.CENTER);
		pack();
		setEnabled(true);
		setVisible(true);
	}

}
