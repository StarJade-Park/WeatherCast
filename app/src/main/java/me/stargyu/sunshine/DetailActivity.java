package me.stargyu.sunshine;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import me.stargyu.sunshine.data.WeatherContract;
import me.stargyu.sunshine.data.WeatherDbHelper;

import static me.stargyu.sunshine.R.id.container;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(container, new DetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DetailFragment extends Fragment {
        private static final String LOG_TAG = DetailFragment.class.getSimpleName();
        private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";
        private Uri mForecastUri;
        private ForecastAdapter mForecastAdapter;

        public DetailFragment() { // 정적(static)이라서 onCreate가 필요 없다. ***
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu_detail_fragment, menu);

            MenuItem menuItem = menu.findItem(R.id.action_share);

            ShareActionProvider mShareActionProvider =
                    (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            // 버튼 찾듯이
            // 인텐트 = 통로,행동

            if (mShareActionProvider != null) {
                mShareActionProvider.setShareIntent(createShareForecastIntent());
                // 리스너 붙이듯 액션(인텐트)를 붙여준다
            } else {
                Log.d(LOG_TAG, "Share Action Provider is null");
            }
            super.onCreateOptionsMenu(menu, inflater);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            Intent intent = getActivity().getIntent();

            if (intent != null) {
                mForecastUri = intent.getData();
            }

            if (mForecastUri != null) {
                WeatherDbHelper weatherDbHelper = new WeatherDbHelper(getActivity());

                Cursor cursor = getActivity().getContentResolver().query(
                        mForecastUri, null, null, null, null
                );

                if (cursor != null) {

                    cursor.moveToFirst();
                    String weatherStr = "";

                    int dateIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_DATE);
                    int weatherIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_SHORT_DESC);
                    int maxTempIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP);
                    int minTempIndex = cursor.getColumnIndex(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP);

                    weatherStr = Utility.formatDate(cursor.getLong(dateIndex)) + " || "
                            + cursor.getString(weatherIndex) + " || "
                            + cursor.getString(maxTempIndex) + " / "
                            + cursor.getString(minTempIndex);

                    ((TextView) rootView.findViewById(R.id.detail_text)).setText(weatherStr);
                }
            }

            return rootView;
        }

        private Intent createShareForecastIntent() {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET); // 리셋해줌
            shareIntent.setType("text/plain"); // 일반 글자
            shareIntent.putExtra(Intent.EXTRA_TEXT, mForecastUri + FORECAST_SHARE_HASHTAG);
            return shareIntent;
        }
    }
}