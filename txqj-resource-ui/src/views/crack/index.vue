<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="tableName"
      :rightClickMenus="[
        { name: '关联站点', icon: 'el-icon-upload2',functionName: 'associatedSitesShow'},
      ]"
      @associatedSitesShow="associatedSitesShow"
    >
    </zdzywhPage>
    <!--关联站点-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :title="associatedSites.title + '   关联的站点'"
      v-if="associatedSites.show"
      :visible.sync="associatedSites.show"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <associatedSites style="width: 100%; height: 70vh;" :id="associatedSites.id" :tableName="associatedSites.tableName">
      </associatedSites>
      <div slot="footer" class="dialog-footer">
        <el-button @click="associatedSites.show = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  import associatedSites from '@/views/crack/associatedSites'
  export default {
    name: "index",
    components: {zdzywhPage,associatedSites},
    data(){
      return {
        tableName:'SG_TCCON_TCRACK_B',
        //承载光路
        associatedSites:{
          title: '',
          id: '',
          show: false,
        },
      }
    },
    watch: {
    },
    methods:{
      associatedSitesShow(data){
        this.associatedSites.title = data.rowData.NAME
        this.associatedSites.id = data.rowData.ID
        this.associatedSites.show = true
        this.associatedSites.tableName = this.tableName
      }
    },
    mounted () {
    },
  }
</script>

<style scoped lang="scss">
</style>
