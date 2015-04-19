package app.com.example.malindasuhash.dailyselfie;

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


public class ListImages extends ActionBarActivity {

    private static String LogTag = "Selfie";
    private static int CAMERA = 101;
    private static String SelfieDirectory = "/Selfie/";

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
                showImage.putExtra("SelectedImageIndex", position);
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

            startActivityForResult(showCamera, CAMERA);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(LogTag, "Received callback " + requestCode);

        if (requestCode == CAMERA && resultCode == RESULT_OK)
        {
            Log.i(LogTag, "All ok, rebind the directory.");
        } else {
            Log.i(LogTag, "Something went wrong");
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
                file.mkdirs(); // Create directory.
            } else
            {
                Log.i(LogTag, "Directory already exists");
            }
    }
}

class Selfie
{
    public String FileFullPath;
    public String FriendlyName;
}

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

        //Selfie selfie = mSelfies[position];

        selfieImage.setImageResource(R.drawable.abc_btn_radio_material);
        selfieDesc.setText("Boom");

        return selfieRow;
    }
}