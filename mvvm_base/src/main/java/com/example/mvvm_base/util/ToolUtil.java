package com.example.mvvm_base.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.databinding.BindingAdapter;


import com.example.mvvm_base.R;
import com.example.mvvm_base.base.BaseApplication;

import java.io.File;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.mvvm_base.util.Constants.TAG;


/**
 * 其他工具类
 */
public class ToolUtil {


    /**
     * 根据手机的分辨率从px(像素)的单位转成为dp
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从dp的单位转成为px(像素)
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * 获取当前时间
     *
     * @param format 例格式：yyyy年MM月dd日 HH:mm:ss("yyyy-MM-dd HH:mm:ss")
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    /**
     * 间隔天数
     *
     * @param str1
     * @param str2
     * @param format
     * @return
     */
    public static int getInterval(String str1, String str2, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);// 输入日期的格式
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date date2 = null;
        try {
            date2 = simpleDateFormat.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24));// 从间隔毫秒变成间隔天数
    }

    /**
     * 隐藏虚拟键盘
     *
     * @param v
     */
    public static void hideKeyboard(View v) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) BaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        v.clearFocus();
    }

    /**
     * 清除layout下输入框焦点
     *
     * @param layout
     */
    public static void clearEditTextFocus(LinearLayout layout) {
        InputMethodManager imm =
                (InputMethodManager) BaseApplication.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);

        int eCellCount = layout.getChildCount();
        for (int i = 0; i < eCellCount; i++) {
            EditText et_phone = (EditText) layout.getChildAt(i);
            imm.hideSoftInputFromWindow(et_phone.getWindowToken(), 0);
        }
    }

    /**
     * 是否今日
     *
     * @param date
     * @return
     */
    public static boolean isToday(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = new Date(System.currentTimeMillis());
        return format.format(date1).equals(date);
    }

    /**
     * 隐藏activity软键盘
     *
     * @param activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = activity.getWindow().peekDecorView();
        if (null != v) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 判断activity下软键盘是否显示
     *
     * @param activity
     * @return
     */
    private static boolean isSoftShowing(Activity activity) {
        //获取当前屏幕内容的高度
        int screenHeight = activity.getWindow().getDecorView().getHeight();
        //获取View可见区域的bottom
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);

        return screenHeight - rect.bottom != 0;
    }

    /**
     * 获取当前日期与之间隔interval的日期 格式：yyyy-MM-dd
     *
     * @return
     */
    public static String getSomeDay(int interval) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        //过去七天
        c.setTime(new Date());
        c.add(Calendar.DATE, interval);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }


    /**
     * 获取当前日期 格式：list(year,month,day)
     *
     * @return
     */
    public static List<Integer> currentDate() {
        int mYear, mMonth, mDay;
        List<Integer> date = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);
        date.add(mYear);
        date.add(mMonth);
        date.add(mDay);
        return date;
    }

    /**
     * 比较两个日期大小
     *
     * @param date1
     * @param date2
     * @param format
     * @return
     */
    public static boolean compareDate(String date1, String date2, String format) {
        if (TextUtils.isEmpty(date1) || TextUtils.isEmpty(date2))
            return false;
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(date1);
            dt2 = sdf.parse(date2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() > dt2.getTime() || dt1.getTime() == dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }

    /**
     * 将时间戳转成日期格式
     *
     * @param seconds
     * @param format
     * @return
     */
    public static String timeStamp2Date(String seconds, String format) {
        if (TextUtils.isEmpty(seconds)) {
            return "";
        }
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(Long.valueOf(seconds)));//毫秒需加000
    }

    /**
     * null转空字符串
     *
     * @param content
     * @return
     */
    public static String null2String(Object content) {
        if (content == null) {
            return "";
        } else {
            return String.valueOf(content);
        }
    }


    /**
     * 共享元素跳转
     *
     * @param context
     * @param toActivity
     * @param imageView
     * @param bundle
     */
    public static void startActivityWithTransition(Activity context, Class toActivity,
                                                   ImageView imageView, Bundle bundle) {
        Intent intent = new Intent(context, toActivity);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context, imageView,
                        context.getString(R.string.transition_logo_img));
        //与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }


    /**
     * 获取顶部导航高度
     */
    public static void getStatusBarHeight() {
        Resources resources = BaseApplication.getInstance().getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.e(TAG, "getStatusBarHeight: ===============>" + height);
    }

    /**
     * 获取底部虚拟按键高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int result = 0;
        if (hasNavBar(context)) {
            Resources res = context.getResources();
            int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = res.getDimensionPixelSize(resourceId);
            }
        }
        Log.e(TAG, "getNavigationBarHeight: ===============>" + result);
        return result;
    }


    /**
     * 检查是否存在虚拟按键栏
     *
     * @param context
     * @return
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static boolean hasNavBar(Context context) {
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("config_showNavigationBar", "bool", "android");
        if (resourceId != 0) {
            boolean hasNav = res.getBoolean(resourceId);
            // check override flag
            String sNavBarOverride = getNavBarOverride();
            if ("1".equals(sNavBarOverride)) {
                hasNav = false;
            } else if ("0".equals(sNavBarOverride)) {
                hasNav = true;
            }
            return hasNav;
        } else { // fallback
            return !ViewConfiguration.get(context).hasPermanentMenuKey();
        }
    }

    /**
     * 判断虚拟按键栏是否重写
     *
     * @return
     */
    public static String getNavBarOverride() {
        String sNavBarOverride = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                Class c = Class.forName("android.os.SystemProperties");
                Method m = c.getDeclaredMethod("get", String.class);
                m.setAccessible(true);
                sNavBarOverride = (String) m.invoke(null, "qemu.hw.mainkeys");
            } catch (Throwable e) {
            }
        }
        return sNavBarOverride;
    }

