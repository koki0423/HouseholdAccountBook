package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

public class DelWindowController implements Initializable {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Button btn1;

	@FXML
	private Button btn2;

	@FXML
	private Button btn3;

	@FXML
	private CheckBox chk;

	@FXML
	private TableView<Account> tb;

	@FXML
	private ComboBox<String> cmb1;

	@FXML
	private ComboBox<String> cmb2;

	@FXML
	private TextArea txa1;

	@FXML
	private TextField txf1;

	private int year = 0, month = 0;

	@FXML
	void ButtonEvent(ActionEvent event) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("入力エラー");
		alert.setHeaderText("確認");

		if (cmb1.getValue() == null || cmb2.getValue() == null) {

			if (cmb1.getValue() == null && cmb2.getValue() != null)
				alert.setContentText("年が未選択です。");
			else if (cmb1.getValue() != null && cmb2.getValue() == null)
				alert.setContentText("月が未選択です。");
			else
				alert.setContentText("年、月が未選択です。");
			alert.show();
		}

		if (cmb1.getValue() != null && cmb2.getValue() != null) {
			DataBase DB = DataBase.getInstance();
			String tmp[] = cmb1.getValue().split("年");
			String tmp2[] = cmb2.getValue().split("月");
			year = Integer.parseInt(tmp[0]);
			month = Integer.parseInt(tmp2[0]);

			tb.setItems(DB.ShowData(year, month));
		}
	}

	@FXML
	void ButtonEvent2(ActionEvent event) throws SQLException {
		DataBase DB = DataBase.getInstance();

		int id = Integer.parseInt(txf1.getText());
		DB.DeleteData(id);
		tb.setItems(DB.ShowData(year, month));

		txf1.setText("");
	}

	@FXML
	void ButtonEvent3(ActionEvent event) {

	}

	@FXML
	void WindowClose(ActionEvent event) {
		Scene scene = ((Node) event.getSource()).getScene();
		Window window = scene.getWindow();
		window.hide();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		for (int i = 1; i <= 12; i++) {
			cmb1.getItems().add(2020 + i + "年");
			cmb2.getItems().add(i + "月");
		}

		//tableの初期化
		TableColumn<Account, Integer> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Account, String> dateColumn = new TableColumn<>("Date");
		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

		TableColumn<Account, String> placeColumn = new TableColumn<>("Place");
		placeColumn.setCellValueFactory(new PropertyValueFactory<>("place"));

		TableColumn<Account, String> purposeColumn = new TableColumn<>("Purpose");
		purposeColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));

		TableColumn<Account, Integer> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

		idColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		dateColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		placeColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		purposeColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		priceColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));

		tb.getColumns().addAll(idColumn, dateColumn, placeColumn, purposeColumn, priceColumn);
		//ここまで

	}

}
