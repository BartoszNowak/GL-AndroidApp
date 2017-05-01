package com.example.bartekpc.gl_shoppinglist.model;

import io.realm.RealmObject;
import lombok.Getter;

@Getter
public class AppSettings extends RealmObject
{
    private String sortMode;
    private int sortModeIndex;
    private int filterModeIndex;

    public AppSettings(final ProductSortMode sortMode, final ProductFilterMode filterMode)
    {
        this.sortMode = sortMode.toString();
        this.sortModeIndex = sortMode.getIndex();
        this.filterModeIndex = filterMode.getIndex();
    }

    public AppSettings() {}

    public void setSortMode(final ProductSortMode sortMode)
    {
        this.sortMode = sortMode.toString();
        this.sortModeIndex = sortMode.getIndex();
    }

    public void setFilterMode(final ProductFilterMode filterMode)
    {
        this.filterModeIndex = filterMode.getIndex();
    }
}
