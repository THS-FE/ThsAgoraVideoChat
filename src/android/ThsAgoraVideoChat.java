package cn.com.ths.agora.videochat;

import android.content.ActivityNotFoundException;
import android.content.Intent;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import io.agora.utils.Constant;
import io.agora.activity.MessageActivity;
// import io.agora.tutorials1v1vcall.VideoChatViewActivity;

/**
 * This class echoes a string called from JavaScript.
 */
public class ThsAgoraVideoChat extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("startVideoChat")) {
            String agoraAppId = args.getString(0);
            String agoraAccessToken = args.getString(1);
            String channelName = args.getString(2);
            int uid = args.getInt(3);
            String pid = args.getString(4);
            String rtmToken = args.getString(5);
            this.startVideoChat(agoraAppId,agoraAccessToken,channelName,uid,pid,rtmToken, callbackContext);
            return true;
        }
        return false;
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
                Intent intent =new Intent(cordova.getActivity(), VideoChatViewActivity.class);
                intent.putExtra(Constant.AGORA_APP_ID_K,agoraAppId);
                intent.putExtra(Constant.AGORA_ACCESS_TOKEN_K,agoraAccessToken);
                intent.putExtra(Constant.CHANNEL_NAME_K,channelName);
                intent.putExtra(Constant.UID_K,uid);
                intent.putExtra(Constant.M_PEER_ID_K,pid);
                intent.putExtra(Constant.RTM_TOKEN_K,rtmToken);
                startActivity(intent);
            }catch (ActivityNotFoundException e){
                callbackContext.success("ActivityNotFoundException");
            }
            callbackContext.success("success");
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
