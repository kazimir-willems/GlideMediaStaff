package staff.com.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;
import staff.com.R;
import staff.com.application.DeliveryApplication;
import staff.com.consts.StateConsts;
import staff.com.fragment.HomeFragment;
import staff.com.fragment.DeleteZoneFragment;
import staff.com.fragment.StaffFragment;
import staff.com.fragment.ZoneFragment;
import staff.com.model.StaffItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.toolbar)
    Toolbar toolBar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;

    private ActionBarDrawerToggle toggle;

    public ProgressDialog dlgProg;

    private StaffItem staffItem = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        setSupportActionBar(toolBar);

        toggle = new ActionBarDrawerToggle(MainActivity.this, drawerLayout, toolBar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            public void onDrawerOpened(View drawerView) {
            }
        };

        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        navigationView.getMenu().getItem(3).setChecked(true);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        FragmentManager manager = getFragmentManager();
        manager.beginTransaction()
                .replace(R.id.main_frame, StaffFragment.newInstance())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int selectedIndex = -1;

        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = HomeFragment.newInstance();
                getSupportActionBar().setTitle(R.string.app_name);
                navigationView.getMenu().getItem(0).setChecked(true);
                break;
            case R.id.nav_delete_zone:
                fragment = DeleteZoneFragment.newInstance();
                getSupportActionBar().setTitle(R.string.app_name);
                navigationView.getMenu().getItem(1).setChecked(true);
                break;
            case R.id.nav_staff:
                fragment = StaffFragment.newInstance();
                getSupportActionBar().setTitle(R.string.staff);
                navigationView.getMenu().getItem(3).setChecked(true);
                break;
            case R.id.nav_start_stock_take:
                fragment = ZoneFragment.newInstance();
                getSupportActionBar().setTitle(R.string.start_stock_take);
                navigationView.getMenu().getItem(2).setChecked(true);
                break;
            default:
                break;
        }

        invalidateOptionsMenu();

        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
        }

        drawerLayout.closeDrawers();

        return true;
    }

    public void showZoneFragment() {
        Fragment fragment = null;
        fragment = DeleteZoneFragment.newInstance();

        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
        }
    }

    public StaffItem getStaffItem() {
        return staffItem;
    }

    public void setStaffItem(StaffItem item) {
        this.staffItem = item;
    }

    public void moveToMain() {
        Fragment fragment = HomeFragment.newInstance();
        getSupportActionBar().setTitle(R.string.app_name);

        if (fragment != null) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
        }

        navigationView.getMenu().getItem(0).setChecked(true);
    }

}