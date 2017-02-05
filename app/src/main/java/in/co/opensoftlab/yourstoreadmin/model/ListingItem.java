package in.co.opensoftlab.yourstoreadmin.model;

/**
 * Created by dewangankisslove on 06-01-2017.
 */

public class ListingItem {
    String id;
    String productUrl;
    String productName;
    int productPrice;
    int quantity;

    public ListingItem(String id, String productUrl, String productName, int productPrice, int quantity) {
        this.id = id;
        this.productUrl = productUrl;
        this.productName = productName;
        this.productPrice = productPrice;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
