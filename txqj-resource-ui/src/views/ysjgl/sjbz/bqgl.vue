<template>
    <div class="bqgl">
      <div>
        <el-form ref="selForm" style="margin-top:20px;display: flex" size="small" :inline="true" :model="selForm" label-width="80px">
          <el-form-item label="标签名称" style="flex: 1">
            <el-input v-model="selForm.labelName"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button size="mini" plain @click="getCategoryList"  type="info" icon="el-icon-search" v-hasPermi="['mm:label:list']">搜索</el-button>
            <el-button size="mini" plain @click="openAdd"  type="primary" icon="el-icon-plus" v-hasPermi="['mm:label:add']">新增</el-button>
            <el-button size="mini" plain @click="getCategoryList"  icon="el-icon-refresh">刷新</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="tabSpace" style="margin-left:10px">
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
            width="220"
            align="center"
            :formatter="snFormatter">
          </el-table-column>
          <el-table-column
            label="标签编码"
            prop="labelCode"
            align="center"
            width="400">
          </el-table-column>
          <el-table-column
            label="标签名称"
            prop="labelName"
            align="center"
            width="450">
          </el-table-column>
          <el-table-column
            prop="remark"
            width="400"
            align="center"
            label="备注">
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
          <el-table-column
            label="操作"
            width="210"
            align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="editColumn(scope.row)" icon="el-icon-edit" v-hasPermi="['mm:label:edit']">修改</el-button>
              <el-button size="mini" type="text" @click="delColumn(scope.row.id)" icon="el-icon-delete" v-hasPermi="['mm:label:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          v-show="total>0"
          :total="total"
          :page.sync="pageIndex"
          :limit.sync="pageSize"
          @pagination="getCategoryList"
        />
      </div>
      <el-dialog
        :visible.sync="addShow"
        top="13%"
        width="500px"
        title="添加标签"
        :close-on-click-modal="false"
      >
        <el-form ref="addForm" :model="addForm" label-width="80px" style="text-align: center">
          <el-form-item label="标签编码">
            <el-input v-model="addForm.labelCode" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="标签名称">
            <el-input v-model="addForm.labelName" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model="addForm.remark"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="addCategory()" >确 定</el-button>
          <el-button @click="addShow= false">取 消</el-button>
        </div>
      </el-dialog>
      <el-dialog
        :visible.sync="editShow"
        top="13%"
        width="500px"
        title="修改标签"
        :close-on-click-modal="false"
      >
        <el-form ref="editForm" :model="editForm" label-width="80px" style="text-align: center">
          <el-form-item label="标签编码">
            <el-input v-model="editForm.labelCode" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="标签名称">
            <el-input v-model="editForm.labelName" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input type="textarea" v-model="editForm.remark"></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button  type="primary" @click="editCategory()" >确 定</el-button>
          <el-button @click="editShow= false">取 消</el-button>
        </div>
      </el-dialog>
    </div>
  </template>

<script>
import request from '@/utils/request'
import qs from 'qs'
export default {
  name: 'bqgl',
  data () {
    return {
      addForm: {
        id: '',
        labelCode: '',
        labelName: '',
        remark: ''
      },
      selForm: {
        labelName: ''
      },
      editForm: {
        id: '',
        labelCode: '',
        labelName: '',
        remark: ''
      },
      addShow: false,
      editShow: false,
      pageSize: 10,
      pageIndex: 1,
      total: 0,
      loading: true,
      tableData: []
    }
  },
  methods: {
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
    getCategoryList: function () {
      this.loading = true
      this.tableData = []
      var ss = '?pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize + '&labelName=' + this.selForm.labelName
      request.get('/label/list' + ss, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        console.log(res)
        this.loading = false
        this.tableData = res.rows
        this.total = res.total
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    openAdd: function () {
      this.addForm.id = ''
      this.addForm.labelCode = ''
      this.addForm.labelName = ''
      this.addForm.remark = ''
      this.addShow = true
    },
    addCategory: function () {
      var params = {
        id: this.addForm.id,
        labelCode: this.addForm.labelCode,
        labelName: this.addForm.labelName,
        remark: this.addForm.remark
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'post',
        url: '/label',
        data: params
      }).then((res) => {
        // 请求成功处理。
        this.getCategoryList()
        this.addShow = false
        if(res.code===200||res.code==='200'){
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })}
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    editCategory: function () {
      var params = {
        id: this.editForm.id,
        labelCode: this.editForm.labelCode,
        labelName: this.editForm.labelName,
        remark: this.editForm.remark
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/label',
        data: params
      }).then((res) => {
        // 请求成功处理。
        this.getCategoryList()
        this.editShow = false
        if(res.code===200||res.code==='200'){
        this.$message({
          message: res.msg,
          type: 'success',
          center: true
        })}
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    editColumn: function (row) {
      request.get('/label/' + row.id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.editForm = res.data
        this.editShow = true
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true,
            duration: 0,
            showClose: true
        })
      })
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
          if(res.code===200||res.code==='200'){
          this.$message({
            message: res.msg,
            type: 'success',
            center: true
          })}
        }).catch(e => {
          // 请求失败处理。
          this.$message({
            message: e,
            type: 'error',
            center: true,
            duration: 0,
            showClose: true
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
  <style lang="stylus" rel="stylesheet/stylus">
    .bqgl
      width 100%
      .tabSpace
        height 690px
  </style>
