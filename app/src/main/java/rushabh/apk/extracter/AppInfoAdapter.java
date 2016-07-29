package rushabh.apk.extracter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.MyViewHolder> implements Filterable {

    public  ArrayList<AppInfo> dataSet = null;
    public  ArrayList<AppInfo> filterSet = null;
    private Context mContext;
    String PATH;


    public AppInfoAdapter(Context c, ArrayList<AppInfo> data) {
        this.mContext = c;
        this.dataSet = data;
        this.filterSet = data;

    }

    @Override
    public Filter getFilter() {
        return new AppFilter() ;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView name;
        TextView package_name;
        ImageView image;
        CardView option;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.package_name = (TextView) itemView.findViewById(R.id.package_name);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.option = (CardView) itemView.findViewById(R.id.card_view);
        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

       final  AppInfo app = dataSet.get(listPosition);

        holder.name.setText(app.getApp_name());
        holder.package_name.setText(app.getPackage_name());
        holder.image.setBackground(app.getApp_image());

        holder.option.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                getApk(app.getPackage_name());
            }
        });

        holder.option.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                builderSingle.setIcon(app.getApp_image());
                builderSingle.setTitle(app.getApp_name());

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        mContext,
                        android.R.layout.simple_list_item_1);
                arrayAdapter.add("Share");
                arrayAdapter.add("App Info");

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);

                                if(which == 0){
                                    try {
                                        PackageManager pm = mContext. getPackageManager();
                                        ApplicationInfo ai = pm.getApplicationInfo(app.getPackage_name(), 0);
                                        File srcFile = new File(ai.publicSourceDir);
                                        Intent share = new Intent();
                                        share.setAction(Intent.ACTION_SEND);
                                        share.setType("application/vnd.android.package-archive");
                                        share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(srcFile));
                                       mContext.startActivity(Intent.createChooser(share, "Share "+app.getApp_name()));
                                    } catch (Exception e) {
                                        Log.e("ShareApp", e.getMessage());
                                    }
                                } else {
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + app.getPackage_name()));
                                   mContext. startActivity(intent);
                                }
                            }
                        });
                builderSingle.show();

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    private class AppFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            dataSet = filterSet;

            final ArrayList<AppInfo> list = dataSet;

            int count = list.size();
            final ArrayList<AppInfo> nlist = new ArrayList<AppInfo>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i).getApp_name();
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(list.get(i));
                }
            }

            results.values = nlist;
            results.count = nlist.size();
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,FilterResults results) {

            dataSet = (ArrayList<AppInfo>) results.values;
            notifyDataSetChanged();

        }

    }


    public void getApk(String packagename){
        final PackageManager pm = mContext.getPackageManager();
//get a list of installed apps.
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        PATH = sharedPref.getString("pref_path", "/sdcard/Apk-Extract/");

        if(PATH == null){
            PATH = "/sdcard/Apk-Extract/";
        }
        List<ApplicationInfo> packages =  pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.matches(packagename)) {

                    Log.d(packageInfo.publicSourceDir, packageInfo.packageName + "    " + PATH + packageInfo.packageName);
                try {
                    copyFile(packageInfo.publicSourceDir, PATH + packageInfo.loadLabel(mContext.getPackageManager()).toString() + ".apk");
                } catch (Exception e){

                }
                Toast.makeText(mContext,"APK extracted successfully in\n" + PATH + packageInfo.loadLabel(mContext.getPackageManager()).toString()+".apk",Toast.LENGTH_SHORT).show();

            }
        }

    }


  public  void copyFile(String s, String d) throws IOException {
      File src = new File(s);
      File dst = new File(d);
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }


}