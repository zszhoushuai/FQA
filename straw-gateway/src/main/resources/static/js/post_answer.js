let postAnswerApp=new Vue({
    el:"#postAnswerApp",
    data:{
        message:"",
        hasError:false
    },
    methods:{
        postAnswer:function(){
            postAnswerApp.hasError=false;
            let questionId=location.search;
            if(!questionId){
                this.message="没有问题ID";
                this.hasError=true;
                return;
            }
            //去掉?
            questionId=questionId.substring(1);
            let content=$("#summernote").val();
            if(!content){
                this.message="请填写回复内容";
                this.hasError=true;
                return;
            }
            let data={
                questionId:questionId,
                content:content
            }
            $.ajax({
                url:"/faq/v1/answers",
                method:"post",
                data:data,
                success:function(r){
                    if(r.code==CREATED){
                        let answer=r.data;//这个r.data就是新增的回答
                        //将这个问题的持续时间计算出来
                        addDuration(answer);
                        //将新增的方法插入到anwsers数组的后面
                        answersApp.answers.push(answer);
                        //回答已经显示,清空富文本编辑器中的内容
                        $("#summernote").summernote("reset");
                        postAnswerApp.message=r.message;
                        postAnswerApp.hasError=true;
                        //2秒中之后信息消失
                        setTimeout(function(){
                            postAnswerApp.hasError=false;
                        },2000);
                    }else{
                        postAnswerApp.message=r.message;
                        postAnswerApp.hasError=true;
                    }
                }
            })
        }
    }
})