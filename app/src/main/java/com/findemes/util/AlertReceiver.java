package com.findemes.util;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.findemes.R;
import com.findemes.activities.EditarMovimientoActivity;
import com.findemes.model.Categoria;
import com.findemes.model.FrecuenciaEnum;
import com.findemes.model.Movimiento;
import com.findemes.room.MyDatabase;

import java.io.File;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.ContextCompat.getSystemService;

public class AlertReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {

        System.out.println("AlertReceiver: Broadcast received");

        //Notificacion

        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
            this.rebootAlarms(context,intent);
            return;
        }

        //Se crea el channel
        Integer id = intent.getIntExtra("Id",-1);
        System.out.println(id);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            if(id!=-1){
                NotificationChannel channel = new NotificationChannel("Recordatorios", "Recordatorios", importance);
                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }


        boolean gasto = intent.getBooleanExtra("Gasto",true);
        String tipo;
        if(gasto) tipo=context.getResources().getString(R.string.gasto); else tipo=context.getResources().getString(R.string.ingreso);
        Double monto = intent.getDoubleExtra("Monto",0);
        String descripcion = intent.getStringExtra("Descripcion");
        String titulo = intent.getStringExtra("Titulo");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,"Recordatorios")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle("Recordatorio - "+tipo)
                .setContentText(titulo+" - $"+monto)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(titulo+"\nMonto: $"+monto+"\n"+descripcion))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mBuilder.setColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);
        mBuilder.setVibrate(new long[] {0,500});


        if(intent.getStringExtra("PhotoPath")!=null){

            String absolutePhotoPath = intent.getStringExtra("PhotoPath");

            File photoFile = new File(absolutePhotoPath);

            if(photoFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());

                mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(myBitmap).bigLargeIcon(null));
                mBuilder.setLargeIcon(myBitmap);

            }

        }



        Intent editarIntent = new Intent(context, EditarMovimientoActivity.class);
        editarIntent.putExtra("Id",id);
        editarIntent.putExtra("Access","Notificacion");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(editarIntent);

        PendingIntent editarPendingIntent = stackBuilder.getPendingIntent(id,PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.addAction(R.drawable.ic_edit, context.getResources().getString(R.string.editar),editarPendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id,mBuilder.build());


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
                        if(movimiento.getPhotoPath()!=null)intentNuevo.putExtra("PhotoPath",movimiento.getPhotoPath());

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,id, intentNuevo, 0);

                        alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                recordatorio.getTime(),
                                //TEST
                                /*new Date().getTime()+30000,*/
                                pendingIntent);

                    }
                }
            }).start();

        }

        //

    }


    //Recibe una fecha de inicio, y calcula la proxima ocurrencia de recordatorio segun la frecuencia establecida
    public static Date proximaOcurrencia(Date date, FrecuenciaEnum frecuenciaEnum){

        Date today=new Date();
        Date ultimaOcurrencia=new Date();
        ultimaOcurrencia.setTime(date.getTime());

        do {

            switch (frecuenciaEnum) {
                case SEMANAL:
                    ultimaOcurrencia.setDate(ultimaOcurrencia.getDate() + 7);
                    break;
                case MENSUAL:
                    ultimaOcurrencia.setMonth(ultimaOcurrencia.getMonth() + 1);
                    break;
                case ANUAL:
                    ultimaOcurrencia.setYear(ultimaOcurrencia.getYear() + 1);
                    break;
                case DIARIO:
                    ultimaOcurrencia.setDate(ultimaOcurrencia.getDate() + 1);
                    break;
                case BIMENSUAL:
                    ultimaOcurrencia.setMonth(ultimaOcurrencia.getMonth() + 2);
                    break;
            }

        } while (ultimaOcurrencia.before(today));

        return ultimaOcurrencia;
    }

    private void rebootAlarms(final Context context, final Intent intent) {

        //Cuando el telefono se reinicia se reprograman todas las alarmas para los movimientos
        new Thread(new Runnable() {
            @Override
            public void run() {

                final List<Movimiento> movimientos = MyDatabase.getInstance(context).getMovimientoDAO().getAll();

                for(Movimiento mov : movimientos){

                    if(mov != null){

                        if(mov.getFrecuenciaEnum()!=null && mov.isRecordatorio()){

                            Date recordatorio = AlertReceiver.proximaOcurrencia(mov.getFechaInicio(),mov.getFrecuenciaEnum());

                            if(recordatorio.before(mov.getFechaFinalizacion())){

                                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                                Intent intentNuevo = new Intent(context, AlertReceiver.class);

                                intentNuevo.putExtra("Titulo",mov.getTitulo());
                                intentNuevo.putExtra("Monto",mov.getMonto());
                                intentNuevo.putExtra("Descripcion",mov.getDescripcion());
                                intentNuevo.putExtra("Gasto",mov.isGasto());
                                intentNuevo.putExtra("Id",mov.getId());
                                intentNuevo.putExtra("FechaInicio",mov.getFechaInicio());
                                intentNuevo.putExtra("FechaFinalizacion",mov.getFechaFinalizacion());
                                intentNuevo.putExtra("Frecuencia",mov.getFrecuenciaEnum());
                                if(mov.getPhotoPath()!=null)intentNuevo.putExtra("PhotoPath",mov.getPhotoPath());

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,mov.getId(), intentNuevo, 0);

                                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                                        recordatorio.getTime(),
                                        //TEST
                                        /*new Date().getTime()+30000,*/
                                        pendingIntent);

                            }

                        }

                    }

                }

            }
        }).start();

    }

}
