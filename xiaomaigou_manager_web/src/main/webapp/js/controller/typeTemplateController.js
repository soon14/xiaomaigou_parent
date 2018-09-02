 // 控制层
 // 由于下拉列表中的品牌数据是在brandService实现的，所以需要引入brandService，一定要注意：在此处引入了brandService，那么在前端的页面中就一定要引入brandService.js，并且：特别注意，js引入的位置，要在typeTemplateController.js之前，因为该控制器要使用到它
 // 由于下拉列表中的规格数据是在specificationService实现的，所以需要引入specificationService，一定要注意：在此处引入了specificationService，那么在前端的页面中就一定要引入specificationService.js，并且：特别注意，js引入的位置，要在typeTemplateController.js之前，因为该控制器要使用到它
 app.controller('typeTemplateController',function($scope,$controller,typeTemplateService,brandService,specificationService){

    // 继承标准格式（加上这行代码，就可以实现继承的效果，其实是伪继承）
    //$controller也是angular提供的一个服务，可以实现伪继承，实际上就是与baseController共享$scope
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		typeTemplateService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		typeTemplateService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;//显示当前页数据
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		typeTemplateService.findOne(id).success(
			function(response){
				$scope.entity= response;

                //转换字符串为json对象（集合）
				//从数据库中查询出来的是字符串，我们必须将其转换为json对象才能实现信息的回显
                $scope.entity.brandIds=JSON.parse( $scope.entity.brandIds);
                $scope.entity.specIds=JSON.parse($scope.entity.specIds);
                $scope.entity.customAttributeItems = JSON.parse($scope.entity.customAttributeItems);

            }
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=typeTemplateService.update($scope.entity); //修改  
		}else{
			serviceObject=typeTemplateService.add($scope.entity);//增加 
		}
		//将数据$scope.entity以post的方式传到后台，其中entity的名字可以随便定义，但是必须与ng-model="entity">中绑定的名字一致		
		serviceObject.success(
			function(response){
				if(response.success){
                    alert(response.message);
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
            typeTemplateService.dele($scope.selectIds).success(
                function (response) {
                    if (response.success) {
                        alert(response.message);
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
		typeTemplateService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;//显示当前页数据
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //读取品牌下拉列表数据
    $scope.brandList={data:[]};//品牌列表
    // $scope.brandList={data:[{id:1,text:'联想'},{id:2,text:'华为'},{id:3,text:'小米'}]};//品牌列表
    //读取品牌下拉列表数据
    $scope.findBrandList=function(){
        brandService.selectOptionList().success(
            function(response){
                $scope.brandList={data:response};
            }
        );
    }

     //读取规格下拉列表数据
    $scope.specList={data:[]};//规格列表
    //读取列表
    $scope.findSpecList=function(){
        specificationService.selectOptionList().success(
            function(response){
                $scope.specList={data:response};
            }
        );
    }

    //增加扩展属性行
    $scope.addTableRow=function(){
        $scope.entity.customAttributeItems.push({});
    }
    //删除扩展属性行
    $scope.deleTableRow=function(index){
        $scope.entity.customAttributeItems.splice( index,1);
    }

});	
