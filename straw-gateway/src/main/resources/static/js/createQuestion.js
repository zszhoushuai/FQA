//启动v-select标签
Vue.component("v-select", VueSelect.VueSelect);
let createQuestionApp = new Vue({
    el: "#createQuestionApp",
    data: {
        title:"",
        selectedTags:[],
        tags:[],
        selectedTeachers:[],
        teachers:["苍老师","范老师","克晶老师"]
    },
    methods: {
        loadTags:function(){
            $.ajax({
                url:"/faq/v1/tags",
                method:"get",
                success:function(r){
                    console.log(r);
                    if(r.code==OK){
                        let list=r.data;//获得所有标签数组
                        //list=[{id:1,name:"java基础"},{...},{...}]
                        let tags=[];
                        for(let i=0;i<list.length;i++){
                            //push方法表示向这个数组的最后位置添加元素
                            //效果和java中list的add方法一致
                            tags.push(list[i].name);
                        }
                        console.log(tags);
                        createQuestionApp.tags=tags;
                    }
                }
            });
        },
        loadTeachers:function(){
            $.ajax({
                url:"/sys/v1/users/masters",
                method:"get",
                success:function(r){
                    console.log(r);
                    if(r.code==OK){
                        let list=r.data;//获得所有讲师数组
                        let teachers=[];
                        for(let i=0;i<list.length;i++){
                            //push方法表示向这个数组的最后位置添加元素
                            //效果和java中list的add方法一致
                            teachers.push(list[i].nickname);
                        }
                        console.log(teachers);
                        createQuestionApp.teachers=teachers;
                    }
                }
            });
        },
        createQuestion:function(){
            let content=$("#summernote").val();
            console.log(content);
            //定义一个data对象,用于ajax提交信息到控制器
            let data={
                title:this.title,
                tagNames:this.selectedTags,
                teacherNickNames:this.selectedTeachers,
                content:content
            }
            console.log(data);
            $.ajax({
                url:"/faq/v1/questions",
                traditional:true,//使用传统数组的编码方式,SpringMvc才能接收
                method:"post",
                data:data,
                success:function(r){
                    console.log(r)
                    if(r.code== OK){
                        console.log(r.message);
                    }else{
                        console.log(r.message);
                    }
                }
            });
        }
    },
    created:function(){
        this.loadTags();
        this.loadTeachers();
    }
});