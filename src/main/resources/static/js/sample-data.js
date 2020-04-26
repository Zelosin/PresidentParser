
var data;
var targetFrame = document.getElementById("targetFrame");
queryInfo = [];
sectionInfo = [];

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
        modalWindow.serverResponse = "Не выбран файл конфигурации.";
    }
};