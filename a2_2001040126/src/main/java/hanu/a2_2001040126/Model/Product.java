package hanu.a2_2001040126.Model;

public class Product {
    private int id;
    private String category;
    private String productImg;
    private String name;
    private double price;
    private int quantity = 0;

    public Product(int id , String productImg, String name,String category, double price) {
        this.id = id;
        this.productImg = productImg;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Product(int id, String productImg, String name, String category, double price, int quantity) {
        this.id = id;
        this.productImg = productImg;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
