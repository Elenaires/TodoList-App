package com.example.mytodolist;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewTodoActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.example.android.MyTodoList.REPLY";
    public static final String EXTRA_REPLY_ID = "com.example.android.MyTodoList.REPLY_ID";


    private EditText mEditTodoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);

        mEditTodoView = findViewById(R.id.edit_todo);

        final Bundle extras = getIntent().getExtras();

        if(extras != null) {
            String todo = extras.getString(MainActivity.EXTRA_DATA_UPDATE_TODO, "");
             if(!todo.isEmpty()) {
                mEditTodoView.setText(todo);
                mEditTodoView.setSelection(todo.length());
                mEditTodoView.requestFocus();
            }
        } // else start with empty fields

        // button to save todoitem
        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                if(TextUtils.isEmpty(mEditTodoView.getText())) {
                    setResult(RESULT_CANCELED, replyIntent);
                }
                else {
                    String todo = mEditTodoView.getText().toString();
                    replyIntent.putExtra(EXTRA_REPLY, todo);
                    if(extras != null && extras.containsKey(MainActivity.EXTRA_DATA_ID)) {
                        int id = extras.getInt(MainActivity.EXTRA_DATA_ID, -1);
                        if(id != -1) {
                            replyIntent.putExtra(EXTRA_REPLY_ID, id);
                        }
                    }
                    setResult(RESULT_OK, replyIntent);
                }
                finish();
            }
        });
    }
}
