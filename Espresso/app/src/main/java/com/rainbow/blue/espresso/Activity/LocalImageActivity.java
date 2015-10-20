package com.rainbow.blue.espresso.Activity;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.rainbow.blue.espresso.R;
import com.rainbow.blue.espresso.base.FileUtil;

/**
 * Created by blue on 2015/10/19.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class LocalImageActivity extends ListActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.LATITUDE,
            MediaStore.Images.Media.LONGITUDE,
            MediaStore.Images.Media._ID
    };
    private Bitmap bitmap = null;
    private byte[] mContent = null;
    private SimpleCursorAdapter simpleCursorAdapter = null;

    /**
     * Called when the activity is first created.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.list_item_sample,
                null,
                STORE_IMAGES,
                new int[]{R.id.item_title, R.id.item_value}
        );

        simpleCursorAdapter.setViewBinder(new ImageLocationBinder());
        setListAdapter(simpleCursorAdapter);
        getLoaderManager().initLoader(0, null, this);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        // TODO Auto-generated method stub
        // 为了查看信息，需要用到CursorLoader。
        CursorLoader cursorLoader = new CursorLoader(
                this,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                STORE_IMAGES,
                null,
                null,
                null);
        return cursorLoader;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        simpleCursorAdapter.swapCursor(data);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onLoaderReset(Loader<Cursor> arg0) {
        // TODO Auto-generated method stub
        simpleCursorAdapter.swapCursor(null);
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        final Dialog dialog = new Dialog(LocalImageActivity.this);
        // 以对话框形式显示图片
        dialog.setContentView(R.layout.item_image_show);
        dialog.setTitle("图片显示");

        ImageView ivImageShow = (ImageView) dialog.findViewById(R.id.ivImageShow);
        Button btnClose = (Button) dialog.findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                dialog.dismiss();

                // 释放资源
                if (bitmap != null) {
                    bitmap.recycle();
                }
            }
        });

        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                appendPath(Long.toString(id)).build();
        FileUtil file = new FileUtil();
        ContentResolver resolver = getContentResolver();

        // 从Uri中读取图片资源
        try {
            mContent = file.readInputStream(resolver.openInputStream(Uri.parse(uri.toString())));
            bitmap = file.getBitmapFromBytes(mContent, null);
            ivImageShow.setImageBitmap(bitmap);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

        dialog.show();
    }

    // 将图片的位置绑定到视图
    private class ImageLocationBinder implements SimpleCursorAdapter.ViewBinder {
        @Override
        public boolean setViewValue(View view, Cursor cursor, int arg2) {
            // TODO Auto-generated method stub
            if (arg2 == 1) {
                // 图片经度和纬度
                double latitude = cursor.getDouble(arg2);
                double longitude = cursor.getDouble(arg2 + 1);

                if (latitude == 0.0 && longitude == 0.0) {
                    ((TextView) view).setText("位置：未知");
                } else {
                    ((TextView) view).setText("位置：" + latitude + ", " + longitude);
                }

                // 需要注意：在使用ViewBinder绑定数据时，必须返回真；否则，SimpleCursorAdapter将会用自己的方式绑定数据。
                return true;
            } else {
                return false;
            }
        }
    }
}