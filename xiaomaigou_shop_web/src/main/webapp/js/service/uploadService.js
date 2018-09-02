// 文件上传服务层
app.service('uploadService',function($http){

    //上传文件
    this.uploadFile=function(){
        var formdata=new FormData();
        formdata.append('file',file.files[0]);//file.files[0]中的file是文件上传框的name

        //anjularjs对于post和get请求默认的Content-Type header 是application/json。通过设置‘Content-Type’: undefined，这样浏览器会帮我们把Content-Type 设置为 multipart/form-data.
        // 通过设置 transformRequest: angular.identity ，anjularjs transformRequest function 将序列化我们的formdata object.
            return $http({
            url:'../upload.do',
            method:'post',
            data:formdata,
            //anjularjs对于post和get请求默认的Content-Type header 是application/json。通过设置‘Content-Type’: undefined，这样浏览器会帮我们把Content-Type 设置为 multipart/form-data.
            headers:{ 'Content-Type':undefined },
            // 通过设置 transformRequest: angular.identity ，anjularjs transformRequest function 将formdata object进行二进制序列化
            transformRequest: angular.identity
        });

    }

});