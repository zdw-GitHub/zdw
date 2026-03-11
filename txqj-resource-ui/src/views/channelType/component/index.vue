<template>
  <div style="height: 100%">
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="tableName"
      :operate="{
            slot: 'handle',
            label: '操作',
            width: '200',
        }"
      :tabOperate="false"
    >
      <template v-slot:handle="slot">
        <el-button size="mini" type="primary" @click="openEdit(slot.scope)" v-hasPermi="['system:b:update']">编辑</el-button>
        <el-button type="primary" size="mini" @click="searchStation(slot.scope)" v-hasPermi="['system:b:list']">查看</el-button>
        <el-button size="mini" type="danger" @click="delData(slot.scope)" v-hasPermi="['system:b:remove']">删除</el-button>
        <el-dropdown trigger="click" style="margin-left: 5px">
          <span class="el-dropdown-link"><i class="el-icon-arrow-down el-icon--right"></i></span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item @click.native="openJump(slot.scope)">编辑沟道图</el-dropdown-item>
            <el-dropdown-item @click.native="checkUpdate(slot.scope)">绑定光缆段</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </zdzywhPage>

    <!--已选光缆段-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="已选光缆段"
      :visible.sync="checkUpdateDialog"
      width="80%"
      :close-on-click-modal="false"
      :modal="false"
      :before-close="checkUpdateDialogFalse">
      <div style="width: 100%; height: 70vh;" class="commonWh">
        <el-form>
          <el-form-item>
            <el-button type="primary" @click="openAddOpticalCable()" size="mini" v-hasPermi="['middledb:tcpipeseg:add']">新增光缆段</el-button>
          </el-form-item>
        </el-form>
        <el-table
          tooltip-effect="light"
          ref="checkUpdateMultipleTable"
          :data="checkUpdateData"
          border
          height="calc(100% - 60px)">
          <el-table-column label="序号" width="50" align="center" type="index"></el-table-column>
          <el-table-column
            prop="SEGNAME"
            label="光缆段名称"
            :min-width="500"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="ASiteName"
            label="A端站点"
            :min-width="200"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="ZSiteName"
            label="Z端站点"
            :min-width="200"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column label="操作" width="80" fixed="right" align="center">
            <template slot-scope="scope">
              <el-button size="mini" type="danger" @click="delcheckData(scope)" v-hasPermi="['middledb:tcpipeseg:removeOne']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="checkUpdateDialogFalse()">取 消</el-button>
      </div>
    </el-dialog>
    <!--新增光缆段-->
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="新增光缆段"
      :visible.sync="newCheckUpdateDialog"
      width="80%"
      :modal="false"
      :close-on-click-modal="false"
      :before-close="newCheckUpdateDialogFalse"
    >
      <div style="width: 100%; height: 70vh;" class="commonWh">
        <el-form label-width="100px" :model="queryFormLabelNewCheck" :inline="true">
          <el-form-item label="光缆段名称 : ">
            <el-input size="mini" v-model="queryFormLabelNewCheck.segName"></el-input>
          </el-form-item>
          <el-form-item style="float: right;">
            <el-button size="mini" type="primary" @click="queryNewCheckByName()">查询</el-button>
          </el-form-item>
        </el-form>
        <el-table
          tooltip-effect="light"
          :data="newCheckUpdateData"
          height="calc(100% - 60px)"
          border
          @selection-change="tcpipesegSelectionChange">
          <el-table-column type="selection" width="50"></el-table-column>
          <el-table-column label="序号" width="50" align="center" type="index"></el-table-column>
          <el-table-column
            prop="SEGNAME"
            label="光缆段名称"
            :min-width="400"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="ASiteName"
            label="A端站点"
            :min-width="200"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="ZSiteName"
            label="Z端站点"
            :min-width="200"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
          <el-table-column
            prop="PipoName"
            label="已关联沟道"
            :min-width="200"
            align="center"
            show-overflow-tooltip
          ></el-table-column>
        </el-table>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addOpticalCable()">保 存</el-button>
        <el-button @click="newCheckUpdateDialogFalse()">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  import {nameTo_, nameToTuoFeng} from "@/utils"
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {
    queryChannelType,
    querysegByPipoId,
    querysegbysiteBySiteId,
    addChannelType,
    sgTcconTcpipesegBByPipoId,
    updateChannelType,
    delTcpipeseg,
    addTcpipesegAndChannel,
    exportChannelType,
    delChannelType
  } from "@/api/channelType/channelType";
  import {getModelId,getData,getForm} from "@/api/zdwh/zdzywh";

  export default {
    name: "Zdzywh",
    components: {zdzywhPage},
    props:{
      tableName:String
    },
    data() {
      return {
        modelObj: {},
        // 已选光缆段
        querysegPageSize: 15,
        querysegPageIndex: 1,
        querysegTotal:0,
        // 未选光缆段
        querysegbysitePageSize: 15,
        querysegbysitePageIndex: 1,
        querysegbysiteTotal:0,
        tableData: [],
        tcpipesegSelection:[],

        checkUpdateDialog:false,//已选光缆段弹窗
        newCheckUpdateDialog:false,//新增光缆段弹窗
        siteId:"",//站点Id
        pipoId:"",//沟道Id
        queryFormLabelNewCheck:{
          segName:''
        },//新增光缆段查询
        newCheckUpdateData:[],
        checkUpdateData:[],
      }
    },
    watch:{
      //解决操作栏固定后表格错位问题
      'checkUpdateData':{
        handler(){
          this.$nextTick(()=>{
            this.$refs.checkUpdateMultipleTable.doLayout();

          })
        },
        deep:true
      },
    },
    created() {
      this.getCommonData()
    },
    methods: {
      //编辑
      openEdit(scope){
        this.$refs.zdzywhPage.openEdit(scope)
      },
      //查看
      searchStation(scope){
        this.$refs.zdzywhPage.searchStation(scope)
      },
      delData(scope){
        this.$refs.zdzywhPage.delData(scope)
      },

      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
        })
      },
      async getDataById (id) {
        let params={
          modelId:this.modelObj.modelId,
          isPage:'0',
          whereAttributes:[{
            attributeName: 'ID',
            attributeValue: id,
            isPrimaryKey:'1'}
          ]}
        let data = {}
        await getForm(params).then((res) => {
          data = res.data
        })
        return data
      },
      // 跳转到图形化编辑
      openJump(scope){
        this.getDataById(scope.row.ID).then(res => {
          this.$router.push({
            path:'/channelType/drawingBoard/index/' + res.PAR_STATION,
            query: {
              name: scope.row.PAR_STATION
            }
          })
        })
      },

      // 绑定光缆段框打开
      checkUpdate(scope){
        this.getDataById(scope.row.ID).then(res => {
          this.siteId = res.PAR_STATION
          this.pipoId = res.ID
          this.querysegByPipoIdFun()
          this.checkUpdateDialog=true
        })
      },
      // 已选光缆段框关闭
      checkUpdateDialogFalse(){
        this.$refs.zdzywhPage.getTableData()
        this.checkUpdateDialog=false
      },
      // 获取已经绑定光缆段
      querysegByPipoIdFun(){
        querysegByPipoId(this.pipoId).then(response => {
          this.checkUpdateData = response.data;
        }).catch(() => {
          this.loading = false
        });
      },
      // 新增光缆框打开
      openAddOpticalCable(){
        this.queryFormLabelNewCheck.segName=''
        this.queryNewCheckByName()
        this.newCheckUpdateDialog=true
      },









      delcheckData(scope){
        this.$confirm('此操作将永久删除该数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          delTcpipeseg(scope.row.ID).then((res) => {
            if (res.code === 200) {
              this.querysegByPipoIdFun()
              this.$message.success('删除成功')
            } else {
              this.$message.error('删除失败')
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },

      newCheckUpdateDialogFalse(){
        this.queryFormLabelNewCheck.segName=''
        this.querysegByPipoIdFun()
        this.newCheckUpdateDialog=false
      },
      // 查询未添加光缆段
      queryNewCheckByName(){
        querysegbysiteBySiteId(this.pipoId,this.queryFormLabelNewCheck.segName,this.siteId).then(response => {
            this.newCheckUpdateData = response.data;
            this.loading = false;
          }
        ).catch(() => {
          this.loading = false
        });
      },
      // 获取选中光缆段
      tcpipesegSelectionChange(val){
        this.tcpipesegSelection=val
      },
      // 新增光缆段保存
      addOpticalCable(){
        if(this.tcpipesegSelection.length==0){
          this.$message.error("请选中数据进行绑定")
        }else{
          let params=[]
          for(let i = 0; i < this.tcpipesegSelection.length; i++) {
            let obj = {
              segId:this.tcpipesegSelection[i].SEGID,
              resId:this.pipoId
            }
            params.push(obj)
          }
          addTcpipesegAndChannel(params).then(response =>{
              this.querysegByPipoIdFun()
              this.newCheckUpdateDialog=false
          }).catch(() => {
          })
        }
      },
    },
  };
</script>
