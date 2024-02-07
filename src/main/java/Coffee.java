import javax.swing.*;

public class Coffee {
    private String name;
    private ImageIcon image;
    private double price;
    public Coffee(){

    }
    public Coffee(String name, String imagePath, double price) {
        this.name = name;
        this.image = new ImageIcon(imagePath);
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ImageIcon getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }
}
