<template>
    <div class="lsbbgl">
      <div class="search">
        <el-form ref="selForm" style="width: 100%;margin-top:20px" :model="selForm" label-width="120px">
          <div style="display:flex;flex-wrap:wrap">
          <el-form-item label="元数据" style="width:30%;margin-left:-10px">
            <el-input v-model="selForm.tableName" style="width:230px"></el-input>
          </el-form-item>
          <el-form-item label="版本号" style="width:30%">
            <el-input v-model="selForm.modelVersion" style="width:230px"></el-input>
          </el-form-item>
          <el-form-item label="更新内容" style="width:30%">
            <el-input v-model="selForm.updateLog" style="width:230px"></el-input>
          </el-form-item>
          <el-form-item label="更新人" style="width:30%">
            <el-input v-model="selForm.operator" style="width:230px"></el-input>
          </el-form-item>
          <el-form-item label="发布时间" style="margin-left:-10px">
            <el-date-picker
                      v-model="selForm.daterange"
                      type="daterange"
                      range-separator="-"
                      start-placeholder="开始日期"
                      end-placeholder="结束日期">
                    </el-date-picker>
          </el-form-item>
        </div>
        </el-form>
        <el-button @click="getCategoryList" plain style="width:60px;position: absolute;right: 105px;top: 30px;" type="primary" icon="el-icon-search">搜索</el-button>
        <el-button @click="getCategoryList" plain style="width:60px;position: absolute;right: 30px;top: 30px;" type="primary" icon="el-icon-refresh">刷新</el-button>
      </div>
      <div class="tabSpace">
        <el-table
          ref="multipleTable"
          :data="tableData"
          stripe
          height="100%"
          v-loading="loading"
          tooltip-effect="dark"
          style="width: 100%" :header-cell-style="{background:'#F5F5F5'}">
          <el-table-column
            label="序号"
            width="200"
            align="center"
            :formatter="snFormatter">
          </el-table-column>
          <el-table-column
            label="元数据"
            prop="tableName"
            align="center"
            width="190">
          </el-table-column>
          <el-table-column
            label="版本号"
            prop="modelVersion"
            align="center"
            width="250">
          </el-table-column>
          <el-table-column
            prop="updateLog"
            width="240"
            align="center"
            label="更新内容">
          </el-table-column>
          <el-table-column
            prop="operator"
            width="270"
            align="center"
            label="更新人">
          </el-table-column>
          <el-table-column
            prop="createDate"
            width="270"
            align="center"
            label="更新时间">
          </el-table-column>
          <el-table-column
            label="操作"
            width="250"
            align="center">
            <template slot-scope="scope">
              <!-- <el-button type="primary" @click="editColumn(scope.row)" icon="el-icon-edit"></el-button>
              <el-button type="primary" @click="delColumn(scope.row.id)" icon="el-icon-delete"></el-button> -->
              <el-button type="primary" plain @click="searchData(scope.row)">查看</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
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
      <el-dialog
        :visible.sync="searchShow"
        top="13%"
        title="查看"
        style="wifth"
      > 
      </el-dialog>
    </div>
  </template>
  
<script>
import request from '@/utils/request'
import qs from 'qs'
export default {
  name: 'lsbbgl',
  data () {
    return {
      addForm: {
        id: '',
        labelCode: '',
        labelName: '',
        remark: ''
      },
      selForm: {
        tableName: '',
        modelVersion: '',
        updateLog: '',
        operator: '',
        daterange: []
      },
      editForm: {
        id: '',
        labelCode: '',
        labelName: '',
        remark: ''
      },
      addShow: false,
      editShow: false,
      searchShow: false,
      pageSize: 10,
      pageIndex: 1,
      total: 1,
      loading: false,
      tableData: [
        {labelName: '222'}
      ]
    }
  },
/*   components: {
    ysjgl
  }, */
  methods: {
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      // return this.pageNumber * (this.pageSize - 1) + index + 1;
      return index + 1
    },
    handleSizeChange (val) {
      this.pageSize = val
      this.getCategoryList()
    },
    handleCurrentChange (val) {
      this.pageIndex = val
      this.getCategoryList()
    },
    getCategoryList: function () {
      /* this.loading = true */
      let params = {
        pageNum: this.pageIndex,
        pageSize: this.pageSize,
        operator: this.selForm.operator,
        tableName: this.selForm.tableName,
        modelVersion: this.selForm.modelVersion,
        updateLog: this.selForm.updateLog
      }
      if (this.selForm.daterange.length && this.selForm.daterange.length > 0) {
        params.startTime = this.selForm.daterange[0]
        params.endTime = this.selForm.daterange[1]
      } 
      let str = '?' + qs.stringify(params)
      request.get('/release/historicalVersionManagement' + str).then((res) => {
        // 请求成功处理。
        /* console.log(res) */
        this.tableData = res.rows
        this.total = res.total
        this.loading = false
      }).catch(e => {
        // 请求失败处理。
        /*  this.$message({
          message: e,
          type: 'error',
          center: true
        }) */
      })
    },
    openAdd: function () {
      this.addForm.id = ''
      this.addForm.labelCode = ''
      this.addForm.labelName = ''
      this.addForm.remark = ''
      this.addShow = true
    },
    // 查看按钮
    searchData: function (row) {
      this.searchShow = true
    },
    delColumn: function (id) {
      this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        request.delete('/label/' + id).then((res) => {
          // 请求成功处理。
          this.getCategoryList()
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  },
  beforeMount () {
    this.getCategoryList()
  }
}
</script>
  <style lang="stylus" rel="stylesheet/stylus" scoped>
    .lsbbgl
      width 100%
      .el-dialog
        width 1400px
        height auto
        margin-left:20%
      .read input[readonly='readonly']
        background #F5F7FA
        color #CAC4CC
      .el-pagination__total
        float left
        margin-left 20px
      .el-pagination__sizes
        float left
      .search .el-form
        width 100%
        display flex
        margin-bottom 10px
      .tabSpace
        height 650px
      .el-input__inner{
        width:310px !important
      }
      .el-form-item{
        margin-right:-100px !important;
      }
      .el-button{
        font-size: 12px;
        padding: 5px 10px
      }
</style>
