package com.example.arnold.itsosgadda.activities;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.arnold.itsosgadda.R;

import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;
import static android.content.Intent.createChooser;
import static android.util.Patterns.EMAIL_ADDRESS;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

import androidx.appcompat.app.AppCompatActivity;


@SuppressWarnings("FieldCanBeLocal")
public class EmailSendingActivity extends AppCompatActivity {
    private Button buttonSend;
    private EditText txtMessage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_send_layout);
        buttonSend = findViewById(R.id.emailSend);
        txtMessage = findViewById(R.id.editTextMessage);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            public void onClick(View v) {
                final String to = "staff@iissgadda.it";
                final String subject = "IISS Gadda App - Android";
                String message = txtMessage.getText().toString();
                if (to.length() == 0) {
                    makeText(getApplicationContext(),
                            getString(R.string.forgot_email_id_enter),
                            LENGTH_SHORT).show();

                } else if (to.length() > 0 && !isEmailValid(to)) {
                    makeText(getApplicationContext(),
                            getString(R.string.emailId_not_valid), LENGTH_SHORT)
                            .show();
                } else if (subject.length() == 0) {
                    makeText(getApplicationContext(),
                            getString(R.string.forgot_digit_subject),
                            LENGTH_SHORT).show();
                } else {
                    if (message != null && message.length() == 0) {
                        makeText(getApplicationContext(),
                                getString(R.string.forgot_digit_message),
                                LENGTH_SHORT).show();
                    } else if (message != null) {
                        Intent email = new Intent(ACTION_SEND);
                        email.putExtra(EXTRA_EMAIL, new String[]{to});
                        email.putExtra(EXTRA_SUBJECT, subject);
                        email.putExtra(EXTRA_TEXT, message);
                        email.setType("message/rfc822");
                        startActivity(createChooser(email,
                                getString(R.string.choose_email_client)));

                    }
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    boolean isEmailValid(CharSequence email) {
        return EMAIL_ADDRESS.matcher(email).matches();
    }
}
