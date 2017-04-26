package com.example.bartekpc.gl_shoppinglist.model;

/**
 * Created by BartekPC on 4/26/2017.
 */

public enum ProductFilterMode
{
    FILTER_ALL(0),
    FILTER_NOT_PURCHASED(1);

    private final int index;

    ProductFilterMode(final int index)
    {
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }
}
