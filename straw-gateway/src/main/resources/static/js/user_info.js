let userApp = new Vue({
    el: "#userApp",
    data: {
        user: {}
    },
    methods: {
        loadCurrentUser: function () {
            $.ajax({
                url: "/sys/v1/users/me",
                method: "get",
                success: function (r) {
                    console.log(r)
                    if (r.code==OK) {
                        userApp.user=r.data;
                    }else{
                        console.log(r.message);
                    }
                }
            });
        }
    },
    created: function () {
        //页面加载完毕后立即调用loadCurrentUser方法
        this.loadCurrentUser();
    }
});