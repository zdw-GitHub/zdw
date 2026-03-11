<template>
  <div>
      <div id="echarts1" style="width: 100%;height: 32vh;margin-top: 2vh;">
      </div>
      <div style="width: 100%;margin-top: 2vh;"> 
        <el-table :data="events" style="width: 100%">
        <el-table-column type="index" >
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
  </div>
</template>

<script>
import { getOtdrData } from '@/api/manageFiber/YmFiber'
import * as echarts from 'echarts'


export default {
  name: 'otdrImg',
  data() {
    return {
      events: {},
      dataX: [],
      dataLine: [],
      dataScatter: [],
    }
  },
  watch: {},
  created() {
    this.showOtdr()
  },
  mounted() {
  },
  methods: {
    showOtdr() {
      this.showBox = true
      getOtdrData({ id: this.$route.query.id }).then(res => {
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
