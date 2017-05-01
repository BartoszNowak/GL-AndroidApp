package com.example.bartekpc.gl_shoppinglist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    public Product(final String name)
    {
        this.name = name;
        //TODO:
        this.isFavourite = false;
        this.price = 0f;
        this.amount = 1f;
    }

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
}
