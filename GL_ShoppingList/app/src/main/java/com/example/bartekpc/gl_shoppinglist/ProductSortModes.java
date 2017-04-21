package com.example.bartekpc.gl_shoppinglist;

/**
 * Created by BartekPC on 4/19/2017.
 */

public enum ProductSortModes
{
    SORT_BY_NAME("name"),
    SORT_BY_PRICE("price");

    private final String text;

    ProductSortModes(final String text)
    {
        this.text = text;
    }

    @Override
    public String toString()
    {
        return text;
    }
}
