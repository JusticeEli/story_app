package com.justice.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class AddActivity extends AppCompatActivity {
    private TextInputLayout titleTxtInput;
    private TextInputLayout storyTxtInput;
    private Spinner genreSpinner;
    private FloatingActionButton fob;
    private ProgressBar progressBar;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public static final String COMEDY = "comedy";
    public static final String FANTACY = "fantacy";
    public static final String REAL_LIFE = "real life";
    public static final String MOTIVATION = "motivation";

    private boolean updating = false;

    private StoryData storyDataOriginal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initWidgets();
        setOnClickListeners();
        setUpSpinner();
        checkIfWeAreUpdatingStory();
        if (!updating) {
            getSupportActionBar().hide();
        }
    }


    private void checkIfWeAreUpdatingStory() {
        if (ApplicationClass.documentSnapshot != null) {
            storyDataOriginal = ApplicationClass.documentSnapshot.toObject(StoryData.class);
            updating = true;
            setDefaultValues();
            setWidgetsDisabled();

        }
    }

    private void setWidgetsDisabled() {

        titleTxtInput.getEditText().setEnabled(false);
        storyTxtInput.getEditText().setEnabled(false);
        genreSpinner.setEnabled(false);
        fob.setVisibility(View.INVISIBLE);

    }

    private void setDefaultValues() {
        titleTxtInput.getEditText().setText(storyDataOriginal.getTitle());
        storyTxtInput.getEditText().setText(storyDataOriginal.getStory());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                genreSpinner.setSelection(storyDataOriginal.getGenre());
            }
        }, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editMenu) {
            setWidgetsEnabled();
        } else if (item.getItemId() == R.id.deleteMenu) {
            if (updating) {
                deleteStory();
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteStory() {
        ApplicationClass.documentSnapshot.getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toasty.success(AddActivity.this, "deletion success", Toast.LENGTH_SHORT,true).show();
                } else {
                    Toast.makeText(AddActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setWidgetsEnabled() {
        titleTxtInput.getEditText().setEnabled(true);
        storyTxtInput.getEditText().setEnabled(true);
        genreSpinner.setEnabled(true);
        fob.setVisibility(View.VISIBLE);
    }

    private void setUpSpinner() {
        String[] genres = {COMEDY, FANTACY, REAL_LIFE, MOTIVATION};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genres);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genreSpinner.setAdapter(adapter);
    }

    private void setOnClickListeners() {
        fob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveStoryToDatabase();

            }
        });
    }

    private void saveStoryToDatabase() {
        String title = titleTxtInput.getEditText().getText().toString().trim();
        String story = storyTxtInput.getEditText().getText().toString().trim();
        if (title.isEmpty()) {
            titleTxtInput.setError("Please Fill Field");
            titleTxtInput.requestFocus();
            return;
        }
        if (story.isEmpty()) {
            storyTxtInput.setError("Please Fill Field");
            storyTxtInput.requestFocus();
            return;
        }

        saveStoryToDatabase(title, story);


    }

    private void saveStoryToDatabase(String title, String story) {
        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("story", story);
        map.put("date", FieldValue.serverTimestamp());
        map.put("genre", genreSpinner.getSelectedItemPosition());

        if (updating) {
            ApplicationClass.documentSnapshot.getReference().delete();

        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseFirestore.collection(genreSpinner.getSelectedItem().toString()).add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Toasty.success(AddActivity.this, "story uploaded", Toast.LENGTH_SHORT,true).show();
                    finish();
                } else {
                    Toast.makeText(AddActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });


    }


    private void initWidgets() {
        titleTxtInput = findViewById(R.id.titleTxtInput);
        storyTxtInput = findViewById(R.id.storyTxtInput);
        genreSpinner = findViewById(R.id.genreSpinner);
        fob = findViewById(R.id.fob);
        progressBar = findViewById(R.id.progressBar);

    }
}
