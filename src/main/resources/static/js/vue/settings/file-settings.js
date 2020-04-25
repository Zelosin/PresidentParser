fileSettings = new Vue({
   el: '#downloadLink',
   methods: {
       downloadTemplate: function(){
           fetch('/transfer/configuration?type=sample')
               .then(resp => {
                       return resp.blob()
                   }
               )
               .then(blob => {
                   const url = window.URL.createObjectURL(blob);
                   const a = document.createElement('a');
                   a.style.display = 'none';
                   a.href = url;
                   a.download = primeSettings.dFileName;
                   document.body.appendChild(a);
                   a.click();
                   window.URL.revokeObjectURL(url);
               })
       }
   }
});