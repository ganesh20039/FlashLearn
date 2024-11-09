// HomeActivity.java
package com.example.flashlearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlashcardAdapter adapter;
    private List<Flashcard> flashcardsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        flashcardsList = new ArrayList<>();

        adapter = new FlashcardAdapter(flashcardsList);
        recyclerView.setAdapter(adapter);

        loadFlashcards();

        FloatingActionButton addButton = findViewById(R.id.fabAdd);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, FlashcardEditActivity.class);
            startActivity(intent);
        });
    }

    private void loadFlashcards() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("flashcards")
                .get()
                .addOnSuccessListener(new com.google.android.gms.tasks.OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        flashcardsList.clear();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Flashcard flashcard = document.toObject(Flashcard.class);
                            flashcardsList.add(flashcard);
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Error loading data", Toast.LENGTH_SHORT).show());
    }
}
