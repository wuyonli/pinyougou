// 定义基础的控制器
app.controller('baseController', function ($scope, baseService) {

    // 获取登录用户名
    $scope.loadUsername = function () {

        // 对请求URL进行unicode编码
        $scope.redirectUrl = window.encodeURIComponent(location.href);

        // 发送异步请求
        baseService.sendGet("/user/showName").then(function(response){
            // 获取响应数据
            $scope.userName = response.data.loginName;
        });
    };
});