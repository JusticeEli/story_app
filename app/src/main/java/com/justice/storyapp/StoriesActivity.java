package com.justice.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import es.dmoral.toasty.Toasty;

import static com.justice.storyapp.AddActivity.COMEDY;
import static com.justice.storyapp.AddActivity.FANTACY;
import static com.justice.storyapp.AddActivity.MOTIVATION;
import static com.justice.storyapp.AddActivity.REAL_LIFE;

public class StoriesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TextView headerTxtView;
    private StoriesAdapter adapter;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
        initWidgets();
        setUpRecyclerAdapter();
    }

    private void initWidgets() {
        recyclerView = findViewById(R.id.recyclerView);
        headerTxtView = findViewById(R.id.headerTxtView);

    }

    private void setUpRecyclerAdapter() {
        Query query = null;
        switch (MainActivity.genre) {
            case 0:
                headerTxtView.setText(COMEDY);
                query = firebaseFirestore.collection(COMEDY);
                break;
            case 1:
                headerTxtView.setText(FANTACY);

                query = firebaseFirestore.collection(FANTACY);
                break;
            case 2:
                headerTxtView.setText(REAL_LIFE);

                query = firebaseFirestore.collection(REAL_LIFE);
                break;
            case 3:
                headerTxtView.setText(MOTIVATION);

                query = firebaseFirestore.collection(MOTIVATION);
                break;

        }
        FirestoreRecyclerOptions<StoryData> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder<StoryData>().setQuery(query, StoryData.class).setLifecycleOwner(this).build();
        adapter = new StoriesAdapter(this, firestoreRecyclerOptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                adapter.getSnapshots().getSnapshot(viewHolder.getAdapterPosition()).getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toasty.success(StoriesActivity.this, "deletion success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StoriesActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        }).attachToRecyclerView(recyclerView);

    }
}
