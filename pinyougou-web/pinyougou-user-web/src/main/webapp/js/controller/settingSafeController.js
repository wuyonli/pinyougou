/** 定义控制器层 */
app.controller('settingSafeController', function ($scope, $controller,$timeout, $http, baseService) {

    /** 指定继承indexController */
    $controller('indexController', {$scope: $scope});

    // 定义user对象
    $scope.user = {};

    // 修改用户密码
    $scope.updatePassword = function () {
        // 判断两次密码是否一致
        if ($scope.user.okPassword && $scope.user.newPassword == $scope.user.okPassword) {
            // $http.get()发送异步请求
            $http.post("/user/updatePassword", {
                "userName": $scope.user.userName,
                "newPassword": $scope.user.newPassword
            })
                .then(function (response) { // 请求成功
                    if (response.data) {
                        // 清空表单数据
                        $scope.user = {};
                        $scope.user.userName = "";
                        $scope.user.newPassword = "";
                        $scope.user.okPassword = "";
                        alert("密码设置成功!");

                    } else {
                        alert("密码设置失败！");
                    }
                });
        } else {
            alert("两次密码不一致！");
        }

    }

    // 获取用户信息
    $scope.getUserInfo= function () {
        baseService.sendGet("/user/getUserInfo?userName=" +  $scope.loginName)
            .then(function(response){
                $scope.entity = response.data;
            });
    };

    //发送短信方法
    $scope.sendMsg = function () {
        $http.post("/user/sendMsg", {
            "inputCode": $scope.inputCode,
            "phone": $scope.entity.phone
        })
            .then(function (response) { // 请求成功
                if (response.data) {

                    // 调用倒计时方法
                    $scope.downcount(90);

                } else {
                    alert("发送短信失败！");
                }
            });
    }

    $scope.smsTip = "获取短信验证码";
    $scope.disabled = false;

    // 倒计时方法
    $scope.downcount = function (seconds) {

        seconds--;

        if (seconds >= 0){
            $scope.smsTip = seconds + "秒后，重新获取！";
            $scope.disabled = true;
            // 第一个参数：回调的函数
            // 第二个参数：间隔的时间毫秒数
            $timeout(function(){
                $scope.downcount(seconds);
            }, 1000);
        }else {
            $scope.smsTip = "获取短信验证码";
            $scope.disabled = false;
        }

    };


    $scope.msgCodeVerify = function () {
        $http.post("/user/msgCodeVerify", {
            "phone": $scope.entity.phone,
            "msgCode": $scope.msgCode,
        })
            .then(function (response) { // 请求成功
                if (response.data) {

                    alert("验证成功");
                  location.href="home-setting-address-phone.html";

                } else {
                    alert("验证失败！");
                }
            });
    }

});