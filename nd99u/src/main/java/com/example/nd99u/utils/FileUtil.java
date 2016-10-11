package com.example.nd99u.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hjx
 * @date 7/1/2015
 * @time 11:57
 * @description
 */
public final class FileUtil {

    private static final String TAG = "FileUtil";

    private FileUtil() {
    }

    /**
     * 判断SD卡是否存在 <br>
     * Created 2014-8-22 下午5:42:24
     *
     * @return SD卡存在返回true，否则返回false
     * @author huangyx
     */
    public static boolean isSdCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 返回SD卡跟目录 <br>
     * Created 2014-8-22 下午5:41:22
     *
     * @return SD卡跟目录
     * @author huangyx
     */
    public static String getSdCardPath() {
        File sdDir;
        boolean sdCardExist = isSdCardExist(); // 判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
            return sdDir.toString();
        } else {
            return null;
        }
    }

    /**
     * 根据用户ID返回用户在SD卡上的目录
     *
     * @return
     */
    public static String getAppCachePath() {
        return FileUtil.getSdCardPath() + File.separator + "nd99u_auto";
    }


    public static final boolean createDirsIfNecessary(String path) {
        File file = new File(path);
        return createDirsIfNecessary(file);
    }

    public static final boolean createDirsIfNecessary(File file) {
        try {
            if (file == null) return false;

            if (!file.exists() || !file.isDirectory()) {
                file.mkdirs();
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取指定目录下的所有文件的文件名（不包含子目录，以及子目录下的文件）
     *
     * @param dir
     * @return
     */
    public static List<String> getAllSubFiles(String dir) {
        List<String> result = new ArrayList<>();
        File dirFile = new File(dir);
        File[] subFiles = dirFile.listFiles();

        if (subFiles == null) {
            return result;
        }

        for (int i = 0; i < subFiles.length; i++) {
            if (!subFiles[i].isDirectory()) {
                result.add(subFiles[i].getName());
            }
        }

        return result;
    }

    public static final String getFileSuffix(String fileName) {
        int idx = fileName.lastIndexOf(".");
        return fileName.substring(idx, fileName.length());
    }

    public static final String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }

    public static final String getFileDir(String filePath) {
        File file = new File(filePath);
        return file.getParent();
    }

    public static final String getMainFileName(String fileName) {
        int idx = fileName.lastIndexOf(".");
        return fileName.substring(0, idx);
    }

    public static final boolean isFileExists(String filePath) {
        try {
            File file = new File(filePath);
            return file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    /**
     * 文件复制类
     *
     * @param srcFile  源文件
     * @param destFile 目标文件
     * @return boolean
     */
    public static boolean copy(String srcFile, String destFile) {
        FileInputStream in = null;
        FileOutputStream out = null;
        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int c;
            while ((c = in.read(bytes)) != -1) {
                out.write(bytes, 0, c);
            }
            out.flush();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Copy exception-->", e);
            return false;
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != out) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Delete file(file or folder).
     *
     * @param file
     */
    public static void delete(File file) {
        if (file == null) {
            return;
        }
        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            if (f.isDirectory()) {
                delete(f);
            } else {
                f.delete();
            }
        }
        file.delete();
    }

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i].getAbsolutePath()); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        } else {
            Log.d(TAG, "file no exists");
        }
    }


    /**
     * 删除path 下全部文件
     *
     * @param path
     * @return true删除成功
     */
    public static boolean delAllFile(String path) {
        return delAllFile(path, null);
    }

    /**
     * 删除文件夹内所有文件
     *
     * @param path
     * @param filenameFilter 过滤器 支持null
     * @return boolean
     */
    public static boolean delAllFile(String path, FilenameFilter filenameFilter) {
        boolean flag = false;

        File file = new File(path);
        if (!file.exists()) return true;
        if (!file.isDirectory()) return false;

        File[] tempList = file.listFiles(filenameFilter);
        int length = tempList.length;
        for (int i = 0; i < length; i++) {

            if (tempList[i].isFile()) {
                tempList[i].delete();
            }

            if (tempList[i].isDirectory()) {
                /**
                 * 删除内部文件
                 */
                delAllFile(tempList[i].getAbsolutePath(), filenameFilter);
                /**
                 * 删除空文件夹
                 */
                String[] ifEmptyDir = tempList[i].list();
                if (ifEmptyDir == null || ifEmptyDir.length <= 0) {
                    tempList[i].delete();
                }
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 删除文件夹
     *
     * @param folderPath
     */
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容

            File f = new File(folderPath);
            f.delete(); // 删除空文件夹

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 删除文件夹下每个子文件夹的某个目录
     *
     * @param folderPath 文件路径
     * @param folderName 子文件夹名称
     */
    public static void delSubFilterFolder(String folderPath, final String folderName) {
        File file = new File(folderPath);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }

        File[] tempList = file.listFiles();

        if (tempList == null) {
            return;
        }

        for (File subFile : tempList) {
            if (subFile.isDirectory()) {
                File[] subFolders = subFile.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String filename) {

                        return TextUtils.isEmpty(folderName) || folderName.equals(filename);
                    }
                });

                if (subFolders != null && subFolders.length > 0) {
                    for (File mFile : subFolders) {
                        delFolder(mFile.getAbsolutePath());
                    }
                }
            }
        }

        return;
    }

    /**
     * 计算文件的 SHA-256 值
     *
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[8192];
        int len;
        try {
            in = new FileInputStream(file);
            digest = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
            BigInteger bigInt = new BigInteger(1, digest.digest());
            return bigInt.toString(16);
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "getFileSha256 Exception-->", e);
            return null;
        } catch (FileNotFoundException e) {
            Log.e(TAG, "getFileSha256 Exception-->", e);
            return null;
        } catch (IOException e) {
            Log.e(TAG, "getFileSha256 Exception-->", e);
            return null;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "getFileSha256 Exception-->", e);
            }
        }
    }
}
