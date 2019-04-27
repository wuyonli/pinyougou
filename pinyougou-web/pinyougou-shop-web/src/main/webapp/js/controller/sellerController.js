/** 定义控制器层 */
app.controller('sellerController', function($scope, $controller, baseService){

    /** 指定继承baseController */
    $controller('baseController',{$scope:$scope});

    /*查询用户信息*/
    $scope.findAll=function () {
      baseService.sendGet("/seller/findOne").then(function(response){
          $scope.seller=response.data;
      });


    };

    /** 修改或添加 */
    $scope.saveOrUpdate= function(){
        var url="save";
        if($scope.seller.sellerId){
            url="update";
        }
        /** 发送post请求 */
        baseService.sendPost("/seller/"+url, $scope.seller)
            .then(function(response){
                if (response.data){
                    /** 跳转到登录页面 */
                    $scope.reload();
                    alert("保存成功");
                }else{
                    alert("操作失败");
                }
            });
    };





    /** 查询条件对象 */
    $scope.searchEntity = {};
    /** 分页查询(查询条件) */
    $scope.search = function(page, rows){
        baseService.findByPage("/seller/findByPage", page,
			rows, $scope.searchEntity)
            .then(function(response){
                /** 获取分页查询结果 */
                $scope.dataList = response.data.rows;
                /** 更新分页总记录数 */
                $scope.paginationConf.totalItems = response.data.total;
            });
    };

    /** 显示修改 */
    $scope.show = function(entity){
       /** 把json对象转化成一个新的json对象 */
       $scope.entity = JSON.parse(JSON.stringify(entity));
    };

    /** 批量删除 */
    $scope.delete = function(){
        if ($scope.ids.length > 0){
            baseService.deleteById("/seller/delete", $scope.ids)
                .then(function(response){
                    if (response.data){
                        /** 重新加载数据 */
                        $scope.reload();
                    }else{
                        alert("删除失败！");
                    }
                });
        }else{
            alert("请选择要删除的记录！");
        }
    };

    /*修改用户的密码*/
    $scope.updatePassword=function(pwd,newPwd,newPwd2){
            if(newPwd==newPwd2){
                baseService.sendPost("/seller/updatePassword?pwd="+pwd+"&newPwd="+newPwd).then(function(response){
                    if(response.data){
                            /** 跳转到登录页面 */
                            location.href = "/shoplogin.html";
                        }else{
                            alert("操作失败！");
                        }
                });
            }else{
                alert("两次输入的密码不相同,请重新输入")
            }
    };
    /*对修改密码进行重置*/
    $scope.reset=function(){
        $scope.pwd=[];
        $scope.newPwd=[];
        $scope.newPwd2=[];
    };

});