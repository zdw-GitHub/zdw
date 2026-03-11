<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="tableName"
      :operate="{slot: 'handle',label: '操作',width: '200'}"
    >
      <template v-slot:handle="slot">
        <el-button size="mini" type="primary" @click="openInfo(true,slot.scope.row)" v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="openInfo(false,slot.scope.row)" v-hasPermi="['system:b:list']">查看</el-button>
        <el-button size="mini" type="danger" @click="delData(slot.scope.row)" v-hasPermi="['system:b:remove']">删除</el-button>
      </template>
    </zdzywhPage>
    <el-dialog
      v-dialogDrag
      :custom-class="infoBox.class"
      :visible.sync="infoBox.show"
      v-if="infoBox.show"
      :width="infoBox.width"
      :modal="false"
      :close-on-click-modal="false">
      <info
        style="width: 100%; height: 80vh;"
        :isEditMode="infoBox.isEditMode"
        :dataObj="infoBox.data"
        :tableName="tableName"
        @switchingModes="switchingModes">
      </info>
    </el-dialog>
  </div>
</template>

<script>
import zdzywhPage from '@/views/commonPage/zdzywh/index'
import info from '@/views/commonPage/info/index'

export default {
  name: 'index',
  components: { zdzywhPage, info },
  data() {
    return {
      tableName: 'SG_TCCON_TCSITE_B',
      infoBox: {
        isEditMode: false,
        show: false,
        data: {},
        width: '1800px',
        class: 'framePosition',
      },
    }
  },
  watch: {},
  methods: {
    openInfo(type,row){
      this.infoBox.isEditMode = type
      this.infoBox.show = true
      this.infoBox.width = '1600px'
      this.infoBox.class = ''
      this.infoBox.data = row
    },
    switchingModes(data){
      this.infoBox.width = data ? '1800px' : '500px'
      this.infoBox.class = data ? '' : 'framePosition'
    },
    delData (scope) {
      this.$refs.zdzywhPage.delData(scope)
    },
  },
  mounted() {
  }
}
</script>

<style scoped lang="scss">
</style>
