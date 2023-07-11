package com.ramotion.directselect.examples.advanced;

import com.banestudio.imperialvpn.R;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AdvancedExampleCountryPOJO {

    private String title;
    private int icon;

    public AdvancedExampleCountryPOJO(String title, int icon) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public static AdvancedExampleCountryPOJO getByCountry(String countryTitle) {
        for (AdvancedExampleCountryPOJO element: getExampleDataset()) {
            if(Objects.equals(element.title, countryTitle)) {
                return element;
            }
        }
        return null;
    }

    public static List<AdvancedExampleCountryPOJO> getExampleDataset() {
        return Arrays.asList(
                new AdvancedExampleCountryPOJO("Germany", R.drawable.ds_countries_germany),
                new AdvancedExampleCountryPOJO("USA", R.drawable.ds_countries_us),
                new AdvancedExampleCountryPOJO("Auto", R.drawable.ds_countries_random)/*,
                new AdvancedExampleCountryPOJO("Russia", R.drawable.ds_countries_ru),
                new AdvancedExampleCountryPOJO("Canada", R.drawable.ds_countries_ca),
                new AdvancedExampleCountryPOJO("China", R.drawable.ds_countries_cn),
                new AdvancedExampleCountryPOJO("Brazil", R.drawable.ds_countries_br),
                new AdvancedExampleCountryPOJO("Australia", R.drawable.ds_countries_au),
                new AdvancedExampleCountryPOJO("India", R.drawable.ds_countries_in),
                new AdvancedExampleCountryPOJO("Argentina", R.drawable.ds_countries_ar),
                new AdvancedExampleCountryPOJO("Kazakhstan", R.drawable.ds_countries_kz),
                new AdvancedExampleCountryPOJO("Algeria", R.drawable.ds_countries_dz)*/
        );
    }
}
