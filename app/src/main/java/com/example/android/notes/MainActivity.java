package com.example.android.notes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
 public static final int ADD_NOTES_REQUEST=1;
 public static final int EDIT_NOTES_REQUEST=2;
    private NoteViewModel noteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.button_add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,AddEditNoteActivity.class);
                startActivityForResult(intent,ADD_NOTES_REQUEST);
            }
        });
        RecyclerView recyclerView=findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter adapter=new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //this method will act when the activity is in foreground state
                adapter.setNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                 noteViewModel.delete(adapter.getNotesAt(viewHolder.getAdapterPosition()));
                 Toast.makeText(MainActivity.this,"Note Deleted",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this,AddEditNoteActivity.class);
                //we need primary key because room needs key to identify which note entry to update
                intent.putExtra(AddEditNoteActivity.EXTRA_ID,note.getId());
                intent.putExtra(AddEditNoteActivity.EXTRA_TITLE,note.getTitle());
                intent.putExtra(AddEditNoteActivity.EXTRA_DESCRIPTION,note.getDescription());
                intent.putExtra(AddEditNoteActivity.EXTRA_PRIORITY,note.getPriority());
                startActivityForResult(intent,EDIT_NOTES_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent intent){
        super.onActivityResult(requestCode,resultCode,intent);
        if(requestCode==ADD_NOTES_REQUEST && resultCode==RESULT_OK){
                String title =intent.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
                String description =intent.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
                int priority = intent.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

                Note note = new Note(title,description,priority);
                noteViewModel.insert(note);
                Toast.makeText(MainActivity.this,"Note Added",Toast.LENGTH_SHORT).show();

        }else if(requestCode==EDIT_NOTES_REQUEST && resultCode==RESULT_OK){

            int id =intent.getIntExtra(AddEditNoteActivity.EXTRA_ID,-1);
            if(id==-1){
                Toast.makeText(MainActivity.this,"Note can not be updated",Toast.LENGTH_SHORT).show();
                return;
            }
            String title =intent.getStringExtra(AddEditNoteActivity.EXTRA_TITLE);
            String description =intent.getStringExtra(AddEditNoteActivity.EXTRA_DESCRIPTION);
            int priority = intent.getIntExtra(AddEditNoteActivity.EXTRA_PRIORITY,1);

            Note note = new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);
            Toast.makeText(MainActivity.this,"Note Updated",Toast.LENGTH_SHORT).show();
        }

        else{
            Toast.makeText(MainActivity.this,"Note not Added",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.delete_all_notes:
                noteViewModel.deleteCompleteNotes();
                Toast.makeText(MainActivity.this,"All notes deleted",Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
