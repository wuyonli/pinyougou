/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){

    $scope.loginName="";
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
                if (response.data.headPic != null && response.data.headPic != ''){
                    $scope.headPic = response.data.headPic;
                } else{
                    $scope.headPic = "img/_/photo.png";
                }
                $scope.showInfo();
            });
    };



});