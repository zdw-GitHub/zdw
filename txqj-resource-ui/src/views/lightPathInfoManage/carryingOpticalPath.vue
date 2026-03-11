<template>
  <div>
    <!--搜索-->
    <el-form label-width="100px" :model="searchForm" :inline="true">
      <el-form-item label-width="80px" label="名称" prop="NAME">
        <el-input size="mini" v-model="searchForm.NAME" placeholder="请输入名称"></el-input>
      </el-form-item>
      <el-form-item v-if="pattern" label="A端设备" prop="A_RES_ID">
        <el-select
          size="mini"
          placeholder="请选择"
          clearable
          filterable
          remote
          @focus="selectName = 'A_RES_ID'"
          :loading="A_RES_ID_Loading"
          v-loadmore="loadmore"
          :remote-method="(val) => remoteMethod(val, 'A_RES_ID')"
          v-model="searchForm.A_RES_ID"
        >
          <el-option
            v-for="item in A_RES_ID.list"
            :key="item.code"
            :label="item.name"
            :value="item.code"
            :disabled="item.disabled">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="pattern" label="Z端设备" prop="Z_RES_ID">
        <el-select
          size="mini"
          placeholder="请选择"
          clearable
          filterable
          remote
          @focus="selectName = 'Z_RES_ID'"
          :loading="Z_RES_ID_Loading"
          v-loadmore="loadmore"
          :remote-method="(val) => remoteMethod(val, 'Z_RES_ID')"
          v-model="searchForm.Z_RES_ID"
        >
          <el-option
            v-for="item in Z_RES_ID.list"
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
      <el-form-item style="float: right;margin-right: 20px!important;">
        <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
        <el-button size="mini" @click="resetSearch()">重置</el-button>
        <el-button type="primary" size="mini" @click="carryingOpticalPathExport()">导出</el-button>
      </el-form-item>
    </el-form>
    <carrying
      v-loading="loading"
      ref="carryingPage"
      :tableName="listTableName"
      :jumpLink="jumpLink"
      :dataList="dataList"
      :fromTableName="tableName"
      :searchForm="searchForm"
      :pattern="pattern"
      :openNewPage="openNewPage"
      @getSearchCriteria="getSearchCriteria"
    ></carrying>
  </div>
</template>

<script>
import carrying from '@/views/commonPage/carrying/index';
import {getModelId, tableDropDownBox,export_excel_file,fileDownload} from "@/api/zdwh/zdzywh";
import {
  getCarryingOpticalPathById,
  getSDHCarryingOpticalPathById,
  getBKCarryingOpticalPathById,
  selectBuzCarryingOpticalPathById,
  carryingOpticalPathExport,
  selectLineSn,
  analysisFiberOpticCableCarryingOpticalPath
} from '@/api/lightPathInfoManage/carryingOpticalPath'

export default {
  name: "carryingOpticalPath",
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
      listTableName: 'SG_TCCON_TCOPTICALROAD_B',
      jumpLink: [
        {attributeNameUnderline: 'NAME', menuPath: 'transsource/lightPathInfoManage'}
      ],
      ids: '',
      searchForm: {
        ID: '',
        NAME: '',
        A_RES_ID: '',
        Z_RES_ID: '',
        DSP_LEVEL: '',
      },
      modelObj: {},
      // 下拉框框配置
      selectName: '',
      A_RES_ID: {},
      Z_RES_ID: {},
      DSP_LEVEL: {},
      A_RES_ID_Loading: true,
      Z_RES_ID_Loading: true,
      DSP_LEVEL_Loading: true,
      searchOptionStr: '',
      loading: false,
      resType: '',
      dataList:[]
    }
  },
  methods: {
    getTable() {
      //获取表基本信息
      getModelId({tableName: 'SG_TCCON_TCOPTICALROAD_B'}).then(res => {
        this.modelObj = res.data
        this.getOption('A_RES_ID')
        this.getOption('Z_RES_ID')
        this.getOption('DSP_LEVEL')
      })
      this.loading = true
      switch (this.tableName) {
        case 'SG_TCDEV_FIBER_B':
          this.resType = '光缆'
          getCarryingOpticalPathById({resId: this.id}).then(res => {
            this.ids = res.msg
            selectLineSn({resId: this.id}).then(r => {
              this.dataList = r.data
              this.getTableData();
              this.loading = false
            })
          })
          analysisFiberOpticCableCarryingOpticalPath({fiberId: this.id})

          break
        case 'SG_TCDEV_TCNESDH_B':
          this.resType = 'SDH'
          getSDHCarryingOpticalPathById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCDEV_TCNEOTN_B':
          this.resType = 'OTN'
          getSDHCarryingOpticalPathById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCCON_TCBUZ_B':
          this.resType = '业务'
          selectBuzCarryingOpticalPathById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCDEV_TCCARD_B':
          this.resType = '板卡'
          getBKCarryingOpticalPathById({resId: this.id}).then(res => {
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
    carryingOpticalPathExport(){
      this.searchForm.ID = this.ids
      this.$confirm('是否导出数据?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$refs.carryingPage.otherHandleExport()
      })
    },
    getSearchCriteria(params){
      console.log(params)
      this.$message({
        type: 'success',
        message: '正在导出...'
      })
      carryingOpticalPathExport(params,{
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
          this[this.selectName + 'Loading'] = true
          if (res.rows.length > 0) {
            this[this.selectName].list.push.apply(
              this[this.selectName].list,
              res.rows
            )
          }
          this.$nextTick(() => {
            this[this.selectName + 'Loading'] = false
          })
        })
      } else {
        if (
          !this[this.selectName].list[this[this.selectName].list.length - 1].disabled
        ) {
          this[data + 'Loading'] = true
          this[this.selectName].list.push({
            code: 'aaabbbccc',
            name: '没有更多了',
            disabled: true
          })
          this.$nextTick(() => {
            this[data + 'Loading'] = false
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
    //重置选择光缆段中的搜索
    resetSearch() {
      this.searchForm.NAME = ''
      this.searchForm.A_TCSITE_ID = ''
      this.searchForm.Z_TCSITE_ID = ''
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
