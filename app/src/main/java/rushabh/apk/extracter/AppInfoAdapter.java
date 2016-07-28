package rushabh.apk.extracter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AppInfoAdapter extends RecyclerView.Adapter<AppInfoAdapter.MyViewHolder> {

    private ArrayList<AppInfo> dataSet;
    private Context mContext;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView package_name;
        ImageView image;
        ImageButton option;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.package_name = (TextView) itemView.findViewById(R.id.package_name);
            this.image = (ImageView) itemView.findViewById(R.id.image);
            this.option = (ImageButton) itemView.findViewById(R.id.option);
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

        AppInfo app = dataSet.get(listPosition);

        holder.name.setText(app.getApp_name());
        holder.package_name.setText(app.getPackage_name());
        Picasso.with(mContext).load(Uri.parse(app.getApp_image())).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}