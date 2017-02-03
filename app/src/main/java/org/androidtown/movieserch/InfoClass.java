package org.androidtown.movieserch;

/**
 * Created by JuHyeok on 2017-01-29.
 */
public class InfoClass {
    public int _id;
    public String name;
    public String genre;
    public String story;

    public InfoClass(){}

    public InfoClass(int _id , String name , String genre , String story){
        this._id = _id;
        this.name = name;
        this.genre = genre;
        this.story = story;
    }
}
