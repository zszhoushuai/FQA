let questionApp = new Vue({
    el: "#questionApp",
    data: {
        question: {}
    },
    methods: {
        loadQuestion: function () {
            //获取浏览器地址栏中当前url中?之后的内容
            let questionId = location.search;
            console.log("questionId:" + questionId);
            //判断是不是获得了?之后的内容
            if (!questionId) {
                //如果没有?则终止
                alert("必须指定问题id");
                return;
            }
            //如果存在?之后的内容,则去掉?    ?354
            questionId = questionId.substring(1);
            //发送异步请求
            $.ajax({
                url: "/faq/v1/questions/" + questionId,//v1/questions/15
                method: "get",
                success: function (r) {
                    console.log(r);
                    if (r.code == OK) {
                        questionApp.question = r.data;
                        addDuration(questionApp.question);
                    } else {
                        alert(r.message);
                    }
                }
            })
        }
    },
    created: function () {
        this.loadQuestion();
    }
});

$(function () {
    $('#summernote').summernote({
        height: 300,
        lang: 'zh-CN',
        placeholder: '请输入问题的详细描述...',
        callbacks: {
            //在执行指定操作后自动调用下面的方法
            //onImageUpload方法就会在用户选中图片之后立即运行
            onImageUpload: function (files) {
                //参数是一个file数组取出第一个,因为我们只会选中一个
                let file = files[0];
                //构建表单
                let form = new FormData();
                form.append("imageFile", file);
                $.ajax({
                    url: "/resource/v1/images",
                    method: "post",
                    data: form,//发送的是我们构建的表单中的数据
                    //下面有两个特殊参数,需要在文件上传时设置
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (r) {
                        if (r.code == OK) {
                            console.log(r);
                            //将刚刚上传成功的图片显示在summernote富文本编辑器中
                            var img = new Image();//实例化了一个img标签
                            img.src = r.message;//将img标签的src属性赋值为刚上传的图片
                            //summernote方法中提供了插入标签的功能
                            //支持使用"insertNode"表示要向富文本编辑器中添加标签内容
                            $("#summernote").summernote(
                                "insertNode", img
                            )
                        } else {
                            alert(r.message);
                        }
                    }
                });
            }
        }
    });
})


let answersApp = new Vue({
    el: "#answersApp",
    data: {
        message: "",
        hasError: false,
        answers: []
    },
    methods: {
        loadAnswers: function () {
            let questionId = location.search;
            if (!questionId) {
                this.message = "必须有问题ID";
                this.hasError = true;
                return;
            }
            questionId = questionId.substring(1);
            $.ajax({
                url: "/faq/v1/answers/question/" + questionId,
                method: "get",
                success: function (r) {
                    if (r.code == OK) {
                        answersApp.answers = r.data;
                        answersApp.updateDuration();
                    } else {
                        answersApp.message = r.message;
                        answersApp.hasError = true;
                    }
                }
            })
        },
        removeComment: function (commentId, index, comments) {
            if (!commentId) {
                return;
            }
            $.ajax({
                //   匹配/v1/comments/{id}/delete
                url: "/faq/v1/comments/" + commentId + "/delete",
                method: "get",
                success: function (r) {
                    if (r.code == GONE) {
                        //splice方法是从指定数组中,从index的位置开始删除,删除几个元素
                        //这里写1就表示只删除index位置的一个元素
                        comments.splice(index, 1);
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        updateComment: function (commentId, answerId, index, comments) {
            let textarea = $("#editComment" + commentId + " textarea");
            let content = textarea.val();
            if (!content) {
                return;
            }
            let data = {
                answerId: answerId,
                content: content
            };
            $.ajax({
                url: "/faq/v1/comments/" + commentId + "/update",
                method: "post",
                data: data,
                success: function (r) {
                    console.log(r)
                    if (r.code == OK) {
                        //如果是对数组内部的属性值的修改
                        //不会触发Vue的绑定更新
                        //Vue提供了手动绑定更新的方法,能够修改数组中的值
                        // 而且还能触发绑定的更新
                        Vue.set(comments, index, r.data)
                        //将当前显示编辑输入框的div隐藏
                        $("#editComment" + commentId).collapse("hide");
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        postComment: function (answerId) {
            //现在我们需要获得回答id和评论内容,以新增评论
            let content = $("#addComment" + answerId + " textarea").val()
            if (!content) {
                console.log("评论内容不能为空");
                return;
            }
            let data = {
                answerId: answerId,
                content: content
            }
            $.ajax({
                url: "/faq/v1/comments",
                method: "post",
                data: data,
                success: function (r) {
                    console.log(r);
                    if (r.code == CREATED) {
                        //清空textarea的内容
                        $("#addComment" + answerId + " textarea").val("");
                        //获得新增的评论
                        let comment = r.data;
                        //获得当前所有的回答
                        let answers = answersApp.answers;
                        //遍历所有回答
                        for (let i = 0; i < answers.length; i++) {
                            //判断本次添加的评论是不是属于当前回答的
                            if (answers[i].id == answerId) {
                                //把新增的评论保存到当前回答中评论的集合里
                                answers[i].comments.push(comment);
                                break;
                            }
                        }
                    } else {
                        alert(r.message);
                    }
                }
            })
        },
        //问题采纳
        answerSolved: function (answerId, answer) {
            if (!answerId) {
                return;
            }
            //判断这个问题是否已经被采纳
            if (answer.acceptStatus == 1) {
                alert("此问题已经被采纳")
                return;
            }
            $.ajax({
                url: "/faq/v1/answers/" + answerId + "/solved",
                method: "get",
                success: function (r) {
                   console.log(r);
                   if(r.code==ACCEPTED){
                       answer.acceptStatus=1;
                   }else{
                       alert(r.message);
                   }
                }
            });

        }
        ,
        updateDuration: function () {
            for (let i = 0; i < this.answers.length; i++) {
                addDuration(this.answers[i]);
            }
        }
    },
    created: function () {
        this.loadAnswers();
    }
})

























