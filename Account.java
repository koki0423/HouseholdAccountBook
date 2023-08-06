package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Account {
	private final SimpleIntegerProperty id;
	private final SimpleStringProperty date;
	private final SimpleStringProperty place;
	private final SimpleStringProperty purpose;
	private final SimpleIntegerProperty price;
	private final SimpleStringProperty memo;

	public Account(int id, String date, String place, String purpose, int price,String memo) {
		this.id = new SimpleIntegerProperty(id);
		this.date = new SimpleStringProperty(date);
		this.place = new SimpleStringProperty(place);
		this.purpose = new SimpleStringProperty(purpose);
		this.price = new SimpleIntegerProperty(price);
		this.memo=new SimpleStringProperty(memo);
	}

	public int getId() {
		return id.get();
	}

	public String getDate() {
		return date.get();
	}

	public String getPlace() {
		return place.get();
	}

	public String getPurpose() {
		return purpose.get();
	}

	public int getPrice() {
		return price.get();
	}
	
	public String getMemo() {
		return memo.get();
	}
	
}
