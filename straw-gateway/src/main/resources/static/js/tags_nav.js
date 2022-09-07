let tagsApp = new Vue({
    el:'#tagsApp',
    data:{
        tags:[]
    },
    methods:{
        loadTags:function () {
            console.log('执行了 loadTags');
            $.ajax({
                url:'/faq/v1/tags',
                method:'GET',
                success:function (r) {
                    console.log(r);
                    if (r.code === OK){
                        console.log('成功获取tags');
                        //将从控制器获得的所有标签赋值给vue定义的
                        //tags数组,由于双向绑定,赋值同时页面就开始循环了
                        tagsApp.tags = r.data;
                    }
                }
            });
        }
    },
    //这个方法会在页面加载完毕之后运行
    created:function () {
        //页面加载完毕,立即调用loadTags
        this.loadTags();
    }
});