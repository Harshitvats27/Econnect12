package com.harshit_vats.econnect12;

import android.app.Dialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Start extends AppCompatActivity {

    private TextView heading, description, issue;
    private ImageView logo;
    private Button login;
    private LinearLayout linearLayout;
    Animation topAnim, bottomAnim , leftAnim, rightAnim;
ggggggggggggggggggggggggggggggggggggggggg
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);
        heading =findViewById(R.id.textView);
        description = findViewById(R.id.editTextTextMultiLine);
        issue = findViewById(R.id.textView3);
       logo =findViewById(R.id.imageView);
        login = findViewById(R.id.button);
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
     rightAnim = AnimationUtils.loadAnimation(this,R.anim.right_annimation);
        leftAnim = AnimationUtils.loadAnimation(this,R.anim.left_annimation);


        heading.setAnimation(leftAnim);
        logo.setAnimation(rightAnim);
        description.setAnimation(leftAnim);
        issue.setAnimation(leftAnim);

        login.setAnimation(rightAnim);

        login.setOnClickListener(v -> showconfirmationdialog());







        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    //Show logout confirmation dialog
    private void showconfirmationdialog() {
        Dialog dialog = new Dialog(Start.this, R.style.dialoge);
        dialog.setContentView(R.layout.dialog_layout);

         Button student = dialog.findViewById(R.id.studentbtn);
        Button faculty = dialog.findViewById(R.id.Faculty);
        Button admin =dialog.findViewById(R.id.admintbtn);
        admin.setOnClickListener(v -> {
            Intent intent = new Intent(Start.this, adminlogin.class);
            startActivity(intent);
            dialog.cancel();
        });

        student.setOnClickListener(v -> {
            Intent intent = new Intent(Start.this, studentlogin.class);
            startActivity(intent);
            dialog.cancel();
        });

        faculty.setOnClickListener(v ->{
            Intent intent = new Intent(Start.this, facultylogin.class);
            startActivity(intent);
            dialog.cancel();

    });
        // Show the dialog
        dialog.show();
    }
}
