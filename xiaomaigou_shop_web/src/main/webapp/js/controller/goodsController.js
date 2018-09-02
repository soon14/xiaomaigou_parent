// 商品控制层（商家后台）
// 如果需要使用静态页面之间传参，则需要引入$location服务
app.controller('goodsController' ,function($scope,$controller,$location,goodsService,uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}

    //查询实体
    $scope.findOne=function(){
        //获取参数值,search()方法会获取页面上传过来的所有参数，并且获取的方式是数组
        //注意：地址书写格式如：
        // http://localhost:9102/admin/goods_edit.html#?id=149187842867969
        // 注意： ?前要加# ，则是angularJS的地址路由的书写形式
        var id=$location.search()['id'];
        if(id==null){
            return ;
        }
        goodsService.findOne(id).success(
            function(response){

                $scope.entity= response;

                editor.html($scope.entity.goodsDesc.introduction );//商品介绍
                //商品图片
                $scope.entity.goodsDesc.itemImages=JSON.parse($scope.entity.goodsDesc.itemImages);
                //扩展属性
                $scope.entity.goodsDesc.customAttributeItems=JSON.parse($scope.entity.goodsDesc.customAttributeItems);
                //规格选择
                $scope.entity.goodsDesc.specificationItems= JSON.parse($scope.entity.goodsDesc.specificationItems);
                //转换sku列表中的规格对象

                for(var i=0;i< $scope.entity.itemList.length;i++ ){
                    $scope.entity.itemList[i].spec=  JSON.parse($scope.entity.itemList[i].spec);
                }

            }
        );
    }

    //保存
    $scope.save=function(){
        // 获取富文本编辑器中的内容作为商品介绍
        $scope.entity.goodsDesc.introduction=editor.html();

        var serviceObject;//服务层对象
        if($scope.entity.goods.id!=null){//如果有ID
            serviceObject=goodsService.update( $scope.entity ); //修改
        }else{
            serviceObject=goodsService.add( $scope.entity  );//增加
        }
        serviceObject.success(
            function(response){
                if(response.success){
                    alert(response.message);
                    //保存成功后需要清空entity实体的内容
                    $scope.entity={};
                    //清空富文本编辑器
                    editor.html("");
                    //跳转到商品列表页面
                    location.href='goods.html';

                }else{
                    alert(response.message);
                }
            }
        );
    }

	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}
		);
	}

    //上传图片
    $scope.uploadFile=function(){
        uploadService.uploadFile().success(function(response) {
            if(response.success){//如果上传成功，取出url
                $scope.image_entity.url=response.message;//设置文件地址
            }else{
                alert(response.message);
            }
        }).error(function() {
            alert("上传发生错误");
        });
    };

    //上传图片后的保存（还没保存到数据库）
    $scope.entity={ goodsDesc:{itemImages:[],specificationItems:[]}  };//定义页面实体结构
    //将当前上传的图片实体添加图片列表
    $scope.add_image_entity=function(){
        $scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //移除图片
    $scope.remove_image_entity=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //查询一级商品分类列表
    $scope.selectItemCat1List=function(){

        itemCatService.findByParentId(0).success(
            function(response){
                $scope.itemCat1List=response;
            }
        );

    }

    //查询二级商品分类列表($watch表示变量监控方法，当变量值发生变化时将触发这个方法，自动触发，变量自动传入)
    $scope.$watch('entity.goods.category1Id',function(newValue,oldValue){

        $scope.itemCat2List={};
        $scope.itemCat3List={};

        itemCatService.findByParentId(newValue).success(
            function(response){
                $scope.itemCat2List=response;
            }
        );

    });

    //查询三级商品分类列表
    $scope.$watch('entity.goods.category2Id',function(newValue,oldValue){
        $scope.itemCat3List={};

        itemCatService.findByParentId(newValue).success(
            function(response){
                $scope.itemCat3List=response;
            }
        );

    });

    //读取模板ID
    $scope.$watch('entity.goods.category3Id',function(newValue,oldValue){

        itemCatService.findOne(newValue).success(
            function(response){
                $scope.entity.goods.typeTemplateId=response.typeId;
            }
        );
    });

    //读取模板ID后，读取品牌列表 扩展属性  规格列表
    $scope.$watch('entity.goods.typeTemplateId',function(newValue,oldValue){
        typeTemplateService.findOne(newValue).success(
            function(response) {
                $scope.typeTemplate = response;// 模板对象

                $scope.typeTemplate.brandIds = JSON.parse($scope.typeTemplate.brandIds);//品牌列表类型转换
                //扩展属性
                if ($location.search()['id'] == null) {//如果是增加商品
                    $scope.entity.goodsDesc.customAttributeItems = JSON.parse($scope.typeTemplate.customAttributeItems);
                }
            }
        );
        //根据用户选择的模板id读取规格列表及规格选项
        typeTemplateService.findSpecList(newValue).success(
            function(response){
                $scope.specList=response;
            }
        );

    });

    $scope.updateSpecAttribute=function($event,name,value){

        var object= $scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems ,'attributeName', name);

        if(object!=null){
            if($event.target.checked ){
                object.attributeValue.push(value);
            }else{//取消勾选
                object.attributeValue.splice( object.attributeValue.indexOf(value ) ,1);//移除选项
                //如果选项都取消了，将此条记录移除
                if(object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
                }

            }
        }else{
            $scope.entity.goodsDesc.specificationItems.push({"attributeName":name,"attributeValue":[value]});
        }

    }

    //创建SKU列表（非常难）
    $scope.createItemList=function(){

        $scope.entity.itemList=[{spec:{},price:0,num:99999,status:'0',isDefault:'0'} ];//列表初始化

        var items= $scope.entity.goodsDesc.specificationItems;

        for(var i=0;i<items.length;i++){
            $scope.entity.itemList= addColumn($scope.entity.itemList, items[i].attributeName,items[i].attributeValue);
        }

    }
//这里没加$scope表示该方法只在本控制器有效（局部方法），页面中无法调用，若需要在页面中调用则需要加$scope
    addColumn=function(list,columnName,columnValues){

        var newList=[];
        for(var i=0;i< list.length;i++){
            var oldRow=  list[i];
            for(var j=0;j<columnValues.length;j++){
                var newRow=  JSON.parse( JSON.stringify(oldRow));//深克隆
                newRow.spec[columnName]=columnValues[j];
                newList.push(newRow);
            }
        }
        return newList;
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

    //判断规格与规格选项是否应该被勾选
    $scope.checkAttributeValue=function(specName,optionName){
        var items= $scope.entity.goodsDesc.specificationItems;
        var object =$scope.searchObjectByKey( items,'attributeName', specName);

        if(object!=null){
            if(object.attributeValue.indexOf(optionName)>=0){//如果能够查询到规格选项
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

});	
