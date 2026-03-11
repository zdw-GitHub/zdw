<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="纤芯" name="OTDR">
        <el-table stripe border height="68vh" :data="list" v-loading="loading">
          <el-table-column show-overflow-tooltip label="ID" align="center" v-if="false" prop="log_id" />
          <el-table-column show-overflow-tooltip label="纤芯" align="center" prop="ln_seq_no" />
          <el-table-column show-overflow-tooltip label="衰耗" align="center" prop="dl_atten" />
          <el-table-column show-overflow-tooltip label="衰耗更新时间" align="center" prop="dl_on" />
          <el-table-column show-overflow-tooltip label="状态" align="center" prop="dl_warning" />          
          <el-table-column show-overflow-tooltip label="故障距离(m)" align="center" prop="ln_point" />
          <el-table-column show-overflow-tooltip label="故障距离的站点" align="center" prop="point_sta_name" />
          <el-table-column show-overflow-tooltip label="故障距离的机房" align="center" prop="point_rm_name" />
          <el-table-column fixed="right" label="操作" width="100">
            <template slot-scope="scope">
              <el-button @click="showOtdr(scope.row, scope.$index)" type="text" size="small">OTDR</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="关联光缆" name="关联光缆">
        <carrying v-if="show" v-loading="fiber.loading" style="height: 68vh" ref="fiber" :tableName="fiber.tableName"
          :searchForm="fiber.searchForm" :pattern="pattern"
          :jumpLink="[{ attributeNameUnderline: 'NAME', menuPath: 'fibersource/fiber' }]" :openNewPage="false"></carrying>
      </el-tab-pane>
    </el-tabs>

    <!--查看测试记录-->
    <el-dialog title="OTDR测试记录" :close-on-click-modal="false" :visible.sync="showBox" :destroy-on-close="true" width="72%"
      top="0" :modal="false">
      <div id="echarts1" style="width: 100%;height: 32vh;margin-top: 2vh;">
      </div>
      <div style="width: 100%;margin-top: 2vh;"> 
        <el-table :data="events" style="width: 100%">
        <el-table-column type="index" :index="indexMethod">
        </el-table-column>
        <el-table-column prop="eventMode" label="事件类型" width="180">
        </el-table-column>
        <el-table-column prop="eventStation" label="位置" width="180">
        </el-table-column>
        <el-table-column prop="eventInLoss" label="插入损耗" width="180">
        </el-table-column>
        <el-table-column prop="eventBkLoss" label="回波衰耗（dB）" width="180">
        </el-table-column>
        <el-table-column prop="decayRatio" label="衰减系数（dB/km）" width="180">
        </el-table-column>
        <el-table-column prop="eventTlLoss" label="累计衰耗（dB）" width="auto">
        </el-table-column>
      </el-table>
      </div>      
    </el-dialog>
  </div>
</template>

<script>
import carrying from '@/views/commonPage/carrying/index.vue'
import { getFiberLine, getOtdrData } from '@/api/manageFiber/YmFiber'
import * as echarts from 'echarts'


