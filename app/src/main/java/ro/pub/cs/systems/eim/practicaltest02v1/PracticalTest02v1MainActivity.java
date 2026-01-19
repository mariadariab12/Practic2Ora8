package ro.pub.cs.systems.eim.practicaltest02v1;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02v1MainActivity extends AppCompatActivity {

    private EditText prefixEditText;
    private TextView resultTextView;
    private BroadcastReceiver definitionReceiver;
    private final AutocmpleteButtonClickListener autocompleteButtonClickListener = new AutocmpleteButtonClickListener();
    private class AutocmpleteButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            StringBuilder prefix = new StringBuilder(prefixEditText.getText().toString());
            if (prefix.length() == 0) {
                Toast.makeText(getApplication(), Constants.EMPTY_KEYWORD_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            } else {

                new AutocompleteAsyncTask(PracticalTest02v1MainActivity.this).execute(prefix.toString());
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02v1_main);

        prefixEditText = (EditText)findViewById(R.id.prefix_edit_text);
        resultTextView = (TextView)findViewById(R.id.result_text_view);

        Button autocmpleteButton = (Button) findViewById(R.id.autocomplete_button);
        autocmpleteButton.setOnClickListener(autocompleteButtonClickListener);

        definitionReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(Constants.TAG, "aici");
                if (Constants.ACTION_DEFINITION_READY.equals(intent.getAction())) {
                    String def = intent.getStringExtra(Constants.EXTRA_DEFINITION);
                    if (def != null) {
                        Log.i(Constants.TAG, "def: " + def);
                        resultTextView.setText(def);
                    }
                }
            }
        };
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(Constants.ACTION_DEFINITION_READY);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(definitionReceiver, filter, Context.RECEIVER_NOT_EXPORTED);
        } else {
            registerReceiver(definitionReceiver, filter);
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(definitionReceiver);
    }
}