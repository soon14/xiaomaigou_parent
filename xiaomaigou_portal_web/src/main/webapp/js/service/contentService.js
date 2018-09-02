app.service('contentService' ,function($http){
	
	//根据广告分类ID查询广告
	this.findByCategoryId=function(categoryId){
		//注意：此处的页面index.html在webapp下，所以不需要向其他服务那样前面添加../
		return $http.get('content/findByCategoryId.do?categoryId='+categoryId);
	}
	
});