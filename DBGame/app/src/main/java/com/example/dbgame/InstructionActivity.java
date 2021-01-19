package com.example.dbgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
/**
 * @author chirag vijay 27009630
 * Class for instruction xml page
 */
public class InstructionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        findViewById(R.id.lvl1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstructionActivity.this, level1.class)); // create intent and start activity to level one page

            }
        });
        findViewById(R.id.lvl2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstructionActivity.this, level2.class));  // create intent and start activity to level two page

            }
        });
        findViewById(R.id.lvl3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstructionActivity.this, level3.class));  // create intent and start activity to level three page

            }
        }); findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InstructionActivity.this, MainActivity.class));  // create intent and start activity to main menu page

            }
        });
    }

}
