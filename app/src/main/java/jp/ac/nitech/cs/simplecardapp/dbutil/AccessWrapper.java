package jp.ac.nitech.cs.simplecardapp.dbutil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.UnsupportedEncodingException;
import java.nio.charset.UnsupportedCharsetException;

import jp.ac.nitech.cs.simplecardapp.model.Card;
import jp.ac.nitech.cs.simplecardapp.model.Content;

/**
 * Created by keitaro on 2015/02/05.
 */
public class AccessWrapper {
    private static final String DB_NAME = "simple_card_app.db";
    private static final int DB_VERTION = 1;
    private final Helper helper;

    public AccessWrapper(Context c){
        this.helper = new Helper(c);
    }

    public Card getCard(int index, String language){
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql =
                "select " +
                " cards._id as _id," +
                " cardtype.schema as schema," +
                " cards.lang as lang," +
                " cards.page_index as page_index," +
                " contents1._id as content1_id," +
                " contents1.type as content1_type," +
                " contents1.content as content1," +
                " contents2._id as content1_id," +
                " contents2.type as content2_type," +
                " contents2.content as content2," +
                " contents3._id as content1_id," +
                " contents3.type as content3_type," +
                " contents3.content as content3" +
                " from (" +
                " select * from cards" +
                " where page_index = ? and lang = ?" +
                " ) as cards" +
                " inner join cardtype cardtype on cards.type = cardtype.name" +
                " inner join contents contents1 on cards.content1 = contents1._id" +
                " left outer join contents contents2 on cards.content2 = contents2._id" +
                " left outer join contents contents3 on cards.content3 = contents3._id" +
                "";


        Cursor cursor = db.rawQuery(sql, new String[]{Integer.toString(index), language});

        int id = 0;
        String schema = null;
        String lang = null;
        int page_index = 0;
        int content1_id = 0;
        String content1type = null;
        byte[] content1 = null;
        int content2_id = 0;
        String content2type = null;
        byte[] content2 = null;
        int content3_id = 0;
        String content3type = null;
        byte[] content3 = null;

        while(cursor.moveToNext()) {
            id = cursor.getInt(cursor.getColumnIndex("_id"));
            schema = cursor.getString(cursor.getColumnIndex("schema"));
            lang = cursor.getString(cursor.getColumnIndex("lang"));
            page_index = cursor.getInt(cursor.getColumnIndex("page_index"));
            content1_id = cursor.getInt(cursor.getColumnIndex("content1_id"));
            content1type = cursor.getString(cursor.getColumnIndex("content1_type"));
            content1 = cursor.getBlob(cursor.getColumnIndex("content1"));
            if(!cursor.isNull(cursor.getColumnIndex("content2_id"))) {
                content2_id = cursor.getInt(cursor.getColumnIndex("content2_id"));
                content2type = cursor.getString(cursor.getColumnIndex("content2_type"));
                content2 = cursor.getBlob(cursor.getColumnIndex("content2"));
            }
            if(!cursor.isNull(cursor.getColumnIndex("content3_id"))) {
                content3_id = cursor.getInt(cursor.getColumnIndex("content3_id"));
                content3type = cursor.getString(cursor.getColumnIndex("content3_type"));
                content3 = cursor.getBlob(cursor.getColumnIndex("content3"));
            }
        }

        Content[] contents = new Content[3];
        try {
            contents[0] = new Content(content1_id, content1type, content1);
            contents[1] = new Content(content2_id, content2type, content2);
            contents[2] = new Content(content3_id, content3type, content3);
        }catch (NullPointerException e){
//            e.printStackTrace();
        }

        Card c = new Card(id, schema, lang, page_index, contents);

        return c;
    }

    public int getPageNum() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("cards",
                new String[] {"_id", "type", "lang", "page_index"},
                null,
                null,
                null,
                null,
                "page_index desc",
                "1");

//        System.out.println(cursor.getCount());
        cursor.moveToFirst();

