package com.example.mcommerce;

public class Product
{
    private double price;
    private double amount;
    private String name;
    private String category;
    private String description;
    private String imageName;

    public String getImageName()
    {
        return imageName;
    }

    public void setImageName(String imageName)
    {
        this.imageName = imageName;
    }

    public String getPrice()
    {
        return String.valueOf(price);
    }

    public void setPrice(double price)
    {
        this.price = price;
    }

    public String getAmount()
    {
        return String.valueOf(amount);
    }

    public void setAmount(Double amount)
    {
        this.amount = amount;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getCategory()
    {
        return category;
    }

    public void setCategory(String category)
    {
        this.category = category;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
