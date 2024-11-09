// FlashcardViewActivity.java
package com.example.flashlearn;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Random;

public class FlashcardViewActivity extends AppCompatActivity {

    private TextView questionText, answerText;
    private Button shuffleButton, markKnownButton;
    private List<Flashcard> flashcardsList;
    private Flashcard currentFlashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_view);

        questionText = findViewById(R.id.tvQuestion);
        answerText = findViewById(R.id.tvAnswer);
        shuffleButton = findViewById(R.id.btnShuffle);
        markKnownButton = findViewById(R.id.btnMarkKnown);

        loadFlashcards();

        questionText.setOnClickListener(v -> {
            flipCard();
        });

        answerText.setOnClickListener(v -> {
            flipCard();
        });

        shuffleButton.setOnClickListener(v -> {
            shuffleFlashcards();
        });

        markKnownButton.setOnClickListener(v -> {
            Toast.makeText(FlashcardViewActivity.this, "Flashcard marked as known!", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadFlashcards() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("flashcards")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    flashcardsList = queryDocumentSnapshots.toObjects(Flashcard.class);
                    shuffleFlashcards();
                });
    }

    private void shuffleFlashcards() {
        if (flashcardsList != null && !flashcardsList.isEmpty()) {
            Random random = new Random();
            currentFlashcard = flashcardsList.get(random.nextInt(flashcardsList.size()));
            displayFlashcard();
        }
    }

    private void displayFlashcard() {
        questionText.setText(currentFlashcard.getQuestion());
        answerText.setText(currentFlashcard.getAnswer());
        answerText.setVisibility(View.GONE);  // Initially hide the answer
    }

    private void flipCard() {
        Animation flipAnimation = AnimationUtils.loadAnimation(this, R.anim.flip);
        answerText.startAnimation(flipAnimation);
        if (answerText.getVisibility() == View.GONE) {
            answerText.setVisibility(View.VISIBLE);
            questionText.setVisibility(View.GONE);
        } else {
            questionText.setVisibility(View.VISIBLE);
            answerText.setVisibility(View.GONE);
        }
    }
}
