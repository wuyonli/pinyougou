/** 定义控制器层 */
app.controller('settingSafeController', function ($scope, $controller, $http, baseService) {

    /** 指定继承baseController */
    $controller('indexController', {$scope: $scope});

    // 定义user对象
    $scope.user = {};


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
                    } else {
                        alert("密码设置失败！");
                    }
                });
        } else {
            alert("两次密码不一致！");
        }

    }

});