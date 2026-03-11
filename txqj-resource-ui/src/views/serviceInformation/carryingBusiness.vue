<template>
  <div>
    <!--搜索-->
    <el-form label-width="100px" :model="searchForm" :inline="true">
      <el-form-item label-width="80px" label="名称" prop="NAME">
        <el-input size="mini" v-model="searchForm.NAME" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item v-if="pattern" label="A端站点" prop="A_SITE_ID">
        <el-select
          size="mini"
          placeholder="请选择"
          clearable
          filterable
          remote
          @focus="selectName = 'A_SITE_ID'"
          :loading="A_SITE_ID_Loading"
          v-loadmore="loadmore"
          :remote-method="(val) => remoteMethod(val, 'A_SITE_ID')"
          v-model="searchForm.A_SITE_ID"
        >
          <el-option
            v-for="item in A_SITE_ID.list"
            :key="item.code"
            :label="item.name"
            :value="item.code"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="pattern" label="Z端站点" prop="Z_SITE_ID">
        <el-select
          size="mini"
          placeholder="请选择"
          clearable
          filterable
          remote
          @focus="selectName = 'Z_SITE_ID'"
          :loading="Z_SITE_ID_Loading"
          v-loadmore="loadmore"
          :remote-method="(val) => remoteMethod(val, 'Z_SITE_ID')"
          v-model="searchForm.Z_SITE_ID"
        >
          <el-option
            v-for="item in Z_SITE_ID.list"
            :key="item.code"
            :label="item.name"
            :value="item.code"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="pattern" label="调度等级" prop="DSP_LEVEL">
        <el-select
          size="mini"
          placeholder="请选择"
          clearable
          filterable
          remote
          @focus="selectName = 'DSP_LEVEL'"
          :loading="DSP_LEVEL_Loading"
          v-loadmore="loadmore"
          :remote-method="(val) => remoteMethod(val, 'DSP_LEVEL')"
          v-model="searchForm.DSP_LEVEL"
        >
          <el-option
            v-for="item in DSP_LEVEL.list"
            :key="item.code"
            :label="item.name"
            :value="item.code"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="pattern" label="业务类型" prop="BUZ_TYPE">
        <el-button size="mini" type="primary" @click="getTableDataByBuzType('1010')">继电保护</el-button>
        <el-button size="mini" type="success" @click="getTableDataByBuzType('1200')">调度数据网</el-button>
        <el-button size="mini" type="warning" @click="getTableDataByBuzType('1900')">综合数据网</el-button>
      </el-form-item>
      <el-form-item style="float: right;margin-right: 20px!important;">
        <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
        <el-button size="mini" @click="resetSearch()">重置</el-button>
        <el-button type="primary" size="mini" @click="carryingBusinessExport()">导出</el-button>
      </el-form-item>
    </el-form>
    <carrying
      v-loading="loading"
      ref="carryingPage"
      :tableName="listTableName"
      :jumpLink="jumpLink"
      :searchForm="searchForm"
      :pattern="pattern"
      @getSearchCriteria="getSearchCriteria"
      :openNewPage="openNewPage"
    ></carrying>
  </div>
</template>

<script>
import carrying from '@/views/commonPage/carrying/index';
import {getModelId, tableDropDownBox,export_excel_file,fileDownload} from "@/api/zdwh/zdzywh";
import {
  getCarryingBusinessById,
  getSDHCarryingBusinessById,
  getGLCarryingBusinessById,
  getBKCarryingBusinessById,
  carryingBusinessExport
} from '@/api/serviceInformation/carryingBusiness'

