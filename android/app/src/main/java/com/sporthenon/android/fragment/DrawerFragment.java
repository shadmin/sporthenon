package com.sporthenon.android.fragment;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sporthenon.android.R;

public class DrawerFragment extends Fragment {

    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";
    private NavigationDrawerCallbacks callbacks;
    private ActionBarDrawerToggle drawerToggle;

    private DrawerLayout drawerLayout;
    private ListView drawerList;
    private View fragmentContainerView;

    private int currentIndex = 0;
    private boolean fromSavedInstanceState;
    private boolean userLearnedDrawer;

    public DrawerFragment() {
    }

    public ActionBarDrawerToggle getDrawerToggle() {
        return drawerToggle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        userLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            fromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        drawerList = (ListView) inflater.inflate(R.layout.list_menu, container, false);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectItem(position, true);
            }
        });
        String[] t = new String[]{getString(R.string.title_results), getString(R.string.title_calendar), getString(R.string.title_olympics), getString(R.string.title_us_leagues), getString(R.string.title_settings)};
        ArrayAdapter adapter = new ArrayAdapter<Object>(getActionBar().getThemedContext(), R.layout.item_menu, R.id.menu_item_title, t){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View view = inflater.inflate(R.layout.item_menu, parent, false);

                int[] tImg = new int[]{R.drawable.results, R.drawable.calendar, R.drawable.olympics, R.drawable.usleagues, R.drawable.settings};
                int[] tTitle = new int[]{R.string.title_results, R.string.title_calendar, R.string.title_olympics, R.string.title_us_leagues, R.string.title_settings};

                ImageView img = (ImageView) view.findViewById(R.id.menu_item_icon);
                TextView title = (TextView) view.findViewById(R.id.menu_item_title);
                img.setImageResource(tImg[position]);
                title.setText(getString(tTitle[position]).toUpperCase());

                return view;
            }
        };
        drawerList.setAdapter(adapter);
        drawerList.setItemChecked(currentIndex, true);
        return drawerList;
    }

    public boolean isDrawerOpen() {
        return drawerLayout != null && drawerLayout.isDrawerOpen(fragmentContainerView);
    }

    public void setUp(int id, DrawerLayout drawerLayout) {
        fragmentContainerView = getActivity().findViewById(id);
        this.drawerLayout = drawerLayout;
        //this.drawerLayout.setDrawerShadow(R.drawable.shadow, GravityCompat.START);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        drawerToggle = new ActionBarDrawerToggle(getActivity(), DrawerFragment.this.drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
                if (!isAdded())
                    return;
                if (!userLearnedDrawer) {
                    userLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }
                getActivity().supportInvalidateOptionsMenu();
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActionBar().setTitle(getActivity().getTitle());
                if (!isAdded())
                    return;
                getActivity().supportInvalidateOptionsMenu();
            }
        };


        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                // show the given tab
            }

            public void onTabUnselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, android.support.v4.app.FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        // Add 3 tabs, specifying the tab's text and TabListener
        for (int i = 0; i < 3; i++) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText("Tab " + (i + 1))
                            .setTabListener(tabListener));
        }

        if (!userLearnedDrawer && !fromSavedInstanceState)
            this.drawerLayout.openDrawer(fragmentContainerView);

        this.drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        this.drawerLayout.setDrawerListener(drawerToggle);
    }

    public void selectItem(int position, boolean redirect) {
        currentIndex = position;
        if (drawerList != null)
            drawerList.setItemChecked(position, true);
        if (drawerLayout != null)
            drawerLayout.closeDrawer(fragmentContainerView);
        if (callbacks != null && redirect)
            callbacks.onNavigationDrawerItemSelected(position);
        String[] tColor = new String[]{"#389f47", "#f47322", "#1482c5", "#de1f26", "#AAA"};
        getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor(tColor[position])));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            callbacks = (NavigationDrawerCallbacks) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, currentIndex);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    public interface NavigationDrawerCallbacks {
        void onNavigationDrawerItemSelected(int position);
    }

}