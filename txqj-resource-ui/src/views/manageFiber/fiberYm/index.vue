<template>
  <div class="commonWh">
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="120px">
            <el-form-item label="光缆名称 : " prop="fiberName">
              <el-input
                size="mini"
                v-model="queryParams.fiberName"
                placeholder="请输入光缆名称"
              ></el-input>
            </el-form-item>
            <el-form-item label="是否关联 : " prop="isAssociation">
              <el-select
                size="mini"
                v-model="queryParams.isAssociation"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in associationTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="是否存在故障 : " prop="isFault">
              <el-select
                size="mini"
                v-model="queryParams.isFault"
                placeholder="请选择"
              >
                <el-option
                  v-for="item in isFaultTypes"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item style="float: right;">
              <el-button type="primary" size="mini" @click="handleQuery">查询</el-button>
              <el-button size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="tableBox">
        <el-table
          stripe
          border
          height="calc(100% - 30px)"
          :data="fiberList"
          v-loading="loading">
          <el-table-column
            label="序号"
            width="80"
            align="center"
            :formatter="snFormatter"/>
          <el-table-column
            show-overflow-tooltip
            label="光缆名称"
            align="center"
            min-width="300"
            prop="ln_name"/>
          <el-table-column
            show-overflow-tooltip
            label="起始站点"
            align="center"
            min-width="200"
            prop="from_sta_name"/>
          <el-table-column
            show-overflow-tooltip
            label="终点站点"
            align="center"
            min-width="200"
            prop="to_sta_name"/>
          <el-table-column
            show-overflow-tooltip
            label="光缆长度"
            align="center"
            min-width="100"
            prop="ln_length"/>
          <el-table-column
            show-overflow-tooltip
            label="芯数"
            align="center"
            min-width="100"
            prop="total_cores"/>
          <el-table-column
            show-overflow-tooltip
            label="投运时间"
            align="center"
            min-width="200"
            prop="startOn"/>
          <el-table-column
            show-overflow-tooltip
            label="关联光缆"
            align="center"
            min-width="300"
            prop="ty_name">
            <template slot-scope="scope">
              <a
                class="linkColumn"
                @click="getRouterLink(scope.row)"
              >
                {{scope.row.ty_name}}
              </a>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" min-width="200">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" @click.native="openInfo(scope.row)">详情</el-button>
              <el-button size="mini" type="primary" @click.native="show = true;id = scope.row.ln_id">关联光缆</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize"
          @pagination="getList"
        />
      </div>
    </div>
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="关联光缆"
      :visible.sync="show"
      width="80%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="width: 100%; height: 70vh;" class="commonWh">
        <el-form style="margin-bottom: 10px" ref="searchForm" label-width="120px" :model="searchForm" :inline="true">
          <el-form-item label="光缆名称 : " prop="name">
            <el-input size="mini" v-model="searchForm.name" placeholder="请输入光缆名称"></el-input>
          </el-form-item>
          <el-form-item label="起始站点 : " prop="A_RESOBJID">
            <el-select
              size="mini"
              placeholder="请选择起始站点"
              clearable
              filterable
              remote
              :loading="A_RESOBJID_Loading"
              :remote-method="(val) => remoteMethod2(val, 'A_RESOBJID')"
              v-model="searchForm.A_RESOBJID"
            >
              <el-option
                v-for="item in A_RESOBJID.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="终点站点" prop="Z_RESOBJID">
            <el-select
              size="mini"
              placeholder="请选择终点站点"
              clearable
              filterable
              remote
              :loading="Z_RESOBJID_Loading"
              :remote-method="(val) => remoteMethod2(val, 'Z_RESOBJID')"
              v-model="searchForm.Z_RESOBJID"
            >
              <el-option
                v-for="item in Z_RESOBJID.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item style="float: right;margin-left: 0!important;">
            <el-button type="primary" size="mini" @click="getTableData()">查询</el-button>
            <el-button size="mini" @click="resetTableQuery">重置</el-button>
          </el-form-item>
        </el-form>
        <el-table
          stripe
          border
          style="width: 100%"
          height="calc(100% - 60px)"
          :data="tabData"
          v-loading="tabLoading">
          <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
          <el-table-column
            label="光缆名称"
            align="center"
            show-overflow-tooltip
            min-width="300"
            prop="FIBER_NAME"
          ></el-table-column>
          <el-table-column
            label="起始站点"
            align="center"
            show-overflow-tooltip
            prop="A_SITE_NAME"
          ></el-table-column>
          <el-table-column
            label="终点站点"
            align="center"
            show-overflow-tooltip
            prop="Z_SITE_NAME"
          ></el-table-column>
          <el-table-column
            label="光缆长度"
            align="center"
            show-overflow-tooltip
            prop="FIBER_LENGTH"
          ></el-table-column>
          <el-table-column
            label="芯数"
            align="center"
            show-overflow-tooltip
            prop="LINE_NUMBER"
          ></el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            width="100"
            align="center">
            <template slot-scope="scope">
              <el-button @click="association(scope.row.ID)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="tabTotal"
          :page.sync="searchForm.pageNum"
          :limit.sync="searchForm.pageSize"
          @pagination="getTableData"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="show = false">取 消</el-button>
      </div>
    </el-dialog>
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
        :dataObj="infoBox.data"
        :isEditMode="infoBox.isEditMode"
        tableName="ITEMLN"
        @switchingModes="switchingModes">
      </info>
    </el-dialog>
  </div>
