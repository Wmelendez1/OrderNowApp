package Models;

import java.util.Date;

public class Order {
    private String title;
    private String description;

    private Date datePlaced;
    private boolean isExpanded;

    public Order(String title, String description, Date dateplaced) {
        this.title = title;
        this.description = description;
        this.datePlaced = dateplaced;
        this.isExpanded = false;  // Initially not expanded
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Date getDatePlaced(){
        return datePlaced;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }
}

