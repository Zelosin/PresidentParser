processingRequest = new Vue({
    el: '#processingRequestSettings',
    data: {
        dQueryAction: "basic"
    },
    methods:{
        sendProcessingRequest: function(){
            fetch('/transfer/configuration?type=' + this.dQueryAction)
                .then(resp => {
                        if(this.dQueryAction == "reset"){
                            location.reload();
                            return null;
                        }
                        else {
                            return resp.blob()
                        }
                    }
                )
                .then(blob => {
                    if(blob != null) {
                        const url = window.URL.createObjectURL(blob);
                        const a = document.createElement('a');
                        a.style.display = 'none';
                        a.href = url;
                        a.download = primeSettings.dFileName;
                        document.body.appendChild(a);
                        a.click();
                        window.URL.revokeObjectURL(url);
                    }
            })
        }
    }
});