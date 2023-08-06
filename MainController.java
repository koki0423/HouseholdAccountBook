package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainController implements Initializable {
	@FXML
	private BorderPane bp;

	@FXML
	private Button btn1;

	@FXML
	private Button btn2;

	@FXML
	private TableView<Account> tb;

	@FXML
	private TableView<Income> tb2;

	@FXML
	private ComboBox<String> cmb1;

	@FXML
	private ComboBox<String> cmb2;

	@FXML
	private ComboBox<String> cmb3;

	@FXML
	private DatePicker dp;

	@FXML
	private Label lb1;

	@FXML
	private Label lb2;

	@FXML
	private Label lb3;

	@FXML
	private Label lb4;

	@FXML
	private TextArea txa2;

	@FXML
	private TextField txf1;

	@FXML
	private TextField txf3;

	@FXML
	private MenuButton menu;//修正ボタン

	@FXML
	private MenuButton menu2;//削除ボタン

	public static int publicMonth = 0;
	public static int publicYear = 0;
	private String errmess = null;//エラーメッセージ

	//データの登録
	@FXML
	protected void ButtonEvent(ActionEvent ev) throws SQLException, NumberFormatException {
		//登録ボタン押した後の挙動
		//要素からデータの取得
		LocalDate date = LocalDate.now();//初期化
		String place = null;
		String purpose = null;
		boolean state;//if false -> error
		int year = 0, month = 0, price = 0;

		try {
			date = dp.getValue();
			if (date == null) {
				errmess = "日付が選択されていません";
				throw new NullPointerException();
			}
			year = date.getYear();
			month = date.getMonthValue();

			place = txf1.getText();
			if (place == null || place.isEmpty()) {
				errmess = "場所が入力されていません";
				throw new IllegalArgumentException();
			}

			purpose = cmb3.getValue();
			if (purpose == null) {
				errmess = "目的が選択されていません。";
				throw new NullPointerException();
			}

			try {
				price = Integer.parseInt(txf3.getText());
			} catch (NumberFormatException nfe) {
				errmess = "価格には数値を入力してください";
				throw new NumberFormatException();
			}

			state = true;
		} catch (NullPointerException | IllegalArgumentException e) {
			System.out.println(e.getMessage());
			state = false;
		}

		String memo = "";

		if (!state) {
			Arart();
			lb1.setText("ERROR");
		}

		else {
			DataBase DB = DataBase.getInstance();
			if (purpose == "")
				purpose = "*****";
			if (place == "")
				place = "*****";

			price = cmb3.getValue() == "収入" ? price : -price;

			DB.Register(date, place, purpose, price, memo);
			tb.setItems(DB.ShowData(year, month));

			int data[] = DB.GetParam(year, month);
			lb2.setText(String.valueOf(data[0]));//収入
			lb3.setText(String.valueOf(data[1]));//支出
			lb4.setText(String.valueOf(data[2]));//収支

			txf1.setText("");
			txf3.setText("");
			lb1.setText("OK");

		}
	}

	@FXML
	protected void ButtonEvent2(ActionEvent ev) throws SQLException {
		DataBase DB = DataBase.getInstance();

		//このifを修正する
		if (cmb1.getValue() == null || cmb2.getValue() == null) {
			Arart();
		}

		if (cmb1.getValue() != null && cmb2.getValue() != null) {
			String tmp[] = cmb1.getValue().split("年");
			String tmp2[] = cmb2.getValue().split("月");
			int year = Integer.parseInt(tmp[0]);
			int month = Integer.parseInt(tmp2[0]);
			publicMonth = month;
			publicYear = year;

			tb.setItems(DB.ShowData(year, month));

			int data[] = DB.GetParam(year, month);
			lb2.setText(String.valueOf(data[0]));//支出
			lb3.setText(String.valueOf(data[1]));//収入
			lb4.setText(String.valueOf(data[2]));//収支
		}
	}

	//修正のウィンドウ表示
	@FXML
	protected void ButtonEvent3(ActionEvent ev) {
		try {
			FixWindow();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void FixWindow() throws IOException, SQLException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("FixWindow.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle("修正画面");
		stage.setScene(scene);

		//サブウィンドウが閉じられたことの判定
		stage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				DataBase DB = DataBase.getInstance();
				tb.setItems(DB.ShowData(publicYear, publicMonth));

				int data[] = DB.GetParam(publicYear, publicMonth);
				lb2.setText(String.valueOf(data[0]));//支出
				lb3.setText(String.valueOf(data[1]));//収入
				lb4.setText(String.valueOf(data[2]));//収支
			}
		});
		//ここまで
		stage.showAndWait();

	}
	//ここまで

	//削除のウィンドウ表示
	@FXML
	protected void ButtonEvent4(ActionEvent ev) throws SQLException {
		try {
			DelWindow();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void DelWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("DelWindow.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle("削除画面");
		stage.setScene(scene);
		//サブウィンドウが閉じられたことの判定
		stage.setOnHidden(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent we) {
				DataBase DB = DataBase.getInstance();
				tb.setItems(DB.ShowData(publicYear, publicMonth));

				int data[] = DB.GetParam(publicYear, publicMonth);
				lb2.setText(String.valueOf(data[0]));//支出
				lb3.setText(String.valueOf(data[1]));//収入
				lb4.setText(String.valueOf(data[2]));//収支

			}
		});
		//ここまで
		stage.showAndWait();
	}

	//設定のウィンドウ表示
	@FXML
	protected void ButtonEvent5(ActionEvent ev) {
		try {
			SettingWindow();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}

	void SettingWindow() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SettingWindow.fxml"));
		BorderPane root = (BorderPane) loader.load();
		Scene scene = new Scene(root);
		Stage stage = new Stage();
		stage.setTitle("設定画面");
		stage.setScene(scene);
		stage.show();
	}

	public void Arart() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("入力エラー");
		alert.setHeaderText("確認");
		if(errmess==null)
			alert.setContentText("入力されていない項目があります。");
		else
			alert.setContentText(errmess);
		alert.show();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		DataBase DB = DataBase.getInstance();
		//コンボボックスの初期化
		for (int i = 1; i <= 12; i++) {
			cmb1.getItems().add(2021 + i + "年");
			cmb2.getItems().add(i + "月");
		}
		//ここまで

		//目的用コンボボックスの初期化
		cmb3.getItems().addAll("収入", "食費", "外食", "住居・備品", "光熱・水道", "被服", "保健・衛生", "教育", "娯楽", "交際", "交通・通信", "小遣い",
				"その他");
		//ここまで
		LocalDate now = LocalDate.now();
		int month = now.getMonthValue();
		int year = now.getYear();
		publicYear = year;
		publicMonth = month;

		int data[] = DB.GetParam(year, month);
		lb2.setText(String.valueOf(data[0]));//支出
		lb3.setText(String.valueOf(data[1]));//収入
		lb4.setText(String.valueOf(data[2]));//収支

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
		tb.setItems(DB.ShowData(year, month));
		//ここまで

	}

}
