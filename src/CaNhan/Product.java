package CaNhan;

import java.sql.Date;

public class Product {
    private int id;
    private String name;
    private String producer;
    private double price;
    private Date created_date;

    // Constructor không đối số
    public Product() {}

    // Constructor có đối số
    public Product(int id, String name, String producer, double price, Date created_date) {
        this.id = id;
        this.name = name;
        this.producer = producer;
        this.price = price;
        this.created_date = created_date;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getProducer() { return producer; }
    public void setProducer(String producer) { this.producer = producer; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Date getCreated_date() { return created_date; }
    public void setCreated_date(Date created_date) { this.created_date = created_date; }
}
