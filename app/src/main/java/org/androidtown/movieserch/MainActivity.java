package org.androidtown.movieserch;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        mDbOpenHelper.insertColumn("terminate", "SF", "로봇");
        mDbOpenHelper.insertColumn("무서운이야기", "공포", "귀신");
        
        //mDbOpenHelper.insertColumn("맨인블랙", "코미디", "양복");
        //mDbOpenHelper.insertColumn("말할수없는비밀", "로맨스", "피아노");

        mInfoArray = new ArrayList<InfoClass>();
        doWhileCursorToArray();

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

    private void doWhileCursorToArray()
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

           // if("ter".equals(mInfoClass.name))
                mInfoArray.add(mInfoClass);
        }
        mCursor.close();
    }

    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.horror:
                Intent intent = new Intent(this, HorrorActivity.class);
                startActivity(intent);
                break;
            case R.id.SF:
                break;
            case R.id.comedy:
                break;
            case R.id.romance:
                break;
            case R.id.btn_add:

                mDbOpenHelper.insertColumn(
                        mEditTexts[Constants.NAME].getText().toString().trim(),
                        mEditTexts[Constants.GENRE].getText().toString().trim(),
                        mEditTexts[Constants.STORY].getText().toString().trim()
                );

                mInfoArray.clear();
                doWhileCursorToArray();
                mAdapter.setArrayList(mInfoArray);
                mAdapter.notifyDataSetChanged();
                mCursor.close();
                break;
            default:
                break;

        }
    }
    private EditText[] mEditTexts;
    private ListView mListView;

    private void setLayout(){
        mEditTexts = new EditText[]{
                (EditText)findViewById(R.id.et_name),
                (EditText)findViewById(R.id.et_genre),
                (EditText)findViewById(R.id.et_story)
        };
        mListView = (ListView) findViewById(R.id.lv_list);
    }
}
