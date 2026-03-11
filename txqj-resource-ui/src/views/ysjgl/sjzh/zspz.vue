<template>
    <div class="zspz">
      <div class="zspz_space">
        <div class="con_left">
          <el-input
            placeholder="元模型名称"
            v-model="filterText" style="margin-bottom: 10px">
          </el-input>
          <el-tree
            class="filter-tree"
            :data="data"
            highlight-current
            node-key="id"
            :props="defaultProps"
            :default-expanded-keys="idArr"
            :filter-node-method="filterNode"
            ref="tree" style="font-size: 14px;height: 80vh;overflow-y: auto">
            <span style="width: 100%;height:100%;display: block" class="custom-tree-node" slot-scope="{ node, data }">
              <span style="width: 100%;display: block;line-height: 25px" @click="searchTable(data)">
                  <i :class="data.icon" :style="data.color"></i>{{data.label}}
              </span>
          </span>
          </el-tree>
        </div>
        <div class="con_right">
        <div class="r_top">
          <div class="zspztit">
            搜索结果
          </div>
          <div class="table_space" style="height: 150px;">
            <el-form :model="tableForm">
              <div class="el_group" style="margin-top: 20px">
                <el-form-item style="flex: 12" label="名称：">
                 <span class="form">{{tableForm.tableName}}</span>
                </el-form-item>
                <el-form-item style="flex: 12" label="中文名称：">
                    <span class="form">{{tableForm.tableNameCn}}</span>
                </el-form-item>
              </div>
              <div class="el_group" style="margin-top: 20px">
                <el-form-item style="flex: 12" label="所属类目：">
                    <span class="form">{{categoryName}}</span>
                </el-form-item>
                <el-form-item style="flex: 12" label="创建时间：">
                    <span class="form">{{tableForm.createDate}}</span>
                </el-form-item>

              </div>
            </el-form>
          </div>
        </div>
        <div class="r_bot">
          <div class="zspztit cell">
            详细信息
            <div class="cell" style="float: right;width: 50%;text-align: right">
              <el-form style="margin-right:20px;display: inline;" ref="selForm" :model="selForm" label-width="80px">
                <el-input style="width: 300px;height: 30px" v-model="selForm.cluName" placeholder="请输入属性名"></el-input>
              </el-form>
              <el-button  type="info" icon="el-icon-search" size="mini" plain @click="getTabData()">搜索</el-button>
              <el-button  icon="el-icon-refresh" size="mini" plain @click="getTabData()">刷新</el-button>
            </div>
          </div>
          <div class="table_space">
            <el-table :data="tableData" stripe height="520px" tooltip-effect="dark" :header-cell-style="{background:'#F5F5F5'}">
              <el-table-column label="序号" width="100" align="center" :formatter="snFormatter">
              </el-table-column>
              <el-table-column prop="columnName" align="center" label="属性名称">
              </el-table-column>
              <el-table-column prop="columnNameCn" align="center" label="中文名">
              </el-table-column>
              <el-table-column prop="columnType" align="center" label="属性类型" width="120">
              </el-table-column>
              <el-table-column prop="columnLength" align="center" label="属性长度" width="120">
              </el-table-column>
              <el-table-column prop="isQuery" align="center" label="是否查询" width="120">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-select v-model="scope.row.isQuery" clearable v-show="scope.$index == cellIndex" style="width:100px">
                    <el-option value="1" label="是"></el-option>
                    <el-option value="0" label="否"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isQuery==='1'">是</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isQuery==='0'">否</p>
                </template>
              </el-table-column>
              <el-table-column prop="isVisible" align="center" label="是否显示" width="120">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-select v-model="scope.row.isVisible" clearable v-show="scope.$index == cellIndex" style="width:100px">
                    <el-option value="1" label="是"></el-option>
                    <el-option value="0" label="否"></el-option>
                  </el-select>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isVisible==='1'">是</p>
                  <p v-show="scope.$index != cellIndex" v-if="scope.row.isVisible==='0'">否</p>
                </template>
              </el-table-column>
              <el-table-column prop="sortNum" align="center" label="显示顺序" width="120">
                <template slot-scope="scope">
                  <!-- scope.$index为当前行下标，cellIndex所点击行下标 -->
                  <el-input v-model="scope.row.sortNum" clearable v-show="scope.$index == cellIndex">
                  </el-input>
                  <p v-show="scope.$index != cellIndex">{{ scope.row.sortNum }}</p>
                </template>
              </el-table-column>
              <el-table-column fixed="right" label="操作" width="181" align="center">
                <template slot-scope="scope">
                  <el-button @click="editTable(scope)" size="mini" type="text" icon="el-icon-edit"
                    v-if="scope.$index != cellIndex">编辑</el-button>
                  <el-button @click="saveTable(scope)" size="mini" type="text" v-else icon="el-icon-circle-check">保存</el-button>
                  <el-button type="danger" v-show="false" @click="deletTable(scope)" size="small" icon="el-icon-delete" v-if="scope.$index != cellIndex">删除</el-button>
                  <el-button @click="cellIndex = null" size="mini" type="text" v-else icon="el-icon-circle-close">取消</el-button>
                </template>
                </el-table-column>
            </el-table>
          </div>
        </div>
      </div>
        </div>
      </div>
  </template>
