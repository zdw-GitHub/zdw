<template>
  <div class="commonWh">
    <div style="margin: 10px">
    </div>
    <el-table
      stripe
      border
      style="width: 100%"
      height="calc(100% - 60px)"
      ref="multipleTable"
      :data="tableData"
      v-loading="loading">
      <el-table-column label="序号" width="80" align="center" :formatter="snFormatter" ></el-table-column>
      <template
        v-if="tableData[0]"
        v-for="item in tableColData"
      >
        <el-table-column
          :label="item.name"
          :min-width="getColSize(tableData[0][item.prop], item.name)"
          align="center"
          show-overflow-tooltip
          :prop="item.prop"
        ></el-table-column>
      </template>
      <el-table-column label="操作" width="100"  fixed="right" align="center">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="lookData(scope.row)" v-hasPermi="['system:b:list']">查看</el-button>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="total"
      :page.sync="pageNum"
      :limit.sync="pageSize"
      @pagination="getTableData"
    />
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="查看"
      :modal="false"
      :visible.sync="stationDrawer"
      width="1800px"
      :close-on-click-modal="false"
    >
      <seeData ref="seeChild" :modelObj="modelObj" :dataObj="editForm"></seeData>
      <div slot="footer" class="dialog-footer" style="margin-bottom:20px">
        <el-button @click="stationDrawer=false">关 闭</el-button>
      </div>
    </el-dialog>
  </div >
</template>

<script>
  import {nameTo_} from "@/utils"
  import {getColSize} from "@/utils/getColSize"
  import{getModelId} from "@/api/zdwh/zdzywh";
  import seeData from "@/views/publicPage/edit/seeData";
  import {
    getGlTableApi,
  } from '@//api/serviceInformation'
  export default {
    name: "GL",
    components: {seeData},
    props: {
      llId: String,
      ywId: String,
    },
    data(){
      return {
        tableColData: [
          {name: '名称', prop: 'name'},
          {name: '描述', prop: 'des'},
          {name: '起始设备', prop: 'aResId'},
          {name: '终止设备', prop: 'zResId'},
          {name: '运行监视单位', prop: 'operationMonitoring'},
          {name: '调管单位', prop: 'authorityUnit'},
          {name: '维护单位', prop: 'manageDeptId'},
          {name: '创建时间', prop: 'createDate'},
          {name: '变更时间', prop: 'updateDate'},
        ],
        loading:false,
        tableName:'SG_TCCON_TCCHANNELFIBER_B',
        //表对象信息
        modelObj: {},
        //表的分页
        pageSize: 15,
        pageNum: 1,
        total: 0,
        //  表格数据
        tableData:[],
        stationDrawer:false,
        //  单条数据
        editForm:{},
        ifFirstSee: true,
      }
    },
    watch: {
      //解决操作栏固定后表格错位问题
      'tableData': {
        handler() {
          this.$nextTick(() => {
            this.$refs.multipleTable.doLayout();
          })
        },
        deep: true
      },
    },
    created(){
      this.getCommonData()
    },
    methods:{
      getColSize (val, titVal) {
        return getColSize(val, titVal)
      },
      /* 格式序列化列 */
      snFormatter: function (row, col, value, index) {
        return this.pageSize * (this.pageNum - 1) + index + 1;
      },
      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
        })
      },
      /*获取表格数据*/
      getTableData(){
        this.loading = true;
        let params={
          parChannel: this.llId,
          pageIndex: this.pageNum,
          pageSize: this.pageSize,
        }
        getGlTableApi(params).then(response => {
          this.tableData = response.rows;
          this.total = response.total;
          this.loading = false;
        }).catch(() => {
          this.loading = false
        });
      },
      //查看
      lookData(row){
        this.editForm = row
        for (let key in row) {
          this.editForm[nameTo_(key)] = row[key]
        }
        this.stationDrawer = true
        this.$nextTick(()=>{
          this.$refs.seeChild.modelObj=this.modelObj
          this.$refs.seeChild.dataObj=this.editForm
          if(this.ifFirstSee){
            this.$refs.seeChild.beforeLoading()
            this.ifFirstSee=false
          }else{
            this.$refs.seeChild.reOpen()
          }
        })
      },
    },
  }
</script>

