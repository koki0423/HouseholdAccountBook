package application;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
	static DataBase DB;

	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane) FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("家計簿");
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Properties properties = new Properties();
		//プロパティファイルのパスを指定する
		String strpass = "connection.properties";

		try {
			InputStream istream = new FileInputStream(strpass);
			properties.load(istream);

			DataBase DB = DataBase.getInstance();
			// 取得したデータをDataBaseクラスのフィールド変数に設定
			DB.setURL(properties.getProperty("ip"));
			DB.setUser(properties.getProperty("user"));
			DB.setPassword(properties.getProperty("password"));

		} catch (IOException e) {
			e.printStackTrace();
		}
		launch(args);
	}
}
