package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Window;

public class SettingWindowController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btnDiscard;

	@FXML
	private Button btnSave;

	@FXML
	private TextField txfIP;

	@FXML
	private TextField txfPass;

	@FXML
	private TextField txfPort;

	@FXML
	private TextField txfUser;

	//保存して戻る
	@FXML
	public void ButtonEvent(ActionEvent ev) throws FileNotFoundException {
		Properties properties = new Properties();
		if (txfIP.getText() == "" || txfIP.getText() == "" || txfIP.getText() == "") {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("入力エラー");
			alert.setHeaderText("確認");
			alert.setContentText("入力されていない項目があります。");
			alert.show();
		} else {
			try {
				properties.setProperty("ip", txfIP.getText());
				properties.setProperty("user", txfUser.getText());
				properties.setProperty("password", txfPass.getText());
				properties.store(new FileOutputStream("connection.properties"), "Comments");
			} catch (IOException e) {
				e.printStackTrace();
			}
			WindowClose(ev);
		}

		Scene scene = ((Node) ev.getSource()).getScene();
		Window window = scene.getWindow();
		window.hide();
	}

	@FXML
	void WindowClose(ActionEvent ev) {
		Scene scene = ((Node) ev.getSource()).getScene();
		Window window = scene.getWindow();
		window.hide();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Properties properties = new Properties();
		String strpass = "connection.properties";

		try {
			InputStream istream = new FileInputStream(strpass);
			properties.load(istream);

			txfIP.setText(properties.getProperty("ip"));
			txfUser.setText(properties.getProperty("user"));
			txfPass.setText(properties.getProperty("password"));

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
