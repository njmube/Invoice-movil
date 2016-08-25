package ec.bigdata.facturaelectronicamovil.adaptador;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DavidLeonardo on 30/7/2016.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> tituloArrayList = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    //Agregando fragmentos y el título del tab
    public void addFrag(Fragment fragment, String title) {
        fragmentList.add(fragment);
        tituloArrayList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return tituloArrayList.get(position);
    }

}
