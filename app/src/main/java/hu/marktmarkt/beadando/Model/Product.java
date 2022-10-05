package hu.marktmarkt.beadando.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private final String name;
    private final int price;
    private final String desc;
    private final String img;

    public Product(String name, int price, String desc, String img){
        this.name = name;
        this.price = price;
        this.desc = desc;
        this.img = img;
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
}
