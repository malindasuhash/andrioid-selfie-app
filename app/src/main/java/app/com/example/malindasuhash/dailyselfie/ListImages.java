package app.com.example.malindasuhash.dailyselfie;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/*
    References:
    http://developer.android.com/guide/topics/media/camera.html#saving-media
    http://www.vogella.com/tutorials/AndroidListView/article.html
    http://stackoverflow.com/questions/5374311/convert-arrayliststring-to-string
    http://stackoverflow.com/questions/8646984/how-to-list-files-in-an-android-directory

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
                showImage.putExtra("ImagePath", selectedImage.FileFullPath);

                startActivity(showImage);
            }
        });

        createLocalDirectory();
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

        File files = new File(getExternalFilesDir(null) + SelfieDirectory);

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
            startActivityForResult(Intent.createChooser(showCamera, "Choose an application:"), CAMERA);

            return true;
        }

        if (id == R.id.nooooo)
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

    private String getFileNameToStore()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        String filePath = getExternalFilesDir(null) + SelfieDirectory + timeStamp + ".jpg";

        Log.i(LogTag, filePath);

        return filePath;
    }

    private void createLocalDirectory()
    {
        String directory = getExternalFilesDir(null) + SelfieDirectory;

        File file = new File(directory, "temp");

            if (!file.exists())
            {
                Log.i(LogTag, "Directory created");
                file.mkdirs();
            } else
            {
                Log.i(LogTag, "Directory already exists");
            }
    }
}

/**
 * Simple DTO to store the full path and the name of the file.
 */
class Selfie
{
    public String FileFullPath;
    public String FriendlyName;
}


/**
 * This is the adapter that builds each row in the list view.
 */
class SelfieAdapter extends ArrayAdapter<Selfie>
{
    private Context mContext;
    private Selfie[] mSelfies;

    public SelfieAdapter(Context context, Selfie[] selfies)
    {
        super(context, R.layout.row_item, selfies);
        this.mContext = context;
        this.mSelfies = selfies;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View selfieRow = inflater.inflate(R.layout.row_item, parent, false);

        ImageView selfieImage = (ImageView) selfieRow.findViewById(R.id.selfie_img);
        TextView selfieDesc = (TextView) selfieRow.findViewById(R.id.selfie_des);

        Selfie selfie = mSelfies[position];

        selfieImage.setImageURI(Uri.parse(selfie.FileFullPath));
        selfieDesc.setText(selfie.FriendlyName.replace(".jpg", "")); // removes extension JPG.

        return selfieRow;
    }
}