/** 定义控制器层 */
app.controller('indexController', function($scope, baseService  , $interval , $location ,$controller){


    $scope.loginName="";
    /** 定义获取登录用户名方法 */
    $scope.showName = function(){
        baseService.sendGet("/user/showName")
            .then(function(response){
                $scope.loginName = response.data.loginName;
                $scope.findOrderByUserId($scope.loginName , 1);
                if (response.data.headPic != null && response.data.headPic != ''){
                    $scope.headPic = response.data.headPic;
                } else{
                    $scope.headPic = "img/_/photo.png";
                }
                $scope.showInfo();
            });
    };


    /** 分页查询 */
    $scope.searchParam = {page:1 , row:5};
    /** 查询用户的订单 */
    $scope.findOrderByUserId = function (userId , page) {
        baseService.sendGet("/user/findOrderByUserId?userId=" + userId + "&page=" + page + "&row=" + $scope.searchParam.row).then(function (response) {
            if (response.data){
                for (var x = 0; x < response.data.rows.length; x++){
                    response.data.rows[x].orderId = BigInt(response.data.rows[x].orderId);
                }
                $scope.resultMap = response.data;
                $scope.searchParam.page = page;
                $scope.selectPage = page;
                /** 总页数 */
                if($scope.resultMap.total%$scope.searchParam.row != 0){
                    $scope.totalPages = ($scope.resultMap.total / $scope.searchParam.row).toFixed(0);
                }else{
                    $scope.totalPages = ($scope.resultMap.total / $scope.searchParam.row);
                }
                $scope.setPage();
            }
        });
    };



    /** 页码 */
    $scope.setPage = function () {
        //起始页码
        var firstPage = 1;
        //结束页码
        var lastPage = $scope.totalPages;
        //初始化页码数组
        $scope.pageNum = [];
        //总页数大于5
        if($scope.totalPages > 5){
            if($scope.searchParam.page <= 3){
                lastPage = 5;
            }// 如果当前页码处于后面位置
            else if ($scope.searchParam.page >= $scope.totalPages - 3){
                firstPage = $scope.totalPages - 4;  // 生成后5页页码
            }else{ // 当前页码处于中间位置
                firstPage = $scope.searchParam.page - 2;
                lastPage = $scope.searchParam.page + 2;
            }

        }
        //设置页码数组(如果总页数大于5,先执行上面得设置起始页和结束页的页码,再执行下面的代码)
        for (var i = firstPage ;  i <= lastPage; i++){
            $scope.pageNum.push(i);
        }
    };

    /** 上一页下一页和输入框跳转*/
    $scope.addPage = function (addPage) {
        var inPage = parseInt(addPage);
        var page = $scope.searchParam.page + inPage;
        if(page >= 1 && page <= $scope.totalPages && page != $scope.searchParam.page){
            $scope.findOrderByUserId($scope.loginName , page);
        }
    };

    /** 输入框跳转 */
    $scope.jumpPage = function (seletePage) {
        var page = parseInt(seletePage);
        if(page >= 1 && page <= $scope.totalPages && page != $scope.searchParam.page){
            $scope.findOrderByUserId($scope.loginName , page);
        }
    };

    // 提交订单
    $scope.order = {userId:'',outTradeNo:'',totalFee:''};
    $scope.wxPay = function (loginName , outTradeNo , totalFee) {
        $scope.order.userId = loginName;
        $scope.order.orderId = outTradeNo.toString();
        $scope.order.payment = totalFee;
        // 发送异步请求
        baseService.sendPost("/user/saveOrder", $scope.order).then(function(response){
            // 获取响应数据
            if (response.data){
                /** 跳转 */
                $scope.getPayUserId();
            }else{
                alert("提交订单失败！");
            }
        });
    };

    $scope.getPayUserId = function () {
        location.href = "/order/pay.html?orderId=" + $scope.order.orderId;
    };


    // 生成微信支付二维码
    $scope.genPayCode = function () {
        baseService.sendGet("/user/genPayCode").then(function(response){
            // 获取响应数据  response.data : {outTradeNo: '', money : 1, codeUrl : ''}
            // 获取交易订单号
            $scope.outTradeNo = response.data.outTradeNo;
            // 获取支付金额
            $scope.money = (response.data.totalFee / 100).toFixed(2);
            /** 生成二维码 */
            var qr = new QRious({
                element : document.getElementById('qrious'),
                size : 250,
                level : 'H',
                value : response.data.codeUrl
            });


            // 开启定时器
            // 第一个参数：定时需要回调的函数
            // 第二个参数：间隔的时间毫秒数 3秒
            // 第三个参数：总调用次数 100
            var timer = $interval(function(){

                // 发送异步请求，获取支付状态
                baseService.sendGet("/user/queryPayStatus?outTradeNo="
                    + $scope.outTradeNo).then(function(response){
                    // 获取响应数据: response.data: {status : 1|2|3} 1:支付成功 2：未支付 3:支付失败
                    if (response.data.status == 1){// 支付成功
                        // 取消定时器
                        $interval.cancel(timer);
                        // 跳转到支付成功的页面
                        location.href = "/order/paysuccess.html?money=" + $scope.money;
                    }
                    if (response.data.status == 3){
                        // 取消定时器
                        $interval.cancel(timer);
                        // 跳转到支付失败的页面
                        location.href = "/order/payfail.html";
                    }
                });
            }, 5000, 100);

            // 总调用次数结束后，需要调用的函数
            timer.then(function(){
                // 关闭订单
                $scope.tip = "二维码已过期，刷新页面重新获取二维码。";
            });

        });
    };


    // 获取支付的总金额
    $scope.getMoney = function () {
        return $location.search().money;
    };



});