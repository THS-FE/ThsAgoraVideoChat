package cn.com.ths.agora.videochat;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            this.startVideoChat(agoraAppId,agoraAccessToken,channelName, callbackContext);
            return true;
        }
        return false;
    }

    private void startVideoChat(String agoraAppId,String agoraAccessToken,String channelName, CallbackContext callbackContext) {
        if (agoraAppId != null && agoraAppId.length() > 0) {
            callbackContext.success(agoraAppId);
        } else {
            callbackContext.error("Expected one non-empty string argument.");
        }
    }
}
