package controller;

import java.net.URL;
import java.util.ResourceBundle;

import event.Trigger;
import event.TriggerHandler;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.util.Duration;
import multideployer.MultiDeployer;


public class Layout3Controller {
	
	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button btnClose, btnCopy;

    @FXML
    private Hyperlink hlinkEmail;

    @FXML
    private Label lblAdvice;
    
	private Trigger trigger;
	private TriggerHandler triggerHandler;
	
           
	public Button getBtnClose() {
		return btnClose;
	}

	public void setBtnClose(Button btnClose) {
		this.btnClose = btnClose;
	}

	public Label getLblAdvice() {
		return lblAdvice;
	}

	public void setLblAdvice(Label lblAdvice) {
		this.lblAdvice = lblAdvice;
	}


	@FXML
	void initialize() {
		assert btnClose != null : "fx:id=\"btnClose\" was not injected: check your FXML file 'Layout3.fxml'.";
		assert btnCopy != null : "fx:id=\"btnClose\" was not injected: check your FXML file 'Layout3.fxml'.";
		assert hlinkEmail != null : "fx:id=\"lblEmail\" was not injected: check your FXML file 'Layout3.fxml'.";
		assert lblAdvice != null : "fx:id=\"lblAdvice\" was not injected: check your FXML file 'Layout3.fxml'.";
        this.initLayout();
	}

    @FXML
    void goToScreen1(ActionEvent event) {    	
    	MultiDeployer.changeScene(MultiDeployer.LAYOUT1);
    }

    /**
     * <p> Method to send email by mail to.
     * <a href=""></a>
     * </p>
     * @param ActionEvent triggered in FXML
     * @return void
     * @see <a href=""></a>
     * @since 1.0
     */
    @FXML
    void sendEmail(ActionEvent event) {
    	trigger.addListener(triggerHandler);
    	trigger.triggerEvent("mailto:henrique.prosa@hotmail.com");
	}
    
    /**
     * <p>Method to copy the email to clipboard.
     * </p>
     * @param MouseEvent
     * @return void
     * @see <a href="http://www.link_to_jira/HERO-402">HERO-402</a>
     * @since 1.0
     */
    @FXML
    void doCopy(ActionEvent event) {
    	
    	FadeTransition fade = new FadeTransition(Duration.millis(2000), lblAdvice);
    	fade.setFromValue(0.0);
    	fade.setToValue(1.0);
        fade.setCycleCount(2);
        fade.setAutoReverse(true);
    	fade.play();
    	
    	final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString("henrique.prosa@hotmail.com");
        clipboard.setContent(content);
		
    }
    
	private void initLayout() {
		trigger = new Trigger();
		triggerHandler = new TriggerHandler();		
	}

	
}
