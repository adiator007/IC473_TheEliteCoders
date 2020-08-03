package com.example.smartpds.model;

public class Product {

    long  price;
    String  quanity;
    String cartUserQuntity;
    String productImage;

    String cartPriceQuantity ;
    public String getCartPriceQuantity() {
        return cartPriceQuantity;
    }

    public void setCartPriceQuantity(String cartPriceQuantity) {
        this.cartPriceQuantity = cartPriceQuantity;
    }


    public String getCartUserQuntity() {
        return cartUserQuntity;
    }

    public void setCartUserQuntity(String cartUserQuntity) {
        this.cartUserQuntity = cartUserQuntity;
    }

    public String getQuanity() {
        return quanity;
    }

    public Product() {
    }

    public void setQuanity(String quanity) {
        this.quanity = quanity;
    }

    public long getPrice() {
        return price;
    }
    public String getProductImage() {
        return productImage;
    }

    public void setPrice(long price) {
        this.price = price;
    }
    public void setProductImage(String image) {
        this.productImage = image;
    }

    public Product(long price, String quanity) {
        this.price = price;
        this.quanity = quanity;
    }



}
