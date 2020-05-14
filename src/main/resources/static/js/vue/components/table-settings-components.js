Vue.component('query-settings', {
    props: ['conf'],
    data: function () {
        return {
            variablesArray : {},
            sectionsArray : {},
            actionsArray : {},
            compareValue : ""
        }
    },
    template:
        `
          <div class = "card p-3 pb-4 mb-3">
            <div class="d-flex flex-row-reverse">
              <button type="button" class="close align-text-bottom" aria-label="Close" @click="deleteConf">
                <span aria-hidden="true">&times;</span>
              </button>
            </div>


            <form>
              <div class="form-row">
                <div class="form-group col-md-2" >
                  <label>Тип ячейки</label>
                  <select class="form-control" v-model="conf.selectedCellType">
                    <option selected value="Cell">Столбец</option>
                    <option value="Label">Строка</option>
                  </select>
                </div>

                <div class="form-group col-md-6">
                  <label>Отображаемый текст ячейки</label>
                  <input type="text" v-model="conf.cellText" class="form-control">
                </div>

                <div class="form-group col-md-2">
                  <label>Номер столбца</label>
                  <input type="number" v-model.number="conf.columnNumber" class="form-control">
                </div>

                <div class="form-group col-md-2">
                  <label>Номер строки</label>
                  <input type="number" v-model.number="conf.rowNumber" class="form-control">
                </div>

              </div>
              <div class="form-row" v-if="conf.selectedCellType === 'Cell'">
                <div class="form-group col-md-4">
                  <label>Тип столбца</label>
                  <select  @change="changelst" v-model="conf.selectedQueryType" class="form-control" >
                    <option selected value="Resource">Ресурсы</option>
                    <option value="Document">Документы</option>
                    <option value="NIR">Научно исследовательские работы</option>
                    <option value="RID">Результаты интеллектуальной деятельности</option>
                  </select>
                </div>

                <div class="form-group col-md-4">
                  <label>Секция</label>
                  <select v-model="conf.selectedSection" class="form-control" >
                    <option v-for="(item, key) in sectionsArray" :value="item">
                      {{key}}
                    </option>
                  </select>
                </div>
                <div class="form-group col-md-4">
                  <label>Значение</label>
                  <select v-model="conf.selectedVariable" class="form-control" >
                    <option v-for="(item, key) in variablesArray" :value="item">
                      {{key}}
                    </option>
                  </select>
                </div>
              </div>

              <div class="form-row" v-if="conf.selectedCellType === 'Cell'">
    
                    <div class="form-group col-md-4" >
                      <label>Тип фильтра</label>
                      <select @change="changeFilterType" v-model="conf.filterType" class="form-control">
                        <option selected value="NoFilter">Нет фильтра</option>
                        <option value="StringCompare">Сравнение строк</option>
                        <option value="NumberCompare">Сравнение чисел</option>
                        <option value="DateCompare">Сравнение дат</option>
                        <option value="DateInterval">Период дат</option>
                      </select>
                    </div>
                   
                   <template v-if="conf.filterType !== 'NoFilter'">
                        <div class="form-group col-md-4">
                          <label>Способ сравнения</label>
                          <select v-model="conf.filterAction" class="form-control" >
                            <option  v-for="(item, key) in actionsArray" :value="key">
                              {{item}}
                            </option>
                          </select>
                        </div>
                        
                        <div class="form-group col-md-4" v-if="conf.filterType == 'DateInterval'">
                          <label>Сравниваемый промежуток</label>
                          <date-picker
                                  v-model="conf.filterDateValue"
                                  lang="ru"
                                  format="DD-MM-YYYY"
                                  height="38"
                                  width="328"
                                  range>
                          </date-picker>
                        </div>
                        
                        <div class="form-group col-md-4" v-else-if="conf.filterType == 'DateCompare'">
                          <label>Сравниваемая дата</label>
                          <date-picker
                                  v-model="conf.filterDateValue"
                                  lang="ru"
                                  format="DD-MM-YYYY"
                                  height="38"
                                  width="328">
                          </date-picker>
                        </div>
                        
                        <div class="form-group col-md-4" v-else>
                          <label>Сравниваемое значение</label>
                          <input v-model.number="conf.filterCompValue" class="form-control">
                        </div>
                    </template>      
                </div>
            </form>
          </div>
          `,
    methods: {
        changelst: function () {
            switch (this.conf.selectedQueryType) {
                case "Resource":{
                    this.variablesArray = queryInfo[0];
                    this.sectionsArray = sectionInfo[0];
                    break;
                }
                case "Document":{
                    this.variablesArray = queryInfo[1];
                    this.sectionsArray = sectionInfo[1];
                    break;
                }
                case "NIR":{
                    this.variablesArray = queryInfo[2];
                    this.sectionsArray = sectionInfo[2];
                    break;
                }
                case "RID":{
                    this.variablesArray = queryInfo[3];
                    this.sectionsArray = sectionInfo[3];
                    break;
                }
            }
        },
        deleteConf: function(){
            sheetsSettings.configsList.splice(sheetsSettings.configsList.indexOf(this.conf), 1);
        },
        changeFilterType: function(){
            switch (this.conf.filterType) {
                case "DateCompare":{
                    this.actionsArray = filterDateAction;
                    this.conf.filterValue = new Date();
                    this.conf.filterAction = "After";
                    break;
                }
                case "StringCompare":{
                    this.actionsArray = filterStringAction;
                    this.conf.filterValue = "";
                    this.conf.filterAction = "Equal";
                    break;
                }
                case "NumberCompare":{
                    this.actionsArray = filterNumberAction;
                    this.conf.filterValue = "";
                    this.conf.filterAction = "Equal";
                    break;
                }
                case "DateInterval":{
                    this.actionsArray = filterDateItervalAction;
                    this.conf.filterAction = "InInterval";
                    this.conf.filterValue = new Date();
                    break;
                }
                case "NoFilter":{
                    this.actionsArray = {};
                    this.compareValue = "";
                    break;
                }
            }
        },
    },
    mounted : function (){
        this.changelst();
        this.changeFilterType();
    },
});
