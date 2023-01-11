package com.drdistributor.dr;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class Notification_Service extends FirebaseMessagingService {
    public static final String ANDROID_CHANNEL_ID = "com.drdistributor.dr";
    //private Noti mNotificationUtils;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String id = remoteMessage.getData().get("id");
        String title = remoteMessage.getData().get("title");
        String message = remoteMessage.getData().get("message");
        String funtype = remoteMessage.getData().get("funtype");
        String itemid = remoteMessage.getData().get("itemid");
        String division = remoteMessage.getData().get("division");
        String company_full_name = remoteMessage.getData().get("company_full_name");
        String image = remoteMessage.getData().get("image");

        //Toast.makeText(this, "hellog", Toast.LENGTH_SHORT).show();

        message = message.replace("<br>", "\n");
        message = message.replace("<br/>", "\n");
        message = message.replace("<br />", "\n");
        /*message = message.replace("<b>", "*");
        message = message.replace("</b>", "*");*/

        if (funtype.equals("100")) {

        } else {
            addNotification(Integer.parseInt(id), title, message, funtype, itemid, division, company_full_name, image);
        }
    }

    private void addNotification(int myid, String title, String message, String funtype, String itemid, String division, String company_full_name, final String image) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            contentView.setImageViewResource(R.id.image, R.drawable.logo);
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.text,Html.fromHtml(message));

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(R.drawable.logo) //set icon for notification
                            .setContent(contentView)//this is notification message
                            .setAutoCancel(true) // makes auto cancel of notification
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(soundUri);

            Intent notificationIntent = null;
            if(funtype.equals("0")) {
                notificationIntent = new Intent(this, My_notification.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("1")) {
                notificationIntent = new Intent(this, Medicine_details.class);
                notificationIntent.putExtra("item_code",itemid);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("2")) {
                notificationIntent = new Intent(this, Medicine_category.class);
                notificationIntent.putExtra("item_code",itemid);
                notificationIntent.putExtra("item_division",division);
                notificationIntent.putExtra("item_page_type","featured_brand");
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("3")) {
                notificationIntent = new Intent(this, Track_order_page.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("4")) {
                notificationIntent = new Intent(this, My_order.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("5")) {
                notificationIntent = new Intent(this, My_invoice.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            if(image.equals("not")) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(myid, builder.build());
            }
            else{
                //Handle image url if present in the push message
                String attachmentUrl = image;

                if (attachmentUrl != null) {
                    Bitmap image1 = getBitmapFromURL(attachmentUrl);
                    if (image != null) {
                        builder.setStyle(new
                                NotificationCompat.BigPictureStyle().bigPicture(image1));
                    }
                }

                final Notification notification = builder.build();
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    notification.bigContentView = contentView;
                }

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(myid, builder.build());
            }

        } else {
            RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification_layout);
            contentView.setImageViewResource(R.id.image, R.drawable.logo);
            contentView.setTextViewText(R.id.title, title);
            contentView.setTextViewText(R.id.text, Html.fromHtml(message));

            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(this)
                            .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                            .setSmallIcon(R.drawable.logo) //set icon for notification
                            .setContent(contentView)//this is notification message
                            .setAutoCancel(true) // makes auto cancel of notification
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSound(soundUri)
                            .setPriority(Notification.PRIORITY_DEFAULT); //set priority of notification


            Intent notificationIntent = null;
            if(funtype.equals("0")) {
                notificationIntent = new Intent(this, My_notification.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("1")) {
                notificationIntent = new Intent(this, Medicine_details.class);
                notificationIntent.putExtra("item_code",itemid);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("2")) {
                notificationIntent = new Intent(this, Medicine_category.class);
                notificationIntent.putExtra("item_code",itemid);
                notificationIntent.putExtra("item_division",division);
                notificationIntent.putExtra("item_page_type","featured_brand");
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("3")) {
                notificationIntent = new Intent(this, Track_order_page.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("4")) {
                notificationIntent = new Intent(this, My_order.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            if(funtype.equals("5")) {
                notificationIntent = new Intent(this, My_invoice.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);

            if(image.equals("not")) {
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(myid, builder.build());
            }
            else{
                //Handle image url if present in the push message
                String attachmentUrl = image;

                if (attachmentUrl != null) {
                    Bitmap image1 = getBitmapFromURL(attachmentUrl);
                    if (image != null) {
                        builder.setStyle(new
                                NotificationCompat.BigPictureStyle().bigPicture(image1));
                    }
                }

                final Notification notification = builder.build();
                if (android.os.Build.VERSION.SDK_INT >= 16) {
                    notification.bigContentView = contentView;
                }

                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                manager.notify(myid, builder.build());
            }
        }
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}
