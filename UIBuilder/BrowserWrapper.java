package UIBuilder;

import org.cef.CefApp;
import org.cef.CefApp.CefAppState;
import org.cef.CefClient;
import org.cef.CefSettings;
import org.cef.browser.CefBrowser;
import org.cef.handler.CefAppHandlerAdapter;
import org.cef.handler.CefFocusHandlerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class BrowserWrapper {

	final CefClient client;
	final CefApp app;
	final CefBrowser browser;
	final Component ui;

	public BrowserWrapper(String startUrl){
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

		app = CefApp.getInstance(settings);
		client = app.createClient();
		browser = client.createBrowser(startUrl, false, false);
		ui = browser.getUIComponent();
	}
}
