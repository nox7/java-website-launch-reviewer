import UIBuilder.AppWindow;

public class SiteReviewer {

	public static void initWindow(){
		System.out.println("Init");
		final AppWindow window = new AppWindow("Site Launch Reviewer");
		window.build();

	}

	public static void main(String[] args){
		initWindow();
	}

}
