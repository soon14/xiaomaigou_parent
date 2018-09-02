//广告控制层
app.controller('contentController',function($scope,contentService){

    //所有广告列表（小标为categoryId，这样可以少定义很多list）
	$scope.contentList=[];
	
	$scope.findByCategoryId=function(categoryId){
		contentService.findByCategoryId(categoryId).success(
			function(response){
				$scope.contentList[categoryId]=response;
			}
		);		
	}
	
});