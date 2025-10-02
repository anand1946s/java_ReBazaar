package model;

public class Product {
	private int id;
	private String name;
	private String description;
	private String category;
	private double price;

	public Product(String name, String description, String category, double price) {
		this.name = name;
		this.description = description;
		this.category = category;
		this.price = price;
	}

	// getters / setters
	public int getId() { return id; }
	public void setId(int id) { this.id = id; }

	public String getName() { return name; }
	public String getDescription() { return description; }
	public String getCategory() { return category; }
	public double getPrice() { return price; }

	@Override
	public String toString() {
		return String.format("[%d] %s (%.2f) - %s", id, name, price, category);
	}
}
