package com.harshit_vats.econnect12;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Start extends AppCompatActivity {

    private TextView appTitle, description, helpText;
    private ImageView logo;
    private Button loginButton;

    Animation topAnim, bottomAnim, leftAnim, rightAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_start);

        // Bind views
        appTitle = findViewById(R.id.appTitle);
        description = findViewById(R.id.description);
        helpText = findViewById(R.id.helpText);
        loginButton = findViewById(R.id.loginButton);

        // Load click animation
        AnimatorSet clickAnimation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.button_click_anim);
        clickAnimation.setTarget(loginButton);

        // Load entrance animations
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_annimation);
        rightAnim = AnimationUtils.loadAnimation(this, R.anim.right_annimation);

        // Apply animations
        appTitle.setAnimation(topAnim);
        description.setAnimation(leftAnim);
        loginButton.setAnimation(bottomAnim);
        helpText.setAnimation(bottomAnim);

        // Click listener with animation
        loginButton.setOnClickListener(v -> {
            clickAnimation.start();
            new Handler().postDelayed(() -> showConfirmationDialog(), 200);
        });

        // Handle insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Show role selection dialog (without blur)
    private void showConfirmationDialog() {
        Dialog dialog = new Dialog(Start.this, R.style.dialoge);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        View dialogView = dialog.findViewById(R.id.dialogRoot);
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.dialog_slide_up);
        dialogView.startAnimation(slideUp);

        Button student = dialog.findViewById(R.id.studentbtn);
        Button faculty = dialog.findViewById(R.id.Faculty);
        Button admin = dialog.findViewById(R.id.admintbtn);

        View.OnClickListener listener = v -> {
            dialog.dismiss();
            Intent intent = null;
            if (v == student) intent = new Intent(Start.this, studentlogin.class);
            else if (v == faculty) intent = new Intent(Start.this, FacultyLoginActivity.class);
            else if (v == admin) intent = new Intent(Start.this, adminlogin.class);
            if (intent != null) startActivity(intent);
        };

        student.setOnClickListener(listener);
        faculty.setOnClickListener(listener);
        admin.setOnClickListener(listener);

        dialog.show();
    }
}
