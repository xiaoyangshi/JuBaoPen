package com.jinjiuyun.mz.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.widget.Toast;

import com.jinjiuyun.mz.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author LZe
 *
 */
public class FileManager {

    public static final String save_cache = "cache";
    public static final String save_temp = "temp";
    public static final String save_long = "long";
    public static final String sdCardDir = "/moshi/";
    public static final String xmlFile_common = "common";
    public static final String xmlFile_bind = "bind";
    public static final String sdCameraDir = "/moshi/cache/";
    public static final String sdMp3 = "/moshi/mp3/";
    public static final String sdLrc = "/moshi/lrc/";
    public static final String downMp3 = "MR_MP3/";//晨读缓存MP3路径
    public static final String downLrc="lrc/";
    public static final String SDCARDPATH = Environment
            .getExternalStorageDirectory() + "";
    public static final String sdDownloadDir = Environment
            .getExternalStorageDirectory().getAbsolutePath() + sdCardDir;
    public static final String sdCardCut = "/moshi/cut/";
    public static final int MSG_ALIPAY_BACK = 1040;

    /**
     * 读取SD卡上文件
     *
     * @param fileName
     * @return
     */
    public static InputStream loadSDFile(String fileName) {
        try {
            InputStream fi = new FileInputStream(Environment
                    .getExternalStorageDirectory().getAbsolutePath()
                    + sdCardDir + fileName);
            return fi;
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    /**
     * 在SD卡上存文件（需在多线程中完成）
     *
     * @param fileName
     * @param is
     * @return 成功file，失败null
     */
    public static File saveSDFile(String fileName, InputStream is,
                                  boolean append) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + sdCardDir + fileName);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();
        if (is != null) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, append);
                byte[] b = new byte[1024];
                int len = -1;
                while ((len = is.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
                fos.close();
                is.close();
                return file;
            } catch (Exception e) {
               // StringManager.reportError(MoshiApp.context.getString(R.string.write_sd_error), null);
                return null;
            }
        } else
            return null;
    }

    /**
     * 在SD卡上存文件（需在多线程中完成）
     *
     * @param fileName
     * @param str
     * @param append
     *            是否在文件后面增加字符
     * @return 成功file，失败null
     */
    public static File saveSDFile(String fileName, String str, boolean append) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + sdCardDir + fileName);
        FileOutputStream fileOutputStream = null;
        try {
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();
            fileOutputStream = new FileOutputStream(file, append);
            fileOutputStream.write(str.getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            return null;
        }
        return file;
    }

    /**
     * 在SD卡上存文件
     *
     * @param completePath
     *            : 完整路径
     * @param is
     * @return 成功file，失败null
     */
    public static File saveFileToCompletePath(String completePath,
                                              InputStream is, boolean append) {
        File file = new File(completePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists())
            parentFile.mkdirs();
        if (is != null) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file, append);
                byte[] b = new byte[4*1024];
                int len = -1;
                while ((len = is.read(b)) != -1) {
                    fos.write(b, 0, len);
                }
                fos.close();
                is.close();
                return file;
            } catch (Exception e) {
                e.printStackTrace();
                // LogManager.reportError("写sd文件异常",e);
                return null;
            }
        } else
            return null;
    }

    /**
     * 存为xml
     *
     * @param context
     * @param map
     */
    public static void saveShared(Context context, String name,
                                  Map<String, String> map) {
        SharedPreferences mShared = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = mShared.edit();
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            editor.putString(entry.getKey(), entry.getValue());
        }
        editor.commit();
    }

    /**
     * 读取xml元素
     *
     * @param context
     * @param key
     * @return
     */
    public static Object loadShared(Context context, String name, String key) {
        SharedPreferences mShared = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        if (key.length() == 0)
            return mShared.getAll();
        return mShared.getString(key, "");
    }

    /**
     * 删除xml元素
     *
     * @param context
     * @param key
     */
    public static void delShared(Context context, String name, String key) {
        SharedPreferences mShared = context.getSharedPreferences(name,
                Context.MODE_PRIVATE);
        Editor editor = mShared.edit();
        if (key.equals(""))
            editor.clear();
        else
            editor.remove(key);
        editor.commit();
    }

    // 新建文件夹
    public static boolean makeDir(String path) {

        if (Environment.getExternalStorageDirectory().equals(
                Environment.MEDIA_MOUNTED)) {

            return false;
        }
        File file = new File(path);
        if (!file.exists()) {

            file.mkdirs();// 创建文件夹
        }
        return true;
    }

    /**
     * 删除SD卡上时间较早的文件
     *
     * @param path
     * @param keep 文件夹内只保留
     *            (keep~keep*2)个文件
     */
    public static void delSDFile(String path, int keep) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + sdCardDir + path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length - keep * 2 > 0) {
                if (keep > 0) {
                    System.setProperty("java.util.Arrays.useLegacyMergeSort",
                            "true");
                    try {
                        Arrays.sort(files, new Comparator<Object>() {
                            @Override
                            public int compare(Object object1, Object object2) {
                                File file1 = (File) object1;
                                File file2 = (File) object2;
                                long result = file1.lastModified()
                                        - file2.lastModified();
                                if (result < 0) {
                                    return -1;
                                } else if (result > 0) {
                                    return 1;
                                } else {
                                    return 0;
                                }
                            }
                        });
                    } catch (Exception e) {
                        //StringManager.reportError(MoshiApp.context.getString(R.string.files_sort_error), e);
                    }
                }
                for (int i = 0; i < files.length - keep; i++) {
                    files[i].delete();
                }
            }
        } else if (file.isFile()) {
            file.delete();
        }
    }
    /**
     * 获取文件大小
     *
     * @param f
     * @return
     */
    public static long getFileSize(File f) {
        long size = 0;
        try {
            File[] listFiles = f.listFiles();
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    size = size + getFileSize(listFiles[i]);
                } else {
                    size = size + listFiles[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size / 1024 / 1024;
    }
    /**
     * 获取文件大小
     *
     * @param f
     * @return
     */
    public static long getFileSizeNotM(String f) {

        long fileSize = 0;

        try {

            File copyf = new File(f);       //copyPath 为目标文件的全路径  例如要将a/b/文件夹下的c.xml复制到其他处 a/b/c.xml

            FileInputStream fis = null;

            fis = new FileInputStream(copyf);

            fileSize = (long)fis.available();     //文件大小

        }catch(Exception e){

        }




//		long size = 0;
//		try {
//			File[] listFiles = f.listFiles();
//			for (int i = 0; i < listFiles.length; i++) {
//				if (listFiles[i].isDirectory()) {
//					size = size + getFileSize(listFiles[i]);
//				} else {
//					size = size + listFiles[i].length();
//				}
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
        return fileSize;
    }

    // /**
    // * 删除制定目录下的文件及目录
    // */
    // public static void deleteFolderFile(String filePath, boolean
    // deleteThisPath){
    // try{
    //
    // File file =new
    // File(Environment.getExternalStorageDirectory().getAbsolutePath()+NCE.getBaseDir()+filePath);
    // System.out.println("file--------------------->"+file);
    // if(file.isDirectory()){
    // File[] listFiles = file.listFiles();
    // for(int i =0; i<listFiles.length;i++){
    // System.out.println("file----------------2222"+listFiles[i].getAbsolutePath());
    // deleteFolderFile(listFiles[i].getAbsolutePath(), true);
    // }
    // }
    //
    // if(deleteThisPath){
    // if(!file.isDirectory()){
    // file.delete();
    // System.out.println("删了吗大哥");
    // }else{
    // if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
    // file.delete();
    // System.out.println("这删没用大哥");
    // }
    // }
    // }
    //
    //
    // }catch(Exception e){
    // e.printStackTrace();
    // }
    //
    //
    // }
    /**
     * 删除制定目录下的文件及目录
     */
    public static void delete(File file) {
        System.out.println("路径是---------------》" + file);
        if (file.isFile()) {
            file.delete();
            System.out.println("第一个if");
            return;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            System.out.println("第2个if");
            if (listFiles == null || listFiles.length == 0) {
                file.delete();
                System.out.println("第3个if");
                return;
            }
            for (int i = 0; i < listFiles.length; i++) {
                delete(listFiles[i]);
                System.out.println("第4个for");
            }
            file.delete();
        }
    }

    /**
     * 获取sd卡路径
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        if(sdDir==null){
            return "/sdcard/";
        }
        return sdDir.getPath();

    }

    public static boolean checkEnvironment(Activity act) {
        if (!FileUtils.isSdcardExist()) {
            Toast.makeText(act, R.string.check_no_sdcard, Toast.LENGTH_LONG).show();
            return false;
        }
        long availablesize = getUsableStorage();
        if (availablesize < 20) {
            Toast.makeText(act, R.string.sdcard_space_is_not_enough,
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    /*
     * 返回SD卡可用容量 --#
     */
    private static long getUsableStorage() {
        String sDcString = Environment.getExternalStorageState();

        if (sDcString.equals(android.os.Environment.MEDIA_MOUNTED)) {
            File pathFile = android.os.Environment
                    .getExternalStorageDirectory();

            android.os.StatFs statfs = new android.os.StatFs(pathFile.getPath());

            // 获取可供程序使用的Block的数量
            long nAvailaBlock = statfs.getAvailableBlocks();

            long nBlocSize = statfs.getBlockSize();

            // 计算 SDCard 剩余大小MB
            return nAvailaBlock * nBlocSize / 1024 / 1024;
        } else {
            return -1;
        }

    }

    public static String getSDDir() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/wyFile" + "/";
    }


    /**
     * 保存MP3文件.先从本地找,若没有,在下载
     *
     * @return
     *
     */
    public static File getMP3(String mp3) {
        File file = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath()
                + FileManager.sdCardDir
                + FileManager.downMp3);
        if (!file.getParentFile().exists())
            return null;
        // 读取文件

        // InputStream mp3 = other.FileManager.loadSDFile("");
        File file2 = null;
        try {
            file2 = new File(file, mp3);

            if (!file2.exists()) {
                // 没有
                long mb = FileManager.getFileSize(file);

                if (mb >= 20) {
                    FileManager.delSDFile(FileManager.downMp3, 10);
                }
                return null;
            }

        } catch (Exception e) {
            // 没有
            long mb = FileManager.getFileSize(file);

            if (mb >= 20) {
                FileManager.delSDFile(FileManager.downMp3, 10);
            }
            return null;
        }

        return file2;
    }


    /**
     * path  文件名
     * @return
     */
    public static File getOralDir(String path) {
        File dir = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + FileManager.sdCardDir +path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }

    /**
     * 判断文件是否存在
     * @return
     */
    public static boolean fileIsExists(String path){
        try{
            File f=new File(path);
            if(!f.exists()){
                return false;
            }

        }catch (Exception e) {
            // TODO: handle exception
            return false;
        }
        return true;
    }
    public static void copyFile2(String fromPath, String toPath){

        if(TextUtils.isEmpty(fromPath)) {
            return;
        }

        File fromFile = new File(fromPath);

        if (!fromFile.exists()) {//如果文件不存在则不必复制
            return;
        }

        File toFile = new File(toPath);
        if (!toFile.getParentFile().exists()) {
            toFile.getParentFile().mkdirs();
        }


        try {
            FileInputStream fis=new FileInputStream(fromFile);

            FileOutputStream fos=new FileOutputStream(toFile);

            byte[] data=new byte[1024];
            do{
                int len=fis.read(data);
                if(len==-1){
                    break;
                }
                fos.write(data,0,len);
            }while(true);

            fis.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //获取下载文件的大小
    public static long getDirectoryLength(File file){
        long sum=0;
        //如果是个null或不存在的
        if(file==null || !file.exists()){
            return 0;
        }

        //如果你给我传的是文件，直接返回文件的大小
        if(file.isFile()){
            return sum+file.length();
        }

        //如果是文件夹，应该算上所有子子孙孙的大小
        if(file.isDirectory()){
            //需要得到下级的文件对象（可能是文件可能是目录）
            File[] files=file.listFiles();
//			if(files!=null){
            for(File f:files){
                sum= sum + getDirectoryLength(f);
            }
            return sum;
        }

        return 0;

    }
    
    /**
     * 系统相册路径 /mizhua
     *
     * @return
     */
    public static String getAlbum() {
        return Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "mizhua" + File.separator;
    }
    
    /**
     * 保存到系统相册
     *
     * @param context
     * @param bitmap
     * @param name
     */
    public static void saveAlbum(final Context context, final Bitmap bitmap, final String name) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(FileManager.getAlbum(), name + ".jpg");
                File fileParent = file.getParentFile();
                if (!fileParent.exists()) {
                    // 文件夹不存在
                    fileParent.mkdirs();// 创建文件夹
                }
                String fileName = file.toString();
                try {
                    file.createNewFile();
                    L.e(file.getAbsolutePath());
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //通知相册更新
                MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, fileName, null);
                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri uri = Uri.fromFile(file);
                intent.setData(uri);
                context.sendBroadcast(intent);
                
            }
        }).start();
        
    }
    public static String getSave_temp(){
        return SDCARDPATH+sdCardCut;
    }
}
