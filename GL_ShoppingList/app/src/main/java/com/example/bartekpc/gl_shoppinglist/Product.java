package com.example.bartekpc.gl_shoppinglist;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by BartekPC on 4/1/2017.
 */

public class Product extends RealmObject
{
    @PrimaryKey
    private long id;
    private String name;
    private float price;

    public Product(final String name, final float price)
    {
        this.name = name;
        this.price = price;
    }

    public Product()
    {
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

    @Override
    public String toString()
    {
        return "Product{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
