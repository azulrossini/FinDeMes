package com.findemes.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.findemes.model.FrecuenciaEnum;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;

import java.util.Date;
import java.util.List;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        //Notificacion Push

            Toast.makeText(context,"receiver",Toast.LENGTH_SHORT);

        //


        //Programado del proximo alarm (si corresponde)
        FrecuenciaEnum frecuenciaEnum = FrecuenciaEnum.values()[intent.getIntExtra("Frecuencia",0)];
        Date fechaFinalizacion = (Date)intent.getSerializableExtra("FechaFinalizacion");
        final Date recordatorio = AlertReceiver.proximaOcurrencia(new Date(),frecuenciaEnum);

        if(recordatorio.before(fechaFinalizacion)){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    int id = intent.getIntExtra("Id",-1);
                    if (id==-1) return;

                    List<Movimiento> movimientos = MyDatabase.getInstance(context).getMovimientoDAO().get(id);

                    if(!movimientos.isEmpty()){

                        Movimiento movimiento = movimientos.get(0);

                        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                        Intent intentNuevo = new Intent(context, AlertReceiver.class);

                        intentNuevo.putExtra("Titulo",movimiento.getTitulo());
                        intentNuevo.putExtra("Monto",movimiento.getMonto());
                        intentNuevo.putExtra("Descripcion",movimiento.getDescripcion());
                        intentNuevo.putExtra("Gasto",movimiento.isGasto());
                        intentNuevo.putExtra("Id",id);
                        intentNuevo.putExtra("FechaInicio",movimiento.getFechaInicio());
                        intentNuevo.putExtra("FechaFinalizacion",movimiento.getFechaFinalizacion());
                        intentNuevo.putExtra("Frecuencia",movimiento.getFrecuenciaEnum());

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id, intentNuevo, 0);

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, recordatorio.getTime(), pendingIntent);

                    }
                }
            }).start();


        }

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
