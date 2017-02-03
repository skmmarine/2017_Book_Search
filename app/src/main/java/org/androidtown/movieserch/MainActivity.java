package org.androidtown.movieserch;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TestDataBaseActivity";
    private DbOpenHelper mDbOpenHelper;
    private Cursor mCursor;
    private InfoClass mInfoClass;
    private ArrayList<InfoClass> mInfoArray;
    private CustomAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setLayout();
        mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.deleteAllColumn();
        mDbOpenHelper.insertColumn("무서운이야기", "공포", "귀신");
        mDbOpenHelper.insertColumn("파라노말엑티비티", "공포", "카메라");
        mDbOpenHelper.insertColumn("terminate", "SF", "로봇");
        mDbOpenHelper.insertColumn("인터스텔라", "SF", "우주");
        mDbOpenHelper.insertColumn("맨인블랙","코미디","양복");
        mDbOpenHelper.insertColumn("너의이름은","로맨스","양복");


        //mDbOpenHelper.insertColumn("맨인블랙", "코미디", "양복");
        //mDbOpenHelper.insertColumn("말할수없는비밀", "로맨스", "피아노");

        mInfoArray = new ArrayList<InfoClass>();
        String first ="all";
        doWhileCursorToArray(first);

        for(InfoClass i : mInfoArray) {
            DLog.d(TAG, "ID = " + i._id);
            DLog.d(TAG, "name = " + i.name);
            DLog.d(TAG, "genre = " + i.genre);
            DLog.d(TAG, "story = " + i.story);
        }
        mAdapter = new CustomAdapter(this, mInfoArray);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemLongClickListener(longClickListener);
    }

    @Override
    protected void onDestroy(){
        mDbOpenHelper.close();
        super.onDestroy();
    }

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {

            DLog.e(TAG, "position = " + position);

            //boolean result = mDbOpenHelper.deleteColumn(position + 1);
            boolean result =  mDbOpenHelper.deleteColumn(mInfoArray.get(position)._id);
            DLog.e(TAG, "result = " + result);

            if(result){
                mInfoArray.remove(position);
                mAdapter.setArrayList(mInfoArray);
                mAdapter.notifyDataSetChanged();
            }else {
                Toast.makeText(getApplicationContext(), "INDEX를 확인해 주세요.",
                        Toast.LENGTH_LONG).show();
            }

            return false;
        }
    };

    private void doWhileCursorToArray(String case_str)
    {
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        DLog.e(TAG, "COUNT = " + mCursor.getCount());
        while(mCursor.moveToNext()){
                mInfoClass = new InfoClass(
                        mCursor.getInt(mCursor.getColumnIndex("_id")),
                        mCursor.getString(mCursor.getColumnIndex("name")),
                        mCursor.getString(mCursor.getColumnIndex("genre")),
                        mCursor.getString(mCursor.getColumnIndex("story"))
                );
            switch(case_str){
                //String List[] = {"all","horror","SF","comedy","romance","search"};
                case "all":
                    mInfoArray.add(mInfoClass);
                    break;
                case "horror":
                    if("공포".equals(mInfoClass.genre))
                        mInfoArray.add(mInfoClass);
                    break;
                case "SF":
                    if("SF".equals(mInfoClass.genre))
                        mInfoArray.add(mInfoClass);
                    break;
                case "comedy":
                    if("코미디".equals(mInfoClass.genre))
                        mInfoArray.add(mInfoClass);
                    break;
                case "romance":
                    if("로맨스".equals(mInfoClass.genre))
                        mInfoArray.add(mInfoClass);
                    break;
                case "search":
                    //need to change
                        mInfoArray.add(mInfoClass);
                    break;
                default:
                    break;
            }
        }
        mCursor.close();
    }
    /**** default doWhileCursorToArray()*********
    private void doWhileCursorToArray(){
        mCursor = null;
        mCursor = mDbOpenHelper.getAllColumns();
        DLog.e(TAG, "COUNT = " + mCursor.getCount());
        while (mCursor.moveToNext()) {
            mInfoClass = new InfoClass(
                    mCursor.getInt(mCursor.getColumnIndex("_id")),
                    mCursor.getString(mCursor.getColumnIndex("name")),
                    mCursor.getString(mCursor.getColumnIndex("genre")),
                    mCursor.getString(mCursor.getColumnIndex("story"))
            );
            mInfoArray.add(mInfoClass);
        }
        mCursor.close();
    }
*******************************************************/
    public void onClick(View view) {
        String List[] = {"all","horror","SF","comedy","romance","search"};
        mInfoArray.clear();
        switch(view.getId()) {
            case R.id.allList:
                doWhileCursorToArray(List[0]);
                break;
            case R.id.horror:
                doWhileCursorToArray(List[1]);
                break;
            case R.id.SF:
                doWhileCursorToArray(List[2]);
                break;
            case R.id.comedy:
                doWhileCursorToArray(List[3]);
                break;
            case R.id.romance:
                doWhileCursorToArray(List[4]);
                break;
            case R.id.btn_search:
                doWhileCursorToArray(List[5]);
                break;
            default:
                break;

        }
        mAdapter.setArrayList(mInfoArray);
        mAdapter.notifyDataSetChanged();
        mCursor.close();
    }
    private EditText[] mEditTexts;
    private ListView mListView;

    private void setLayout(){
        mEditTexts = new EditText[]{
                (EditText)findViewById(R.id.et_name),
                (EditText)findViewById(R.id.et_genre),
                (EditText)findViewById(R.id.et_story)
        };
        mEditTexts[0].setInputType(0);
        mEditTexts[0].setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mEditTexts[0].setInputType(1);
                InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(mEditTexts[0], InputMethodManager.SHOW_IMPLICIT);
            }
        });
    

        mListView = (ListView) findViewById(R.id.lv_list);
    }
}
