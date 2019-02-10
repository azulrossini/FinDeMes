package com.findemes.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class PhotoFileCreator {

    public static File createTempPhotoFile(Context ctx){

        String name = "temp";
        File storageDir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=null;
        try {
            image = File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

    public static File createPermanentPhotoFile(Context ctx, int id){


        String name = "fdm_mov_photo_"+String.valueOf(id);
        File storageDir = ctx.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image=null;
        try {
            image = File.createTempFile(name,".jpg",storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }

}
