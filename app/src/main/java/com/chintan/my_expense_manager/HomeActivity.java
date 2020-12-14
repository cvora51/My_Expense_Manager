package com.chintan.my_expense_manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    //Fragment

    private DashBoard_Fragment dashboardFragment;
    private Income_Fragment incomeFragment;
    private Expense_Fragment expenseFragment;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        toolbar.setTitle("Expense Manager");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle
                (
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                )
        {};
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);


        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView=findViewById(R.id.bottomNavbar);
        frameLayout=findViewById(R.id.main_frame);

        dashboardFragment=new DashBoard_Fragment();
        incomeFragment=new Income_Fragment();
        expenseFragment=new Expense_Fragment();

        setFragment(dashboardFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.dashboard:
                        setFragment(dashboardFragment);
                        return true;
                    case R.id.income:
                        setFragment(incomeFragment);
                        return true;
                    case R.id.expense:
                        setFragment(expenseFragment);
                        return true;

                    default:
                        return false;


                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);

        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        new AlertDialog.Builder(HomeActivity.this)
                .setTitle("Warning")
                .setMessage("Do you wish to exit")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HomeActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("NO",null)
                .show();
//        else{
//            super.onBackPressed();
//        }

    }

    public void displaySelectedListener(int itemId){
        Fragment fragment = null;

        switch(itemId){
            case android.R.id.home:
                DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                return;
            case R.id.dashboard:
                bottomNavigationView.setSelectedItemId(R.id.dashboard);
                fragment=new DashBoard_Fragment();
                break;

            case R.id.income:
                bottomNavigationView.setSelectedItemId(R.id.income);
                fragment=new Income_Fragment();
                break;

            case R.id.expense:
                bottomNavigationView.setSelectedItemId(R.id.expense);
                fragment=new Expense_Fragment();
                break;

            case R.id.account:
                fragment=new AccountFragment();
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        if(fragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i("ITEM ID", Integer.toString(item.getItemId()));
        displaySelectedListener(item.getItemId());
        return true;
    }
}