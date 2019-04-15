package com.example.android.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID="com.example.android.notes.EXTRA_ID";
    public static final String EXTRA_TITLE="com.example.android.notes.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION="com.example.android.notes.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY="com.example.android.notes.EXTRA_PRIORITY";
    private EditText title,description;
    private NumberPicker priorityPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        title = (EditText) findViewById(R.id.edit_text_title);
        description =(EditText) findViewById(R.id.edit_text_description);
        priorityPicker =(NumberPicker) findViewById(R.id.priority_picker);
        priorityPicker.setMinValue(1);
        priorityPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent =getIntent();
        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Notes");
            title.setText(intent.getStringExtra(EXTRA_TITLE));
            description.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priorityPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY,1));

        }else {
            setTitle("Add Notes");
        }
    }

    public void saveNotes(){
            String noteTitle = title.getText().toString().trim();
            String noteDescription = description.getText().toString().trim();
            int priority =priorityPicker.getValue();

            if(noteTitle.isEmpty() || noteDescription.isEmpty()){
                Toast.makeText(AddEditNoteActivity.this,"Please insert both notes and description",Toast.LENGTH_SHORT).show();
                return;
            }

        Intent intent = new Intent();
            intent.putExtra(EXTRA_TITLE,noteTitle);
            intent.putExtra(EXTRA_DESCRIPTION,noteDescription);
            intent.putExtra(EXTRA_PRIORITY,priority);
            //for updating or for editing the notes
            int id = getIntent().getIntExtra(EXTRA_ID,-1);
            if(id!=-1){
             //we want to send id in main activity only if we need to edit notes
             intent.putExtra(EXTRA_ID,id);
            }
            setResult(RESULT_OK,intent);
            finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
       MenuInflater menuInflater = getMenuInflater();
       menuInflater.inflate(R.menu.add_note_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
       switch (item.getItemId()){
           case R.id.save_notes:
               saveNotes();
               default:
                   return super.onOptionsItemSelected(item);
       }
    }
}
