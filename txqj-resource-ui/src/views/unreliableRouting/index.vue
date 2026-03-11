<template>
  <div class="commonWh">
    <div class="box-card">
      <div class="queryBox">
        <!--基础查询-->
        <div class="basicsQuery">
          <el-form :model="queryParams" ref="queryForm" label-width="100px">
            <el-form-item label="线路 : " prop="selectData">
              <el-select
                  size="mini"
                  v-model="queryParams.selectData"
                  filterable
                  remote
                  :loading="optionsLoading"
                  :remote-method="remoteMethod"
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
              <el-button type="primary" size="mini" @click="analysis">分析</el-button>
              <el-button size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>

      <div class="tableBox">
        <div class="tabTopButton">
        </div>

        <div id="unreliableRouting">

          <el-row>
            <el-col :span="24">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">绑定业务</span>
                </p>

                <div style="height: 30vh">
                  <el-table
                    stripe
                    border
                    style="width: 100%"
                    height="100%"
                    :data="buz"
                    v-loading="loading">
                    <el-table-column label="序号" width="50" align="center" :formatter="snFormatter"></el-table-column>
                    <el-table-column
                      label="业务名称"
                      align="center"
                      width="600"
                      show-overflow-tooltip
                      prop="BUZ_NAME"
                    ></el-table-column>
                    <el-table-column
                      label="A端站点"
                      align="center"
                      width="150"
                      show-overflow-tooltip
                      prop="A_SITE_NAME">
                    </el-table-column>
                    <el-table-column
                      label="Z端站点"
                      align="center"
                      width="150"
                      show-overflow-tooltip
                      prop="Z_SITE_NAME">
                    </el-table-column>
                    <el-table-column
                      label="业务路由"
                      align="center"
                      show-overflow-tooltip
                      prop="channel_route"
                    ></el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <div class="editForm">
                <p class="formTop">
                  <span class="formTit">涉及光缆</span>
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
                          {{scope.row.FIBER_NAME}}
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
                      prop="FIBER_TYPE"
                    ></el-table-column>
                    <el-table-column
                      label="A端站点"
                      align="center"
                      show-overflow-tooltip
                      prop="A_SITE_NAME">
                    </el-table-column>
                    <el-table-column
                      label="Z端站点"
                      align="center"
                      show-overflow-tooltip
                      prop="Z_SITE_NAME">
                    </el-table-column>
                  </el-table>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import {
  queryLineList,
  analysis
} from "@/api/unreliableRouting";
export default {
  name: "UnreliableRouting",
  data() {
    return {
      queryParams: {
        selectData: '',
      },
      selectDatas: [],
      optionsLoading: false,
      loading:false,
      buz:[],
      fiber:[],
    };
  },
  created() {
    this.optionsLoading = true
    queryLineList({
      pageNum: 0,
      pageSize: 20,
    }).then((res) => {
      this.selectDatas = res.rows
      this.optionsLoading = false
    })
  },
  activated () {
    if (this.$route.query.id) {
      this.queryParams.selectData = this.$route.query.id
      this.selectDatas.push({id:this.$route.query.id,name:this.$route.query.name})
      this.analysis()
    }
  },
  methods: {
    remoteMethod(query) {
      this.optionsLoading = true
      queryLineList({
        name: query,
        pageNum: 0,
        pageSize: 20,
      }).then((res) => {
        this.selectDatas = res.rows
        this.optionsLoading = false
      })
    },

    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.buz = []
      this.fiber = []
    },

    analysis() {
      if(this.queryParams.selectData){
        this.loading = true
        analysis({id:this.queryParams.selectData}).then(res =>{
          this.buz = res.data.buz
          this.fiber = res.data.fiber
          this.loading = false
        }).catch(e =>{
          this.loading = false
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
          search: JSON.stringify({ID:row.ID}),
          refresh:true,
        }
      })
    },
  }
};
</script>
<style scoped lang="scss">
#unreliableRouting {
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
    }
  }
}
</style>
