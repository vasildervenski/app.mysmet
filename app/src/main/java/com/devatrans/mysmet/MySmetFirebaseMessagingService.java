package com.devatrans.mysmet;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

public class MySmetFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        //Bundle bundle = remoteMessage.getData();

        sendNotification(remoteMessage);
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
////                // For long-running tasks (10 seconds or more) use WorkManager.
////                scheduleJob();
////            } else {
////                // Handle message within 10 seconds
////                handleNow();
////            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());

        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        MainActivity.prefConfig.writeFCMToken(token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        //sendRegistrationToServer(token, id);
    }

    private void scheduleJob() {
        // [START dispatch_job]
        OneTimeWorkRequest work = new OneTimeWorkRequest.Builder(MyWorker.class)
                .build();
        //WorkManager.getInstance().beginWith(work).enqueue();
        WorkManager.getInstance(this).beginWith(work).enqueue();
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param tkn The new token.
     */
    public void sendRegistrationToServer(String tkn, String id, Integer version) {
        // Get User Id
        //Integer id = MainActivity.prefConfig.readId();
        Log.d("ReadId" , id);
        Log.d("ReadToken", tkn);

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), id);
        RequestBody token = RequestBody.create(MediaType.parse("text/plain"), tkn);
        RequestBody app_version = RequestBody.create(MediaType.parse("text/plain"), version.toString());
        RequestBody user_agent = RequestBody.create(MediaType.parse("text/plain"), "Android");

        HashMap<String, RequestBody> map = new HashMap<>();
        map.put("id", user_id);
        map.put("token", token);
        map.put("user_agent", user_agent);
        map.put("app_version", app_version);

        Call<ResponseBody> call = MainActivity.apiInterface.sendToken(map);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(!response.isSuccessful()) {
                    MainActivity.prefConfig.displayToast("Няма отговор от сървъра");
                    //return;
                }
                //if(response.body().getSuccess().equals("ok")) {
                //    //MainActivity.prefConfig.displayToast("Token saved successfully");
                //}
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MainActivity.prefConfig.displayToast("Error Token " + t.getMessage());
            }
        });



    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param message FCM message body received.
     */
    private void sendNotification(RemoteMessage message) {

        Intent webViewIntent = new Intent(this, MainActivity.class);
        webViewIntent.putExtra("order_id", message.getData().get("order_id"));
        webViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(webViewIntent);

        Log.d("Message", message.toString());
        //Intent intent = new Intent(this, MainActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //
        //intent.putExtra("order_id", message.getData().get("order_id"));
        //
        String messageBody = message.getNotification().getBody();
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, webViewIntent,
                PendingIntent.FLAG_ONE_SHOT);
        //
        String channelId = getString(R.string.default_notification_channel_id);
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setContentTitle(message.getNotification().getTitle())
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        //.setSound(defaultSoundUri)
                        //.setPriority(Notification.IMPORTANCE_HIGH)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "New Orders Channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1 /* ID of notification */, notificationBuilder.build());
    }
}
