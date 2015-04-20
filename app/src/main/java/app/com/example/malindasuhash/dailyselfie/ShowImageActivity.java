package app.com.example.malindasuhash.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

public class ShowImageActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.show_selected_image);

        ImageView selectedImageInfo = (ImageView) findViewById(R.id.selectedImageView);

        selectedImageInfo.setImageURI(Uri.parse(intent.getStringExtra("ImagePath")));
    }
}
