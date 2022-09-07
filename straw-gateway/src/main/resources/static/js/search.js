/*
显示查询结果的问题列表
 */
let questionsApp = new Vue({
    el:'#questionsApp',
    data: {
        questions:[],
        pageInfo:{}
    },
    methods: {
        loadQuestions:function (pageNum) {
            if(!pageNum){ //如果pageNum为空,默认页码为1
                pageNum=1;
            }
            let key=location.search;
            if( ! key){//路径没有?直接终止查询
                return;
            }
            if( ! key.startsWith("?key=")){//内容后不是?key=开头,也终止
                return;
            }
            //将条件解码,5是"?key="的长度
            key=decodeURI(key.substring(5));
            $.ajax({
                url: '/search/v1/questions',
                method: "POST",
                data:{
                    key:key,
                    pageNum:pageNum
                },
                success: function (r) {
                    console.log("成功加载数据");
                    console.log(r);
                    if(r.code === OK){
                        questionsApp.questions = r.data.list;
                        //调用计算持续时间的方法
                        questionsApp.updateDuration();
                        //调用显示所有按标签呈现的图片
                        questionsApp.updateTagImage();
                        questionsApp.pageInfo=r.data;
                    }
                }
            });
        },
        updateTagImage:function(){
            let questions = this.questions;
            for(let i=0; i<questions.length; i++){
                //获得当前问题对象的所有标签的集合(数组)
               let tags = questions[i].tags;
               //js代码中特有的写法if(tags)
               //相当于判断tags非空
               if(tags){
                   //获取当前问题的第一个标签对应的图片文件路径
                   let tagImage = 'img/tags/'+tags[0].id+'.jpg';
                   console.log(tagImage);
                   //将这个文件路径保存到tagImage属性用,以便页面调用
                   questions[i].tagImage = tagImage;
               }
            }
        },
        updateDuration:function () {
            let questions=this.questions;
            for(let i=0;i<questions.length;i++){
                addDuration(questions[i]);
            }
        }
    },
    created:function () {
        console.log("执行了方法");
        this.loadQuestions(1);
    }
});










