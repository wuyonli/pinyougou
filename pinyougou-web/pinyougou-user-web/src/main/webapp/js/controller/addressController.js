/** 定义控制器层 */
app.controller('addressController', function($scope, baseService){
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
            });
    };
    //查询所有地址
    $scope.findAll = function () {
        baseService.sendGet("/address/findAll").then(function (response) {
            if (response.data) {
                $scope.entityList = response.data;
            } else {
                alert("暂无数据");
            }
        });
    };

    $scope.entity = {};

    //添加或修改
    $scope.saveOrUpdate = function() {
        var url = 'save';
        if($scope.entity.id){
            url = 'update';
        }
        baseService.sendPost("/address/" + url, $scope.entity).then(function (response) {
            if(response.data){
                alert("操作成功");
                $scope.findAll();
            }else {
                alert("操作失败");
            }
        });
    };

    //查询省
    $scope.findProvinces = function () {
        baseService.sendGet("/provinces/findAll").then(function (response) {
            $scope.provinceList = response.data;
        });
    };
    //监控provinceId
    $scope.$watch('entity.provinceId', function (newVal,oldVal) {
        if(newVal){
            $scope.findCitys(newVal);
        }else{
            $scope.cityList = [];
        }
    });
    //监控cityId
    $scope.$watch('entity.cityId', function (newVal, oldVal) {
        if (newVal) {
            $scope.towns(newVal);
        } else {
            $scope.areaList = [];
        }
    });

    //查询市
    $scope.findCitys = function (newVal) {
        baseService.sendGet("/citys/findByProvinceId?provinceId=" + newVal).then(function (response) {
            $scope.cityList = response.data;
        });
    };
    //查询县区
    $scope.towns = function (newVal) {
        baseService.sendGet("/areas/findByCityId?cityId=" + newVal).then(function (response) {
            $scope.areaList = response.data;
        });
    };
    //点击更换地址别名
    $scope.selectAlias = function (ev) {
        var alias = ev.target.innerText;
        $scope.entity.alias = alias;
    };
    //回显
    $scope.show = function (entity) {
        var s = JSON.stringify(entity);
        $scope.entity = JSON.parse(s);
    };
    //删除
    $scope.delete = function (addressId) {
        baseService.sendGet("/address/delete?id=" + addressId).then(function (response) {
            if (response.data) {
                alert("操作成功");
                $scope.findAll();
            } else {
                alert("操作失败");
            }
        });
    };
    //修改默认地址
    $scope.changer =function (id, isDefault) {
        baseService.sendGet("/address/changer?id=" + id + "&isDefault=" + isDefault).then(function (response) {
            if (response.data) {
                alert("修改成功");
                $scope.findAll();
            }else{
                alert("操作失败");
            }
        });
    }

});