</template>

<script>
import{getFiberList,getTyFiberList,association} from "@/api/manageFiber/YmFiber";
import {tableDropDownBox, getModelId} from "@/api/zdwh/zdzywh";
import info from '@/views/commonPage/info/index'
export default {
  name: 'fiberYm',
  components: {info},
  data() {
    return {
      queryParams: {
        pageNum: 1,
        pageSize: 15,
        fiberName: '',
        isAssociation: '',
        isFault: ''
      },
      associationTypes: [{
        value: '1',
        label: '已关联'
      }, {
        value: '0',
        label: '未关联'
      }],
      isFaultTypes: [{
        value: '1',
        label: '是'
      }, {
        value: '0',
        label: '否'
      }],
      total: 0,
      fiberList:[],
      loading: false,

      tabData:[],
      tabTotal: 0,
      tabLoading: false,
      show: false,
      modelObj: {},
      searchForm: {
        name: '',
        A_RESOBJID: '',
        Z_RESOBJID: '',
        pageSize: 15,
        pageNum: 1,
      },
      A_RESOBJID: {},
      Z_RESOBJID: {},
      A_RESOBJID_Loading: true,
      Z_RESOBJID_Loading: true,
      id: '',
      infoBox: {
        show: false,
        isEditMode: false,
        data: {},
        width: '1800px',
        class: 'framePosition',
      },
    }
  },
  watch: {},
  created() {
    this.handleQuery()
    this.getTableData()
    getModelId({tableName: 'SG_TCDEV_FIBER_B'}).then(res => {
      this.modelObj = res.data
      this.getOption('A_RESOBJID')
      this.getOption('Z_RESOBJID')
    })
  },
  methods: {
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    getList(){
      this.loading = true
      getFiberList(this.queryParams).then(res => {
        this.fiberList = res.rows;
        this.total = res.total;
        this.loading = false
      })
    },
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    openInfo(row){
      this.infoBox.show = true
      this.infoBox.width = '1600px'
      this.infoBox.class = ''
      this.infoBox.data = {ID:row.ln_id,NAME:row.ln_name,ty_id:row.ty_id}
      this.infoBox.isEditMode = false
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
    /** 重置按钮操作 */
    resetTableQuery() {
      this.resetForm("searchForm");
      this.getTableData();
    },
    association(id){
      association({id:this.id,tyId:id}).then(res => {
        this.show = false
        this.$message({
          type: 'success',
          message: '操作成功'
        })
        this.getList();
      })
    },
    getTableData(){
      this.tabLoading = true
      getTyFiberList(this.searchForm).then((res) => {
        this.tabData = res.rows
        this.tabTotal = res.total
        this.tabLoading = false
      })
    },
    remoteMethod2(val, data) {
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
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.queryParams.pageSize * (this.queryParams.pageNum - 1) + index + 1;
    },
    //光缆跳转
    getRouterLink(row){
      this.$router.push({
        path:'/fibersource/fiber',
        query:{
          search: JSON.stringify({ID:row.ty_id}),
          refresh:true,
        }
      })
    },
    switchingModes(data){
      this.infoBox.width = data ? '1800px' : '500px'
      this.infoBox.class = data ? '' : 'framePosition'
    },
  }
}
</script>
<style scoped lang="scss">
</style>
