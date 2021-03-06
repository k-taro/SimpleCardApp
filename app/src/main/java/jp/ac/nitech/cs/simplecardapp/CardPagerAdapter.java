package jp.ac.nitech.cs.simplecardapp;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

import jp.ac.nitech.cs.simplecardapp.dbutil.AccessWrapper;
import jp.ac.nitech.cs.simplecardapp.model.Card;

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

        if(c.type.equals("textonly")){
            view = li.inflate(R.layout.view_card_b, null);

            TextView textViewBody = (TextView) view.findViewById(R.id.textViewBody);
            textViewBody.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeBody);

            String str = "";
            try {
                str = new String(c.contents[0].content, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            textViewBody.setText(str);
        }else{
            view = li.inflate(R.layout.view_card_b, null);
        }

//        if(position % 3 == 0){
//            view = li.inflate(R.layout.view_card_a, null);
//            TextView textViewBody = (TextView) view.findViewById(R.id.textViewBody);
//            textViewBody.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeBody);
//
//            TextView textViewCaption = (TextView) view.findViewById(R.id.textViewCaption);
//            textViewCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeCaption);
//        }else if(position % 3 == 1){
//            view = li.inflate(R.layout.view_card_b, null);
//
//            TextView textViewBody = (TextView) view.findViewById(R.id.textViewBody);
//            textViewBody.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeBody);
//        }else{
//            view = li.inflate(R.layout.view_card_c, null);
//
//            TextView textViewCaption = (TextView) view.findViewById(R.id.textViewCaption);
//            textViewCaption.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeCaption);
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
