var exec = require('cordova/exec');

exports.startVideoChat = function (agoraAppId,agoraAccessToken,channelName,uid,pid,rtmToken, success, error) {
    /**
     * 打开聊天信息页面
     * agoraAppId 应用的唯一标识ID
     * agoraAccessToken  频道访问权限token
     * channelName  频道名称
     * uid 当前用户id
     * pid 单聊对方id
     * rtmToken 消息系统登录认证token
     */
    exec(success, error, 'ThsAgoraVideoChat', 'startVideoChat', [agoraAppId,agoraAccessToken,channelName,uid,pid,rtmToken]);
};
