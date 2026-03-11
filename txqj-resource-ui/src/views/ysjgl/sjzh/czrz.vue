<template>
    <div class="czrz">
      <div class="search" style="margin-top:20px">
        <el-form ref="selForm" style="width: 100%" :model="selForm" label-width="140px">
            <div class="el_group">
                  <el-form-item  label="操作人">
                    <el-input v-model="selForm.operator"></el-input>
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
                <el-form-item label="操作类型" style="padding-left:50px">
                    <el-select filterable  v-model="selForm.operateTypeNo" placeholder="请选择">
                      <el-option
                        v-for="item in typeList"
                        :key="item.pubName"
                        :label="item.pubName"
                        :value="item.pubName">
                      </el-option>
                    </el-select>
                  </el-form-item>
                </div>
                <div class="el_group">
                  <el-form-item  label="操作元数据">
                    <el-input v-model="selForm.modelName" ></el-input>
                  </el-form-item>
                </div>
        </el-form>
        <el-button plain @click="getCategoryList" style="position: absolute;right: 135px;top: 30px;" type="primary" icon="el-icon-search">搜索</el-button>
        <el-button plain @click="resetSearchForm" style="position: absolute;right: 30px;top: 30px;" type="primary" icon="el-icon-refresh">重置</el-button>
      </div>
      <div class="tabSpace">
        <el-table
          :data="tableData"
          stripe
          height="100%"
          v-loading="loading"
          tooltip-effect="dark"
          style="width: 100%" :header-cell-style="{background:'#F5F5F5'}">
          <el-table-column
            label="序号"
            width="150"
            align="center"
            :formatter="snFormatter">
          </el-table-column>
          <el-table-column
            label="操作元数据"
            prop="modelName"
            align="center"
            width="250">
          </el-table-column>
          <el-table-column
            label="操作属性"
            prop="operatorColumn"
            align="center"
            width="250">
          </el-table-column>
          <el-table-column
            label="操作类型"
            prop="operateTypeNo"
            align="center"
            width="250">
          </el-table-column>
          <el-table-column
            prop="operateContent"
            width="280"
            align="center"
            label="操作信息">
          </el-table-column>
          <el-table-column
            label="操作人"
            prop="operator"
            align="center"
            width="250">
          </el-table-column>
          <el-table-column
            label="操作时间"
            prop="operatorTime"
            align="center"
            width="250">
          </el-table-column>
          <!-- <el-table-column
            prop="createTime"
            width="300"
            align="center"
            label="创建时间">
          </el-table-column>
          <el-table-column
            prop="updateTime"
            width="300"
            align="center"
            label="修改时间">
          </el-table-column> -->
          <!-- <el-table-column
            label="操作"
            width="210"
            align="center">
            <template slot-scope="scope">
              <el-button type="primary" @click="editColumn(scope.row)" icon="el-icon-edit"></el-button>
              <el-button type="primary" @click="delColumn(scope.row.id)" icon="el-icon-delete"></el-button>
            </template>
          </el-table-column> -->
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
    </div>
  </template>
<script>
import request from '@/utils/request'
import qs from 'qs'
export default {
  name: 'czrz',
  data () {
    return {
      selForm: {
        operator: '',
        daterange: [],
        operatorColumn: '',
        operateTypeNo: ''
      },
      pageSize: 10,
      pageIndex: 1,
      total: 0,
      loading: true,
      tableData: [],
      typeList: [
        {pubName: '增'},
        {pubName: '删'},
        {pubName: '改'},
        {pubName: '发布'},
        {pubName: '保存'}
      ]
    }
  },
  methods: {
    // 重置搜索
    resetSearchForm () {
      this.selForm = {
        operator: '',
        daterange: [],
        operatorColumn: '',
        operateTypeNo: ''
      }
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return (this.pageIndex - 1) * this.pageSize + index + 1
    /*  return index + 1 */
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
      this.tableData = []
      let params = {
        pageNum: this.pageIndex,
        pageSize: this.pageSize,
        operator: this.selForm.operator,
        modelName: this.selForm.modelName,
        operateTypeNo: this.selForm.operateTypeNo
      }
      if (this.selForm.daterange.length && this.selForm.daterange.length > 0) {
        params.startTime = this.selForm.daterange[0]
        params.endTime = this.selForm.daterange[1]
      }
      let str = '?' + qs.stringify(params)
      request.get('/log/list' + str).then((res) => {
        // 请求成功处理。
        this.loading = false
        this.tableData = res.rows
        this.total = res.total
      }).catch(e => {
        // 请求失败处理。
      })
    }
  },
  created () {
    this.getCategoryList()
  }
}
</script>
  <style lang="stylus" rel="stylesheet/stylus" scoped>
    .czrz
      width 100%
      .el-dialog
        width 10rem
        height 6rem
        margin-left 38%
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
        margin-bottom 10px
      .tabSpace
        height 645px
      .el-form-item{
        margin-right:30px !important;
        }
      .el_group
        display flex
        width 100%
        margin auto
      .el-button{
          width:80px
          font-size: 12px;
          padding: 5px 10px
        }
  </style>
