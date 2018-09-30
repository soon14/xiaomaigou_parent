app.controller("itemController",function($scope,$http){
	
	$scope.specificationItems={};//存储用户选择的规格
	
	//数量加减
	$scope.addNum=function(x){
		$scope.num+=x;
		if($scope.num<1){
			$scope.num=1;
		}		
	}
	
	//用户选择规格
	$scope.selectSpecification=function(key,value){
		$scope.specificationItems[key]=value;		
		searchSku();//查询SKU
	}
	
	//判断某规格是否被选中
	$scope.isSelected=function(key,value){
		if($scope.specificationItems[key]==value){
			return true;
		}else{
			return false;
		}	
	}
	
	$scope.sku={};//当前选择的SKU
	
	//加载默认SKU
	$scope.loadSku=function(){
		$scope.sku=skuList[0];
		$scope.specificationItems= JSON.parse(JSON.stringify($scope.sku.spec)) ;
	}
	
	//匹配两个对象是否相等
	matchObject=function(map1,map2){
		
		for(var k in map1){
			if(map1[k]!=map2[k]){
				return false;
			}			
		}
		// 必须是相等相关，不能是包含关系，所有需要判断两次
		for(var k in map2){
			if(map2[k]!=map1[k]){
				return false;
			}			
		}		
		return true;
		
	}
	
	//根据规格查询sku（用户选择商品规格后调用查询其价格等信息）
	searchSku=function(){
		
		for(var i=0;i<skuList.length;i++){
			 if(matchObject( skuList[i].spec ,$scope.specificationItems)){
				 $scope.sku=skuList[i];
				 return ;
			 }			
		}
		$scope.sku={id:0,title:'-----',price:0};
	}
	
	//添加商品到购物车
	$scope.addToCart=function(){
		// alert('SKUID:'+$scope.sku.id );

        $http.get('http://192.168.199.190:9991/xiaomaigou_cart_web/cart/addGoodsToCartList.do?itemId='+$scope.sku.id+'&num='+$scope.num ,{'withCredentials':true}).success(
            function(response){
                if(response.success){
                    location.href='http://192.168.199.190:9991/xiaomaigou_cart_web/cart.html';
                }else{
                    alert(response.message);
                }
            }
        );

	}

    //搜索（传递参数）并跳转到搜索页
    $scope.search=function(){
        location.href="http://192.168.199.190:9095/xiaomaigou_search_web/search.html#?keywords="+$scope.keywords;

    }

});