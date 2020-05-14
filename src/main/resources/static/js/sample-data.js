
var data;
var targetFrame = document.getElementById("targetFrame");
queryInfo = [];
sectionInfo = [];


filterStringAction = {
    Equal : "Равно",
    NotEqual : "Не равно",
    Contains : "Содержит",
    DontContains : "Не содержит"
};

filterDateAction = {
    After: "Позже",
    Before: "Раньше",
    Equal: "Равно",
    NotEqual: "Не равно"
};

filterDateItervalAction = {
    InInterval: "Входит в интервал",
    OutOfInterval: "Не входит в интервал"
};

filterNumberAction = {
    Biggest : "Больше",
    BiggestOrEqual : "Больше или равно",
    Less : "Меньше",
    LessOrEqual : "Меньше или равно",
    Equal : "Равно",
    NotEqual : "Не равно"
};

$('input[type="file"]').change(function(e){
    data = new FormData();
    data.append('file', e.target.files[0]);
    data.append('user', 'hubot');
    var fileName = e.target.files[0].name;
    $('.custom-file-label').html(fileName);
});


targetFrame.onload = function () {
    modalWindow.serverResponse = "";
    if(targetFrame.contentDocument.body.innerHTML === "OK") {
        modalWindow.serverResponse = "Файл конфигурации успешно добавлен.";
        primeSettings.requestPrimeSettings();
        membersSettings.requestMembers();
        sheetsSettings.requestSheets();
    }
    else{
        modalWindow.serverResponse = "Не удалось добавить файл конфигурации.";
    }
};