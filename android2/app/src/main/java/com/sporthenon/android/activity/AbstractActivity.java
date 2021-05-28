package com.sporthenon.android.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.sporthenon.android.R;
import com.sporthenon.android.fragment.DrawerFragment;
import com.sporthenon.android.fragment.ListFragment;
import com.sporthenon.android.fragment.OlympicsModeFragment;
import com.sporthenon.android.fragment.Result1Fragment;
import com.sporthenon.android.fragment.USLeaguesRecordTypeFragment;
import com.sporthenon.android.fragment.USLeaguesTypeFragment;

import java.util.ArrayList;
import java.util.Locale;

@SuppressWarnings("deprecated")
public abstract class AbstractActivity extends AppCompatActivity implements DrawerFragment.NavigationDrawerCallbacks, AdapterView.OnItemClickListener {

    private Fragment fragment;
    private DrawerFragment drawerFragment;
    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    private Menu menu;

    protected static final int INDEX_RESULTS = 0;
    protected static final int INDEX_CALENDAR = 1;
    protected static final int INDEX_OLYMPICS = 2;
    protected static final int INDEX_USLEAGUES = 3;
    //protected static final int INDEX_SETTINGS = 4;

    protected Integer index;
    protected String lang;
    // Results
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
    // Calendar
    protected Integer clYear;
    protected Integer clMonth;
    // Olympics
    protected static final int OLTYPE_WINTER = 0;
    protected static final int OLTYPE_SUMMER = 1;
    protected static final int OLMODE_PODIUM = 0;
    protected static final int OLMODE_MEDALS = 1;
    protected Integer olId;
    protected String olName;
    protected Integer olType;
    // US Leagues
    protected static final int USLEAGUE_NFL = 1;
    protected static final int USLEAGUE_NBA = 2;
    protected static final int USLEAGUE_NHL = 3;
    protected static final int USLEAGUE_MLB = 4;
    protected static final int USTYPE_CHAMPIONSHIPS = 0;
    public static final int USTYPE_RECORDS = 1;
    protected static final int USTYPE_STATS = 2;
    protected static final int USTYPE_HOF = 3;
    protected static final int USTYPE_RETIRED_NUM = 4;
    protected static final int USTYPE_TEAM_STADIUMS = 5;
    protected Integer leagueId;
    protected String leagueName;
    protected Integer usltype;
    protected String rctype;

    public Menu getMenu() {
        return menu;
    }

    public ListView getList() {
        return (fragment != null && fragment instanceof ListFragment ? ((ListFragment) fragment).getList() : null);
    }

    public ArrayList<Object> getItemList() {
        return ListFragment.getItemList();
    }

    public TextView getPath() {
        if (this instanceof OlympicsModeActivity)
            return OlympicsModeFragment.getPath();
        else if (this instanceof USLeaguesTypeActivity)
            return USLeaguesTypeFragment.getPath();
        else if (this instanceof USLeaguesRecordTypeActivity)
            return USLeaguesRecordTypeFragment.getPath();
        else
            return ListFragment.getPath();
    }

    public TextView getNotice() {
        return ListFragment.getNotice();
    }

    public void setPath(String s) {
        if (s != null) {
            String[] t = s.split("\\|");
            StringBuilder sb = new StringBuilder();
            for (int i = 0 ; i < t.length ; i++) {
                if (i > 0) {
                    sb.append("\r\n");
                    /*for (int j = i - 1 ; j > 0 ; j--) {
                        sb.append("   ");
                    }
                    sb.append(" ― ");*/
                }
                sb.append(t[i]);
            }
            getPath().setText(sb.toString());
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

    public void setEvent3Name(String event3Name) {
        this.event3Name = event3Name;
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

    public Integer getClYear() {
        return clYear;
    }

    public void setClYear(Integer clYear) {
        this.clYear = clYear;
    }

    public void setClMonth(Integer clMonth) {
        this.clMonth = clMonth;
    }

    public Integer getOlType() {
        return olType;
    }

    public void setOlType(Integer olType) {
        this.olType = olType;
    }

    public Integer getOlId() {
        return olId;
    }

    public void setOlId(Integer olId) {
        this.olId = olId;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(Integer leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public Integer getUsltype() {
        return usltype;
    }

    public void setUsltype(Integer usltype) {
        this.usltype = usltype;
    }

    public String getRctype() {
        return rctype;
    }

    public void setRctype(String rctype) {
        this.rctype = rctype;
    }

    public String getOlName() {
        return olName;
    }

    public void setOlName(String olName) {
        this.olName = olName;
    }

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_navigation);

        drawerFragment = (DrawerFragment)  getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        drawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
        drawerFragment.selectItem(index, false);

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (this instanceof  OlympicsModeActivity)
            fragment = OlympicsModeFragment.newInstance(index + 1, this);
        else if (this instanceof  USLeaguesTypeActivity)
            fragment = USLeaguesTypeFragment.newInstance(index + 1, this);
        else if (this instanceof USLeaguesRecordTypeActivity)
            fragment = USLeaguesRecordTypeFragment.newInstance(index + 1, this);
        else
            fragment = ListFragment.newInstance(index + 1, this);
        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        /*List<Fragment> fList = new ArrayList<Fragment>();
       // fList.add(ListFragment.newInstance("Fragment 1"));
        pager = (ViewPager) findViewById(R.id.view_pager);
        pagerAdapter = new MyPageAdapter(getSupportFragmentManager(), fList);
        pager.setAdapter(pagerAdapter);*/

        lang = Locale.getDefault().getLanguage().equalsIgnoreCase("fr") ? "fr" : "en";
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
        else if (n == INDEX_OLYMPICS)
            c = OlympicsTypeActivity.class;
        else if (n == INDEX_USLEAGUES)
            c = LeagueActivity.class;
        Intent i = new Intent(this, c);
        startActivity(i);
        finish();
    }

    public void onSectionAttached(int n) {
        String title = null;
        switch (n) {
            case 1:
                title = getString(R.string.title_results); break;
            case 2:
                title = getString(R.string.title_calendar); break;
            case 3:
                title = getString(R.string.title_olympics); break;
            case 4:
                title = getString(R.string.title_us_leagues); break;
            case 5:
                title = getString(R.string.title_settings); break;
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
        switch (item.getItemId()) {
            case R.id.filter:
                ListFragment.filterClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!drawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.navigation, menu);
            restoreActionBar();
            MenuItem item = menu.findItem(R.id.filter);
            if (this instanceof Result1Activity || this instanceof OlympicsModeActivity || this instanceof OlympicsTypeActivity || this instanceof LeagueActivity || this instanceof USLeaguesTypeActivity || this instanceof USLeaguesRecordTypeActivity)
                item.setVisible(false);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public abstract void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

}