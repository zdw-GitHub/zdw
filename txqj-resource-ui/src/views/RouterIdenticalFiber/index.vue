<template>
  <div class="commonWh">
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="100px">
            <el-form-item label="光路 : " prop="selectData">
              <el-select
                  size="mini"
                  v-model="queryParams.selectData"
                  filterable
                  remote
                  :loading="optionsLoading"
                  :remote-method="remoteMethod"
                  @change="change"
                  placeholder="请选择">
                <el-option
                    v-for="item in selectDatas"
                    :key="item.id"
                    :label="item.name"
                    :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item style="float: right;">
              <el-button size="mini" type="primary" @click="show = true">添加光路</el-button>
              <el-button size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="tableBox">
        <div class="tabTopButton"></div>

        <div id="routerIdenticalFiber">

          <el-row>
            <el-col :span="24">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">分析光路</span>
                  <span class="formBut">
                    <el-button type="primary" size="mini" @click="analysis()">分析</el-button>
                  </span>
                </p>

                <div style="height: 30vh">
                  <el-table
                    stripe
                    border
                    style="width: 100%"
                    height="100%"
                    :data="router">
                    <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                      label="光路名称"
                      align="center"
                      width="600"
                      show-overflow-tooltip
                      prop="name"
                    ></el-table-column>
                    <el-table-column
                      label="A端设备"
                      align="center"
                      width="150"
                      show-overflow-tooltip
                      prop="aResId">
                    </el-table-column>
                    <el-table-column
                      label="A端端口"
                      align="center"
                      width="150"
                      show-overflow-tooltip
                      prop="aPort">
                    </el-table-column>
                    <el-table-column
                      label="Z端设备"
                      align="center"
                      width="150"
                      show-overflow-tooltip
                      prop="zResId">
                    </el-table-column>
                    <el-table-column
                      label="Z端端口"
                      align="center"
                      width="150"
                      show-overflow-tooltip
                      prop="zPort">
                    </el-table-column>
                    <el-table-column
                      label="光路路由"
                      align="center"
                      show-overflow-tooltip
                      prop="opticalRouting"
                    ></el-table-column>
                    <el-table-column
                      label="操作"
                      fixed="right"
                      width="100"
                      align="center">
                      <template slot-scope="scope">
                        <el-button @click="deleteRouter(scope.row.id)">删除</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">同缆</span>
                </p>

                <div style="height: 29vh">
                  <el-table
                    stripe
                    border
                    style="width: 100%"
                    height="100%"
                    :data="fiber"
                    v-loading="loading">
                    <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                      label="光缆名称"
                      align="center"
                      width="600"
                      show-overflow-tooltip
                      prop="FIBER_NAME"
                    >
                      <template slot-scope="scope">
                        <a
                          class="linkColumn"
                          @click="getRouterLink(scope.row)"
                        >
                          {{scope.row.fiberName}}
                        </a>
                      </template>
                    </el-table-column>
                    <el-table-column
                      label="电压等级"
                      align="center"
                      show-overflow-tooltip
                      prop="lineVoltageClass"
                    ></el-table-column>
                    <el-table-column
                      label="光缆类型"
                      align="center"
                      show-overflow-tooltip
                      prop="fiberType"
                    ></el-table-column>
                    <el-table-column
                      label="A端站点"
                      align="center"
                      show-overflow-tooltip
                      prop="aResobjid">
                    </el-table-column>
                    <el-table-column
                      label="Z端站点"
                      align="center"
                      show-overflow-tooltip
                      prop="zResobjid">
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

      </div>
    </div>
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      title="添加光路"
      :visible.sync="show"
      width="80%"
      :modal="false"
      :close-on-click-modal="false"
    >
      <div style="width: 100%; height: 70vh;" class="commonWh">
        <el-form style="margin-bottom: 10px" label-width="120px" :model="searchForm" :inline="true">
          <el-form-item label="名称 : " prop="name">
            <el-input size="mini" v-model="searchForm.name" placeholder="请输入名称名称"></el-input>
          </el-form-item>
          <el-form-item label="A端设备 : " prop="A_RES_ID">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              :loading="A_RES_ID_Loading"
              :remote-method="(val) => remoteMethod2(val, 'A_RES_ID')"
              v-model="searchForm.A_RES_ID"
            >
              <el-option
                v-for="item in A_RES_ID.list"
                :key="item.code"
                :label="item.name"
                :value="item.code"
                :disabled="item.disabled">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="Z端设备" prop="Z_RES_ID">
            <el-select
              size="mini"
              placeholder="请选择"
              clearable
              filterable
              remote
              :loading="Z_RES_ID_Loading"
              :remote-method="(val) => remoteMethod2(val, 'Z_RES_ID')"
              v-model="searchForm.Z_RES_ID"
            >
              <el-option
                v-for="item in Z_RES_ID.list"
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
          :data="tabData">
          <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
          <el-table-column
            label="光路名称"
            align="center"
            show-overflow-tooltip
            prop="name"
          ></el-table-column>
          <el-table-column
            label="A端设备"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="aResId">
          </el-table-column>
          <el-table-column
            label="A端端口"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="aPort">
          </el-table-column>
          <el-table-column
            label="Z端设备"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="zResId">
          </el-table-column>
          <el-table-column
            label="Z端端口"
            align="center"
            width="150"
            show-overflow-tooltip
            prop="zPort">
          </el-table-column>
          <el-table-column
            label="光路路由"
            align="center"
            show-overflow-tooltip
            prop="opticalRouting"
          ></el-table-column>
          <el-table-column
            label="操作"
            fixed="right"
            width="100"
            align="center">
            <template slot-scope="scope">
              <el-button @click="addOpticalPath(scope.row)">选择</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination
          :total="total"
          :page.sync="pageNum"
          :limit.sync="pageSize"
          @pagination="getTableData"
        />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="show = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryRouterList,
  queryRouterById,
  queryRouterByCondition,
  analysis
} from "@/api/RouterIdenticalFiber";
import {tableDropDownBox, getModelId} from "@/api/zdwh/zdzywh";
export default {
  name: "routerIdenticalFiber",
  data() {
    return {
      queryParams: {
        selectData: '',
      },
      selectDatas: [],
      optionsLoading: false,
      loading:false,
      router:[],
      fiber:[],

      tabData:[],
      show: false,

      modelObj: {},
      searchForm: {
        name: '',
        A_RES_ID: '',
        Z_RES_ID: '',
      },
      pageSize: 15,
      pageNum: 1,
      total: 0,
      A_RES_ID: {},
      Z_RES_ID: {},
      A_RES_ID_Loading: true,
      Z_RES_ID_Loading: true,
    };
  },
  created() {
    this.remoteMethod();
    this.getTableData()
    getModelId({tableName: 'SG_TCCON_TCOPTICALROAD_B'}).then(res => {
      this.modelObj = res.data
      this.getOption('A_RES_ID')
      this.getOption('Z_RES_ID')
    })
  },
  methods: {
    remoteMethod(query) {
      this.optionsLoading = true
      queryRouterList({
        name: query,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this.selectDatas = res.rows
        this.optionsLoading = false
      })
    },change(val){
      let index = this.router.findIndex(item => item.id == val)
      if (index == -1){
        queryRouterById({
          routerId: val,
        }).then((res) => {
          this.router.push(res.data)
        })
      }else {
        this.$message({type: 'info', message: '光路已存在'})
      }
    },

    deleteRouter(id){
      let index = this.router.findIndex(item => item.id == id)
      if (index != -1){
        this.router.splice(index,1)
      }
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.router = []
      this.fiber = []
    },
    /** 重置按钮操作 */
    resetTableQuery() {
      this.searchForm = {
        name: '',
        A_RES_ID: '',
        Z_RES_ID: '',
      }
    },
    addOpticalPath(row){
      let index = this.router.findIndex(item => item.id == row.id)
      if (index == -1){
        this.router.push(row)
      }else {
        this.$message({type: 'info', message: '光路已存在'})
      }
    },
    getTableData(){
      queryRouterByCondition({
        name: this.searchForm.name,
        aResId: this.searchForm.A_RES_ID,
        zResId: this.searchForm.Z_RES_ID,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
      }).then((res) => {
        this.tabData = res.rows
        this.total = res.total
      })
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

    analysis() {
      if(this.router.length > 0){
        this.loading = true
        let ids = this.router.map(item => item.id)
        analysis(ids).then(res =>{
          this.fiber = res.data
          this.loading = false
          let message = '分析成功，'
          if (this.fiber.length > 0){
            message += '存在同缆'
          }else {
            message += '不存在同缆'
          }
          this.$message({
            type: 'success',
            message: message
          })
        }).catch(e =>{
          this.loading = false
          this.$message({
            type: 'info',
            message: '分析失败'
          })
        })
      }else {
        this.$message({
          type: 'info',
          message: '请选择分析数据'
        })
      }
    },
    /* 格式序列化列 */
    snFormatter: function (row, col, value, index) {
      return index + 1;
    },

    //跳转
    getRouterLink(row){
      this.$router.push({
        path:'/fibersource/fiber',
        query:{
          search: JSON.stringify({ID:row.id}),
          refresh:true,
        }
      })
    },
  }
};
</script>
<style scoped lang="scss">
#routerIdenticalFiber {
  width: 100%;
  height: calc(100% - 70px);
  background-color: #fff;
  overflow: auto;
  .editForm {
    width: 100%;
    padding-right: 20px;

    .formTop {
      width: 100%;
      height: 40px;
      line-height: 40px;

      .formTit {
        font-size: 16px;
        font-weight: bold;
        color: #079b9f;
      }
      .formBut {
        float: right;
      }
    }
  }
}
</style>
