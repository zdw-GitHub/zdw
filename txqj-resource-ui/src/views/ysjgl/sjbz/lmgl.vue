<template>
    <div class="lmgl">
      <div class="search" style="">
        <el-form ref="selForm" style="width: 80%;display: flex;" size="small" :model="selForm" label-width="130px">
          <el-form-item label="类目名称">
            <el-input v-model="selForm.categoryName" style="width:200px"></el-input>
          </el-form-item>
          <el-form-item  label="类目中文名称">
            <el-input v-model="selForm.categoryNameCn" style="width:200px"></el-input>
          </el-form-item>
          <el-form-item style="margin-left: 100px">
            <el-button size="mini" plain @click="getCategoryList"  type="info" icon="el-icon-search" v-hasPermi="['mm:category:list']">搜索</el-button>
            <el-button size="mini" plain @click="openAdd"  type="primary" icon="el-icon-plus" v-hasPermi="['mm:category:add']">新增</el-button>
            <el-button size="mini" plain @click="getCategoryList"  icon="el-icon-refresh" >刷新</el-button>
          </el-form-item>
        </el-form>
      </div>
      <div class="tabSpace" style="margin-left:10px">
        <el-table
          ref="multipleTable"
          :data="tableData"
          stripe
          height="600px"
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
            label="类目名称"
            prop="categoryName"
            align="center"
            >
          </el-table-column>
          <el-table-column
            label="类目中文名称"
            prop="categoryNameCn"
            align="center"
            >
          </el-table-column>
          <el-table-column
            prop="remark"

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
              <el-button size="mini" type="text" @click="editColumn(scope.row)" icon="el-icon-edit" v-hasPermi="['mm:category:edit']">修改</el-button>
              <el-button size="mini" type="text" @click="delColumn(scope.row.id)" icon="el-icon-delete" v-hasPermi="['mm:category:delete']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          style="margin-right:60px"
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
        title="添加类目"
        :modal-append-to-body="true"
        :append-to-body="true"
        :close-on-click-modal="false"
      >
        <el-form ref="addForm"  :model="addForm" label-width="120px" style="text-align: center">
          <el-form-item label="类目名称">
            <el-input v-model="addForm.categoryName" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="类目中文名称" style="margin-left: 20px">
            <el-input v-model="addForm.categoryNameCn" maxlength="20"></el-input>
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
        title="修改类目"
        :modal-append-to-body="true"
        :append-to-body="true"
        :close-on-click-modal="false"
      >
        <el-form ref="editForm" :model="editForm" label-width="120px" style="text-align: center">
          <el-form-item label="类目名称">
            <el-input v-model="editForm.categoryName" maxlength="20"></el-input>
          </el-form-item>
          <el-form-item label="类目中文名称">
            <el-input v-model="editForm.categoryNameCn" maxlength="20"></el-input>
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
  name: 'lmgl',
  data () {
    return {
      addForm: {
        id: '',
        categoryName: '',
        categoryNameCn: '',
        remark: ''
      },
      selForm: {
        categoryName: '',
        categoryNameCn: ''
      },
      editForm: {
        id: '',
        categoryName: '',
        categoryNameCn: '',
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
      var ss = '?pageNum=' + this.pageIndex + '&pageSize=' + this.pageSize + '&categoryName=' + this.selForm.categoryName
      + '&categoryNameCn=' + this.selForm.categoryNameCn
      request.get('/category/list' + ss, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        /* console.log(res) */
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
      this.addForm.categoryName = ''
      this.addForm.categoryNameCn = ''
      this.addForm.remark = ''
      this.addShow = true
    },
    addCategory: function () {
      var params = {
        categoryName: this.addForm.categoryName,
        categoryNameCn: this.addForm.categoryNameCn,
        remark: this.addForm.remark
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'post',
        url: '/category',
        data: params
      }).then((res) => {
        // 请求成功处理。
        this.getCategoryList()
        this.addShow = false
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
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    editCategory: function () {
      var params = {
        id: this.editForm.id,
        categoryName: this.editForm.categoryName,
        categoryNameCn: this.editForm.categoryNameCn,
        remark: this.editForm.remark
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/category',
        data: params
      }).then((res) => {
        // 请求成功处理。
        this.getCategoryList()
        this.editShow = false
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
          center: true,
            duration: 0,
            showClose: true
        })
      })
    },
    editColumn: function (row) {
      request.get('/category/' + row.id, qs.stringify({
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
        request.delete('/category/' + id).then((res) => {
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
  <style lang="stylus" rel="stylesheet/stylus" scoped>
    .lmgl
      width 100%
      .tabSpace
        height 600px
    .pagination-container
      margin-top -45px
    .el-table::before {
        left: 0;
        bottom: 0;
        width: 100%;
    }
  </style>
