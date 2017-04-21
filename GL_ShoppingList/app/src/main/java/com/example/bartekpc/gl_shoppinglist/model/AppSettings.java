package com.example.bartekpc.gl_shoppinglist.model;

import com.example.bartekpc.gl_shoppinglist.ProductSortModes;

import io.realm.RealmObject;

/**
 * Created by BartekPC on 4/19/2017.
 */

public class AppSettings extends RealmObject
{
    private String sortMode;

    public String getSortMode()
    {
        return sortMode;
    }

    public void setSortMode(final ProductSortModes sortMode)
    {
        this.sortMode = sortMode.toString();
    }
}
