<template>
  <div>
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <div style="height: 40px;font-size: 20px;line-height: 30px;padding-left: 20px">关联线路：{{relationLine.lineNames}}</div>
    <el-table
      stripe
      height="calc(100% - 82px)"
      style="width: 100%;"
      ref="relationLine"
      :data="relationLine.data"
      border
      @selection-change="handleSelectionChange"
      v-loading="relationLine.loading">
      <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
      <el-table-column
        v-if="relationLine.data.length"
        v-for="item in segTableColData"
        :label="item.name"
        :min-width="getColSize(relationLine.data[0][item.prop], item.name)"
        align="center"
        show-overflow-tooltip
        :prop="item.prop"
      ></el-table-column>
      <el-table-column label="操作" width="200" fixed="right" align="center">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="associatedLines(scope.row)">关联线路</el-button>
          <el-button type="danger" size="mini" @click="delData(scope.row)">删除关联</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog
      v-dialogDrag
      title="选择线路"
      :visible.sync="selectRoute.show"
      :modal="false"
      :close-on-click-modal="false"
      width="90%">
      <div style="width: 100%; height: 70vh;" class="commonWh">
        <el-form style="margin-bottom: 10px" label-width="120px" :model="searchForm" :inline="true" @submit.native.prevent >
          <el-form-item label="线路名称 : " prop="name">
            <el-input size="mini" @keyup.enter.native="getTableData()" v-model="searchForm.name" placeholder="请输入名称名称"></el-input>
          </el-form-item>
          <el-form-item style="float: right;margin-left: 0!important;">
            <el-button type="primary"  size="mini" @click="getTableData()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          stripe
          border
          style="width: 100%"
          height="calc(100% - 60px)"
          :data="selectRoute.data">
          <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
          <el-table-column
            v-if="selectRoute.data.length"
            v-for="item in tableColData"
            :label="item.name"
            :min-width="getColSize(selectRoute.data[0][item.prop], item.name)"
            align="center"
            show-overflow-tooltip
            :prop="item.prop"
          ></el-table-column>
          <el-table-column label="操作" fixed="right" width="100" align="center">
            <template slot-scope="scope">
              <el-button @click="bindingLine(scope.row.ID)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="selectRoute.total"
          :page.sync="selectRoute.pageNum"
          :limit.sync="selectRoute.pageSize"
          @pagination="getTableData"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="selectRoute.show = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getColSize} from "@/utils/getColSize"
import {getOneLine, getNotOneLine} from '@/api/manageFiber/fiberLineRelation'
import {addSgTcdevFiberRLine,delSgTcdevFiberRLine} from "@/api/manageFiber/SgTcdevFiberRLine";

export default {
  name: "index",
  props: {
    fiberId: String
  },
  watch: {
    'relationLine.data'() {
      this.$nextTick(() => {
        this.$refs.relationLine.doLayout()
      })
    }
  },
  data() {
    return {
      segTableColData: [
        {prop: 'segName', name: '光缆段名称'},
        {prop: 'lineName', name: '关联一次线路'},
        {prop: 'aResobjid', name: '起点接续装置'},
        {prop: 'zResobjid', name: '终点接续装置'},
        {prop: 'lineNumber', name: '芯数'},
        {prop: 'fiberType', name: '光缆段类型'},
        {prop: 'serviceState', name: '运行状态'},
      ],
      tableColData: [
        {prop: 'NAME', name: '线路名称'},
        {prop: 'DEV_TYPE', name: '型号'},
        {prop: 'A_RESOBJID', name: '起点厂站'},
        {prop: 'Z_RESOBJID', name: '终点厂站'},
        {prop: 'QDTG', name: '起点杆塔'},
        {prop: 'ZDTG', name: '终点杆塔'},
        {prop: 'IS_ROAD', name: '是否有光纤'},
        {prop: 'LENGTH', name: '线路全长'},
        {prop: 'TYPE', name: '线路类型'},
        {prop: 'LAYING_TYPE', name: '架设方式'},
        {prop: 'VOLTAGE_CLASS', name: '电压等级'},
        {prop: 'PAR_GRID', name: '所属电网'},
        {prop: 'ZLJSYS', name: '所属直流极系统'},
        {prop: 'PRODUCER_NAME', name: '生产厂家'},
        {prop: 'AUTHORITY_UNIT', name: '调度机构'},
        {prop: 'BEG_TIME', name: '投运日期'},
        {prop: 'EXPIRY_DATE', name: '退役日期'},
        {prop: 'OWNER', name: '拥有者'},
        {prop: 'STAMP', name: '更新标志'},
        {prop: 'DATA_CATEGORY', name: '数据类型'},
        {prop: 'DELETE_FLAG', name: '删除标记'},
        {prop: 'ZDJG', name: '终点所属间隔'},
      ],
      relationLine: {
        data: [],
        lineNames: '',
        loading: false
      },
      selRelationLine: [],
      searchForm: {
        name: ''
      },
      selectRoute: {
        data: [],
        pageNum: 1,
        pageSize: 15,
        total: 0,
        show: false
      },
      fiberSegId: '',
    }
  },
  methods: {
    associatedLines(row){
      this.fiberSegId = row.segId
      this.selectRoute.show = true
    },
    getTable() {
      this.relationLine.loading = true
      getOneLine({
        fiberId: this.fiberId
      }).then(res => {
        this.relationLine.data = res.data.list
        this.relationLine.lineNames = res.data.lineNames
        this.relationLine.loading = false
      })
    },
    getTableData() {
      getNotOneLine({
        fiberId: this.fiberId,
        name: this.searchForm.name
      }, {
        pageNum: this.selectRoute.pageNum,
        pageSize: this.selectRoute.pageSize,
      }).then(res => {
        this.selectRoute.data = res.rows
        this.selectRoute.total = res.total
      })
    },
    async bindingLine(id) {
      addSgTcdevFiberRLine({
        lineId: id,
        fiberId: this.fiberId,
        fiberSeg: this.fiberSegId,
      }).then(response => {
        this.$message.success("绑定成功");
        this.selectRoute.show = false
        this.getTable()
      })
    },
    //删除数据
    delData(row){
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        delSgTcdevFiberRLine({
          fiberId: this.fiberId,
          fiberSeg: row.segId,
        }).then(res => {
          this.$message.success("删除成功");
          this.getTable()
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消删除' })
      })
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return this.selectRoute.pageSize * (this.selectRoute.pageNum - 1) + index + 1;
    },
    handleSelectionChange(val) {
      this.selRelationLine = val;
    },
    getColSize(val, titVal) {
      return getColSize(val, titVal)
    },

  },
  created() {
    this.getTable()
    this.getTableData()
  }
}
</script>

<style scoped>

</style>
