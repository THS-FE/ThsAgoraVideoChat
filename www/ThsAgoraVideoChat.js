var exec = require('cordova/exec');

exports.startVideoChat = function (agoraAppId,agoraAccessToken,channelName,uid, success, error) {
    exec(success, error, 'ThsAgoraVideoChat', 'startVideoChat', [agoraAppId,agoraAccessToken,channelName,uid]);
};
