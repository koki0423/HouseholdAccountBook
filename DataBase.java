package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataBase {
	private String url;
	private String user;
	private String password;
	private static DataBase instance = null;

	public void setURL(String ip) {
		this.url = "jdbc:mariadb://" + ip + ":3306/AccountBook";
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	//シングルトンパターン
	public static DataBase getInstance() {
		if (instance == null) {
			instance = new DataBase();
		}
		return instance;
	}

	//このコンストラクタはいじらないこと
	//家族の家計簿入力業務が停止する！
	//絶対に コンストラクタ 消すな！ 編集するな！！ 
	private DataBase() {
	}
	//ここまで

	//データを表示する
	public ObservableList<Account> ShowData(int year, int month) {
		String sql = "SELECT * FROM account WHERE YEAR(date) = ? AND MONTH(date) = ?";
		ObservableList<Account> accounts = FXCollections.observableArrayList();

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, Integer.toString(year));
			pstmt.setString(2, String.format("%02d", month));

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				Account account = new Account(rs.getInt("id"), rs.getString("date"),
						rs.getString("place"), rs.getString("purpose"), Math.abs(rs.getInt("amount")),
						rs.getString("memo"));
				accounts.add(account);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

		return accounts;
	}

	//データの登録
	public void Register(LocalDate Date, String place, String purpose, int amount, String memo) {

		memo = "";//テストなのでmemoはnullにする

		String date = Date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
		String sql = "INSERT INTO account(date, place, purpose, amount,memo) VALUES(?, ?, ?, ?, ?)";
		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setString(1, date);
			pstmt.setString(2, place);
			pstmt.setString(3, purpose);
			pstmt.setInt(4, amount);
			pstmt.setString(5, memo);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void DeleteData(int id) {
		String sql = "DELETE FROM account WHERE id = ?";

		try (Connection conn = DriverManager.getConnection(url, user, password);
				PreparedStatement pstmt = conn.prepareStatement(sql)) {

			pstmt.setInt(1, id);
			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public void Update(int id, LocalDate Date, String place, String purpose, int price) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String date = Date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));

		try {
			conn = DriverManager.getConnection(url, user, password);
			String sql = "UPDATE account SET date = ?, place = ?, purpose = ?, amount = ? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, date);
			pstmt.setString(2, place);
			pstmt.setString(3, purpose);
			pstmt.setInt(4, price);
			pstmt.setInt(5, id);

			int rowsAffected = pstmt.executeUpdate();
			if (rowsAffected > 0) {
				System.out.println("Update successful.");
			} else {
				System.out.println("Update failed or no rows were affected.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public Object[] SearchData(int id) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Object[] data = new Object[4];

		try {
			conn = DriverManager.getConnection(url, user, password);
			String sql = "SELECT date, place, purpose, amount FROM account WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {

				data[0] = rs.getString("date");
				data[1] = rs.getString("place");
				data[2] = rs.getString("purpose");
				data[3] = rs.getInt("amount");

			}
		} catch (SQLException se) {
			se.printStackTrace();
		}

		return data;
	}

	//指定した年月支出と収入、収支の配列を返す
	public int[] GetParam(int year, int month) {
		Connection conn = null;
		Statement stmt = null;
		int[] data = new int[3];

		try {
			//収入のカウント収入
			conn = DriverManager.getConnection(url, user, password);
			stmt = conn.createStatement();
			String sql = "SELECT amount FROM account WHERE EXTRACT(YEAR FROM date) = " + year +
					" AND EXTRACT(MONTH FROM date) = " + month +
					" AND amount > 0";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				data[0] += rs.getInt("amount");
			}

			rs.close();

			//支出のカウント
			sql = "SELECT amount FROM account WHERE EXTRACT(YEAR FROM date) = " + year +
					" AND EXTRACT(MONTH FROM date) = " + month +
					" AND amount < 0";
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				data[1] += rs.getInt("amount");
			}
			rs.close();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}

		//支出はマイナスで登録されてるので加算する
		data[2] = data[0] + data[1];
		return data;
	}

}
