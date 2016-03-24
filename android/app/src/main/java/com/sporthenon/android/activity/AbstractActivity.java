package com.sporthenon.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.sporthenon.android.R;
import com.sporthenon.android.fragment.DrawerFragment;
import com.sporthenon.android.fragment.ListFragment;
import com.sporthenon.android.fragment.Result1Fragment;
import com.sporthenon.android.utils.AndroidUtils;

import java.util.ArrayList;

@SuppressWarnings("deprecated")
public abstract class AbstractActivity extends ActionBarActivity implements DrawerFragment.NavigationDrawerCallbacks, AdapterView.OnItemClickListener {

    private DrawerFragment drawerFragment;

    protected static final int INDEX_RESULTS = 0;
    protected static final int INDEX_CALENDAR = 1;
    protected static final int INDEX_OLYMPICS = 2;
    protected static final int INDEX_USLEAGUES = 3;
    protected static final int INDEX_SETTINGS = 4;

    protected Integer index;
    protected String lang;
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
        return ListFragment.getList();
    }

    public ArrayList<Object> getItemList() {
        return ListFragment.getItemList();
    }

    public TextView getPath() {
        return ListFragment.getPath();
    }

    public void setPath(String s) {
        if (s != null) {
            getPath().setText((AndroidUtils.notEmpty(getPath().getText().toString()) ? getPath().getText() + "\r\n" : "") + s.replaceAll("\\r\\n\\+", "\\\r\\\n"));
            getPath().setVisibility(View.VISIBLE);
        }
    }

    public void hideProgress() {
        if (this instanceof Result1Activity)
            Result1Fragment.hideProgress();
        else
            ListFragment.hideProgress();
    }

    public String getLang() {
        return lang;
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

    public void setEvent3Name(String event3Name) {
        this.event3Name = event3Name;
    }

    public TextView getLabelResult() {
        return Result1Fragment.getLabelResult();
    }

    public ListView getRankList() {
        return Result1Fragment.getRankList();
    }

    public TextView getYear() {
        return Result1Fragment.getYear();
    }

    public TextView getSport() {
        return Result1Fragment.getSport();
    }

    public TextView getChampionship() {
        return Result1Fragment.getChampionship();
    }

    public TextView getEvent() {
        return Result1Fragment.getEvent();
    }

    public TextView getSubevent() {
        return Result1Fragment.getSubevent();
    }

    public TextView getSubevent2() {
        return Result1Fragment.getSubevent2();
    }

    public TextView getDate() {
        return Result1Fragment.getDate();
    }

    public TextView getPlace1() {
        return Result1Fragment.getPlace1();
    }

    public TextView getPlace2() {
        return Result1Fragment.getPlace2();
    }

    public TextView getLabelDate() {
        return Result1Fragment.getLabelDate();
    }

    public TextView getLabelPlace() {
        return Result1Fragment.getLabelPlace();
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_navigation);

        drawerFragment = (DrawerFragment)  getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        drawerFragment.selectItem(index, false);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.container, ListFragment.newInstance(index + 1, this)).commit();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        lang = prefs.getString("lang", null);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerFragment.getDrawerToggle().syncState();
    }

    @Override
    public void onNavigationDrawerItemSelected(int n) {
        Class c = null;
        if (n == INDEX_RESULTS)
            c = SportActivity.class;
        else if (n == INDEX_CALENDAR)
            c = YearActivity.class;
        Intent i = new Intent(this, c);
        startActivity(i);
        finish();
    }

    public void onSectionAttached(int n) {
        String title = null;
        switch (n) {
            case 1:
                title = getString(R.string.title_results);
                break;
            case 2:
                title = getString(R.string.title_calendar);
                break;
            case 3:
                title = getString(R.string.title_olympics);
                break;
            case 4:
                title = getString(R.string.title_us_leagues);
                break;
            case 5:
                title = getString(R.string.title_settings);
                break;
        }
        setTitle(title);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(getTitle());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.navigation, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public abstract void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3);

}