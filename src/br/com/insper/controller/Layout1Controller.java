package br.com.insper.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.AndroidDebugBridge.IDeviceChangeListener;
import com.android.ddmlib.IDevice;

import br.com.insper.controller.util.AppHelper;
import br.com.insper.event.Trigger;
import br.com.insper.event.TriggerHandler;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import br.com.insper.MultiDeployer;
import net.dongliu.apk.parser.ApkFile;
import br.com.insper.resources.strings.StringResources;
import br.com.insper.service.ConnectionService;
import br.com.insper.service.InstallationService;
import br.com.insper.service.LaunchService;
import br.com.insper.service.UninstallationService;





public class Layout1Controller {
	
	private final int MIN_DEVICES = 1;
	
	private FileChooser fileChooser;
	private AndroidDebugBridge adb;
	private AppHelper helper;
	private Trigger trigger;
	private TriggerHandler triggerHandler;

	private IDevice devices[];
	private Properties prop;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;
	
	@FXML
    private Label lblOS;
	
	@FXML
	private TextField txtFieldAppPath, txtFieldAdbPath;

	@FXML
	private ProgressBar progressBar;

	@FXML
	private Button btnInstall, btnClear, btnUninstall, btnLaunch,
					btnAppFile, btnAdbFile;

	@FXML
	private ComboBox<String> cbDevices;

	@FXML
	private TextArea txaLog;	

    @FXML
    private MenuItem miClose;

    @FXML
    private MenuItem miClearLogs, miAboutUs, miGitCode;

    @FXML
    private MenuItem miBugReport;

    @FXML
    private ImageView imgInsper;

		
	public AndroidDebugBridge getAdb() {
		return adb;
	}

	public void setAdb(AndroidDebugBridge adb) {
		this.adb = adb;
	}

	public AppHelper getHelper() {
		return helper;
	}

	public void setHelper(AppHelper helper) {
		this.helper = helper;
	}

	public IDevice[] getDevices() {
		return devices;
	}

	public void setDevices(IDevice[] devices) {
		this.devices = devices;
	}

	public Properties getProp() {
		return prop;
	}

	public void setProp(Properties prop) {
		this.prop = prop;
	}

	public ResourceBundle getResources() {
		return resources;
	}

	public void setResources(ResourceBundle resources) {
		this.resources = resources;
	}

	public URL getLocation() {
		return location;
	}

	public void setLocation(URL location) {
		this.location = location;
	}
		
	public Label getLblOS() {
		return lblOS;
	}

	public void setLblOS(Label lblOS) {
		this.lblOS = lblOS;
	}

	public TextField getTxtFieldAppPath() {
		return txtFieldAppPath;
	}

	public void setTxtFieldAppPath(TextField txtFieldAppPath) {
		this.txtFieldAppPath = txtFieldAppPath;
	}

	public TextField getTxtFieldAdbPath() {
		return txtFieldAdbPath;
	}

