//广告控制层
app.controller('contentController',function($scope,contentService){

    //所有广告列表（小标为categoryId，这样可以少定义很多list）
	$scope.contentList=[];
	
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
			function(response){
				$scope.contentList[categoryId]=response;
			});
	}

    //搜索（传递参数）并跳转到搜索页
    $scope.search=function(){
        location.href="http://192.168.199.190:9095/xiaomaigou_search_web/search.html#?keywords="+$scope.keywords;

    }
	
});