package com.waracle.androidtest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static String JSON_URL = "https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/" +
            "raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Fragment is responsible for loading in some JSON and
     * then displaying a list of cakes with images.
     * Fix any crashes
     * Improve any performance issues
     * Use good coding practices to make code more secure
     */
    public static class PlaceholderFragment extends ListFragment {

        private static final String TAG = PlaceholderFragment.class.getSimpleName();

        private ListView mListView;
        private MyAdapter mAdapter;

        public PlaceholderFragment() { /**/ }

        @Override
        public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                                 final Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mListView = (ListView) rootView.findViewById(R.id.list);
            return rootView;
        }

        @Override
        public void onActivityCreated(final Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            // Create and set the list adapter.
            mAdapter = new MyAdapter();
            mListView.setAdapter(mAdapter);

            // Load data from net.
            try {
                JSONArray array = loadData();
                List<Item> items = Item.fromJsonArray(array);
                mAdapter.addItems(items);
            } catch (IOException | JSONException e) {
                Log.e(TAG, e.getMessage());
            }
        }


        private JSONArray loadData() throws IOException, JSONException {
            URL url = new URL(JSON_URL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                // Can you think of a way to improve the performance of loading data
                // using HTTP headers???

                // Also, Do you trust any utils thrown your way????

                byte[] bytes = StreamUtils.read(in);

                // Read in charset of HTTP content.
                String charset = parseCharset(urlConnection.getRequestProperty("Content-Type"));

                // Convert byte array to appropriate encoded string.
                String jsonText = new String(bytes, charset);

                // Read string as JSON.
                return new JSONArray(jsonText);
            } finally {
                urlConnection.disconnect();
            }
        }

        /**
         * Returns the charset specified in the Content-Type of this header,
         * or the HTTP default (ISO-8859-1) if none can be found.
         */
        public static String parseCharset(final String contentType) {
            if (contentType != null) {
                String[] params = contentType.split(",");
                for (int i = 1; i < params.length; i++) {
                    String[] pair = params[i].trim().split("=");
                    if (pair.length == 2) {
                        if (pair[0].equals("charset")) {
                            return pair[1];
                        }
                    }
                }
            }
            return "UTF-8";
        }

        private class MyAdapter extends BaseAdapter {

            private List<Item> mItems = new ArrayList<>();
            private ImageLoader mImageLoader;

            MyAdapter() {
            }

            public MyAdapter(final List<Item> items) {
                if (items != null) {
                    mItems = items;
                }
                mImageLoader = new ImageLoader();
            }

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

            @SuppressLint("ViewHolder")
            @Override
            public View getView(final int position, final View convertView, final ViewGroup parent) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                View root = inflater.inflate(R.layout.list_item_layout, parent, false);
                if (root == null) {
                    return null;
                }

                TextView title = (TextView) root.findViewById(R.id.title);
                TextView desc = (TextView) root.findViewById(R.id.desc);
                ImageView image = (ImageView) root.findViewById(R.id.image);

                Item object = (Item) getItem(position);
                title.setText(object.getTitle());
                desc.setText(object.getDescription());
                mImageLoader.load(object.getImage(), image);

                return root;
            }

            public void addItems(final List<Item> items) {
                if (items != null) {
                    mItems.addAll(items);
                }
            }
        }
    }
}
