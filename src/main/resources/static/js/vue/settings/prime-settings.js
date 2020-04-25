primeSettings = new Vue({
    el: '#primeSettings',
    data: {
        dQueryType: 'Resource',
        dParseType: 'Resource',
        dDepartmentLink: '',
        dProfileService: 'https://lk.pnzgu.ru',
        dStudentsLinkList: '',
        dFileName: '',
        dQueryAction: 'basicAction'
    },

    methods: {
        requestPrimeSettings: function(){
            fetch('/provider?type=prime')
                .then(response =>{
                    if(response.status !== 204) {
                        return response.json();
                    }
                    return null;
                })
                .then(primeSettings =>{
                    if(primeSettings != null){
                        this.dQueryType = primeSettings.QueryType;
                        this.dParseType = primeSettings.ParseType;
                        this.dDepartmentLink = primeSettings.DepartmentLink;
                        this.dProfileService = primeSettings.ProfileService;
                        this.dStudentsLinkList = primeSettings.StudentsLinkList;
                        this.dFileName = primeSettings.FileName;
                        this.dQueryAction = primeSettings.QueryAction;
                    }
                })
        },
        submitMainSettings: function () {
            fetch("/provider?type=prime", {
                method : 'post',
                body: JSON.stringify({
                    QueryType : this.dQueryType,
                    ParseType : this.dParseType,
                    DepartmentLink : this.dDepartmentLink,
                    ProfileService : this.dProfileService,
                    StudentsLinkList : this.dStudentsLinkList,
                    FileName : this.dFileName,
                    QueryAction : this.dQueryAction,
                })
            }).then(response =>{
                if(response.status === 204) {
                    $(document).ready(function(){
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
        },
    },
    created: function () {
        this.requestPrimeSettings();
    },
});