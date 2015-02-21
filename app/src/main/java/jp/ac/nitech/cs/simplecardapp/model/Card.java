package jp.ac.nitech.cs.simplecardapp.model;

/**
 * Created by keitaro on 2015/02/05.
 */
public class Card {
    public final int id;
    public final String schema;
    public final String language;
    public final int index;
    public final Content[] contents;

    public Card(int id, String type, String language, int index, Content[] contents){
        this.id = id;
        this.schema = type;
        this.language = language;
        this.index = index;
        this.contents = contents;
    }
}
