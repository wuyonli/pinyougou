// 定义购物车的控制器
app.controller('cartController', function ($scope, $controller, baseService) {

    // 继承baseController
    $controller('baseController', {$scope : $scope});

    // 查询购物车
    $scope.findCart = function () {
        baseService.sendGet("/cart/findCart").then(function(response){
            // 获取响应数据
            $scope.carts = response.data;

            // 初始化 checkedArr
            for (var i = 0 ;i < $scope.carts.length;i ++ ){
                $scope.checkedArr.push([]);
                $scope.ids.push([]);
            }



            // 定义json对象封装统计的数据
            $scope.totalEntity = {totalNum : 0, totalMoney : 0};
            // 迭代用户的购物车集合
            for (var i = 0; i < $scope.carts.length; i++){
                // 获取商家的购物车
                var cart = $scope.carts[i];
                // 迭代商家购物车中的商品
                for (var j = 0; j < cart.orderItems.length; j++){
                    // 获取购买的商品
                    var orderItem = cart.orderItems[j];

                    // 统计购买数量
                    $scope.totalEntity.totalNum += orderItem.num;
                    // 统计总金额
                    $scope.totalEntity.totalMoney += orderItem.totalFee;

                }
            }
        });
    };

    // 添加商品到购物车
    $scope.addCart = function (itemId, num) {
        baseService.sendGet("/cart/addCart?itemId="
            + itemId + "&num=" + num).then(function(response){
            // 获取响应数据
            if (response.data){
                // 重新查询购物车
                $scope.findCart();
            }
        });
    };

///////////////////////////////////////////////////////////////////////////////////////////////
// 存放被选中的商品
    $scope.ids=[];
    // 存放被选中的商品
    $scope.checkedArr=[];
    // 商家商品是否全选
    $scope.ckAll=[];
// 购物车集合商品是否全选
    $scope.gwcAll=false;


    // 全选商家购物车
    $scope.checkAllItem= function (cartNum,$event) {
                //清空ids
                 $scope.ids[cartNum]=[];
            // 获取商家的购物车
            var cart = $scope.carts[cartNum];
        // 迭代商家购物车中的商品
        for (var j = 0; j < cart.orderItems.length; j++){

            // 初始化数组
            $scope.checkedArr[cartNum][j] = $event.target.checked;
            // 获取购买的商品
            var orderItem = cart.orderItems[j];

            // 判断是否选中
            if ($event.target.checked) {
                // 添加到 ids
                $scope.ids[cartNum][j] = orderItem;
                // 计算购物车集合中确定要购买的商品的总价格 和总数量
                calculateTotalAndCount();

            }else {
                // 计算购物车集合中确定要购买的商品的总价格 和总数量
                calculateTotalAndCount();
            }
            // 重新赋值 ,再次绑定 checkbox
            $scope.ckAll[cartNum] = cart.orderItems.length == $scope.ids[cartNum].length;
        }


        // 判断是否购物车全选
        if( $scope.ckAll.length == $scope.carts.length  ){
            // 判断每个商家的购物车的商品是否都被选中了
            for (var i = 0 ; i < $scope.ckAll.length; i ++){
                if ($scope.ckAll[i] == false){
                    $scope.gwcAll = false;
                    return;
                }else {
                    $scope.gwcAll = true;
                }
            }
        }


    };

    // 为单选checkbox绑定点击事件
    $scope.updateSelection = function(cartNum,$event, oderItem, i){

        var cart = $scope.carts[cartNum];
        // 判断checkbox是否选中 dom
        // $event.target: dom
        if ($event.target.checked){ // 选中
            // 往数组中添加元素
            $scope.ids[cartNum].push(oderItem);

            // 计算购物车集合中 确定要购买的商品的总价格 和总数量
            calculateTotalAndCount();

        }else { // 没有选中
            // 得到该元素在数组中的索引号
            var idx = $scope.ids[cartNum].indexOf(oderItem);
            // 删除数组元素
            $scope.ids[cartNum].splice(idx, 1);

            // 计算购物车集合中 确定要购买的商品的总价格 和总数量
            calculateTotalAndCount();
        }
        // 重新赋值，再次绑定checkbox
        $scope.checkedArr[cartNum][i] = $event.target.checked;
        // 让商家商品全选是否选中,再次绑定checkbox
        $scope.ckAll[cartNum] = cart.orderItems.length == $scope.ids[cartNum].length;


        // 判断是否购物车全选
        if( $scope.ckAll.length == $scope.carts.length  ){
            // 判断每个商家的购物车的商品是否都被选中了
            for (var i = 0 ; i < $scope.ckAll.length; i ++){
                if ($scope.ckAll[i] == false){
                    $scope.gwcAll = false;
                    return;
                }else {
                    $scope.gwcAll = true;
                }
            }
        }


    };

    //  全选整个购物车集 方法
    $scope.checkAllcarts= function ($event) {
        for (var i = 0 ;i < $scope.carts.length; i++){
            if ($event.target.checked){
                $scope.checkAllItem(i,$event);

            }else {
                $scope.checkAllItem(i,$event);

            }
        }

        // 判断是否购物车全选
        if( $scope.ckAll.length == $scope.carts.length  ){
            // 判断每个商家的购物车的商品是否都被选中了
            for (var i = 0 ; i < $scope.ckAll.length; i ++){
                if ($scope.ckAll[i] == false){
                    $scope.gwcAll = false;
                    return;
                }else {
                    $scope.gwcAll = true;
                }
            }
        }


    };

    // 算出 购物车集合中 选中的所有商品总金额
    var calculateTotalAndCount = function () {
        $scope.totalEntity.totalNum = 0;
        $scope.totalEntity.totalMoney= 0 ;

        for (var i = 0 ;i < $scope.carts.length; i++){
            if ($scope.ids[i].length > 0) {
                for (var j = 0; j < $scope.ids[i].length; j++) {
                    // 确定购买 商品总数
                    $scope.totalEntity.totalNum += $scope.ids[i][j].num;
                    $scope.totalEntity.totalMoney += $scope.ids[i][j].totalFee;

                }

            }

        }
    }

});