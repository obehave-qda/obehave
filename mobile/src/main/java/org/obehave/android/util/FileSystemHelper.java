package org.obehave.android.util;

import android.content.Context;

import java.io.*;

/**
 * Created by patrick on 07.03.2015.
 */
public class FileSystemHelper {

    public static File createFolderOnInternalStorage(String folder, Context context){
        File directory = context.getDir(folder, Context.MODE_PRIVATE);
        return directory;
    }


    public static boolean copyFile(File src, File dst) throws IOException {
        if(src.getAbsolutePath().toString().equals(dst.getAbsolutePath().toString())){
            return true;
        }else{
            InputStream is=new FileInputStream(src);
            OutputStream os=new FileOutputStream(dst);
            byte[] buff=new byte[1024];
            int len;
            while((len=is.read(buff))>0){
                os.write(buff,0,len);
            }
            is.close();
            os.close();
        }
        return true;
    }
}
