package com.example.sagiproject;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

// google explanations
// https://firebase.google.com/docs/database/android/lists-of-data#java_1


public class FBsingleton {
    WordRepository wordRepository= new WordRepository();
    private static FBsingleton instance;
    private static Context context;
    private ArrayList<WordPair>arrayListEasy=wordRepository.getEasyWords();
    private ArrayList<WordPair>arrayListHard=wordRepository.getHardWords();



    FirebaseDatabase database;

    private FBsingleton() {
        database = FirebaseDatabase.getInstance();

        // read the records from the Firebase and order them by the record from highest to lowest
        // limit to only 8 items
        //Query myQuery = database.getReference("records").orderByChild("score").limitToLast(10);
        DatabaseReference myRef = database.getReference("SagiProject");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot snapshot) {
                if(snapshot != null)
                {
                    ArrayList<WordPair> arrayList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        WordPair wordPair = dataSnapshot.getValue(WordPair.class);
                        arrayList.add(wordPair);
                    };
                    ((GameActivity)context).updateWords(arrayList);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


    }

    public static FBsingleton getInstance(Context context1) {
        context = context1;
        if (null == instance) {
            instance = new FBsingleton();
        }
        return instance;
    }

    public void setCard(WordPair wordPair, String packageName)
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("YanivGame/" + packageName + "/");

        myRef.setValue(wordPair);
    }

    public void setPackage(ArrayList<WordPair> cards, String packageName)
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("sagi/" + packageName + "/");
        myRef.setValue(cards);
    }

    public void setName(String name)
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid() + "/MyName");

        myRef.setValue(name);
    }
    public void setScore()
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid() + "/MyScore");

        myRef.setValue(0);
    }
    public void setEasyWords()
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid() + "/EasyWords");

        myRef.setValue(arrayListEasy);
    }
    public void setHardWords()
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid() + "/HardWords");

        myRef.setValue(arrayListHard);
    }

    public void setDetails(String type, int price)
    {
        // Write a message to the database
        //DatabaseReference myRef = database.getReference("records").push(); // push adds new node with unique value

        DatabaseReference myRef = database.getReference("records/" + FirebaseAuth.getInstance().getUid() + "/MyDetails");

        MyDetailsInFb rec = new MyDetailsInFb(type, price);
        myRef.setValue(rec);
    }
}
