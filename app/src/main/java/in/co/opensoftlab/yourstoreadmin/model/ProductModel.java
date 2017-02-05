package in.co.opensoftlab.yourstoreadmin.model;


/**
 * Created by dewangankisslove on 12-12-2016.
 */
public class ProductModel {
    String productName;
    String productUrls;
    String category;
    String subCategory;
    String brand;
    int mrp;
    int sellingPrice;
    int discount;
    int qty;
    String productFeatures;
    String productSpecifications;
    String sellerName;
    String sellerId;
    String sellerUrl;
    String storeName;
    String services;
    String address;
    String geoLocation;

    public ProductModel() {
    }

    public ProductModel(String productName, String productUrls, String category, String subCategory, String brand, int mrp, int sellingPrice, int discount, int qty, String productFeatures, String productSpecifications, String sellerName, String sellerId, String sellerUrl, String storeName, String services, String address, String geoLocation) {
        this.productName = productName;
        this.productUrls = productUrls;
        this.category = category;
        this.subCategory = subCategory;
        this.brand = brand;
        this.mrp = mrp;
        this.sellingPrice = sellingPrice;
        this.discount = discount;
        this.qty = qty;
        this.productFeatures = productFeatures;
        this.productSpecifications = productSpecifications;
        this.sellerName = sellerName;
        this.sellerId = sellerId;
        this.sellerUrl = sellerUrl;
        this.storeName = storeName;
        this.services = services;
        this.address = address;
        this.geoLocation = geoLocation;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductUrls() {
        return productUrls;
    }

    public void setProductUrls(String productUrls) {
        this.productUrls = productUrls;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getMrp() {
        return mrp;
    }

    public void setMrp(int mrp) {
        this.mrp = mrp;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getProductFeatures() {
        return productFeatures;
    }

    public void setProductFeatures(String productFeatures) {
        this.productFeatures = productFeatures;
    }

    public String getProductSpecifications() {
        return productSpecifications;
    }

    public void setProductSpecifications(String productSpecifications) {
        this.productSpecifications = productSpecifications;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerUrl() {
        return sellerUrl;
    }

    public void setSellerUrl(String sellerUrl) {
        this.sellerUrl = sellerUrl;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getServices() {
        return services;
    }

    public void setServices(String services) {
        this.services = services;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(String geoLocation) {
        this.geoLocation = geoLocation;
    }
}
