package rushabh.apk.extracter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.MyViewHolder> {

    private ArrayList<AppInfo> dataSet;
    public static String PATH = Environment.getExternalStorageDirectory() + "/APK-Extract";
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

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

    public AppInfoAdapter(Context c, ArrayList<AppInfo> data) {
        this.mContext = c;
        this.dataSet = data;
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

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }


    public void init_direcctory(){
        File folder = new File(PATH);

        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public void getApk(String packagename){
        final PackageManager pm = mContext.getPackageManager();
//get a list of installed apps.
        List<ApplicationInfo> packages =  pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if(packageInfo.packageName.matches(packagename)) {

                    Log.d(packageInfo.publicSourceDir, packageInfo.packageName + "    " + PATH + packageInfo.packageName);
                try {
                    copyFile(packageInfo.publicSourceDir, PATH + "/" + packageInfo.loadLabel(mContext.getPackageManager()).toString() + ".apk");
                } catch (Exception e){

                }
                Toast.makeText(mContext,"APK extracted successfully in\n" +  PATH+"/" + packageInfo.loadLabel(mContext.getPackageManager()).toString()+".apk",Toast.LENGTH_SHORT).show();

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