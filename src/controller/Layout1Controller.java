package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Layout1Controller {

	final FileChooser fileChooser = new FileChooser();
	private AndroidDebugBridge adb;
	private File file;
	private String apkPath;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TextField txtFieldFileAddress;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Button btnDeploy;

	@FXML
	private TextArea txaLog;

	@FXML
	private ComboBox<?> cbxDevices;

	@FXML
	private Button btnScan;

	@FXML
	private Button btnClear;

	@FXML
	void initialize() {
		assert txtFieldFileAddress != null : "fx:id=\"txtFieldFileAddress\" was not injected: check your FXML file 'Layout1.fxml'.";
		assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'Layout1.fxml'.";
		assert btnDeploy != null : "fx:id=\"btnDeploy\" was not injected: check your FXML file 'Layout1.fxml'.";
		assert txaLog != null : "fx:id=\"txaLog\" was not injected: check your FXML file 'Layout1.fxml'.";
		assert cbxDevices != null : "fx:id=\"cbxDevices\" was not injected: check your FXML file 'Layout1.fxml'.";
		assert btnScan != null : "fx:id=\"btnScan\" was not injected: check your FXML file 'Layout1.fxml'.";
		assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'Layout1.fxml'.";

		txtFieldFileAddress.setFocusTraversable(false);

		try {
			System.out.println("Initializing the DEBUG bridge.");
			AndroidDebugBridge.init(false);
		} catch (Exception e) {
			txaLog.appendText("Exception in init()");
			e.printStackTrace();
		}

		Properties prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("file.path");

			// load a properties file
			prop.load(input);
			
			// get the property value and print it out
			System.out.println(prop.getProperty("apkPath"));
			apkPath = prop.getProperty("apkPath");
			txtFieldFileAddress.setText(apkPath);
			file = new File(apkPath);

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public void closeWindowEvent() {
		try {
			System.out.println("Closing the DEBUG bridge.");
			AndroidDebugBridge.terminate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void selectFile(Event event) {

		LocalDate localDate = LocalDate.now();
		LocalTime localTime = LocalTime.now();

		Parent parent = new Pane();
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);

		file = fileChooser.showOpenDialog(stage);

//		txaLog.appendText("Mouse clicked in TxtFieldFileAddress. On " + localDate + "_" + localTime + ".\n");

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Applications files", "*.apk"));
		if (file != null) {
			apkPath = file.getPath();
			txaLog.appendText(apkPath + "\n");
			txtFieldFileAddress.setText(apkPath);

			Properties prop = new Properties();
			OutputStream output = null;

			try {
				output = new FileOutputStream("file.path");

				// set the properties value
				prop.setProperty("apkPath", apkPath);

				// save properties to project root folder
				prop.store(output, null);

			} catch (IOException ioe) {
				ioe.printStackTrace();
			} catch (NullPointerException npe) {
				System.out.println("Output: " + output);
				npe.printStackTrace();

			} finally {
				if (output != null) {
					try {
						output.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}
		} else {
			txaLog.appendText("No file selected. \n");
		}

	}

	@FXML
	void scanADBDevices(ActionEvent event) {

		progressBar.setProgress(0.5);
		this.adb = AndroidDebugBridge.createBridge("/home/user/Android/Sdk/platform-tools/adb", true); // TODO:
																														// create
																														// multiplataform
																														// (Add
																														// windows
																														// ADB
																														// path
																														// trough
																														// an
																														// FileChooser).
		if (this.adb == null) {
			System.err.println("Invalid ADB location.");
			txaLog.appendText("Erro na localizaçao do ADB. \n");
			return;
		} else {
			txaLog.appendText("DEVICES: \n");
			for (IDevice device : this.adb.getDevices()) {
				txaLog.appendText(device.getName() + "|" + device.getSerialNumber() + "\n");
			}
		}

		AndroidDebugBridge.addDeviceChangeListener(new IDeviceChangeListener() {

			@Override
			public void deviceChanged(IDevice device, int arg1) {
				System.out.println(String.format("%s changed", device.getSerialNumber()));
				try {
					txaLog.appendText("Changed: " + device.getName() + " BAT LEVEL: "
							+ device.getBattery().get().toString() + "%\n");

					btnDeploy.setDisable(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void deviceConnected(IDevice device) {
				progressBar.setProgress(1.0);
				System.out.println(String.format("%s connected", device.getSerialNumber()));
				try {
					txaLog.appendText("Connected: " + device.getName() + " BAT LEVEL: "
							+ device.getBattery().get().toString() + "%\n");
					btnDeploy.setDisable(false);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void deviceDisconnected(IDevice device) {
				progressBar.setProgress(0.0);
				System.out.println(String.format("%s disconnected", device.getSerialNumber()));
				txaLog.appendText("Disconnected: " + device.toString() + "\n");
				btnDeploy.setDisable(true);
			}

		});

	}

	@FXML
	void deploy(ActionEvent event) {

		try {
			// IShellOutputReceiver receiver = new NullOutputReceiver();
			
			IShellOutputReceiver receiver = new IShellOutputReceiver() {
				
				@Override
				public boolean isCancelled() {
					System.out.println("Receiver.isCancelled()");
					return false;
				}
				
				@Override
				public void flush() {
					System.out.println("Receiver.flush()");
					
				}
				
				@Override
				public void addOutput(byte[] arg0, int arg1, int arg2) {
					System.out.println("Outputs: Arg0: " + arg0 + "Arg1: " + arg1 + "Arg2: " + arg2 + "\n" );
				}
			};
			
			for (IDevice device : this.adb.getDevices()) {
				StringBuffer command = new StringBuffer("adb install-multiple -r -t ");
				command.append(file.getPath()); // TODO: Exception when file is null.
				txaLog.appendText("Command: " + command.toString() + "\n");
				device.executeShellCommand(command.toString(), receiver);
				command = new StringBuffer("adb shell monkey -p app.package.name -c android.intent.category.LAUNCHER 1");
				txaLog.appendText("Command: " + command.toString() + "\n");
				device.executeShellCommand(command.toString(), receiver);
			}
		} catch (TimeoutException | AdbCommandRejectedException | ShellCommandUnresponsiveException | IOException e) {
			e.printStackTrace();
		} catch (NullPointerException npe) {
			System.out.println("NullPointerException occured...\n");
			txaLog.appendText("NullPointerException occured...\n");
			if (file.getPath().isEmpty()) {
				System.out.println("File Path: " + file.getPath() + "\n");
				txaLog.appendText("File Path: " + file.getPath() + "\n");
			} else {
				npe.printStackTrace();
				System.out.println(npe.getCause());
			}
		} catch (Exception e) {
			System.out.println("Exception occured...\n");
			txaLog.appendText("Exception occured..\n");
			e.printStackTrace();
		}

	}

	@FXML
	void ClearHistory(ActionEvent event) {
		txaLog.clear();
	}

}
