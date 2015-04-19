package app.com.example.malindasuhash.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ShowImageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.show_selected_image);

        TextView selectedImageInfo = (TextView) findViewById(R.id.selected_image_info);

        selectedImageInfo.setText("Selected index is " + intent.getIntExtra("SelectedImageIndex", -1));
    }
}
