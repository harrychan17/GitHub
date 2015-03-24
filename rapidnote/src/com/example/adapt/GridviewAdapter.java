package com.example.adapt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.constant.Constant;
import com.example.rapidnote.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class GridviewAdapter extends BaseAdapter {
    Context context;
    private LayoutInflater inflater;
    private File[] notes_dir_Files;

    public GridviewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public int getCount() {
        File file = new File(Constant.NOTEPATH);
        if (!file.exists()) {
            notes_dir_Files = null;
            return 0;
        } else {
            notes_dir_Files = file.listFiles();
            return notes_dir_Files.length;
        }
    }

    public String getItem(int position) {
        File jsonFile = new File(notes_dir_Files[position].toString() + "/"
                + "index.json");
        StringBuilder builder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(
                    jsonFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.gridview_item, null);
            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(R.id.gridView_item_title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.gridView_item_time);
            viewHolder.text = (TextView) convertView.findViewById(R.id.gridView_item_text);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.gridView_item_image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // get json data
        JSONObject jsonRoot;
        try {
            jsonRoot = new JSONObject(getItem(position));
            //get title and alter_time


            viewHolder.title.setText(jsonRoot.getString("title"));
            viewHolder.time.setText(jsonRoot.getString("alter_time"));
            JSONArray jsonArray = jsonRoot.getJSONArray("list");
            //get text
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                if (jsonItem.getString("type").equals("eText")) {
                    viewHolder.text.setText(jsonItem.getString("value"));
                    break;
                }
            }
            //get image
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonItem = jsonArray.getJSONObject(i);
                if (jsonItem.getString("type").equals("picture")) {
                    BitmapFactory.Options opts = new BitmapFactory.Options();
                    opts.inSampleSize = 20;
                    Bitmap bitmap = BitmapFactory.decodeFile(jsonItem.getString("value"), opts);
                    viewHolder.imageView.setImageBitmap(bitmap);
                    break;
                } else if (jsonItem.getString("type").equals("recorder")) {
                    ;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return convertView;
    }
}

class ViewHolder {
    TextView title;
    TextView time;
    TextView text;
    ImageView imageView;
}