        return cursor.getInt(cursor.getColumnIndex("page_index"));
    }


    class Helper extends SQLiteOpenHelper{

        public Helper(Context context) {
            super(context, DB_NAME, null, DB_VERTION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String createContentTable = "create table contents ( _id integer primary key asc autoincrement, type text, content blob);";
            String createCardsTable = "create table cards ( _id integer primary key asc autoincrement, type text, lang text, page_index integer not null check(page_index > 0), content1 integer, content2 integer, content3 integer);";
            String createCardTypeTable = "create table cardtype ( name text primary key, desc text, schema text);";

            db.execSQL(createContentTable);
            db.execSQL(createCardsTable);
            db.execSQL(createCardTypeTable);

            addTestData(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("drop table contents;");
            db.execSQL("drop table cards;");
            db.execSQL("drop table cardtype");
            onCreate(db);
        }

        private void addTestData(SQLiteDatabase db){
            ContentValues[] contentValues = new ContentValues[4];
            try {
                contentValues[0] = new ContentValues();
                contentValues[0].put("_id",1);
                contentValues[0].put("type","text");
                contentValues[0].put("content","Content 1".getBytes("UTF-8"));

                contentValues[1] = new ContentValues();
                contentValues[1].put("_id", 2);
                contentValues[1].put("type", "text");
                contentValues[1].put("content", "Content 2".getBytes("UTF-8"));

                contentValues[2] = new ContentValues();
                contentValues[2].put("_id", 3);
                contentValues[2].put("type", "text");
                contentValues[2].put("content", "コンテンツ 3".getBytes("UTF-8"));

                contentValues[3] = new ContentValues();
                contentValues[3].put("_id", 4);
                contentValues[3].put("type", "text");
                contentValues[3].put("content", "コンテンツ 4".getBytes("UTF-8"));
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
            for(ContentValues v : contentValues) {
                db.insert("contents", null, v);
            }


            ContentValues[] cardValues = new ContentValues[4];

            cardValues[0] = new ContentValues();
            cardValues[0].put("_id", 1);
            cardValues[0].put("type", "textonly");
            cardValues[0].put("lang", "en");
            cardValues[0].put("page_index", 1);
            cardValues[0].put("content1",1);

            cardValues[1] = new ContentValues();
            cardValues[1].put("_id", 2);
            cardValues[1].put("type", "textonly");
            cardValues[1].put("lang", "en");
            cardValues[1].put("page_index", 2);
            cardValues[1].put("content1",2);

            cardValues[2] = new ContentValues();
            cardValues[2].put("_id", 3);
            cardValues[2].put("type", "textonly");
            cardValues[2].put("lang", "ja");
            cardValues[2].put("page_index", 1);
            cardValues[2].put("content1",3);

            cardValues[3] = new ContentValues();
            cardValues[3].put("_id", 4);
            cardValues[3].put("type", "textonly");
            cardValues[3].put("lang", "ja");
            cardValues[3].put("page_index", 2);
            cardValues[3].put("content1",4);

            for(ContentValues v : cardValues){
                db.insert("cards",null,v);
            }

            ContentValues[] cardtypeValues = new ContentValues[3];

            cardtypeValues[0] = new ContentValues();
            cardtypeValues[0].put("name", "textonly");
            cardtypeValues[0].put("desc", "Only text");
            cardtypeValues[0].put("schema", "<div>{$text}</div>");

            cardtypeValues[1] = new ContentValues();
            cardtypeValues[1].put("name", "titletext");
            cardtypeValues[1].put("desc", "Title and text");
            cardtypeValues[1].put("schema", "<h2>{$title}</h2><div>{$text}</div>");

            cardtypeValues[2] = new ContentValues();
            cardtypeValues[2].put("name", "imgtext");
            cardtypeValues[2].put("desc", "Image and text");
            cardtypeValues[2].put("schema", "<img src=\"{$img}\"/><div>{$text}</div>");

            for(ContentValues v : cardtypeValues){
                db.insert("cardtype",null,v);
            }
        }
    }

}
