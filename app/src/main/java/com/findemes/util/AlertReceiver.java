package com.findemes.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.findemes.model.FrecuenciaEnum;

import java.util.Date;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        //Notificacion Push



        //


        //Programado del proximo alarm (si corresponde)

        //

    }

    public static Date proximaOcurrencia(Date date, FrecuenciaEnum frecuenciaEnum){

        Date ultimaOcurrencia=new Date();
        ultimaOcurrencia.setTime(date.getTime());

        switch(frecuenciaEnum){
            case SEMANAL: ultimaOcurrencia.setDate(ultimaOcurrencia.getDate()+7); break;
            case MENSUAL: ultimaOcurrencia.setMonth(ultimaOcurrencia.getMonth()+1); break;
            case ANUAL: ultimaOcurrencia.setYear(ultimaOcurrencia.getYear()+1); break;
            case DIARIO: ultimaOcurrencia.setDate(ultimaOcurrencia.getDate()+1); break;
            case BIMENSUAL: ultimaOcurrencia.setMonth(ultimaOcurrencia.getMonth()+2); break;
        }

        return ultimaOcurrencia;
    }

}
