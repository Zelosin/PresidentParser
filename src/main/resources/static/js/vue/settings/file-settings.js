fileSettings = new Vue({
   el: '#downloadLink',
   methods: {
       downloadTemplate: function(){
           var link = document.createElement("a");
           link.download = name;
           link.href = "/transfer/configuration?type=sample";
           link.click();
       }
   }
});