package uk.ac.ncl.cs.team16.lloydsbankingapp.Activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import uk.ac.ncl.cs.team16.lloydsbankingapp.R;

public class DictionaryEntryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Potentially add more information to this activity, it's very blank.

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_entry);

        Bundle entryContent = getIntent().getExtras();

        TextView textView = (TextView) findViewById(R.id.textEntryView);
        TextView descView = (TextView) findViewById(R.id.textDescView);
        textView.setText(entryContent.getString("entryName"));
        descView.setText(entryContent.getString("entryDesc"));
    }

}
