package hu.marktmarkt.beadando.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private final int id;
    private final String name;
    private final int price;
    private final String desc;
    private final String img;
    private final int discount;

    public Product(int id, String name, int price, String desc, String img, int discount){
        this.id = id;
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.img = img;
        this.discount = discount;
    }

    public String getImg() {
        return img;
    }

    public String getDesc() {
        return desc;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", desc='" + desc + '\'' +
                ", img='" + img + '\'' +
                ", img='" + discount + '\'' +
                '}';
    }

    public int getDiscount() {
        return discount;
    }
}