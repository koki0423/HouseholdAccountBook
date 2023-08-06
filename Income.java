package application;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Income {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty date;
    private final SimpleStringProperty purpose;
    private final SimpleIntegerProperty amount;

    public Income(Integer id, String date, String purpose, int amount) {
        this.id = new SimpleIntegerProperty(id);
        this.date = new SimpleStringProperty(date);
        this.purpose = new SimpleStringProperty(purpose);
        this.amount = new SimpleIntegerProperty(amount);
    }

    public Integer getId() {
        return id.get();
    }

    public String getDate() {
        return date.get();
    }

    public String getPurpose() {
        return purpose.get();
    }

    public Integer getAmount() {
        return amount.get();
    }
}
