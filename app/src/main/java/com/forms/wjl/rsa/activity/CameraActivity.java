package com.forms.wjl.rsa.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.forms.wjl.rsa.R;
import com.forms.wjl.rsa.adapter.PopupBtnAdapter;
import com.forms.wjl.rsa.adapter.SimpleFragmentPagerAdapter;
import com.forms.wjl.rsa.fragment.SimpleFragment;
import com.forms.wjl.rsa.utils.BitmapUtils;
import com.forms.wjl.rsa.utils.FileUtils;
import com.forms.wjl.rsa.utils.ScreenUtils;
import com.forms.wjl.rsa.utils.dialog.XDialog;
import com.forms.wjl.rsa.utils.http.utils.LogUtils;
import com.forms.wjl.rsa.view.PopupButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author bubbly
 */
public class CameraActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PICKTURE_CODE = 0x0001; // 相册
    private static final int CAMERA_CODE = 0x0002;// 相机
    private static final int ZOOM_CODE = 0x0003;// 裁剪
    private File photoFile; // 照片路径
    private Dialog mScDialog;// 选择照片弹窗
    private Button btnCamera;
    private PopupButton btn;
    private PopupButton btn2;
    private LayoutInflater inflater;
    private List<String> cValues;
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private ImageView ivPhoto;
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initView();
        initViewPager();
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.shape_select_green_bg);
    }

    private void initViewPager() {
        fragmentList.add(new SimpleFragment());
        fragmentList.add(new SimpleFragment());
        titles.add("推荐");
        titles.add("附近");
        //ivPhoto.setImageDrawable(new ColorDrawable(ContextCompat.getColor(this,R.color.colorAccent)));
        ViewPager vpSimple = (ViewPager) findViewById(R.id.vpSimple);
        //PagerTabStrip ptsTitle = (PagerTabStrip) findViewById(R.id.ptsTitle);
        SimpleFragmentPagerAdapter simpleFragmentPagerAdapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        vpSimple.setAdapter(simpleFragmentPagerAdapter);
    }

    private void initView() {
        btnCamera = (Button) findViewById(R.id.btnCamera);
        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
        btnCamera.setOnClickListener(this);
        path = Environment.getExternalStorageDirectory() + File.separator + "image" + File.separator + "temp";
        mScDialog = new XDialog.Builder(this).setContentView(R.layout.dialog_select_picture_camera)
                .setWidthAndHeight(-1, -2).setOnClickListener(R.id.tvPicture, this).setOnClickListener(R.id.tvCamera, this)
                .setOnClickListener(R.id.tvCancel, this).formBottom(true).create();
        btn = (PopupButton) findViewById(R.id.btn);
        inflater = LayoutInflater.from(this);

        View view = inflater.inflate(R.layout.layout_popup_lv, null);
        ListView lv = view.findViewById(R.id.lv);
        final String[] arr = {"蔬菜", "水果", "家禽", "五谷杂粮", "其他"};
        final PopupBtnAdapter adapter = new PopupBtnAdapter(this, arr);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setPressPosition(position);
                adapter.notifyDataSetChanged();
                btn.setText(arr[position]);
                btn.hidePopup();
            }
        });
        btn.setPopupView(view);

        View view2 = inflater.inflate(R.layout.layout_popup_2lv, null);
        ListView pLv = view2.findViewById(R.id.parent_lv);
        final ListView cLv = view2.findViewById(R.id.child_lv);
        List<String> pList = new ArrayList<>();
        final List<List<String>> cList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            pList.add("一级区域" + i);
            List<String> t = new ArrayList<>();
            for (int j = 0; j < 15; j++) {
                t.add(i + "二级区域" + j);
            }
            cList.add(t);
        }

        cValues = new ArrayList<>();
        cValues.addAll(cList.get(0));
        final PopupBtnAdapter pAdapter = new PopupBtnAdapter(this, pList);
        final PopupBtnAdapter cAdapter = new PopupBtnAdapter(this, cValues);
        pAdapter.setPressPosition(0);

        pLv.setAdapter(pAdapter);
        cLv.setAdapter(cAdapter);

        pLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pAdapter.setPressPosition(position);
                pAdapter.notifyDataSetChanged();
                cValues.clear();
                cValues.addAll(cList.get(position));
                cAdapter.notifyDataSetChanged();
                cAdapter.setPressPosition(-1);
                cLv.setSelection(0);
            }
        });

        cLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cAdapter.setPressPosition(position);
                cAdapter.notifyDataSetChanged();
                btn2.setText(cValues.get(position));
                btn2.hidePopup();
            }
        });
        btn2 = (PopupButton) findViewById(R.id.btn2);
        btn2.setPopupView(view2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCamera:
                mScDialog.show();
                break;
            case R.id.tvPicture:
                temUri = getTempUri();
                startPicture();
                mScDialog.dismiss();
                break;
            case R.id.tvCamera:
                temUri = getTempUri();
                startCamera();
                mScDialog.dismiss();
                break;
            case R.id.tvCancel:
                mScDialog.dismiss();
                break;
            default:
                break;
        }
    }

    /**
     * 结果回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            onResult(requestCode, data);
        }
    }

    /**
     * 成功结果处理
     *
     * @param requestCode
     * @param data
     */
    private void onResult(int requestCode, Intent data) {
        Uri uri = null;
        try {
            uri = data.getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        switch (requestCode) {
            case CAMERA_CODE:
                // 获取图片的选转角度
                int degree = BitmapUtils.readPictureDegree(path);
                // 获取旋转后的图片
                Bitmap bitmap = BitmapUtils.rotaingImage(degree, path);
                // 图片转文件
                File f = BitmapUtils.bitmap2File(bitmap, path);
                try {
                    uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), f.getAbsolutePath(), null, null));
                    startPhotoZoom(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                break;
            case PICKTURE_CODE:
                if (null != data) {// 为了取消选取不报空指针用
                    startPhotoZoom(uri);
                }
                break;
            case ZOOM_CODE:
                try {
                    Bitmap bmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(temUri));
                    onBitmapCallResult(bmp, temUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;

        }
    }

    /**
     * 最终图片结果
     *
     * @param bitmap
     */
    private void onBitmapCallResult(Bitmap bitmap, Uri uri) {
        photoFile = BitmapUtils.bitmap2File(bitmap, uri.getPath());
        ivPhoto.setImageURI(uri);
    }

    /**
     * 跳转相册
     */
    private void startPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, PICKTURE_CODE);
    }

    /**
     * 跳裁剪
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);// 输出是X方向的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, temUri);
        intent.putExtra("return-data", false);// 设置为不返回数据
        startActivityForResult(intent, ZOOM_CODE);
    }

    /**
     * 跳相机
     */
    private void startCamera() {
        // 判断sdcard是否存在
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                // qwe为相片保存的文件夹名
                File dir = new File(Environment.getExternalStorageDirectory() + File.separator + "image");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                // asd为保存的相片名
                File f = new File(dir, "temp");// localTempImgDir和localTempImageFileName是自己定义的名字
                Uri u = Uri.fromFile(f);
                intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
                startActivityForResult(intent, CAMERA_CODE);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private Uri temUri;

    /**
     * 裁剪后保存的uri
     */
    private Uri getTempUri() {
        String uriPath = "file://" + File.separator + createSaveImagePath(this);
        String fileName = "/" + getCurrentTime() + ".jpg";
        LogUtils.e(uriPath + fileName);
        String path = uriPath + fileName;
        if (path.contains("////")) {
            path.replace("////", "///");
        }
        return Uri.parse(path);
    }

    public static String createSaveImagePath(Context context) {
        // 判断sdcard是否存在
        File dir = null;
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            try {
                // qwe为相片保存的文件夹名
                String eSDPath = Environment.getExternalStorageDirectory().getPath();
                String pkg = context.getPackageName();
                // qwe为相片保存的文件夹名
                dir = new File(eSDPath + File.separator + pkg + File.separator + "image");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                LogUtils.e(dir.getAbsolutePath());
                LogUtils.e(dir.getPath());
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }
        return dir.getAbsolutePath();
    }

    public static String getCurrentTime() {
        Date dt = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(dt);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        delAllImages(this);
    }

    /**
     * onDestroy的时候删除图片，不做本地保留
     */
    public static void delAllImages(Context context) {
        String eSDPath = Environment.getExternalStorageDirectory().getPath();
        String pkg = context.getPackageName();
        String imgPath1 = eSDPath + File.separator + "image";
        String imgPath2 = eSDPath + File.separator + pkg + File.separator + "image";
        File file1 = new File(imgPath1);
        File file2 = new File(imgPath2);
        FileUtils.deleteFilesByDirectory(file1);
        FileUtils.deleteFilesByDirectory(file2);
    }
}
