membersSettings = new Vue({
    el: '#departmentMemberField',
    data: {
        dpList: [],
    },
    created: function () {
        this.requestMembers()
    },
    methods: {
        requestMembers: function(){
            fetch('/provider?type=members')
                .then(response =>{
                    if(response.status !== 204)
                        return response.json();
                    else
                        return null;
                })
                .then(members =>{
                    if(members != null) {
                        if (members.members_list == null) {
                            this.dpList = [""]
                        } else {
                            this.dpList = members.members_list;
                        }
                    }
                })
        },
        addMember: function () {
            this.dpList.push("")
        },
        sendMembersList: function () {
            fetch("/provider?type=members", {
                method : 'post',
                body: JSON.stringify({members_list : this.dpList})
            }).then(response => {
                if (response.status === 204) {
                    $(document).ready(function () {
                        modalWindow.serverResponse = "Не добавлен файл кофигурации.";
                        $("#serverResponseModal").modal();
                    });
                }
                if (response.status === 202) {
                    $(document).ready(function () {
                        modalWindow.serverResponse = "Данные на сервере были успешно обновлены.";
                        $("#serverResponseModal").modal();
                    });
                }
            })
        }
    },
});