export default {
  name: "carryingBusiness",
  props: {
    id: String,
    tableName: String,
    pattern: {
      type: Boolean,
      default: () => true
    },
    openNewPage: {
      type: Boolean,
      default: () => true
    }
  },
  components: {carrying},
  data() {
    return {
      listTableName: 'SG_TCCON_TCBUZ_B',
      jumpLink: [
        {attributeNameUnderline: 'NAME', menuPath: 'businesssource/serviceInformation'}
      ],
      ids: '',
      searchForm: {
        ID: '',
        NAME: '',
        A_SITE_ID: '',
        Z_SITE_ID: '',
        DSP_LEVEL: '',
      },
      modelObj: {},
      // 下拉框框配置
      selectName: '',
      A_SITE_ID: {},
      Z_SITE_ID: {},
      DSP_LEVEL: {},
      A_SITE_ID_Loading: true,
      Z_SITE_ID_Loading: true,
      DSP_LEVEL_Loading: true,
      searchOptionStr: '',
      loading: false,
      resType: ''
    }
  },
  methods: {
    handleOpenPanel(payload) {
      this.$emit('openPanelInParent', {
          rowData: payload.linkData,
          menu: payload.panelName,
        })
    },


    getTable() {
      //获取表基本信息
      getModelId({tableName: this.listTableName}).then(res => {
        this.modelObj = res.data
        this.getOption('A_SITE_ID')
        this.getOption('Z_SITE_ID')
        this.getOption('DSP_LEVEL')
      })
      this.loading = true
      switch (this.tableName) {
        case 'SG_TCDEV_FIBER_B':
          this.resType = '光缆'
          getCarryingBusinessById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCDEV_TCNESDH_B':
          this.resType = 'SDH'
          getSDHCarryingBusinessById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCDEV_TCNEOTN_B':
          this.resType = 'OTN'
          getSDHCarryingBusinessById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCCON_TCOPTICALROAD_B':
          this.resType = '光路'
          getGLCarryingBusinessById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCDEV_TCCARD_B':
          this.resType = '板卡'
          getBKCarryingBusinessById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        default:
          this.loading = false
      }


    },
    getTableData() {
      this.searchForm.ID = this.ids
      this.$refs.carryingPage.getTableData()
    },
    getTableDataByBuzType(val) {
      this.searchForm.ID = this.ids
      this.searchForm.BUZ_TYPE = val
      this.$refs.carryingPage.getTableData()
    },
    carryingBusinessExport(){
      this.$confirm('是否导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs.carryingPage.otherHandleExport()
      })
    },
    getSearchCriteria(params){
      this.$message({
        type: 'success',
        message: '正在导出...'
      })
      carryingBusinessExport(params,{
        id: this.id,
        resType: this.resType,
        exportType: '条件'
      }).then(res =>{
        let lastInde = res.msg.lastIndexOf("/")+1;
        let fileName = res.msg.substring(lastInde);
        fileDownload({filename:res.msg}).then(res =>{
          export_excel_file(res, fileName);
          this.$message({
            type: 'success',
            message: '导出完成'
          })
        })
      })
    },

    loadmore(val) {
      if (this[this.selectName].total > this[this.selectName].list.length) {
        let pageNum = parseInt(this[this.selectName].list.length / 20) * 20
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: this.selectName,
          name: this.searchOptionStr,
          pageNum: pageNum,
          pageSize: 20,
        }).then((res) => {
          this[this.selectName + '_Loading'] = true
          if (res.rows.length > 0) {
            this[this.selectName].list.push.apply(
              this[this.selectName].list,
              res.rows
            )
          }
          this.$nextTick(() => {
            this[this.selectName + '_Loading'] = false
          })
        })
      } else {
        if (
          !this[this.selectName].list[this[this.selectName].list.length - 1].disabled
        ) {
          this[data + '_Loading'] = true
          this[this.selectName].list.push({
            code: 'aaabbbccc',
            name: '没有更多了',
            disabled: true
          })
          this.$nextTick(() => {
            this[data + '_Loading'] = false
          })
        }
      }
    },
    remoteMethod(val, data) {
      this.searchOptionStr = val
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: data,
        name: this.searchOptionStr,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this[data + '_Loading'] = true
        this[data].list = res.rows
        this[data].total = res.total
        this.$nextTick(() => {
          this[data + '_Loading'] = false
        })
      })
    },
    getOption(data) {
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: data,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        if (res.total > 0) {
          this[data].list = res.rows
          this[data].total = res.total
          this.$nextTick(() => {
            this[data + '_Loading'] = false
          })
        }
      })
    },
    //重置搜索
    resetSearch() {
      this.searchForm.NAME = ''
      this.searchForm.A_SITE_ID = ''
      this.searchForm.Z_SITE_ID = ''
      this.searchForm.BUZ_TYPE = ''
      this.searchForm.DSP_LEVEL = ''
      this.getTableData()
    },
  },
  created() {
    this.getTable()
  }
}
</script>

<style scoped>

</style>
