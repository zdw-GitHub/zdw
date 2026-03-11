<template>
    <div class="czysj">
      <div class="qjcx_con">
        <div class="con_right">
          <div class="r_top">
            <div class="cell" style="width: 50%;text-align: right;position:absolute;top:20px;right:20px">
                <!-- <el-button type="primary" @click="uploadTable()" icon="el-icon-upload2">发布</el-button>
                <el-button type="primary" @click="addTable()" icon="el-icon-plus">新增</el-button> -->
                <!-- <el-button :disabled="ifSel" type="primary" @click="saveTabData()" icon="el-icon-circle-check" style="position:absolute;margin-top:202px;right:171px">保存</el-button> -->
                <!-- <el-button :disabled="ifSel" type="primary" @click="getTabData()" icon="el-icon-circle-close">复制</el-button>
                <el-button :disabled="ifSel" type="primary" @click="delTable()"  icon="el-icon-delete">删除</el-button> -->
              </div>

            <div class="table_space" style="height: 130px;display: flex" >
              <el-form ref="selForm"  :model="tableForm" >
                <div class="el_group" style="margin-top: 20px">
                  <el-form-item  style="flex: 12" label="操作人">
                    <el-input  v-model="selForm.operator"></el-input>
                  </el-form-item>
                  <el-form-item label="操作时间">
                    <el-date-picker
                      v-model="selForm.daterange"
                      type="daterange"
                      range-separator="-"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期">
                    </el-date-picker>
                  </el-form-item>
                </div>
                <div class="el_group">
                  <el-form-item style="flex:12" label="操作类型">
                    <el-select filterable v-model="selForm.operateTypeNo" placeholder="请选择" @change="this.$forcedUpdate()">
                      <el-option
                        v-for="item in typeList"
                        :key="item.pubName"
                        :label="item.pubName"
                        :value="item.pubName">
                      </el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item style="flex: 12" label="操作属性">
                    <el-input  v-model="selForm.operatorColumn"></el-input>
                  </el-form-item>
                </div>
              </el-form>
            </div>
          </div>
          <div class="r_bot">
              <div class="cell" style="position:absolute;right:50px;top:300px">
                <el-button :disabled="ifSel" plain style="padding:5px 25px;width:80px" type="primary" @click="getData()" icon="el-icon-search">搜索</el-button>
                <el-button :disabled="ifSel" plain style="padding:5px 25px;width:80px" @click="resetSearchForm()" type="primary" icon="el-icon-refresh">重置</el-button>
            </div>
            <div class="table_space">
              <el-table
                :data="tableData"
                stripe
                height="350px"
                tooltip-effect="dark"
                :header-cell-style="{background:'#F5F5F5'}">
                <el-table-column
                  label="序号"
                  width="150"
                  align="center"
                  :formatter="snFormatter">
                </el-table-column>
                <el-table-column
                  prop="operator"
                  align="center"
                  label="操作人"
                  width="180">
                  <template slot-scope="scope">
                    <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                    <el-input
                        v-model="scope.row.operator"
                        
                        v-show="scope.$index == cellIndex">
                    </el-input>
                    <p v-show="scope.$index != cellIndex">{{ scope.row.operator }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                  prop="operatorTime"
                  align="center"
                  label="操作时间"
                  width="230">
                  <template slot-scope="scope">
                    <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                    <el-input
                        v-model="scope.row.operatorTime"
                        
                        v-show="scope.$index == cellIndex">
                    </el-input>
                    <p v-show="scope.$index != cellIndex">{{ scope.row.operatorTime }}</p>
                    </template>
                </el-table-column>
                <el-table-column
                  prop="operateTypeNo"
                  align="center"
                  label="操作类型"
                  width="250">
                </el-table-column>
                <el-table-column
                  prop="operatorColumn"
                  align="center"
                  label="操作属性"
                  width="258">
                </el-table-column>
                <el-table-column
                  prop="operateContent"
                  label="操作信息"
                  width="250"
                  align="center">
                  </el-table-column>
              </el-table>
              <div style="text-align: right;margin-top: 5px">
                <el-pagination
                  @size-change="handleSizeChange"
                  @current-change="handleCurrentChange"
                  :page-sizes="[10, 20, 30, 40]"
                  :page-size="pageSize"
                  layout="total, sizes, prev, pager, next"
                  :total="total">
                </el-pagination>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div>

        <div class="addGl">
        </div>
      </div>
    </div>
  </template>
<script>
  import request from '@/utils/request'
import qs from 'qs'

export default {
  name: 'czysj',
  props: {
    modelId: String,
    required: true
  },
  data () {
    return {
      selForm: {
        operator: '',
        daterange: [],
        operateTypeNo: '',
        operatorColumn: ''
      },
      typeList: [
        {pubName: '增'},
        {pubName: '删'},
        {pubName: '改'},
        {pubName: '发布'},
        {pubName: '保存'}
      ],
      pageSize: 10,
      pageIndex: 1,
      total: 0,
      tableData: []
    }
  },
  watch: {
  },
  methods: {
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return (this.pageIndex - 1) * this.pageSize + index + 1
      /* return index + 1 */
    },
    handleSizeChange (val) {
      this.pageSize = val
      this.getCategoryList()
    },
    handleCurrentChange (val) {
      this.pageIndex = val
      this.getCategoryList()
    },
    // 重置搜索
    resetSearchForm () {
      this.selForm = {
        operator: '',
        daterange: [],
        operatorColumn: '',
        operateTypeNo: ''
      }
    },
    /* 格式所属表列 */
    tableIdFormatter: function (row, col, value, index) {
      for (var i = 0; i < this.columnTableList.length; i++) {
        if (this.columnTableList[i].id === value) {
          return this.columnTableList[i].tableName
        }
      }
      return value
    },
    getTableList: function (type) {
      this.tableList = []
      if (type === 0 || type === '0') {
        this.tableForm.relationTableId = ''
      }
      if (this.tableForm.relationDbId !== undefined) {
        request.get(localStorage.URL + '/dbtable/list?dbId=' + this.tableForm.relationDbId, qs.stringify({
        })).then((res) => {
          // 请求成功处理。
          this.tableList = res.data
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }
    },
    searchTable: function (id, nodeType) {
      if (nodeType === 'MODEL') {
        this.modelId = id
        this.selForm.cluName = ''
        this.getData()
        this.getTabData()
        this.ifSel = false
      }
    },
    // 获取表属性列表
    getData: function () {
      let params = {
        pageNum: this.pageIndex,
        pageSize: this.pageSize,
        operator: this.selForm.operator,
        operatorColumn: this.selForm.operatorColumn,
        operateTypeNo: this.selForm.operateTypeNo,
        modelId: this.modelId
      }
      if (this.selForm.daterange.length && this.selForm.daterange.length > 0) {
        params.startTime = this.selForm.daterange[0]
        params.endTime = this.selForm.daterange[1]
      }
      let str = '?' + qs.stringify(params)
      request.get('/log/list' + str).then((res) => {
        // 请求成功处理。
        this.tableData = res.rows
        this.total = res.total
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    }
  },
  beforeMount () {
    this.ifSel = false
    this.getData()
  }
}
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
    .cell >.el-button
      padding 5px 10px !important
    .czysj
      width 100%
      height 100%
      .cell .el-input__inner
        height 30px
      .aa .el-input,.aa .el-form-item__content,.aa .el-input__inner
        width  3rem !important
      .el-dialog
        width 10rem
        height auto
        margin 11% 36%
      .importBox .el-dialog
        width 10rem
        height 5rem
        margin 11% 36%
      .importDatas .el-dialog
        width 18rem
        margin auto
        .el-select
          width 2.5rem
      .addGl .el-dialog
        width 19rem
        height 14rem
        margin 2rem 11rem
      .openClu .el-dialog
        width 14rem
        height 9rem
        margin 2rem 11rem
      input[readonly='readonly']
        background #F5F7FA
        color #CAC4CC
      .el-select input[readonly='readonly']
        background #fff
        color #000
      .el-button
        padding: 5px 30px
      .el-input__inner
        height 40px
      .qjcx_con
        width 100%
        height 100%
        display flex
        .con_left
          flex 6
          width:0
          overflow auto
          flex-shrink: 0
          height 100%
          border-right 1px solid lightgrey
          .left_con
            padding .2rem 0
            font-size: 14px
            line-height 20px
            border-bottom 1px solid lightgrey
            .l_con
              margin-top: 10px
              text-indent 4px
        .con_right
          flex 18
          flex-shrink: 0
          width:0
          .table_space
            width 100%
            margin auto
            .el_group
              display flex
              width 90%
              margin auto
        .tit
          font-size 16px
          height 40px
          line-height 40px
          text-indent 1%
          margin-bottom 10px
          border-bottom 1px solid lightgrey
        .tabBarArea {
            position: relative;
          }
        .tabBarArea:after{
            display: block;
            content: '';
            clear: both;
          }
        .tabBarBtn {
            float: left;
            margin-left: 20px;
            cursor: pointer;
            white-space:nowrap;
            width: auto;
            height:35px
            padding: 0px;
          }
        .tabBarBtn:hover {
            color: #326BE8;
            border-bottom: 2px solid #326BE8;
            border-top:0px;
            border-left:0px;
            border-right:0px;
          }
        .tabBarBtnActive {
            color: #326BE8;
            border-bottom: 2px solid #326BE8;
            border-top:0px;
            border-left:0px;
            border-right:0px;
          }
        .tabBarBtnActive:hover {
            color: #326BE8;
          }
  </style>
  