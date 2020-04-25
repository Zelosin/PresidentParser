Vue.component('department-member', {
    props: ['member', 'index'],
    data: function () {
        return {

        }
    },
    template: `
          <div class="input-group mb-3">
            <input type="text" class="form-control" placeholder="ФИО Преподавателя" v-model = "member"  @change = "editMember(member, index)">
            <div class="input-group-append">
              <button class="btn btn-danger" @click="removeMember(index)" type="button">Удалить</button>
            </div>
          </div>`,
    methods: {
        removeMember: function (index) {
            app.dpList.splice(index, 1)
        },
        editMember: function (member, index) {
            app.dpList[index] = member
        }
    }
});


app = new Vue({
    el: '#departmentMemberField',
    data: {
        dpList: []
    },
    beforeCreate: function () {
        fetch('http://localhost:8080/provider?type=members')
            .then(response =>{
                return response.json();
            })
            .then(members =>{
                this.dpList = members;
                console.log(this.dpList[0])
            })
    },
    methods: {
        addMember: function () {
            app.dpList.push("")
        },
        sendMembersList: function () {
            fetch("http://localhost:8080/provider?type=members", {
                method : 'post',
                body: JSON.stringify(this.dpList)
            })
        }
    },
});