package com.accessibility.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.accessibility.R;

import java.util.List;

public class AppListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        final AlertDialog alertDialog = new ProgressDialog.Builder(this).create();
        alertDialog.show();

        new Thread(){
            @Override
            public void run() {
                super.run();
                final PackageManager pm= getPackageManager();
                final List<PackageInfo> packages = pm.getInstalledPackages(PackageManager.MATCH_UNINSTALLED_PACKAGES);
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        initList(packages,pm);
                        alertDialog.dismiss();
                    }
                });
            }
        }.start();
    }

    public void initList(final List<PackageInfo> packages, final PackageManager pm){

        ListView listView = (ListView) findViewById(R.id.list);

        listView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return packages.size();
            }

            @Override
            public PackageInfo getItem(int position) {
                return packages.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                PackageInfo item = getItem(position);
                Holder holder= null;
                if (convertView == null){
                    holder = new Holder();
                    convertView = View.inflate(AppListActivity.this,R.layout.item_appinfo,null);
                    holder.img = (ImageView) convertView.findViewById(R.id.icon);
                    holder.appName = (TextView) convertView.findViewById(R.id.app_name);
                    holder.appPackge = (TextView) convertView.findViewById(R.id.app_packge);
                    convertView.setTag(holder);
                }else {
                    holder = (Holder) convertView.getTag();
                }

                Drawable drawable = item.applicationInfo.loadIcon(pm);
                holder.img.setImageDrawable(drawable);
                holder.appName.setText(item.applicationInfo.loadLabel(pm));
                holder.appPackge.setText(item.packageName);
                return convertView;
            }

            class Holder{
                ImageView img;
                TextView appName,appPackge;
            }
        });
    }
}
