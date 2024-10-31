package com.example.asyntask;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button buttonContinue;
    private EditText editTextPhoneNumber;
    private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);
        buttonContinue = findViewById(R.id.button_continue);
        editTextPhoneNumber = findViewById(R.id.editText_phoneNumber);
        window = getWindow();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonContinue.setOnClickListener(v -> {
            String phoneNumber = editTextPhoneNumber.getText().toString();
            if (!phoneNumber.isEmpty()) {
                new RegisterAsyncTask().execute();
            } else {
                editTextPhoneNumber.setError("Please enter a phone number");
            }
        });
    }

    private class RegisterAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            // Optionally change background color to indicate loading
            findViewById(R.id.main).setBackgroundColor(getResources().getColor(android.R.color.darker_gray));

            // Set the screen brightness to 50%
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = 0.5f; // 50% brightness
            window.setAttributes(layoutParams);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(2000); // Simulate a loading process
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.GONE);
            // Restore brightness to default (1.0)
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.screenBrightness = 1.0f; // Restore to 100%
            window.setAttributes(layoutParams);

            // Restore background color after loading
            findViewById(R.id.main).setBackgroundColor(getResources().getColor(android.R.color.white));
            showSuccessDialog();
        }
    }

    private void showSuccessDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(createDialogView())
                .setPositiveButton("OK", (dialogInterface, which) -> dialogInterface.dismiss())
                .create();

        dialog.setCancelable(false);
        dialog.show();
    }

    private View createDialogView() {
        RelativeLayout layout = new RelativeLayout(this);
        layout.setPadding(24, 24, 24, 24);

        ImageView icon = new ImageView(this);

        RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        iconParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layout.addView(icon, iconParams);

        // Create a rotation animation
        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        icon.startAnimation(rotation); // Start the animation

        // Add TextView for message
        TextView messageView = new TextView(this);
        messageView.setText("Selamat Anda telah berhasil melakukan Register!");
        messageView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        messageView.setTextColor(getResources().getColor(android.R.color.black));
        RelativeLayout.LayoutParams messageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        messageParams.addRule(RelativeLayout.BELOW, icon.getId());
        messageParams.setMargins(0, 16, 0, 0);
        layout.addView(messageView, messageParams);

        layout.setBackgroundResource(R.drawable.rounded_dialog_background);
        return layout;
    }
}
