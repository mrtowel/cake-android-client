package com.waracle.androidtest.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.waracle.androidtest.R;
import com.waracle.androidtest.data.Item;
import com.waracle.androidtest.net.LoadTask;
import com.waracle.androidtest.net.NetworkResponse;
import com.waracle.androidtest.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment responsible for loading in some JSON and then displaying a list of cakes with images.
 */
public class ItemListFragment extends ListFragment {


    private ListView mListView;
    private ItemAdapter mItemAdapter;
    private List<Item> items;
    private ImageLoader mImageLoader;

    public ItemListFragment() {
        mImageLoader = new ImageLoader();
    }

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain instance across configuration changes
        setRetainInstance(true);

        // Load data from net.
        loadData();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_item_list, container, false);
        mListView = (ListView) rootView.findViewById(android.R.id.list);
        mItemAdapter = new ItemAdapter();
        mListView.setAdapter(mItemAdapter);
        if (items != null && !items.isEmpty()) {
            displayItems();
        }
        return rootView;
    }

    private void displayItems() {
        mItemAdapter.addItems(items);
        mItemAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        LoadTask.Callback callback = new LoadTask.Callback() {
            @Override
            public void onLoaded(final NetworkResponse response) {
                items = NetworkUtils.parseResponseData(response);
                displayItems();
            }
        };

        new LoadTask(callback).execute(getString(R.string.endpoint_url));
    }

    class ItemAdapter extends BaseAdapter {

        private List<Item> mItems = new ArrayList<>();

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(final int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(final int position) {
            return 0;
        }

        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View root = convertView;
            final ItemAdapter.ViewHolder holder;

            if (root == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                root = inflater.inflate(R.layout.list_item_layout, parent, false);
                holder = new ItemAdapter.ViewHolder();
                holder.title = (TextView) root.findViewById(R.id.title);
                holder.description = (TextView) root.findViewById(R.id.desc);
                holder.image = (ImageView) root.findViewById(R.id.image);
                holder.position = position;
                root.setTag(holder);
            } else {
                holder = (ItemAdapter.ViewHolder) root.getTag();
            }

            Item object = (Item) getItem(position);
            holder.title.setText(object.getTitle());
            holder.description.setText(object.getDescription());
            holder.image.setImageBitmap(null);
            mImageLoader.loadImage(
                    object.getImage(), holder.image, holder, position);

            return root;
        }

        void addItems(final List<Item> items) {
            if (items != null) {
                mItems.addAll(items);
            }
        }

        class ViewHolder {

            private TextView title;
            private TextView description;
            private ImageView image;
            int position = -1;
        }

    }

}
