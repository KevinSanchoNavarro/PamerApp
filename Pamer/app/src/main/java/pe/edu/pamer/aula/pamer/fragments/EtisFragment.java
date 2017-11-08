
package pe.edu.pamer.aula.pamer.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import pe.edu.pamer.aula.pamer.R;
import pe.edu.pamer.aula.pamer.activities.NavDrawerActivity;


public class EtisFragment extends Fragment {

    private AppBarLayout appBarLayout;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_etis,container,false);

        ((NavDrawerActivity) getActivity())
                .setActionBarTitle("ETIS");

        View contenedor = (View) container.getParent();

        appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar_etis);
        tabLayout = (TabLayout) view.findViewById(R.id.tablayout_etis);
        //tabs.set(appBar.getHeight());
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#FFFFFF"));

        viewPager = (ViewPager) view.findViewById(R.id.pager_etis);

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            if((NavDrawerActivity.id_perfil_usuario).equals("7")){
                //Colegios
                switch (position) {
                    case 0: return new TabIBimestreFragment();
                    case 1: return new TabIIBimestreFragment();
                    case 2: return new TabIIIBimestreFragment();
                    case 3: return new TabIVBimestreFragment();
                }

            } else {
                //Academias
                    return new TabAcademiasFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            if((NavDrawerActivity.id_perfil_usuario).equals("7")){
                return 4;
            } else {
                return 1;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {

            if((NavDrawerActivity.id_perfil_usuario).equals("7")){
                //Colegios
                switch (position) {
                    case 0:
                        return "Bimestre 1";
                    case 1:
                        return "Bimestre 2";
                    case 2:
                        return "Bimestre 3";
                    case 3:
                        return "Bimestre 4";
                }

            } else {
                //Academias

                    return "Academias";

            }

            return null;
        }
    }


}

