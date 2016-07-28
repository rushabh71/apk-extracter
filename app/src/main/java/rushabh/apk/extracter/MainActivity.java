package rushabh.apk.extracter;

import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Switch;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<AppInfo> data;
    private Switch toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new ArrayList<AppInfo>();

        recyclerView = (RecyclerView) findViewById(R.id.app_list);
        toggle = (Switch) findViewById(R.id.toggle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        adapter = new AppInfoAdapter(MainActivity.this, data);
        recyclerView.setAdapter(adapter);

    }

    public void getapplist_installed(){

        try {
            if (data != null) {
                data.clear();
            }
        }catch (Exception e){

        }

        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<apps.size();i++) {
            PackageInfo p = apps.get(i);

            if ((p.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                AppInfo newInfo = new AppInfo();
                newInfo.setApp_name(p.applicationInfo.loadLabel(getPackageManager()).toString());
                newInfo.setPackage_name(p.packageName);
                newInfo.setApp_image(p.applicationInfo.loadIcon(getPackageManager()));
                data.add(newInfo);
            }
        }
    }

    public void getapplist_system(){

        try {
            if (data != null) {
                data.clear();
            }
        }catch (Exception e){

        }

        List<PackageInfo> apps = getPackageManager().getInstalledPackages(0);

        for(int i=0;i<apps.size();i++) {
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
        menu.add(Menu.NONE, 1, Menu.NONE, "Settings");
        menu.add(Menu.NONE, 2, Menu.NONE, "About Me");
        menu.add(Menu.NONE, 3, Menu.NONE, "Exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 2:

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);

                builderSingle.setTitle("About Me");
                builderSingle.setMessage("Hey, I am Rushabh Patel and This is a simple app for extracting already installed application. :)");

                builderSingle.show();

                return true;

            case 3:

                finish();

                return true;

            case 1:

                return true;

            default:
                return false;
        }
    }
}
