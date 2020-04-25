
sheetsSettings = new Vue({
    el: '#sheetsSettings',
    data: {
        isNewSheetAdded : false,
        newSheetName: "",
        selectedSheet: "",
        selectedSheetIndex: 0,
        sheetsList:[],
        configsList:[],
    },
    created: function () {
        this.requestSheets();
    },
    methods: {
        requestSheets : function(){
            fetch('/provider?type=params')
                .then(response =>{
                    if(response.status !== 204)
                        return response.json();
                    else
                        return null;
                })
                .then(params =>{
                    if(params != null) {
                        queryInfo = params.query;
                        sectionInfo = params.ajax;
                    }
                });
            fetch('/provider?type=sheets')
                .then(response =>{
                    if(response.status !== 204)
                        return response.json();
                    else
                        return null;
                })
                .then(config =>{
                    if(config != null) {
                        this.sheetsList = config.sheets;
                    }
                })
        },
        addConf : function(){
            sheetsSettings.configsList.push(
                {
                    selectedQueryType : "Resource",
                    selectedVariable: "",
                    selectedSection : "",
                    selectedCellType : "Cell",
                    cellText: "",
                    rowNumber: 0,
                    columnNumber : 0
                }
            )
        },
        changeSheet: function(event, selectedIndex){
            this.configsList = this.sheetsList[selectedIndex][1];
            this.selectedSheetIndex = selectedIndex;
        },
        sendConfList : function(){
            fetch("/provider?type=sheets", {
                method : 'post',
                body: JSON.stringify({sheets_list : this.sheetsList})
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
        },
        addSheet: function(){
            this.showModal = true;
            this.sheetsList.push([this.newSheetName, []]);
            this.newSheetName = "";
            this.isNewSheetAdded = true;
        },
        addNewSheet: function(){
            this.isNewSheetAdded = false;
        },
        deleteCurrentSheet: function(){
            this.sheetsList.splice(this.selectedSheetIndex, 1);
            this.configsList = [];
        }
    },

});