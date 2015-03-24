package com.example.activities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.constant.Constant;
import com.example.rapidnote.R;

public class EditNoteActivity extends Activity {
	int displayWidth;
	int displayHeight;
	ImageButton btn_camera;
	ImageButton btn_recorder;
	ImageButton btn_clip;
	ImageButton btn_edit;
	EditText editText_title;
	LinearLayout linearLayout;
	Button btn_cancel;
	Button btn_finish;
	String dirPath;
	String tempFilePath;
	String create_time;
	ArrayList<String> arrayList;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_note);
		findViewById();
		initListener();
		getDisplayscale();
		init();
	}

	private void init() {
		// check SD card
		if (!Environment.getExternalStorageDirectory().exists()) {
			Toast.makeText(getApplicationContext(), "SD�������ڣ�����.",
					Toast.LENGTH_SHORT).show();
			finish();
		}
		create_time = DateFormat.format("yyyy_MM_dd_HH:mm:ss",
				System.currentTimeMillis()).toString();
		dirPath = Constant.NOTEPATH + "/" + create_time;
		// create a note directory
		new File(dirPath).mkdirs();
		arrayList = new ArrayList<String>();
	}

	private void getDisplayscale() {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		displayHeight = displayMetrics.heightPixels;
		displayWidth = displayMetrics.widthPixels;
	}

	private void AddEdtv() {
		// dynamic add EditText from layout >> edtv
		LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
		LinearLayout parent = (LinearLayout) inflater.inflate(R.layout.edtv,
				null);
		EditText eText = (EditText) parent.findViewById(R.id.edtv_lyt);
		parent.removeView(eText);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lParams.setMargins(30, 6, 30, 6);
		linearLayout.addView(eText, lParams);
		arrayList.add("eText:");
	}

	private void writerNote() {
		// store json
		String title = editText_title.getText().toString();
        if(title.equals(""))
            title="无标题";
		JSONObject jsonRoot = new JSONObject();
		try {
			jsonRoot.put("create_time", create_time);
			jsonRoot.put("alter_time", create_time);
			jsonRoot.put("title", title);
			JSONArray jsonArray = new JSONArray();
			for (int i = 0; i < arrayList.size(); i++) {
				String s = arrayList.get(i);
				JSONObject jsonItem = new JSONObject();
				jsonItem.put("index", i);
                String type=s.substring(0,s.indexOf(":"));
				if (type.equals("eText")) {
					jsonItem.put("type", "eText");
					jsonItem.put("value", ((EditText) (linearLayout
                            .getChildAt(i))).getText().toString());

				} else if(type.equals("picture")){
					jsonItem.put("type", "picture");
                    // put value == filePath
					jsonItem.put("value", arrayList.get(i).substring(s.indexOf(":")+1,s.length()));
				}
				jsonArray.put(jsonItem);
			}
			jsonRoot.put("list", jsonArray);

			File file = new File(dirPath + "/" + "index.json");
			file.createNewFile();
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write(jsonRoot.toString());
			fileWriter.flush();
			fileWriter.close();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        Toast.makeText(getApplicationContext(),"已保存记事",Toast.LENGTH_SHORT).show();
		finish();
	}

    @Override
    public void onBackPressed() {

        writerNote();
    }

    private void initListener() {
		btn_edit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AddEdtv();
			}

		});
		btn_camera.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tempFilePath = dirPath + "/" + System.currentTimeMillis()
						+ ".jpg";
				Uri imageUri = Uri.fromFile(new File(tempFilePath));
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent, 2);
			}
		});
		btn_recorder.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(EditNoteActivity.this,RecorderActivity.class);
				tempFilePath = dirPath + "/" + "record"+System.currentTimeMillis()
						+ ".aac";
				intent.putExtra("record_path", tempFilePath);
				startActivityForResult(intent,3);
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		btn_finish.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writerNote();
			}

		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getApplicationContext());
				builder.setMessage("ȷ��ȡ����£�");
				builder.setNegativeButton("ȡ��", null);
				// it should be new DialogInterface.OnClickListener-
				// --http://blog.csdn.net/competerh_programing/article/details/7377950
				builder.setPositiveButton("ȷ��",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								// ----����Ҫɾ��ղż�¼���ļ���
								finish();
							}
						});
				builder.create().show();
			}
		});
	}

	private void findViewById() {
		editText_title = (EditText) findViewById(R.id.edtv_title);
		btn_edit = (ImageButton) findViewById(R.id.btn_edit);
		btn_camera = (ImageButton) findViewById(R.id.btn_camera);
		btn_clip = (ImageButton) findViewById(R.id.btn_clip);
		btn_recorder = (ImageButton) findViewById(R.id.btn_tape);
		linearLayout = (LinearLayout) findViewById(R.id.llyt_show_body);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_finish = (Button) findViewById(R.id.btn_finish);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			// add camera view
			case 2:
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(tempFilePath, opts);
				opts.inSampleSize = opts.outHeight / displayHeight;
				opts.inJustDecodeBounds = false;
				Bitmap bitmap = BitmapFactory.decodeFile(tempFilePath, opts);
				ImageView imageView = new ImageView(getApplicationContext());
				imageView.setImageBitmap(bitmap);
				// bitmap.recycle();
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
				// add on long click listener for imageview
				imageView.setOnLongClickListener(new OnLongClickListener() {
					public boolean onLongClick(View v) {
						System.out.println("long click");
						return false;
					}
				});
				LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						displayHeight / 2);
				lparams.setMargins(0, 10, 0, 10);
				linearLayout.addView(imageView, lparams);
				arrayList.add("picture:" + tempFilePath);
				break;
            case 3:


                break;
			default:
				break;
			}
		}
	}
}
