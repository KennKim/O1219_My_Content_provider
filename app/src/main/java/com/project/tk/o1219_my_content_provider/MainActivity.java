package com.project.tk.o1219_my_content_provider;

import android.Manifest;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.project.tk.o1219_my_content_provider.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding b;
    //    ArrayList<Pair<Uri,Uri>> result;
    private ArrayList<Uri> result;
    private ArrayList<Uri> result3;
    private ArrayList<Photo> resultPhoto;


    private PhotoAdapter mAdapter;
    private ArrayList<Uri> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }

        items = new ArrayList<>();
        mAdapter = new PhotoAdapter(items);
        b.rv.setLayoutManager(new GridLayoutManager(this, 4));
        b.rv.setAdapter(mAdapter);

        b.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchAllImages();
                StringBuffer str = new StringBuffer();
                for (Uri u : result) {
                    str.append("first : " + u.toString() + "\n");
                }

                /*for (Uri u : result) {
                    str.append("first : " + u.toString() + "\n");
                }*/
                b.tv1.setText(str);
//                Uri uri = uriToThumbnail(result.get(100).toString());
                b.tv2.setText(result.get(100).toString());
//                b.iv.setImageURI(uri);

            }
        });
        b.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ArrayList<Photo> resultttt = fetchAllImages2();

                StringBuffer str = new StringBuffer();

                for (Photo p : resultttt) {
                    str.append("first : " + p.a.toString() + "\n");
                    str.append("second : " + p.b.toString() + "\n");
                }
                b.tv1.setText(str);
//                Uri uri = uriToThumbnail(result.get(100).toString());
                b.tv2.setText(result.get(100).toString());
//                b.iv.setImageURI(uri);


            }
        });
        b.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchAllImages3();
                StringBuffer str = new StringBuffer();
                for (Uri u : result3) {
                    str.append("first : " + u.toString() + "\n");
//                    Uri uri = uriToThumbnail(u.toString());
//                    items.add(uri);
                }

                /*for (Uri u : result) {
                    str.append("first : " + u.toString() + "\n");
                }*/
                b.tv1.setText(str);
                mAdapter.notifyDataSetChanged();


            }
        });
    }


    private void fetchAllImages() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        result = new ArrayList<>(cursor.getCount());
        int dataColumnIndex = cursor.getColumnIndex(projection[0]);

        if (cursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
        } else if (cursor.moveToFirst()) {
            do {
                String filePath = cursor.getString(dataColumnIndex);
                Uri imageUri = Uri.parse(filePath);
                result.add(imageUri);
            } while (cursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        cursor.close();
    }
    private ArrayList<Photo> fetchAllImages2() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};

        Cursor imageCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA, _ID를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        resultPhoto = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);
        int idColumnIndex = imageCursor.getColumnIndex(projection[1]);

        if (imageCursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
        } else if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                String imageId = imageCursor.getString(idColumnIndex);

                Uri imageUri = Uri.parse(filePath);
                Uri thumbnailUri = uriToThumbnail(imageId);
                // 원본 이미지와 썸네일 이미지의 uri를 모두 담을 수 있는 클래스를 선언합니다.
                Photo photo = new Photo(imageUri, thumbnailUri);
                resultPhoto.add(photo);
            } while (imageCursor.moveToNext());
        } else {
            // imageCursor가 비었습니다.
        }
        imageCursor.close();
        return resultPhoto;
    }
    private void fetchAllImages3() {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Media._ID};

        Cursor imageCursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // 이미지 컨텐트 테이블
                projection, // DATA를 출력
                null,       // 모든 개체 출력
                null,
                null);      // 정렬 안 함

        result3 = new ArrayList<>(imageCursor.getCount());
        int dataColumnIndex = imageCursor.getColumnIndex(projection[0]);

        if (imageCursor == null) {
            // Error 발생
            // 적절하게 handling 해주세요
        } else if (imageCursor.moveToFirst()) {
            do {
                String filePath = imageCursor.getString(dataColumnIndex);
                Uri uri = uriToThumbnail(filePath);
                    items.add(uri);
                Uri imageUri = Uri.parse(filePath);
                result3.add(imageUri);
            } while (imageCursor.moveToNext());
        } else {
            return;
        }
        imageCursor.close();

    }


    private Uri uriToThumbnail(String imageId) {
        // DATA는 이미지 파일의 스트림 데이터 경로를 나타냅니다.
        String[] projection = {MediaStore.Images.Thumbnails.DATA};

        // 원본 이미지의 _ID가 매개변수 imageId인 썸네일을 출력
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, // 썸네일 컨텐트 테이블
                projection, // DATA를 출력
                MediaStore.Images.Thumbnails.IMAGE_ID + "=?", // IMAGE_ID는 원본 이미지의 _ID를 나타냅니다.
                new String[]{imageId},
                null);

        if (cursor != null && cursor.moveToFirst()) {
            int thumbnailColumnIndex = cursor.getColumnIndex(projection[0]);

            String thumbnailPath = cursor.getString(thumbnailColumnIndex);
            cursor.close();
            return Uri.parse(thumbnailPath);
        } else {
            // thumbnailCursor가 비었습니다.
            // 이는 이미지 파일이 있더라도 썸네일이 존재하지 않을 수 있기 때문입니다.
            // 보통 이미지가 생성된 지 얼마 되지 않았을 때 그렇습니다.
            // 썸네일이 존재하지 않을 때에는 아래와 같이 썸네일을 생성하도록 요청합니다
            Uri uri;
            MediaStore.Images.Thumbnails.getThumbnail(getContentResolver(),
                    Long.parseLong(imageId),
                    MediaStore.Images.Thumbnails.MINI_KIND,
                    null);
            cursor.close();
            return uriToThumbnail(imageId);
        }
    }

}
