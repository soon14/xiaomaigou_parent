// 公共基本控制层
app.controller('baseController', function ($scope) {

    // 重新加载列表数据
    $scope.reloadList = function () {
        //切换页码
        $scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }

    // 分页控件配置
    // currentPage：当前页码
    // totalItems:总条数
    // itemsPerPage:
    // perPageOptions：页码选项
    // onChange：更改页面时触发事件，在加载paginationConf控件的时候会触发onChange方法：更改页面时触发事件，所以无需ng-init="" 调用
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        // onChange：更改页面时触发事件，在加载paginationConf控件的时候会触发onChange方法：更改页面时触发事件，所以无需ng-init="" 调用
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    $scope.selectIds = [];//用户勾选的ID集合
    //更新复选框
    $scope.updateSelection = function ($event, id) {
        if ($event.target.checked) {
            //选中
            $scope.selectIds.push(id);//push向集合添加元素
        } else {
            //取消选中
            var index = $scope.selectIds.indexOf(id);//查找值的位置
            $scope.selectIds.splice(index, 1);//参数1：移除的位置 参数2：移除的个数
        }
    }

    //提取json字符串数据中某个属性，返回拼接字符串，使用逗号分隔
    $scope.jsonToString=function(jsonString,key){
        var json=JSON.parse(jsonString);//将json字符串转换为json对象
        var value="";
        for(var i=0;i<json.length;i++){
            if(i>0){
                value+=","
            }
            value+=json[i][key];
        }
        return value;
    }

});