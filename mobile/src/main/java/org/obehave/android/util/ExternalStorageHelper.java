package org.obehave.android.util;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

public class ExternalStorageHelper {

    public static void createFolderIfNotExists(final String folder) throws IOException {
        if(!exists(folder) || !isDirectory(folder)) {
            createFolder(folder);
        }
    }

    public static boolean exists(final String folder){
        File f = new File(Environment.getExternalStorageDirectory() + "/" + folder);
        return f.exists();

    }

    public static boolean isDirectory(final String folder){
        File f = new File(Environment.getExternalStorageDirectory() + "/" + folder);
        return f.isDirectory();
    }

    public static void createFolder(final String folder) throws IOException {
        File f = new File(Environment.getExternalStorageDirectory() + "/" + folder);
        if(!f.mkdir()){
            throw new IOException("Folder could not been created");
        }
    }

    public static File[] listFiles(final String folder) {
        File f = new File(Environment.getExternalStorageDirectory() + "/" + folder);
        return f.listFiles();
    }

    public static File[] listFiles(final String folder, final String extension) {
        File f = new File(Environment.getExternalStorageDirectory() + "/" + folder);
        return f.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.toLowerCase().endsWith(extension);
            }
        });
    }
}
