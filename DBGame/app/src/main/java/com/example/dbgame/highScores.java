package com.example.dbgame;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author chirag vijay 27009630
 * Class for highscores
 */
public class highScores extends AppCompatActivity {

    private Query reference; //database reference
    private ListView ListView; //creating listview
    private ArrayList<String> arrayList = new ArrayList<>(); //initialse array
    int add = 0; //this is a int which holds a value so that the database entry can be placed in the correct position
    int num = 0; // this holds the position in which the entry needs to be addded in the arraylist


    private ArrayAdapter<String> adapter; //creating array adapter
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);


        reference = FirebaseDatabase.getInstance().getReference("Highscores").orderByValue(); //reference the highscore section in database
        ListView = (ListView) findViewById(R.id.list_view); //find the list view in the xml
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList); // set the adapter to the list
        ListView.setAdapter(adapter);
        reference.addChildEventListener(new ChildEventListener() { // add a child listener for the database

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String value = dataSnapshot.getValue().toString(); // convert the values in the databse to string
                int score = Integer.parseInt((value.substring(value.indexOf(":")+2))); //parse the value as an integer (everything after the colon)

                if (arrayList.size() == 0){ //if there is nothing in the arraylist
                    arrayList.add(value); // add the first value in
                }
                else if (arrayList.size() != 0){ //if there is something in the arraylist, begin descending order algorithm

                    int nextTo = Integer.parseInt(arrayList.get(0).substring(arrayList.get(0).indexOf(":")+2));  //parse first entry in arraylist as int

                    if (score >=  nextTo){ //check the number wanting to be added is larger than the first entry in the arraylist
                        arrayList.add(0, value); //if it is add it to the front because its bigger or the same
                    }
                    else if (score < nextTo){ //otherwise if its less
                        int endNumber =  Integer.parseInt(arrayList.get(arrayList.size()-1).substring((arrayList.get(arrayList.size()-1).indexOf(":")+2))); //parse the number at the end of the arraylist as an int (smallest number)
                        for (int i = arrayList.size()-1; i > 0; i--){ //iterate tthrough the arraylist backwards
                            int afterNumber =  Integer.parseInt(arrayList.get(i).substring((arrayList.get(i).indexOf(":")+2))); //parse the number in front of the place we want to entry
                            int beforePlaceNumber =  Integer.parseInt(arrayList.get(i-1).substring((arrayList.get(i-1).indexOf(":")+2))); //parse the number before the position we wannt to place the entry
                            if (score < endNumber){ // if the entry is less than the number at the end
                                add = 1; // set add to 1
                            }
                            if (score  >= afterNumber && score <= beforePlaceNumber){ //if the number is between the two numbers either side

                                add = 2; //set add to 2
                                num = i; //set the position of the addition as num

                            }
                         }

                        if (add == 1){
                            arrayList.add(value); // as add is 1 add to the end of the arraylist
                        }
                        if (add == 2){
                            arrayList.add(num, value); // as add is 2 add to the position set in the arraylist
                        }

                      }

                }

                adapter.notifyDataSetChanged(); //notify that the dataset has changed



            }


            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });



    }


}