//    /**
//     * 转换条形码
//     *
//     * @param str 条形码的字符串
//     * @return
//     * @throws WriterException
//     */
//    public static Bitmap BarcodeFormatCode(String str) {
//        int width = 800;
//        int height = 160;
//        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;
//        BitMatrix matrix = null;
//        try {
//            matrix = new MultiFormatWriter().encode(str, barcodeFormat, width, height, null);
//            return bitMatrix2Bitmap(matrix);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public static Bitmap bitMatrix2Bitmap(BitMatrix matrix) {
//        int w = matrix.getWidth();
//        int h = matrix.getHeight();
//        int[] rawData = new int[w * h];
//        for (int i = 0; i < w; i++) {
//            for (int j = 0; j < h; j++) {
//                int color = Color.WHITE;
//                if (matrix.get(i, j)) {
//                    // 有内容的部分，颜色设置为黑色，可以自己修改成其他颜色
//                    color = Color.BLACK;
//                }
//                rawData[i + (j * w)] = color;
//            }
//        }
//        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
//        bitmap.setPixels(rawData, 0, w, 0, 0, w, h);
//        return bitmap;
//    }

    /**
     * 月份补零
     *
     * @param month
     * @return
     */
    public static String monthPlusZero(int month) {
        return month < 10 ? "0" + month : String.valueOf(month);
    }

    /**
     * 文件大小转换
     *
     * @param fileS
     * @return
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.0");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }


    /**
     * 获取屏幕像素等
     */
    public static void getAndroidScreenProperty() {
        WindowManager wm = (WindowManager) BaseApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;// 屏幕宽度（像素）
        int height = dm.heightPixels; // 屏幕高度（像素）
        float density = dm.density;//屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;//屏幕密度dpi（120 / 160 / 240）
        //屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);//屏幕宽度(dp)
        int screenHeight = (int) (height / density);//屏幕高度(dp)
        Log.e(TAG, "getAndroidScreenProperty: ===============>" + screenWidth + "///" + screenHeight + "///" + density);
    }

    private static final int MIN_CLICK_DELAY_TIME = 1000;


    private static Map<String, Long> records = new HashMap<>();

    /**
     * 快速点击
     *
     * @return
     */
    public static boolean isFastDoubleClick() {
        if (records.size() > 1000) {
            records.clear();
        }

        //本方法被调用的文件名和行号作为标记
        StackTraceElement ste = new Throwable().getStackTrace()[1];
        String key = ste.getFileName() + ste.getLineNumber();

        Long lastClickTime = records.get(key);
        long thisClickTime = System.currentTimeMillis();
        records.put(key, thisClickTime);
        if (lastClickTime == null) {
            lastClickTime = 0L;
        }
        long timeDuration = thisClickTime - lastClickTime;
        return 0 < timeDuration && timeDuration < MIN_CLICK_DELAY_TIME;
    }


    /**
     * 设置图片圆角（1）
     *
     * @param context
     * @param drawableId
     * @return
     */
    public static Drawable toRoundDrawable(Context context, int drawableId) {
        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), drawableId));
        circularBitmapDrawable.setCornerRadius(8);
        return circularBitmapDrawable;
    }

    /**
     * 设置图片圆角（2）
     *
     * @param bitmap
     * @param pixels
     * @return
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }


    /**
     * 根据Uri获取文件真实地址
     *
     * @param context
     * @param uri
     * @return
     */
    public static String getRealFilePath(Context context, Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String realPath = null;
        if (scheme == null)
            realPath = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            realPath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA},
                    null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        realPath = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        if (TextUtils.isEmpty(realPath)) {
            if (uri != null) {
                String uriString = uri.toString();
                int index = uriString.lastIndexOf("/");
                String imageName = uriString.substring(index);
                File storageDir;

                storageDir = Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES);
                File file = new File(storageDir, imageName);
                if (file.exists()) {
                    realPath = file.getAbsolutePath();
                } else {
                    storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                    File file1 = new File(storageDir, imageName);
                    realPath = file1.getAbsolutePath();
                }
            }
        }
        return realPath;
    }

    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion() {
        try {
            PackageManager manager = BaseApplication.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
            int versionCode = info.versionCode;
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "找不到版本号";
        }
    }

    /**
     * 获取版本code
     *
     * @return
     * @throws PackageManager.NameNotFoundException
     */
    public static int getVersionCode() throws PackageManager.NameNotFoundException {
        PackageManager manager = BaseApplication.getInstance().getPackageManager();
        PackageInfo info = manager.getPackageInfo(BaseApplication.getInstance().getPackageName(), 0);
        int versionCode = info.versionCode;
        return versionCode;
    }
}
