package cn.gavinliu.similar.photo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.gavinliu.similar.photo.entry.Group;
import cn.gavinliu.similar.photo.entry.Photo;
import cn.gavinliu.similar.photo.util.PermissionsUtils;
import cn.gavinliu.similar.photo.util.PhotoRepository;

/**
 * Created by gavin on 2017/3/27.
 */

public class GroupActivity extends AppCompatActivity {

    private Handler mHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        PermissionsUtils.getPermissions(this);
        final List<Photo> photos = PhotoRepository.getPhoto(this);

        final ListView listView = (ListView) findViewById(R.id.list);

        mHandler = new Handler(getMainLooper());

        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<Group> groups = SimilarPhoto.find(GroupActivity.this, photos);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(new Adapter(groups));
                    }
                });
            }
        }).start();
    }

    private class Adapter extends BaseAdapter {

        private List<Group> groups;

        public Adapter(List<Group> groups) {
            this.groups = groups;
        }

        @Override
        public int getCount() {
            return groups == null ? 0 : groups.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_group, parent, false);
            }

            TextView name = (TextView) convertView.findViewById(R.id.name);
            LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.images);
            linearLayout.removeAllViews();

            name.setText("Group: " + position);


            List<Photo> photos = groups.get(position).getPhotos();

            for (Photo p : photos) {
                ImageView image = new ImageView(parent.getContext());
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(300, 300);
                linearLayout.addView(image, param);

                Glide.with(GroupActivity.this)
                        .load(p.getPath())
                        .centerCrop()
                        .crossFade()
                        .into(image);
            }


            return convertView;
        }
    }


}