export default {
  name: 'index',
  components: { carrying },
  props: {
    dataObj: Object,
    isEditMode: {
      type: Boolean,
      default: () => false
    },
    pattern: {
      type: Boolean,
      default: () => true
    }
  },
  data() {
    return {
      events: {},
      showBox: false,
      activeName: 'OTDR',
      list: [],
      loading: false,
      show: false,
      fiber: {
        loading: false,
        tableName: 'SG_TCDEV_FIBER_B',
        searchForm: {
          ID: ''
        }
      },
      dataX: [],
      dataLine: [],
      dataScatter: [],
    }
  },
  watch: {},
  created() {
  },
  mounted() {
    this.getData()
  },
  methods: {
    showOtdr(row, type) {
      console.log(row);
      this.showBox = true
      getOtdrData({ id: row.log_id }).then(res => {
        this.dataX = res.dataX
        this.dataLine = res.dataLine
        this.events = res.events
        this.$nextTick(() => {
          const domElement = document.getElementById('echarts1');
          if (domElement) {
            // 初始化 ECharts 实例
            var myChart = echarts.init(domElement);
            // myChart = echarts.init(document.getElementById('echarts1'))
            myChart.clear()
            let option = {
              tooltip: {
                trigger: 'axis',
                formatter(params) {
                  if (params[1].data == null) {
                    return '距离：' + params[0].axisValue + 'km' + '<br>振幅：' + params[0].data + 'dBm'
                  } else {
                    return '距离：' + params[0].axisValue + 'km' + '<br>振幅/事件：' + params[0].data + 'dBm'
                  }
                }
              },
              grid: {
                top: '5%',
                bottom: '8%',
                left: '5%',
                right: '4%'
              },
              dataZoom: [{
                type: 'inside',
                start: 0,
                end: 1000
              }, {
                start: 0,
                end: 10
              }],
              xAxis: {
                type: 'category',
                axisTick: {
                  show: false
                },
                splitLine: {
                  show: false
                }, // 去除网格线
                splitArea: {
                  show: false
                }, // 保留网格区域
                axisLine: {
                  show: false,
                  position: 'bottom'
                },
                data: this.dataX,
                name: "(m)",
                // nameTextStyle: {
                //   color: '#aaa',
                //   alain: 'right'
                // },
                axisLabel: {
                  formatter: function (value) {
                    return value; // 在每个标签后面添加单位
                  }
                }
              },
              yAxis: {
                type: 'value',
                max: 50,
                // inverse: true,
                splitLine: {
                  show: true
                }, // 去除网格线
                splitArea: {
                  show: false
                }, // 保留网格区域
                axisLine: {
                  show: true,
                  lineStyle: {
                    type: 'solid',
                    color: '#999', // 左边线的颜色
                    width: '1' // 坐标线的宽度
                  }
                },
                // name: "(dB)",
                // nameTextStyle: {
                //   color: '#aaa',
                //   alain: 'left'
                // },
                axisLabel: {
                  formatter: function (value) {
                    return value + "( dB)"; // 在每个标签后面添加单位
                  }
                }
              },
              series: [{
                data: this.dataLine,
                type: 'line',
                showSymbol: false,
                color: '#3790E1',
                smooth: true
              },
              {
                symbolSize: 10,
                data: this.dataScatter,
                type: 'scatter',
                color: '#2C4ABC'
              }]
            }
            option && myChart.setOption(option)
            // 进行其他操作
          } else {
            console.error("初始化图表失败");
          }
        });
      })


    },


    handleClick() {
      if (this.activeName == '关联光缆' && this.show == false) {
        this.show = true
        this.fiber.searchForm.ID = this.dataObj.ty_id || '空'
        this.$nextTick(() => {
          this.$refs.fiber.getTableData()
        })
      }
    },
    getData() {
      this.loading = true
      getFiberLine({ id: this.dataObj.ID }).then(res => {
        this.list = res.rows
        this.loading = false
      })
    },
  }
}
</script>

<style scoped lang="scss">
.showTask {
  width: 100%;
  height: 70vh;
  display: flex;

  .taskLeft {
    flex: 0 0 22%;
    height: 70vh;
    overflow: auto;
    border: 1px solid #ccc;

    .oneBox {
      width: 100%;

      .boxTit {
        height: 36px;
        color: #000;
        line-height: 36px;
        text-indent: 6px;
        font-size: 1vh;
        background: linear-gradient(180deg, #bddfff 0%, #f1f9ff 100%);
      }

      .boxCon {
        width: 100%;
        height: 2vh;
        line-height: 1.5vh;

        span {
          height: 100%;
          float: left;
          display: inline-block;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .tit {
          width: 40%;
          text-indent: 6px;
        }

        .con {
          width: 60%;
        }
      }
    }

  }

  .taskRight {
    flex: 1;
    margin-left: 1vw;
    height: 70vh;
    overflow: auto;

    .rightTop {
      height: 50%;
    }

    .rightBot {
      height: 49%;
    }
  }
}
</style>
