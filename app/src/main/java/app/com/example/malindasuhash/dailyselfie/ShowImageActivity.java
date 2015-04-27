package app.com.example.malindasuhash.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;

public class ShowImageActivity extends Activity {

    private static String LogTag = "Selfie";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();

        setContentView(R.layout.show_selected_image);

        ImageView selectedImageInfo = (ImageView) findViewById(R.id.selectedImageView);

        Selfie selfie = (Selfie) intent.getSerializableExtra("data");

        final Uri imagePath = Uri.parse(selfie.FileFullPath);
        selectedImageInfo.setImageURI(imagePath);

        ImageButton removeFileButton = (ImageButton) findViewById(R.id.removeImage);
        removeFileButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Remove the file
                final File file = new File(imagePath.getPath());

                if (file.exists())
                {
                    // Probably better to show an alert.
                    file.delete();
                    Log.i(LogTag, "File removed.");
                    // Looks like calling finish simply close this view.
                    finish();
                }
            }
        });
    }
}

