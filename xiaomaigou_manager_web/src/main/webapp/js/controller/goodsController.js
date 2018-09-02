// 控制层
app.controller('goodsController', function ($scope, $controller, goodsService,itemCatService) {

    // 继承标准格式（加上这行代码，就可以实现继承的效果，其实是伪继承）
    //$controller也是angular提供的一个服务，可以实现伪继承，实际上就是与baseController共享$scope
    $controller('baseController', {$scope: $scope});//继承

    //读取列表数据绑定到表单中  
    $scope.findAll = function () {
        goodsService.findAll().success(
            function (response) {
                $scope.list = response;
            }
        );
    }

    //分页
    $scope.findPage = function (page, rows) {
        goodsService.findPage(page, rows).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //查询实体
    $scope.findOne = function (id) {
        goodsService.findOne(id).success(
            function (response) {
                $scope.entity = response;
            }
        );
    }

    //保存
    $scope.save = function () {
        var serviceObject;//服务层对象
        if ($scope.entity.id != null) {//如果有ID
            serviceObject = goodsService.update($scope.entity); //修改
        } else {
            serviceObject = goodsService.add($scope.entity);//增加
        }
        //将数据$scope.entity以post的方式传到后台，其中entity的名字可以随便定义，但是必须与ng-model="entity">中绑定的名字一致
        serviceObject.success(
            function (response) {
                if (response.success) {
                    alert(response.message);
                    //重新查询
                    $scope.reloadList();//重新加载
                } else {
                    alert(response.message);
                }
            }
        );
    }

    //批量删除，由于delete是关键字，所以此处使用dele
    $scope.dele = function () {
        if (confirm('确定要删除吗？')) {
            goodsService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        alert(response.message);
                        $scope.reloadList();//刷新列表
                        $scope.selectIds = [];
                    } else {
                        alert(response.message);
                    }
                }
            );
        }
    }

    //定义搜索对象
    $scope.searchEntity = {};
    //搜索
    $scope.search = function (page, rows) {
        //混合提交，如果是普通的参数，仍然可以使用page='+page这种形式	
        goodsService.search(page, rows, $scope.searchEntity).success(
            function (response) {
                $scope.list = response.rows;//显示当前页数据
                $scope.paginationConf.totalItems = response.total;//更新总记录数
            }
        );
    }

    //用于转换状态
    $scope.status=['未审核','审核通过','审核未通过','已关闭'];

    $scope.itemCatList=[];//商品分类列表
    //查询商品所有分类列表
    $scope.findItemCatList=function(){
        itemCatService.findAll().success(
            function(response){
                for(var i=0;i<response.length;i++){
                    //因为需要根据分类ID得到分类名称，所以将返回的查询结果以数组形式再次封装
                    $scope.itemCatList[response[i].id]=response[i].name;
                }
            }
        );

    }

    //更新状态
    $scope.updateStatus=function(status){
        goodsService.updateStatus( $scope.selectIds ,status).success(
            function(response){
                if(response.success){
                    alert(response.message);
                    $scope.reloadList();//刷新页面
                    $scope.selectIds=[];//清空勾选集合
                }else{
                    alert(response.message);
                }
            }
        );
    }

});	
