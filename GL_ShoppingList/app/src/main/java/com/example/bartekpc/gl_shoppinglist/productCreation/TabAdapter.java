package com.example.bartekpc.gl_shoppinglist.productCreation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter
{
    private static final int TAB_COUNT = 3;
    private String tabTitles[] = new String[] { "Popularne", "Ulubione", "Nowy Produkt" };
    private long catalogId;

    public TabAdapter(final FragmentManager fm, final long catalogId)
    {
        super(fm);
        this.catalogId = catalogId;
    }

    @Override
    public Fragment getItem(final int position)
    {
        Fragment fragment;
        switch (position)
        {
            case 2:
            {
                fragment = ProductCreationFragment.getInstance(catalogId);
                break;
            }
            default:
            {
                fragment = ProductListFragment.getInstance(position);
                break;
            }
        }
        return fragment;
    }

    @Override
    public int getCount()
    {
        return TAB_COUNT;
    }

    @Override
    public CharSequence getPageTitle(final int position)
    {
        return tabTitles[position];
    }
}
