package cn.com.ths.agora.videochat;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import io.agora.rtmtutorial.AGApplication;
import io.agora.rtmtutorial.ChatManager;
import io.agora.utils.Constant;
import io.agora.activity.MessageActivity;
import io.agora.utils.MessageUtil;
// import io.agora.tutorials1v1vcall.VideoChatViewActivity;

/**
 * This class echoes a string called from JavaScript.
 */
public class ThsAgoraVideoChat extends CordovaPlugin {
    private ChatManager mChatManager;
    private RtmClient mRtmClient;
    private MsgRevBd msgRevBd;
    private static ThsAgoraVideoChat instance;
    public ThsAgoraVideoChat() {
        instance = this;
    }
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        mChatManager = AGApplication.the().getChatManager();
        mRtmClient = mChatManager.getRtmClient();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = cordova.getActivity().getPackageName();
            PowerManager pm = (PowerManager) cordova.getActivity().getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                try {
                    //some device doesn't has activity to handle this intent
                    //so add try catch
                    Intent intent = new Intent();
                    intent.setAction(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                    intent.setData(Uri.parse("package:" + packageName));
                    cordova.getActivity().startActivity(intent);
                } catch (Exception e) {
                }
            }
        }
        msgRevBd =new MsgRevBd();
        LocalBroadcastManager.getInstance(cordova.getActivity()).registerReceiver(msgRevBd, new IntentFilter(Constant.NEW_MSG_REV));
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("init")){
            String agoraAppId = args.getString(0);
            String agoraAccessToken = args.getString(1);
            String channelName = args.getString(2);
            int uid = args.getInt(3);
            String pid = args.getString(4);
            String rtmToken = args.getString(5);
            String uidname = args.getString(6);
            String centername = args.getString(7);

            Constant.AGORA_APP_ID = agoraAppId;
            Constant.AGORA_ACCESS_TOKEN = agoraAccessToken;
            Constant.CHANNEL_NAME = channelName;
            Constant.UID = uid;
            Constant.RTM_TOKEN = rtmToken;
            Constant.UID_NAME = uidname;
            Constant.ID_NAME_MAP.put(Constant.UID+"",Constant.UID_NAME );
            Constant.ID_NAME_MAP.put(pid,centername);

            // 登录系统
            doLogin();
        }else if (action.equals("startVideoChat")) {
//            String agoraAppId = args.getString(0);
//            String agoraAccessToken = args.getString(1);
//            String channelName = args.getString(2);
//            int uid = args.getInt(3);
            String pid = args.getString(0);
//            String rtmToken = args.getString(5);
//            this.startVideoChat(agoraAppId,agoraAccessToken,channelName,uid,pid,rtmToken, callbackContext);
            startVideoChat(pid);
            return true;
        }
        return false;
    }
   private void startVideoChat(String pid){
       Intent intent =new Intent(cordova.getActivity(), MessageActivity.class);

       intent.putExtra(Constant.AGORA_APP_ID_K,Constant.AGORA_APP_ID);

       intent.putExtra(Constant.AGORA_ACCESS_TOKEN_K,Constant.AGORA_ACCESS_TOKEN);

       intent.putExtra(Constant.CHANNEL_NAME_K,Constant.CHANNEL_NAME);

       intent.putExtra(Constant.UID_K,Constant.UID);

       intent.putExtra(Constant.M_PEER_ID_K,pid);

       intent.putExtra(Constant.RTM_TOKEN_K,Constant.RTM_TOKEN);

       intent.putExtra(Constant.UID_NAME_K,Constant.UID_NAME);
       intent.putExtra(Constant.CENTER_NAME_K,Constant.ID_NAME_MAP.get(pid));
       cordova.getActivity().startActivity(intent);
   }
    /**
     * 启动视频聊天
     * @param agoraAppId agora注册应用唯一标识
     * @param agoraAccessToken   agora 访问鉴权的token
     * @param channelName  信道名称
     * @param callbackContext 回调
     */
    private void startVideoChat(String agoraAppId,String agoraAccessToken,String channelName,int uid,String pid,String rtmToken, CallbackContext callbackContext) {
        if (agoraAppId != null && agoraAppId.length() > 0) {
            try {
                // Intent intent =new Intent(cordova.getActivity(), VideoChatViewActivity.class);
                // intent.putExtra(VideoChatViewActivity.AGORA_APP_ID,agoraAppId);
                // intent.putExtra(VideoChatViewActivity.AGORA_ACCESS_TOKEN,agoraAccessToken);
                // intent.putExtra(VideoChatViewActivity.CHANNEL_NAME,channelName);
                // intent.putExtra(VideoChatViewActivity.UID,uid);
                // cordova.getActivity().startActivity(intent);
                Intent intent =new Intent(cordova.getActivity(), MessageActivity.class);
                intent.putExtra(Constant.AGORA_APP_ID_K,agoraAppId);
                intent.putExtra(Constant.AGORA_ACCESS_TOKEN_K,agoraAccessToken);
                intent.putExtra(Constant.CHANNEL_NAME_K,channelName);
                intent.putExtra(Constant.UID_K,uid);
                intent.putExtra(Constant.M_PEER_ID_K,pid);
                intent.putExtra(Constant.RTM_TOKEN_K,rtmToken);
                cordova.getActivity().startActivity(intent);
            }catch (ActivityNotFoundException e){
                callbackContext.success("ActivityNotFoundException");
            }
            callbackContext.success("success");
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(msgRevBd!=null){
            LocalBroadcastManager.getInstance(cordova.getActivity()).unregisterReceiver(msgRevBd);
        }
    }

    class  MsgRevBd extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
          String msg = intent.getStringExtra("msg");
          sendMsg(msg,"revNewMsg");
        }
    }

    /**
     * 发送消息到
     * @param data
     * @param methodStr
     */
    private  void sendMsg(String data,String methodStr){
        // cordova.plugins.ThsAgoraVideoChat
        String format = "cordova.plugins.ThsAgoraVideoChat."+methodStr+"InAndroidCallback(%s);";
        final String js = String.format(format, "'"+data+"'");
        cordova.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                instance.webView.loadUrl("javascript:" + js);
            }
        });
    }

    /**
     * API CALL: login RTM server
     */
    private void doLogin() {
        mRtmClient.login(Constant.RTM_TOKEN, Constant.UID+"", new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void responseInfo) {
                Log.i("doLogin", Constant.UID + "login success ");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                 Log.i("doLogin", "login failed: " + errorInfo.getErrorCode());

            }
        });
    }
}
