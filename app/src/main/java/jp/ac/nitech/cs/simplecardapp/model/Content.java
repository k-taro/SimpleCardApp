package jp.ac.nitech.cs.simplecardapp.model;

import java.util.Arrays;

/**
 * Created by keitaro on 2015/02/05.
 */
public class Content {
    public final int id;
    public final String type;
    public final byte[] content;

    public Content(int id, String type, byte[] content){
        this.id = id;
        this.type = type;
        this.content = Arrays.copyOf(content, content.length);
    }
}
