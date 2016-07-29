package rushabh.apk.extracter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Switch;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

    private  AppInfoAdapter adapter;
    public static Activity ac;
    private RecyclerView.LayoutManager layoutManager;
    private  RecyclerView recyclerView;
    private ArrayList<AppInfo> data;
    private ArrayList<AppInfo> datai;
    private Switch toggle;
    private SwipeRefreshLayout swipeRefreshLayout;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.settings, false);

        ac = this;

        data = new ArrayList<AppInfo>();
        datai = new ArrayList<AppInfo>();

        recyclerView = (RecyclerView) findViewById(R.id.app_list);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        toggle = (Switch) findViewById(R.id.toggle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (toggle.isChecked() == true) {
                    new loadlist(true).execute();
                } else {
                    new loadlist(false).execute();
                }
            }
        });

        if (toggle.isChecked() == true) {
            new loadlist(true).execute();
        } else {
            new loadlist(false).execute();
        }



        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://rushabh.apk.extracter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://rushabh.apk.extracter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    private class loadlist extends AsyncTask<Void, Void, Void> {

        boolean sys;
        ProgressDialog progressBar;

        public loadlist(boolean system) {
            this.sys = system;
        }

        @Override
        protected void onPreExecute() {

            progressBar = new ProgressDialog(MainActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("Getting info...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            if (sys == true) {
                getapplist_system();
                Collections.sort(data, new Comparator<AppInfo>() {
                    public int compare(AppInfo v1, AppInfo v2) {
                        return v1.getApp_name().compareTo(v2.getApp_name());
                    }
                });
            } else {
                getapplist_onstalled();
                Collections.sort(datai, new Comparator<AppInfo>() {
                    public int compare(AppInfo v1, AppInfo v2) {
                        return v1.getApp_name().compareTo(v2.getApp_name());
                    }
                });
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if(sys == true) {
                adapter = null;
                adapter = new AppInfoAdapter(MainActivity.this, data);
                recyclerView.setAdapter(adapter);
            }else {
                adapter = null;
                adapter = new AppInfoAdapter(MainActivity.this, datai);
                recyclerView.setAdapter(adapter);
            }
            progressBar.dismiss();
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);

        }
    }


    public void getapplist_onstalled() {

        try {
            if (datai != null) {
                datai.clear();
            }
        } catch (Exception e) {

        }

        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);

        for (int i = 0; i < apps.size(); i++) {
            PackageInfo p = apps.get(i);

            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                AppInfo newInfo = new AppInfo();
                newInfo.setApp_name(p.applicationInfo.loadLabel(getPackageManager()).toString());
                newInfo.setPackage_name(p.packageName);
                newInfo.setApp_image(p.applicationInfo.loadIcon(getPackageManager()));
                datai.add(newInfo);
            }
        }
    }


    @Override
    public void onRefresh() {
        if (toggle.isChecked() == true) {
            new loadlist(true).execute();
        } else {
            new loadlist(false).execute();
        }

    }




    public void getapplist_system() {

        try {
            if (data != null) {
                data.clear();
            }
        } catch (Exception e) {

        }

        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < apps.size(); i++) {
            PackageInfo p = apps.get(i);

            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                AppInfo newInfo = new AppInfo();
                newInfo.setApp_name(p.applicationInfo.loadLabel(getPackageManager()).toString());
                newInfo.setPackage_name(p.packageName);
                newInfo.setApp_image(p.applicationInfo.loadIcon(getPackageManager()));
                data.add(newInfo);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.popup_menu, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(myActionMenuItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String charText) {

                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab:

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);

                builderSingle.setTitle("About Me");
                builderSingle.setMessage("Hey, I am Rushabh Patel and This is a simple app for extracting already installed application. :)");

                builderSingle.show();

                return true;

            case R.id.et:

                finish();

                return true;

            case R.id.st:

                startActivity(new Intent(MainActivity.this, SettingsActivity.class));

                return true;

            default:
                return false;
        }
    }
}
