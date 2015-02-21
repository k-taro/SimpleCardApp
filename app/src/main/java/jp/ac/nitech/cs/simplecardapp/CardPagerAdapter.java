package jp.ac.nitech.cs.simplecardapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ac.nitech.cs.simplecardapp.dbutil.AccessWrapper;
import jp.ac.nitech.cs.simplecardapp.model.Card;
import jp.ac.nitech.cs.simplecardapp.model.Content;

/**
 * Created by Keitaro Wakabayashi on 2014/12/04.
 */
public class CardPagerAdapter extends PagerAdapter {
    private final float mTextSizeBody;        // unit : px
    private final float mTextSizeCaption;     // unit : px
    private AccessWrapper dbAccess;
    private final int pageNum;

    /**
     *
     * @param textSizeBody unit : px
     * @param textSizeCaption unit : px
     */
    public CardPagerAdapter(Context c, float textSizeBody, float textSizeCaption){
        mTextSizeBody = textSizeBody;
        mTextSizeCaption = textSizeCaption;
        dbAccess = new AccessWrapper(c);
        this.pageNum = dbAccess.getPageNum();
    }

    @Override
    public int getCount() {
        return pageNum;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater li = ((LayoutInflater)container.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE));
        View view;

        Card c = dbAccess.getCard(position+1, "ja");

        view = li.inflate(R.layout.view_card_web, null);

        String html = new String(c.schema);

        for(int i = 0; i<3; i++){
            if(c.contents[i] == null){
                break;
            }
            String regex = "\\{\\$[a-zA-Z0-9]+\\}";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(html);
            try {
                boolean isFind = m.find();
                String replace = new String(c.contents[i].content, "UTF-8");
                html = m.replaceFirst(replace);
            }catch(UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }

        WebView webView = (WebView) view.findViewById(R.id.webView);
        html = "<html><body>"+ html +"</body></html>";
        webView.loadData(html, "text/html; charset=utf-8", "UTF-8");

//        if(c.schema.equals("textonly")){
//            view = li.inflate(R.layout.view_card_b, null);
//
//            TextView textViewBody = (TextView) view.findViewById(R.id.textViewBody);
//            textViewBody.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeBody);
//
//            String str = "";
//            try {
//                str = new String(c.contents[0].content, "UTF-8");
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//            textViewBody.setText(str);
//        }else{
//            view = li.inflate(R.layout.view_card_b, null);
//        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view.equals(o);
    }

}
