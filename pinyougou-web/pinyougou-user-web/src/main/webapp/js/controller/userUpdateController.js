/** 定义控制器层 */
app.controller('userUpdateController', function ($scope, $timeout, $controller, baseService) {

    /** 指定继承indexController */
    $controller('indexController', {$scope: $scope});

    // 定义user对象
    $scope.user = {};

    /** 注册提交 */
    // ?username="+$scope.loginName+"&nickName="+$scope.user.nickName+"&sex="+$scope.user.sex
    $scope.saveOrUpdate = function () {
        $scope.user.username = $scope.loginName;
        if ($scope.user.job == "") {
            alert("请选择职业");
        } else {
            baseService.sendPost("/userUpdate/updateNickName", $scope.user)
                .then(function (response) {
                    if (response.data) {
                        // location.href = "/home-index.html";
                    } else {
                        alert("保存失败!");
                    }
                })
        }
    };


    /** 文件上传 */
    $scope.uploadFile = function () {
        //调用服务层上传文件
        baseService.uploadFile().then(function (response) {
            //获取响应数据
            if (response.data.status == 200) {
                //获取图片url
                alert(response.data.url);
                $scope.user.headPic = response.data.url;
                alert("最后一步");
            } else {
                alert("上传失败!");
            }
        })
    };


    /** 根据父级id查询省份 */
    $scope.findProvinceByParentId = function(id,name){
      baseService.sendGet("/userUpdate/findProvinceByParentId?id="+id)
          .then(function (response) {
              //获取响应数据
              $scope[name] = response.data;
          })
    };



























    /** 用户注册 */
    $scope.save = function () {

        // 判断两次密码是否一致
        if ($scope.okPassword && $scope.user.password == $scope.okPassword) {
            // 发送异步请求
            baseService.sendPost("/user/save?code=" + $scope.code, $scope.user)
                .then(function (response) {
                    if (response.data) {
                        // 清空表单数据
                        $scope.user = {};
                        $scope.okPassword = "";
                        $scope.code = "";
                    } else {
                        alert("注册失败！");
                    }
                });

        } else {
            alert("两次密码不一致！");
        }
    };


    // 发送短信验证码
    $scope.sendSmsCode = function () {

        // 判断手机号码
        if ($scope.user.phone && /^1[3|4|5|7|8]\d{9}$/.test($scope.user.phone)) {
            // 发送异步请求
            baseService.sendGet("/user/sendSmsCode?phone=" + $scope.user.phone)
                .then(function (response) {
                    if (response.data) {
                        // 调用倒计时方法
                        $scope.downcount(90);

                    } else {
                        alert("发送失败！");
                    }
                });
        } else {
            alert("手机号码格式不正确！")
        }
    };


    $scope.smsTip = "获取短信验证码";
    $scope.disabled = false;

    // 倒计时方法
    $scope.downcount = function (seconds) {

        seconds--;

        if (seconds >= 0) {
            $scope.smsTip = seconds + "秒后，重新获取！";
            $scope.disabled = true;
            // 第一个参数：回调的函数
            // 第二个参数：间隔的时间毫秒数
            $timeout(function () {
                $scope.downcount(seconds);
            }, 1000);
        } else {
            $scope.smsTip = "获取短信验证码";
            $scope.disabled = false;
        }

    };

});