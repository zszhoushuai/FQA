let searchApp=new Vue({
    el:"#searchApp",
    data:{
        key:""
    },
    methods:{
        search:function(){
            //点击查询按钮后跳转到search.html页面
            //search.html?key=java
            location.href="/search.html?key="+encodeURI(this.key);
        }
    },
    created:function(){
        //页面加载完毕之后,检查当前浏览器地址栏上有没有查询关键字
        //先获得?之后的内容
        let key=location.search;
        if(key && key.startsWith("?key=")){
            //当前浏览器地址栏有查询的情况时
            //将地址栏中的乱码转回为中文
            key=decodeURI(key.substring(5));//5是?key= 的长度,从=后面的内容全要
            this.key=key;
        }
    }
});