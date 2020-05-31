package nativespeak.app;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import nativespeak.app.data.response.MessageListResponse;
import nativespeak.app.ui.splash.SplashActivity;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage.getData().size() > 0) { // Data mesajı içeriyor mu
            //Uygulama arkaplanda veya ön planda olması farketmez. Her zaman çağırılacaktır.
            //Gelen içerik json formatındadır.
            JSONObject jObject = new JSONObject(remoteMessage.getData());
            try {
                Log.i("NOTTYPE", "msaj");
                int messageSenderID = jObject.getInt("messageSenderID");
                int messageReceiverID = jObject.getInt("messageReceiverID");
                String messageText = jObject.getString("messageText");
                String translateMessageText = jObject.getString("translateMessageText");
                String messageSendHour = jObject.getString("messageSendHour");
                MessageListResponse response = new MessageListResponse("0", messageText, translateMessageText, String.valueOf(messageReceiverID), String.valueOf(messageSenderID));
                if (isAppIsInBackground(this)) {
                    sendNotificationMessage(response);
                } else {
                    Intent intent = new Intent("UpdateChatActivity");
                    intent.putExtra("msg", response);
                    sendBroadcast(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
            if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                for (String activeProcess : processInfo.pkgList) {
                    if (activeProcess.equals(context.getPackageName())) {
                        isInBackground = false;
                    }
                }
            }
        }

        return isInBackground;
    }

    private void sendNotificationMessage(MessageListResponse message) {

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra("messageSenderId", message.getSenderUserId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setStyle(new NotificationCompat.BigTextStyle())
                        .setGroupSummary(true)
                        .setSmallIcon(R.drawable.ic_toys_black_24dp)
                        .setContentTitle("Yeni bir mesaj aldınız.")
                        .setContentText(message.getTranslate_text())
                        .setGroup(message.getSenderUserId() + "")
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setPriority(Notification.PRIORITY_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder.setCategory(Notification.CATEGORY_MESSAGE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    getString(R.string.default_notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(Integer.valueOf(message.getSenderUserId()), notificationBuilder.build());
    }


    @Override
    public void onNewToken(String s) {
        Log.e("NEW_TOKEN", s);
    }

}