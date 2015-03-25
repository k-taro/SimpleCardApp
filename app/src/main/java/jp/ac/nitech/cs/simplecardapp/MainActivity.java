package jp.ac.nitech.cs.simplecardapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;


public class MainActivity extends Activity {
    public static final String APP_DIRECTORY = Environment.getExternalStorageDirectory() + File.separator + "simplecardapp" + File.separator;
    private double baseDisplaySize = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File appDir = new File(APP_DIRECTORY);
        if(!appDir.exists()){
            appDir.mkdirs();
        }

        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int dpi = displayMetrics.densityDpi;
        int h = displayMetrics.heightPixels;
        int w = displayMetrics.widthPixels;

        double displaySize = (Math.sqrt(h * h + w * w))/dpi;

        float textSizeCaption = getResources().getDimension(R.dimen.textsize_caption);
        textSizeCaption = (float)(textSizeCaption * (displaySize/baseDisplaySize));

        float textSizeBody = getResources().getDimension(R.dimen.textsize_body);
        textSizeBody = (float)(textSizeBody * (displaySize/baseDisplaySize));

        Fragment fragment = new PlaceholderFragment();
        Bundle arg = new Bundle();
        arg.putFloat(PlaceholderFragment.TEXTSIZE_BODY, textSizeBody);
        arg.putFloat(PlaceholderFragment.TEXTSIZE_CAPTION, textSizeCaption);
        fragment.setArguments(arg);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public static final String TEXTSIZE_BODY = "TEXTSIZE_BODY";
        public static final String TEXTSIZE_CAPTION = "TEXTSIZE_CAPTION";

        public PlaceholderFragment(){
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.viewpager);

            float body = getResources().getDimension(R.dimen.textsize_body);
            float caption = getResources().getDimension(R.dimen.textsize_caption);

            body = getArguments().getFloat(TEXTSIZE_BODY, body);
            caption = getArguments().getFloat(TEXTSIZE_CAPTION, caption);

            viewPager.setAdapter(new CardPagerAdapter(getActivity().getApplicationContext(), body, caption));

            return rootView;
        }
    }
}
