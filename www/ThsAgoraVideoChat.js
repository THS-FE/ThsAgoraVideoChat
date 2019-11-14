var exec = require('cordova/exec');
exports.init = function (agoraAppId,agoraAccessToken,channelName,uid,pid,rtmToken, success, error) {
    /**
     * 打开聊天信息页面
     * agoraAppId 应用的唯一标识ID
     * agoraAccessToken  频道访问权限token
     * channelName  频道名称
     * uid 当前用户id  注意类型数字
     * pid 单聊对方id  注意类型字符串
     * rtmToken 消息系统登录认证token
     * uidname 当前登录人姓名 
     * centername 指挥中心人员名字
     */
    exec(success, error, 'ThsAgoraVideoChat', 'init', [agoraAppId,agoraAccessToken,channelName,uid,pid,uidname,centername,rtmToken]);
};

exports.startVideoChat = function (pid, success, error) {
    /**
     * 打开聊天信息页面 ，更新参数，只传递pid
     * pid 单聊对方id  注意类型字符串
     */
    exec(success, error, 'ThsAgoraVideoChat', 'startVideoChat', [pid]);
};

//收到消息刷新UI界面
exports.revNewMsgInAndroidCallback = function(data) {

   cordova.fireDocumentEvent('agora.revNewMsg', data);
};

