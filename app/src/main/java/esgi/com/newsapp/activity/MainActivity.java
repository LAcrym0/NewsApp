package esgi.com.newsapp.activity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import esgi.com.newsapp.R;
import esgi.com.newsapp.fragment.NewsFragment;
import esgi.com.newsapp.fragment.RootFragment;
import esgi.com.newsapp.fragment.RootStackFragment;

/**
 * Created by Grunt on 12/07/2017.
 */

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RootFragment.Interface {

    @BindView(R.id.main_act_toolbar)
    Toolbar toolbar;

    @BindView(R.id.main_act_frame_content)
    FrameLayout frameContent;

    NewsFragment newsFragment;

    public final FragmentManager.OnBackStackChangedListener onBackStackChangedListener = new FragmentManager.OnBackStackChangedListener() {
        @Override
        public void onBackStackChanged() {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                clearFragmentsStack();
            } else {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_act_frame_content);
            if (fragment instanceof RootFragment) {
                setTitle(((RootFragment) fragment).getTitle());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        newsFragment = NewsFragment.newInstance();

        navigationView.setCheckedItem(R.id.main_nav_news);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getSupportFragmentManager().addOnBackStackChangedListener(onBackStackChangedListener);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onPause() {
        super.onPause();
        getSupportFragmentManager().removeOnBackStackChangedListener(onBackStackChangedListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }  else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_act_frame_content);
                if (fragment != null && fragment instanceof RootStackFragment) {
                    if (!((RootStackFragment) fragment).consumeBackPressed()) {
                        popFragment();
                    }
                } else {
                    popFragment();
                }
            } else {
                moveTaskToBack(true);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        RootFragment fragment = null;
        if (id == R.id.main_nav_news) {
            fragment = newsFragment;
        }

        if (fragment != null) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            clearFragmentsStack();


            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_act_frame_content);
            if (currentFragment != null) {
                getSupportFragmentManager().beginTransaction().remove(currentFragment).commit();
            }

            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.transition_in_enter, R.anim.transition_in_exit, R.anim.transition_out_enter, R.anim.transition_out_exit)
                    .replace(R.id.main_act_frame_content, fragment)
                    .commit();

            //setTitle(fragment.getTitle());
            return true;
        }

        return false;
    }

    @Override
    public MainActivity getMainActivity() {
        return this;
    }

    public void pushFragment(RootFragment rootFragment, RootStackFragment rootStackFragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.transition_in_enter, R.anim.transition_in_exit, R.anim.transition_out_enter, R.anim.transition_out_exit)
                .add(R.id.main_act_frame_content, rootStackFragment)
                .addToBackStack(null)
                .commit();
    }

    public void clearFragmentsStack() {
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void finishFragment(RootStackFragment fragment) {
        if (getSupportFragmentManager().findFragmentById(R.id.main_act_frame_content) == fragment) {
            popFragment();
        }
    }

    private void popFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_act_frame_content);
        if (fragment instanceof RootStackFragment) {
            Fragment targetFragment = fragment.getTargetFragment();
            if (targetFragment instanceof RootStackFragment) {
                ((RootStackFragment) targetFragment).onFragmentResult(fragment.getTargetRequestCode(), ((RootStackFragment) fragment).getResult());
            }
        }
        //Util.closeKeyboard(this);
        getSupportFragmentManager().popBackStack();
    }
}
