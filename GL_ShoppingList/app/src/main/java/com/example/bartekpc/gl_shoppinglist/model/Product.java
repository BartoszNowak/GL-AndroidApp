package com.example.bartekpc.gl_shoppinglist.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product extends RealmObject
{
    private static final float AMOUNT_DEFAULT_VALUE = 1f;

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
        this.isFavourite = false;
    }

    public Product(final String name, final float price)
    {
        this.name = name;
        this.price = price;
        amount = AMOUNT_DEFAULT_VALUE;
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
