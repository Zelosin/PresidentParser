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