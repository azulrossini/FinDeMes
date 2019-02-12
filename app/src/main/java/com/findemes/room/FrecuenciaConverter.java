package com.findemes.room;

import android.arch.persistence.room.TypeConverter;

import com.findemes.model.FrecuenciaEnum;

public class FrecuenciaConverter {

    @TypeConverter
    public static FrecuenciaEnum getFrecuenciaEnum(int num) {
        for(FrecuenciaEnum f : FrecuenciaEnum.values()){
            if(f.ordinal()==num){
                return f;
            }
        }
        return null;
    }

    @TypeConverter
    public static int toInt(FrecuenciaEnum frecuenciaEnum) {
        if(frecuenciaEnum == null) return -1;
        return frecuenciaEnum.ordinal();
    }

}
