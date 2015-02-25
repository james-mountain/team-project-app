package uk.ac.ncl.cs.team16.lloydsbankingapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

public class DictionaryEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_entry);

        Bundle dictionaryEntryContent = getIntent().getExtras();

        TextView textView = (TextView) findViewById(R.id.textEntryView);
        TextView descView = (TextView) findViewById(R.id.textDescView);
        textView.setText(dictionaryEntryContent.getString("entryName"));
        descView.setText(dictionaryEntryContent.getString("entryDesc"));
    }

}