<script>
import request from '@/utils/request'
import qs from 'qs'

export default {
  name: 'ymx',
  data () {
    return {
      cellIndex: null,
      filterText:'',
      versionObj:{},
      total: 0,
      tabName: 'Tab1',
      ifSel: true,
      glRelationTableName: '',
      glTableCluName: '',
      selForm: {
        cluName: ''
      },
      dbList: [],
      tableList: [],
      tableStatusList: [
        {id: '0', statusName: '未发布'},
        {id: '1', statusName: '发布'}
      ],
      seeClu: true,
      seeAlias: false,
      modelId: '',
      tableData: [],
      tableDataClu: [],
      glTableCluFormD: {},
      tableForm: {
      },
      categoryName:'',
      labelNameList: {
      },
      categoryList: {
      },
      data: [{
        id: '0',
        label: '元模型',
        icon: 'el-icon-s-home',
        color: 'color: #FFC000',
        children: []
      }],
      idArr:[],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      columnTypeList: [
      ]
    }
  },
  watch: {
    filterText (val) {
      this.$refs.tree.filter(val)
    }
  },
  methods: {
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return index + 1
    },
    // 编辑操作
    editTable ({ $index }) {
      // 获取当前点击行下标
      this.cellIndex = $index
    },
    // 保存操作
    saveTable ({ row }) {
      var param = {
        sortNum: row.sortNum,
        isQuery: row.isQuery,
        isVisible: row.isVisible,
        columnId: row.columnId,
        id: row.id,
        modelVersion:this.versionObj.label,
        modelId:this.versionObj.parentId,
      }
      request({
        headers: {
          'Content-Type': 'application/json'
        },
        method: 'put',
        url: '/show',
        data: param
      }).then((res) => {
        // 请求成功处理。
        this.getTabData()
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
      this.cellIndex = null
    },
    filterNode (value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    searchTable: function (res) {
      this.versionObj={}
      this.tableData=[]
      this.tableForm={}
      this.categoryName=''
      if(res.type==='versionT'){
        this.versionObj=res
        this.getTabData()
        this.getFormData()
      }
    },
    getTree: function () {
      this.idArr=[]
      request.get('/model/treewithversion', qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res=res.data
        this.data[0].children = res
        this.idArr=['0']
      }).catch(e => {
        // 请求失败处理。
        this.$message({
        })
      })
    },
    // 获取表基本信息
    getTabData: function () {
      request.get('show/listwithcolumninfo' + '?modelId=' + this.versionObj.parentId + '&columnName=' + this.selForm.cluName + '&modelVersion='+this.versionObj.label, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        this.tableData = res.rows
      }).catch(e => {
        // 请求失败处理。
      })
    },
    // 获取表单基本信息
    getFormData: function () {
      request.get('model/' + this.versionObj.parentId, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data
        this.tableForm = res
        if(res.categoryNameId!==''&&res.categoryNameId!==undefined){
          //获取所属类目名称
          request.get('category/' + res.categoryNameId, qs.stringify({
          })).then((ress) => {
            // 请求成功处理。
            //res.categoryName = ress.data.categoryNameCn
            this.tableForm.categoryName = ress.data.categoryNameCn
            this.categoryName=ress.data.categoryNameCn
            //获取所属类目名称
          }).catch(e => {
            // 请求失败处理。
          })
        }

      }).catch(e => {
        // 请求失败处理。
      })
    },

  },
  components: {
  },
  beforeMount () {
    this.getTree()
  }
}
</script>
<style lang="stylus" rel="stylesheet/stylus">
      .zspz
        width 100%
        height 100%
        -moz-user-select: none; /*火狐*/
        -webkit-user-select: none; /*webkit浏览器*/
        -ms-user-select: none; /*IE10*/
        user-select: none;/*选中文字时避免出现蓝色背景*/
        .cell >.el-button
          padding 5px 10px !important
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
        .zspz_space
          width 100%
          height 100%
          display flex
          .con_left
            flex 1
            overflow auto
            flex-shrink: 0
            height 100%
            margin 20px
            .left_con
              padding 0.2rem
              font-size: 14px
              line-height 20px
              border-bottom 1px solid lightgrey
              .l_con
                margin-top: 10px
                text-indent 4px
          .con_right
            flex 5
            flex-shrink: 0
            margin 10px
            .table_space
              width 98%
              margin auto
              .el_group
                display flex
                width 80%
                margin auto
                .form
                  font-size:16px
          .zspztit
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
