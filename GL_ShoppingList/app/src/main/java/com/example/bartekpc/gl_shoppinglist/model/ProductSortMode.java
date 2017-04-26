package com.example.bartekpc.gl_shoppinglist.model;

public enum ProductSortMode
{
    SORT_DEFAULT("id", 0),
    SORT_BY_NAME("name", 1),
    SORT_BY_PRICE("price", 2);

    private final String text;
    private final int index;

    ProductSortMode(final String text, final int index)
    {
        this.text = text;
        this.index = index;
    }

    public int getIndex()
    {
        return index;
    }

    @Override
    public String toString()
    {
        return text;
    }
}
