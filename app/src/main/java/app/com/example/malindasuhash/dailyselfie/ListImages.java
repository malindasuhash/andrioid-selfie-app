package app.com.example.malindasuhash.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
    References:
    http://developer.android.com/guide/topics/media/camera.html#saving-media
    http://www.vogella.com/tutorials/AndroidListView/article.html
    http://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string
    http://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory
    https://developer.android.com/training/scheduling/alarms.html
 */
public class ListImages extends ActionBarActivity {

    private static String LogTag = "Selfie";
    private static int CAMERA = 101;
    private static String SelfieDirectory = "/Selfie/";
    private static int TwoMinutes = 60 * 1000 * 2;

    private ArrayAdapter<Selfie> mSelfies;
    private ListView mSelfieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_images);

        mSelfieList = (ListView)findViewById(R.id.selfies);
        mSelfieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i(LogTag, "Event at " + position + " Long id " + id);

                Intent showImage = new Intent(getApplicationContext(), ShowImageActivity.class);

                Selfie selectedImage = mSelfies.getItem(position);
                showImage.putExtra("data", selectedImage); // This class implements Serializable.

                startActivity(showImage);
            }
        });

        createLocalDirectory();
        setupAlarm();
    }

    @Override
    protected void onResume() {
        mSelfies = new SelfieAdapter(this, getAllSelfies());
        mSelfieList.setAdapter(mSelfies);
        super.onResume();
    }

    private Selfie[] getAllSelfies()
    {
        ArrayList<Selfie> selfies = new ArrayList<Selfie>();

        File files = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES) + SelfieDirectory);

        FileFilter fileFilter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(".jpg");
            }
        };

        for (File file : files.listFiles(fileFilter))
        {
            Selfie selfie = new Selfie();
            selfie.FileFullPath = file.getPath();
            selfie.FriendlyName = file.getName();
            selfies.add(selfie);
        }

        Selfie[] s = new Selfie[selfies.size()];

        selfies.toArray(s);

        return s;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_images, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.camera_settings) {

            File file = new File(getFileNameToStore());
            Uri fileLocation = Uri.fromFile(file);

            Intent showCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            showCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileLocation);

            // In case if there multiple applications installed.
            String string = this.getString(R.string.choose_app);
            startActivityForResult(Intent.createChooser(showCamera, string), CAMERA);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LogTag, "Received callback " + requestCode);

        if (requestCode == CAMERA && resultCode == RESULT_OK)
        {
            Log.i(LogTag, "All ok, rebind the directory in onResume.");
        } else {
            Log.i(LogTag, "Something went wrong or user clicked cancel.");
        }
    }

    private void setupAlarm()
    {
        Intent broadcast = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, broadcast, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null)
        {
            alarmManager.cancel(pendingIntent);
            Log.i(LogTag, "Alarm deleted");
        }

        // Create the new one.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TwoMinutes, TwoMinutes, pendingIntent);
        Log.i(LogTag, "New Alarm created");
    }

    private String getFileNameToStore()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String filePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + SelfieDirectory + timeStamp + ".jpg";

        Log.i(LogTag, filePath);

        return filePath;
    }

    private void createLocalDirectory()
    {
        String directory = getExternalFilesDir(Environment.DIRECTORY_PICTURES) + SelfieDirectory;

        File file = new File(directory, "temp");

            if (!file.exists())
            {
                file.mkdirs();
                Log.i(LogTag, "Directory created");
            } else
            {
                Log.i(LogTag, "Directory already exists");
            }
    }
}


