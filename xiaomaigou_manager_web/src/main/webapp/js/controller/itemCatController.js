 // 控制层 
app.controller('itemCatController',function($scope,$controller,itemCatService){	

    // 继承标准格式（加上这行代码，就可以实现继承的效果，其实是伪继承）
    //$controller也是angular提供的一个服务，可以实现伪继承，实际上就是与baseController共享$scope
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;//显示当前页数据
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update($scope.entity); //修改  
		}else{
			serviceObject=itemCatService.add($scope.entity);//增加 
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
            brandService.dele($scope.selectIds).success(
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
		itemCatService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;//显示当前页数据
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //根据上级分类ID查询列表
    $scope.findByParentId=function(parentId){
        itemCatService.findByParentId(parentId).success(
            function(response){
                $scope.list=response;
            }
        );
    }

    // 面包屑导航
    $scope.grade=1;//当前级别
    //设置级别
    $scope.setGrade=function(value){
        $scope.grade=value;
    }

    $scope.selectList=function(p_entity){

        if($scope.grade==1){
            $scope.entity_1=null;
            $scope.entity_2=null;
        }else if($scope.grade==2){

            $scope.entity_1=p_entity;
            $scope.entity_2=null;
        }else if($scope.grade==3){
            $scope.entity_2=p_entity;
        }

        $scope.findByParentId(p_entity.id);

    }
    
});	
