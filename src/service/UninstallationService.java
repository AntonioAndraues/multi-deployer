/**
 * 
 */
package service;

import com.android.ddmlib.InstallException;

import controller.Layout1Controller;

/**
 * @author user
 *
 */
public class UninstallationService implements Runnable {

	private Layout1Controller controller;
	
	public UninstallationService(Layout1Controller controller) {
		this.controller = controller;
	}

	@Override
	public void run() {

		controller.getTxaLog().appendText("Uninstalling app...\n");
		try {
			for (int i = 0; i < controller.getDevices().length; i++) {
				controller.getDevices()[i].uninstallPackage(controller.getHelper().getPackageName());
				controller.getTxaLog().appendText("Uninstalled .apk in device " + i + ".\n");
			}
		} catch (NullPointerException npe) {
			System.out.println("NullPointerException occured...\n");
			controller.getTxaLog().appendText("NullPointerException occured...\n");
			npe.printStackTrace();
			System.out.println(npe.getCause());
		} catch (InstallException ie) {
			System.out.println("ERROR: Not installed due an OLDER SDK.\n");
			controller.getTxaLog().appendText("ERROR: Not installed due an OLDER SDK.\n");
			ie.printStackTrace();
			System.out.println(ie.getCause());
		} catch (Exception e) {
			System.out.println("Exception occured...\n");
			controller.getTxaLog().appendText("Exception occured..\n");
			e.printStackTrace();
		}
	}

}
