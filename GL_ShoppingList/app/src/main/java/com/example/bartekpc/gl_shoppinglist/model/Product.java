package com.example.bartekpc.gl_shoppinglist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Product extends RealmObject
{
    @PrimaryKey
    private long id;
    private String name;
    private float price;
    private float amount;
    private boolean isPurchased;
    private boolean isFavourite;
    private long catalogId;

    public Product(final String name, final float price)
    {
        this.name = name;
        this.price = price;
        amount = 1;
    }

    public Product(final String name, final float price, final float amount)
    {
        this.name = name;
        this.price = price;
        this.amount = amount;
    }

    public Product()
    {
    }

    public long getId()
    {
        return id;
    }

    public void setId(final long id)
    {
        this.id = id;
    }

    public long getCatalogId()
    {
        return catalogId;
    }

    public void setCatalogId(final long catalogId)
    {
        this.catalogId = catalogId;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public float getPrice()
    {
        return price;
    }

    public void setPrice(final float price)
    {
        this.price = price;
    }

    public float getAmount()
    {
        return amount;
    }

    public void setAmount(final int amount)
    {
        this.amount = amount;
    }

    public boolean isPurchased()
    {
        return isPurchased;
    }

    public void setPurchased(final boolean purchased)
    {
        isPurchased = purchased;
    }

    public boolean isFavourite()
    {
        return isFavourite;
    }

    public void setFavourite(final boolean favourite)
    {
        isFavourite = favourite;
    }
}
