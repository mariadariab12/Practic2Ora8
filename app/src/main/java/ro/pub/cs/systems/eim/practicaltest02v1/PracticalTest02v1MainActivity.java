package ro.pub.cs.systems.eim.practicaltest02v1;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02v1MainActivity extends AppCompatActivity {

    private EditText prefixEditText;
    private TextView resultTextView;
    private final AutocmpleteButtonClickListener autocompleteButtonClickListener = new AutocmpleteButtonClickListener();
    private class AutocmpleteButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            StringBuilder prefix = new StringBuilder(prefixEditText.getText().toString());
            if (prefix.length() == 0) {
                Toast.makeText(getApplication(), Constants.EMPTY_KEYWORD_ERROR_MESSAGE, Toast.LENGTH_LONG).show();
            } else {

                new AutocompleteAsyncTask(resultTextView).execute(prefix.toString());
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
    }
}