	public void setTxtFieldAdbPath(TextField txtFieldAdbPath) {
		this.txtFieldAdbPath = txtFieldAdbPath;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(ProgressBar progressBar) {
		this.progressBar = progressBar;
	}

	public Button getBtnInstall() {
		return btnInstall;
	}

	public void setBtnInstall(Button btnInstall) {
		this.btnInstall = btnInstall;
	}

	public Button getBtnClear() {
		return btnClear;
	}

	public void setBtnClear(Button btnClear) {
		this.btnClear = btnClear;
	}

	public Button getBtnUninstall() {
		return btnUninstall;
	}

	public void setBtnUninstall(Button btnUninstall) {
		this.btnUninstall = btnUninstall;
	}

	public Button getBtnLaunch() {
		return btnLaunch;
	}

	public void setBtnLaunch(Button btnLaunch) {
		this.btnLaunch = btnLaunch;
	}

	public ComboBox<String> getCbDevices() {
		return cbDevices;
	}

	public void setCbDevices(ComboBox<String> cbDevices) {
		this.cbDevices = cbDevices;
	}

	public TextArea getTxaLog() {
		return txaLog;
	}

	public void setTxaLog(TextArea txaLog) {
		this.txaLog = txaLog;
	}

	public FileChooser getFileChooser() {
		return fileChooser;
	}
	
	@FXML
	void initialize() {
        assert lblOS != null : "fx:id=\"lblOS\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert txtFieldAppPath != null : "fx:id=\"txtFieldAppPath\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert btnAppFile != null : "fx:id=\"btnAppFile\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert progressBar != null : "fx:id=\"progressBar\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert txtFieldAdbPath != null : "fx:id=\"txtFieldAdbPath\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert btnAdbFile != null : "fx:id=\"btnAdbFile\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert cbDevices != null : "fx:id=\"cbDevices\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert btnInstall != null : "fx:id=\"btnInstall\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert btnLaunch != null : "fx:id=\"btnLaunch\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert txaLog != null : "fx:id=\"txaLog\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert btnUninstall != null : "fx:id=\"btnUninstall\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert btnClear != null : "fx:id=\"btnClear\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert miClose != null : "fx:id=\"miClose\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert miClearLogs != null : "fx:id=\"miClearLogs\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert miAboutUs != null : "fx:id=\"miAboutUs\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert miBugReport != null : "fx:id=\"miBugReport\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert miGitCode != null : "fx:id=\"miGitCode\" was not injected: check your FXML file 'Layout1.fxml'.";
        assert imgInsper != null : "fx:id=\"imgInsper\" was not injected: check your FXML file 'Layout1.fxml'.";

		this.initLayout();
		
		try {
			System.out.println("Initializing the DEBUG bridge.");
			AndroidDebugBridge.init(false);
		} catch (Exception e) {
			txaLog.appendText("Exception in init()\n");
			e.printStackTrace();
		}

		prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("file.path");
			
			prop.load(input);
			helper.setAppPath(prop.getProperty("appPath"));
			System.out.println(helper.getAppPath());
			
			if (helper.getAppPath() != null) {
				txtFieldAppPath.setText(helper.getAppPath());
				txtFieldAppPath.setAlignment(Pos.CENTER_RIGHT);
			}
			helper.setAdbPath(prop.getProperty("adbPath"));
			System.out.println(helper.getAdbPath());
			if (helper.getAdbPath() != null) {
				txtFieldAdbPath.setText(helper.getAdbPath());
				txtFieldAdbPath.setAlignment(Pos.CENTER_RIGHT);
				this.connectDevices();
			}

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

	@FXML
	public void closeWindowEvent(Event event) {
		try {
			System.out.println("Closing the DEBUG bridge.");
			AndroidDebugBridge.terminate();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	void selectAppFile(Event event) {

		Parent parent = new Pane();
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);

		File file = fileChooser.showOpenDialog(stage);

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Applications files", "*.apk"));



		if (file.exists()) {
			helper.setAppPath(file.getPath());
			txaLog.appendText(helper.getAppPath() + "\n");
			txtFieldAppPath.setText(helper.getAppPath());
			txtFieldAppPath.setAlignment(Pos.CENTER_RIGHT);
			
			OutputStream output = null;

			try {
				output = new FileOutputStream("file.path");
				prop.setProperty("appPath", helper.getAppPath());
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
			txaLog.appendText("App not found. \n");
		}

	}

	@FXML
	void key_enterAdbFile(Event event) {
		Parent parent = new Pane();
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);

		String adbPath = txtFieldAdbPath.getText();
		File file = new File(adbPath);

		if (file.exists()) {
			helper.setAdbPath(file.getPath());
			txaLog.appendText(helper.getAdbPath() + "\n");
			txtFieldAdbPath.setText(helper.getAdbPath());
			txtFieldAdbPath.setAlignment(Pos.CENTER_RIGHT);

			OutputStream output = null;

			try {
				output = new FileOutputStream("file.path");
				prop.setProperty("adbPath", helper.getAdbPath());
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
			txaLog.appendText("No adb file selected. \n");
		}
	}

	@FXML
	void key_enterAppFile(Event event) {

		Parent parent = new Pane();
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);

		String appPath = txtFieldAppPath.getText();
		File file = new File(appPath);

		if (file.exists()) {
			helper.setAppPath(file.getPath());
			txaLog.appendText(helper.getAppPath() + "\n");
			txtFieldAppPath.setText(helper.getAppPath());
			txtFieldAppPath.setAlignment(Pos.CENTER_RIGHT);

			OutputStream output = null;

			try {
				output = new FileOutputStream("file.path");
				prop.setProperty("appPath", helper.getAppPath());
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
			txaLog.appendText("No app file selected. \n");
		}
	}


	@FXML
	void selectAdbFile(Event event) {

		Parent parent = new Pane();
		Scene scene = new Scene(parent);
		Stage stage = new Stage();
		stage.setScene(scene);

		File file = fileChooser.showOpenDialog(stage);

		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("ADB executor", "*adb*.*"));
		
		if (file.exists()) {
			helper.setAdbPath(file.getPath());
			txaLog.appendText(helper.getAdbPath() + "\n");
			txtFieldAdbPath.setText(helper.getAdbPath());
			txtFieldAdbPath.setAlignment(Pos.CENTER_RIGHT);
			
			OutputStream output = null;

			try {
				output = new FileOutputStream("file.path");
				prop.setProperty("adbPath", helper.getAdbPath());
				prop.store(output, null);
				this.connectDevices();

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
			txaLog.appendText("No adb file selected. \n");
		}

	}
	
	@FXML
	void selectActivityFile(Event event) {
		txaLog.appendText("Lack of implementation.. rsrs\n");
	}

	private void connectDevices() {
		
		if(this.getAdb() == null) {
			ConnectionService service = new ConnectionService(this);
			Thread thread =  new Thread(service);
			thread.start();
		}
		
		ApkFile apk;
		try {
			apk = new ApkFile(helper.getAppPath());
			String manifest = apk.getManifestXml();
			helper.setActivityName(helper.extractActivity(manifest));
			txaLog.appendText("apk Activity: " + helper.getActivityName() + "\n");
			helper.setPackageName(helper.extractPackage(manifest));
			txaLog.appendText("apk Package: " + helper.getPackageName() + "\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		AndroidDebugBridge.addDeviceChangeListener(new IDeviceChangeListener() {

			@Override
			public void deviceChanged(IDevice device, int arg1) {
				// TODO: Check necessity of addDevices(...) here.
				//addDevices(device);
			}

			@Override
			public void deviceConnected(IDevice device) {
				addDevices(device);
			}

			@Override
			public void deviceDisconnected(IDevice device) {
				removeDevices(device);
			}
		});
	}

	@FXML
	void install(ActionEvent event) {
		
		InstallationService service = new InstallationService(this);
		Thread thread =  new Thread(service);
		thread.start();

		btnLaunch.setDisable(false);
	}

	@FXML
	void launch(ActionEvent event) {
		
		LaunchService service = new LaunchService(this);
		Thread thread =  new Thread(service);
		thread.start();
	}

	@FXML
	void uninstall(ActionEvent event) {
		
		UninstallationService service = new UninstallationService(this);
		Thread thread =  new Thread(service);
		thread.start();
		btnLaunch.setDisable(true);
	}

	@FXML
	void ClearHistory(ActionEvent event) {
		txaLog.clear();
	}

    @FXML
    void showAboutUs(ActionEvent event) {
    	MultiDeployer.changeScene(MultiDeployer.LAYOUT2);
    }

    @FXML
    void showBugReport(ActionEvent event) {
    	MultiDeployer.changeScene(MultiDeployer.LAYOUT3);
    	
    }

    @FXML
    void openGit(ActionEvent event) {
    	trigger.addListener(triggerHandler);
    	trigger.triggerEvent(StringResources.GITHUB);
	}

    @FXML
    void openLinkedin(ActionEvent event) {
    	trigger.addListener(triggerHandler);
    	trigger.triggerEvent(StringResources.HENRIQUE_LINKEDIN);
    }

    @FXML
    void goToInsper(ActionEvent event) {
    	trigger.addListener(triggerHandler);
    	trigger.triggerEvent(StringResources.INSPER);
    }
    
    
    // =========== 			Helpers       ====================
	
	private void initLayout() {

		helper = new AppHelper();
		fileChooser = new FileChooser();
		trigger = new Trigger();
		triggerHandler = new TriggerHandler();
		StringBuilder os = new StringBuilder();

		txtFieldAppPath.setFocusTraversable(false);		
		os.append(System.getProperty("os.name"));
		lblOS.setText(os.toString());
		
		this.showPathHints(os.toString().toLowerCase());		
	}

	
	private void addDevices(IDevice device) {
		
		progressBar.setProgress(1.0);
		System.out.println(String.format("%s connected", device.getSerialNumber()));
		
		if (!cbDevices.getItems().contains(device.getName())) {
			cbDevices.getItems().add(device.getName());
		}
		if(devices.length >= MIN_DEVICES) {
			enableButtons();
		}
		
		try {
			txaLog.appendText("Connected: " + device.getName() + " - Battery: "
					+ device.getBattery().get().toString() + "%\n");
			devices = adb.getDevices();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	} 
	
	private void removeDevices(IDevice device) {

		progressBar.setProgress(0.0);
		System.out.println(String.format("%s disconnected", device.getSerialNumber()));
		txaLog.appendText("Disconnected: " + device.toString() + "\n");
		devices = adb.getDevices();

		if (cbDevices.getItems().contains(device.getName())) {
			cbDevices.getItems().remove(device.getName());
		}
		
		if (cbDevices.getItems().contains(device.getSerialNumber())) {
			cbDevices.getItems().remove(device.getSerialNumber());
		}
		
		if(devices.length < MIN_DEVICES) {
			btnLaunch.setDisable(true);
		}
	}
	
	public void enableButtons() {
		btnInstall.setDisable(false);
		btnLaunch.setDisable(false);
		btnUninstall.setDisable(false);		
	}

	private void showPathHints(String os) {

		if(os.contains("windows")) {
			txaLog.appendText("# Windows Detected\n\n");
			txaLog.appendText("- Example of .apk path:\n");
			txaLog.appendText("C:\\..\\MyApp\\app\\build\\outputs\\apk\\debug\\app-debug.apk\n\n");
			txaLog.appendText("- Example of AVD path:\n");
			txaLog.appendText("C:\\Users\\user\\AppData\\Local\\Android\\Sdk\\platform-tools\\adb.exe\n\n");

		} else if(os.contains("linux")) {
			txaLog.appendText("# Linux Detected\n\n");
			txaLog.appendText("- Example of .apk path:\n");
			txaLog.appendText("GET THE PATH FOR LINUX\n\n");
			txaLog.appendText("- Example of AVD path:\n");
			txaLog.appendText("GET THE PATH FOR LINUX\n\n");

		} else if(os.contains("mac")) {
			txaLog.appendText("# Mac OS Detected\n\n");
			txaLog.appendText("- Example of .apk path:\n");
			txaLog.appendText("/Users/agile/Downloads/apk/app-debug.apk\n\n");
			txaLog.appendText("- Example of AVD path:\n");
			txaLog.appendText("/Users/agile/Library/Android/sdk/platform-tools/adb\n\n");
		}
	}

}
