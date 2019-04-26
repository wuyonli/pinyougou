/** 定义控制器层 */
app.controller('indexController', function($scope, baseService){

    $scope.loginName="";
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
                $scope.headPic = response.data.headPic;
            });
    };



});