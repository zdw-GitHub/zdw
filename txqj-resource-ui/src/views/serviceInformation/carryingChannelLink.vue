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
          @focus="selectName = 'optionsA'"
          :loading="optionsALoading"
          v-loadmore="loadmore"
          :remote-method="(val) => remoteMethod(val, 'optionsA')"
          v-model="searchForm.A_RES_ID"
        >
          <el-option
            v-for="item in optionsA.list"
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
          @focus="selectName = 'optionsZ'"
          :loading="optionsZLoading"
          v-loadmore="loadmore"
          :remote-method="(val) => remoteMethod(val, 'optionsZ')"
          v-model="searchForm.Z_RES_ID"
        >
          <el-option
            v-for="item in optionsZ.list"
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
      </el-form-item>
    </el-form>
    <carrying
      v-loading="loading"
      ref="carryingPage"
      :tableName="listTableName"
      :jumpLink="jumpLink"
      :searchForm="searchForm"
      :pattern="pattern"
      :openNewPage="openNewPage"
    ></carrying>
  </div>
</template>

<script>
import carrying from '@/views/commonPage/carrying/index';
import {getModelId, tableDropDownBox} from "@/api/zdwh/zdzywh";
import {getCarryingChannelLinkById,selectCarryingChannelLinkByGlId,selectCarryingChannelLinkBySDHId,selectCarryingChannelLinkByBuzId} from '@/api/serviceInformation/carryingBusiness'

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
      listTableName: 'SG_TCCON_TCCHANNEL_B',
      jumpLink: [
        {attributeNameUnderline: 'NAME', menuPath: 'transsource/zdwh/common/SG_TCCON_TCCHANNEL_B'}
      ],
      ids: '',
      searchForm: {
        ID: '',
        NAME: '',
        A_RES_ID: '',
        Z_RES_ID: '',
      },
      modelObj: {},
      // 下拉框框配置
      selectName: '',
      optionsA: {},
      optionsZ: {},
      optionsALoading: true,
      optionsZLoading: true,
      searchOptionStr: '',
      loading: false,
    }
  },
  methods: {
    getTable() {
      //获取表基本信息
      getModelId({tableName: this.listTableName}).then(res => {
        this.modelObj = res.data
        this.getOption('optionsA')
        this.getOption('optionsZ')
      })
      this.loading = true
      switch (this.tableName) {
        case 'SG_TCDEV_FIBER_B':
          getCarryingChannelLinkById({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCCON_TCOPTICALROAD_B':
          selectCarryingChannelLinkByGlId({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCDEV_TCNESDH_B':
          selectCarryingChannelLinkBySDHId({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCDEV_TCNEOTN_B':
          selectCarryingChannelLinkBySDHId({resId: this.id}).then(res => {
            this.ids = res.msg
            this.getTableData();
            this.loading = false
          })
          break
        case 'SG_TCCON_TCBUZ_B':
          selectCarryingChannelLinkByBuzId({resId: this.id}).then(res => {
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

    loadmore(val) {
      if (this[this.selectName].total > this[this.selectName].list.length) {
        let pageNum = parseInt(this[this.selectName].list.length / 20) * 20
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: 'A_TCSITE_ID',
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
        attribute: 'A_RES_ID',
        name: this.searchOptionStr,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this[data + 'Loading'] = true
        this[data].list = res.rows
        this[data].total = res.total
        this.$nextTick(() => {
          this[data + 'Loading'] = false
        })
      })
    },
    getOption(data) {
      tableDropDownBox({
        modelId: this.modelObj.modelId,
        modeVersion: this.modelObj.modelVersion,
        attribute: 'A_RES_ID',
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        if (res.total > 0) {
          this[data].list = res.rows
          this[data].total = res.total
          this.$nextTick(() => {
            this[data + 'Loading'] = false
          })
        }
      })
    },
    //重置搜索
    resetSearch() {
      this.searchForm.NAME = ''
      this.searchForm.A_TCSITE_ID = ''
      this.searchForm.Z_TCSITE_ID = ''
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
