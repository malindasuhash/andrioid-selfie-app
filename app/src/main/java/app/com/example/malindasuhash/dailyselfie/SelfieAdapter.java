package app.com.example.malindasuhash.dailyselfie;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
