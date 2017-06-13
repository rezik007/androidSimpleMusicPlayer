package com.example.patryk.wasko1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity {

    GridView androidGridView;
    public static String CHOOSEN_IMG;

    public static Integer[] imageIDs = {
            R.drawable.img01, R.drawable.img02,
            R.drawable.img03, R.drawable.img04,
            R.drawable.img05, R.drawable.img06,
            R.drawable.img07, R.drawable.img08,
            R.drawable.img09, R.drawable.img10,
            R.drawable.img11, R.drawable.img12
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Intent intent = new Intent(getApplicationContext(), DetailsActivity.class);

        androidGridView = (GridView) findViewById(R.id.gridview_android_example);
        androidGridView.setAdapter(new ImageAdapterGridView(this));

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                //intent.setClass(getApplicationContext(), DetailsActivity.class);
                intent.putExtra(CHOOSEN_IMG, new Integer(position));
                startActivity(intent);
                 }
        });

    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIDs.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            mImageView = new ImageView(mContext);
            mImageView.setLayoutParams(new GridView.LayoutParams(androidGridView.getMeasuredWidth()/2, androidGridView.getMeasuredHeight()/4));
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mImageView.setImageResource(imageIDs[position]);
            return mImageView;
        }
    }
}