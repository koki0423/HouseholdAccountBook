package application;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Window;

public class FixWindowController implements Initializable {

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
	private Button btn4;

	@FXML
	private TableView<Account> tb;

	@FXML
	private TableView<Income> tb2;

	@FXML
	private ComboBox<String> cmb1;

	@FXML
	private ComboBox<String> cmb2;

	@FXML
	private DatePicker dp1;

	@FXML
	private TextArea txa1;

	@FXML
	private TextField txf1;

	@FXML
	private TextField txf2;

	@FXML
	private TextField txf3;

	@FXML
	private TextField txf4;

	private int publicYear = 0, publicMonth = 0;

	@FXML
	protected void ButtonEvent(ActionEvent ev) {

		if (cmb1.getValue() == null || cmb2.getValue() == null) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("入力エラー");
			alert.setHeaderText("確認");
			alert.setContentText("入力されていない項目があります。");
			alert.show();
		}

		if (cmb1.getValue() != null && cmb2.getValue() != null) {
			DataBase DB = DataBase.getInstance();
			String tmp[] = cmb1.getValue().split("年");
			String tmp2[] = cmb2.getValue().split("月");

			int year = Integer.parseInt(tmp[0]);
			int month = Integer.parseInt(tmp2[0]);
			tb.setItems(DB.ShowData(year, month));
		}
	}

	@FXML
	protected void ButtonEvent2(ActionEvent ev) throws SQLException {
		try {
			DataBase DB = DataBase.getInstance();
			Object[] data = new Object[4];//idで検索したデータ保管用配列
			int id = Integer.parseInt(txf1.getText());

			data = DB.SearchData(id);
			String[] tmp = data[0].toString().split(" ");//Object型からString型に変換してSplit
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDate date = LocalDate.parse(tmp[0], formatter);

			publicYear = date.getYear();
			publicMonth = date.getMonthValue();

			//ここ少し修正
			dp1.setValue(date);
			txf3.setText(data[2].toString());
			txf4.setText(data[3].toString());
			//ここまで

		} catch (Exception e) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("エラー");
			alert.setHeaderText("確認");
			alert.setContentText("入力されていない項目があります。");
			alert.show();
		}

	}

	@FXML
	protected void ButtonEvent3(ActionEvent ev) throws SQLException {
		LocalDate date = dp1.getValue();
		int id = Integer.parseInt(txf1.getText());
		String place = txf2.getText();
		String purpose = txf3.getText();
		String price = txf4.getText();

		if (Objects.isNull(date) || price == "") {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("入力エラー");
			alert.setHeaderText("確認");
			alert.setContentText("入力されていない項目があります。");
			alert.show();
		}

		else {
			if (purpose == "")
				purpose = "*****";
			if (place == "")
				place = "*****";
			DataBase DB = DataBase.getInstance();
			DB.Update(id, date, place, purpose, Integer.parseInt(price));

			tb.setItems(DB.ShowData(publicYear, publicMonth));

			MainController.publicYear = publicYear;
			MainController.publicMonth = publicMonth;

			txf2.setText("");
			txf3.setText("");
			txf4.setText("");
		}
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

		TableColumn<Account, Double> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
		tb.getColumns().addAll(idColumn, dateColumn, placeColumn, purposeColumn, priceColumn);

		idColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		dateColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		placeColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		purposeColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));
		priceColumn.prefWidthProperty().bind(tb.widthProperty().divide(5));

		
	}

}
