package com.sporthenon.android.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.utils.AndroidUtils;
import com.sporthenon.android.utils.DrawerFragment;

import java.util.ArrayList;

@SuppressWarnings("deprecated")
public abstract class AbstractActivity extends ActionBarActivity implements DrawerFragment.NavigationDrawerCallbacks, AdapterView.OnItemClickListener {

    private DrawerFragment mDrawerFragment;
    private CharSequence mTitle;

    protected Integer sportId;
    protected Integer championshipId;
    protected Integer event1Id;
    protected Integer event2Id;
    protected Integer event3Id;
    protected String sportName;
    protected String championshipName;
    protected String event1Name;
    protected String event2Name;
    protected String event3Name;

    public ListView getList() {
        return PlaceholderFragment.getList();
    }

    public String getLang() {
        return PlaceholderFragment.getLang();
    }

    public ArrayList<Object> getItemList() {
        return PlaceholderFragment.getItemList();
    }

    public TextView getPath() {
        return PlaceholderFragment.getPath();
    }

    public void setPath(String s) {
        if (s != null)
            getPath().setText((AndroidUtils.notEmpty(getPath().getText().toString()) ? getPath().getText() + "\r\n" : "") + s.replaceAll("\\r\\n\\+", "\\\r\\\n"));
    }

    public void hideProgress() {
        PlaceholderFragment.hideProgress();
    }

    public Integer getSportId() {
        return sportId;
    }

    public void setSportId(Integer sportId) {
        this.sportId = sportId;
    }

    public Integer getChampionshipId() {
        return championshipId;
    }

    public void setChampionshipId(Integer championshipId) {
        this.championshipId = championshipId;
    }

    public Integer getEvent1Id() {
        return event1Id;
    }

    public void setEvent1Id(Integer event1Id) {
        this.event1Id = event1Id;
    }

    public Integer getEvent2Id() {
        return event2Id;
    }

    public void setEvent2Id(Integer event2Id) {
        this.event2Id = event2Id;
    }

    public Integer getEvent3Id() {
        return event3Id;
    }

    public void setEvent3Id(Integer event3Id) {
        this.event3Id = event3Id;
    }

    public String getSportName() {
        return sportName;
    }

    public void setSportName(String sportName) {
        this.sportName = sportName;
    }

    public String getChampionshipName() {
        return championshipName;
    }

    public void setChampionshipName(String championshipName) {
        this.championshipName = championshipName;
    }

    public String getEvent1Name() {
        return event1Name;
    }

    public void setEvent1Name(String event1Name) {
        this.event1Name = event1Name;
    }

    public String getEvent2Name() {
        return event2Name;
    }

    public void setEvent2Name(String event2Name) {
        this.event2Name = event2Name;
    }

    public String getEvent3Name() {
        return event3Name;
    }

    public void setEvent3Name(String event3Name) {
        this.event3Name = event3Name;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_navigation);

        mDrawerFragment = (DrawerFragment)  getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        /*
        super.onCreate(state);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

        title = (TextView) findViewById(R.id.title);
        progress = (ProgressBar) findViewById(R.id.progress);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(this);

        search = (EditText) findViewById(R.id.search);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {filter();}
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void afterTextChanged(Editable arg0) { }
        });
        search.setVisibility(View.GONE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lang = prefs.getString("lang", null);
         */
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, PlaceholderFragment.newInstance(position + 1)).commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_results);
                break;
            case 2:
                mTitle = getString(R.string.title_calendar);
                break;
            case 3:
                mTitle = getString(R.string.title_olympics);
                break;
            case 4:
                mTitle = getString(R.string.title_us_leagues);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.navigation, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";
        protected static ProgressBar progress;
        protected static ListView list;
        protected static String lang;
        protected static EditText search;
        protected static TextView path;
        private static ArrayList<Object> itemList;

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        public static ListView getList() {
            return list;
        }

        public static void setList(ListView list_) {
            list = list_;
        }

        public static ArrayList<Object> getItemList() {
            if (itemList == null)
                itemList = new ArrayList<>();
            return itemList;
        }

        public static String getLang() {
            return lang;
        }

        public static void setLang(String lang_) {
            lang = lang_;
        }

        public static TextView getPath() {
            return path;
        }

        public static void setPath(TextView path) {
            PlaceholderFragment.path = path;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.activity_main, container, false);
            //super.onCreate(state);
            //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
            //setContentView(R.layout.activity_main);
            //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);

            progress = (ProgressBar) view.findViewById(R.id.progress);
            list = (ListView) view.findViewById(R.id.list);
            list.setOnItemClickListener((AbstractActivity) this.getActivity());
            path = (TextView) view.findViewById(R.id.path);

            System.out.println(list+" = "+path);

            search = (EditText) view.findViewById(R.id.search);
            search.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {filter();}
                @Override
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
                @Override
                public void afterTextChanged(Editable arg0) { }
            });
            search.setVisibility(View.GONE);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getActivity());
            lang = prefs.getString("lang", null);
            return view;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((AbstractActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void filter() {
           /* String text = search.getText().toString().toLowerCase();
            list.setAdapter(null);
            if (this instanceof ResultActivity) {
                ArrayList<ResultItem> list_ = new ArrayList<>();
                for (Object o : itemList) {
                    ResultItem item = (ResultItem) o;
                    if (text.length() == 0 || item.getTxt1().toLowerCase().matches(".*" + text + ".*"))
                        list_.add(item);
                }
                list.setAdapter(new ResultListAdapter(getApplicationContext(), list_));
            }
            else {
                ArrayList<DataItem> list_ = new ArrayList<>();
                for (Object o : itemList) {
                    DataItem item = (DataItem) o;
                    if (text.length() == 0 || item.getName().toLowerCase().matches(".*" + text + ".*"))
                        list_.add(item);
                }
                list.setAdapter(new ItemListAdapter(getApplicationContext(), list_));
            }*/
        }

        public void onSearchClick(View v) {
            search.setVisibility(search.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            if (search.getVisibility() == View.VISIBLE)
                search.requestFocus();
        }

        public static void hideProgress() {
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public abstract void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3);

    @Override
    public void onBackPressed() {
        finish();
    }

    public void onBackClick(View v) {
        finish();
    }

}