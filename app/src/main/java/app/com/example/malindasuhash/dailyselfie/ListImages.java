package app.com.example.malindasuhash.dailyselfie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;


public class ListImages extends ActionBarActivity {

    private static String LogTag = "Selfie";

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
        selfies.add(new Selfie());
        selfies.add(new Selfie());
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
            return true;
        }

        return super.onOptionsItemSelected(item);
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