
var data;

queryInfo = [];
sectionInfo = [];

$('input[type="file"]').change(function(e){
    data = new FormData();
    data.append('file', e.target.files[0]);
    data.append('user', 'hubot');
    var fileName = e.target.files[0].name;
    $('.custom-file-label').html(fileName);
});

