package com.example.bartekpc.gl_shoppinglist.model;

import io.realm.RealmObject;

public class AppSettings extends RealmObject
{
    private String sortMode;
    private int sortModeIndex;

    private int filterModeIndex;

    public AppSettings(final ProductSortMode sortMode)
    {
        this.sortMode = sortMode.toString();
        this.sortModeIndex = sortMode.getIndex();
    }

    public AppSettings(final ProductSortMode sortMode, final ProductFilterMode filterMode)
    {
        this.sortMode = sortMode.toString();
        this.sortModeIndex = sortMode.getIndex();
        this.filterModeIndex = filterMode.getIndex();
    }

    public AppSettings()
    {}

    public String getSortMode()
    {
        return sortMode;
    }

    public int getSortModeIndex()
    {
        return sortModeIndex;
    }

    public void setSortMode(final ProductSortMode sortMode)
    {
        this.sortMode = sortMode.toString();
        this.sortModeIndex = sortMode.getIndex();
    }

    public int getFilterModeIndex()
    {
        return filterModeIndex;
    }

    public void setFilterMode(final ProductFilterMode filterMode)
    {
        this.filterModeIndex = filterMode.getIndex();
    }
}
