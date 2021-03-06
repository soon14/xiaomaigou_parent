 // 控制层 
app.controller('contentCategoryController',function($scope,$controller,contentCategoryService){	

    // 继承标准格式（加上这行代码，就可以实现继承的效果，其实是伪继承）
    //$controller也是angular提供的一个服务，可以实现伪继承，实际上就是与baseController共享$scope
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		contentCategoryService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		contentCategoryService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;//显示当前页数据
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		contentCategoryService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=contentCategoryService.update($scope.entity); //修改  
		}else{
			serviceObject=contentCategoryService.add($scope.entity);//增加 
		}
		//将数据$scope.entity以post的方式传到后台，其中entity的名字可以随便定义，但是必须与ng-model="entity">中绑定的名字一致		
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
    //批量删除，由于delete是关键字，所以此处使用dele
    $scope.dele = function () {
        if (confirm('确定要删除吗？')) {
            contentCategoryService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        $scope.reloadList();//刷新列表
                        $scope.selectIds=[];
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
	$scope.search=function(page,rows){
        //混合提交，如果是普通的参数，仍然可以使用page='+page这种形式	
		contentCategoryService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;//显示当前页数据
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
    
});	
