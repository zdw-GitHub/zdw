<template>
  <div>
    <zdzywhPage
      ref="zdzywhPage"
      :tableName="tableName"
      :rightClickMenus="[
        {name: '查看同缆光缆分析', functionName: 'seeCommonFiber',},
        {name: '查看三路由分析', functionName: 'seeThreeRout'},
        {name: '查看不可靠路由分析', functionName: 'unreliableRoutingAnalysis'},
      ]"
      @seeCommonFiber="seeCommonFiber"
      @seeThreeRout="seeThreeRout"
      @unreliableRoutingAnalysis="unreliableRoutingAnalysis"
    >
    </zdzywhPage>
    <!--路由维护-->
    <el-dialog
      v-dialogDrag
      :title="title + '一次线路路由'"
      :modal="false"
      :close-on-click-modal="false"
      width="70vw"
      :visible.sync="show"
      top="5vh"
    >
      <div style="height: 70vh" class="oneLineRouter" v-loading="loading">
        <div style="width: 100%; height: 100%" id="routerEcharts" v-loading="mapLoading"></div>
        <div class="commonFiber">
          <span
            v-if="!commonFiberTableShow"
            class="tigger el-icon-d-arrow-right"
            @click="commonFiberTableShow = !commonFiberTableShow"
            :style="{left: !commonFiberTableShow ? '0' : 'auto'}"
          ></span>
          <span
            v-if="commonFiberTableShow"
            class="tigger el-icon-d-arrow-left"
            @click="commonFiberTableShow = !commonFiberTableShow"
          ></span>
          <div class="border" v-show="commonFiberTableShow">
            <p class="title">
              <span>同缆光缆列表</span>
            </p>
            <div class="con">
              <el-table
                :data="commonFiberTable"
                height="100%"
                tooltip-effect="light"
                style="width: 100%">
                <el-table-column
                  type="index"
                  align="center"
                  label="序号"
                  width="50">
                </el-table-column>
                <el-table-column
                  show-overflow-tooltip
                  prop="FIBER_NAME"
                  label="同光缆名称">
                  <template slot-scope="scope">
                    <span class="tableLink" @click="highlightFiber(scope.row.FIBER_NAME)">{{scope.row.FIBER_NAME}}</span>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>


        <div class="threeRout">
          <span
            v-if="!threeRoutTableShow"
            class="tigger el-icon-d-arrow-right"
            @click="threeRoutTableShow = !threeRoutTableShow"
            :style="{left: !threeRoutTableShow ? '0' : 'auto'}"
          ></span>
          <span
            v-if="threeRoutTableShow"
            class="tigger el-icon-d-arrow-left"
            @click="threeRoutTableShow = !threeRoutTableShow"
          ></span>
          <div class="border" v-show="threeRoutTableShow">
            <p class="title">
              <span>三路由分析结果</span>
            </p>
            <div class="con">
              <el-table
                :data="threeRoutTable"
                tooltip-effect="light"
                height="100%"
                @selection-change="handleSelectionChange"
                style="width: 100%">
                <el-table-column
                  type="selection"
                  width="25">
                </el-table-column>
                <el-table-column
                  type="index"
                  label="序号"
                  align="center"
                  width="50">
                </el-table-column>
                <el-table-column
                  show-overflow-tooltip
                  prop="buzRouter"
                  label="路由">
                </el-table-column>
                <el-table-column
                  show-overflow-tooltip
                  prop="buzName"
                  label="业务名称">
                </el-table-column>
              </el-table>
            </div>
          </div>
        </div>

        <div class="legend">
          <span
            v-if="legendShow"
            class="tigger el-icon-d-arrow-right"
            @click="legendShow = !legendShow"
          ></span>
          <span
            v-if="!legendShow"
            class="tigger el-icon-d-arrow-left"
            @click="legendShow = !legendShow"
          ></span>
          <div class="border" v-show="legendShow">
            <p class="title"><span>图例</span></p>
            <div class="con">
              <el-row :gutter="20">
                <el-col :span="10"><p class="legendLine" style="border-color: #ff3b2f"></p></el-col>
                <el-col :span="14">同缆光缆</el-col>
                <el-col :span="10"><p class="legendLine" style="border-color: #68b7f9"></p></el-col>
                <el-col :span="14">默认光缆</el-col>
                <el-col :span="10"><p class="legendLine" style="border-color: #E6A23C"></p></el-col>
                <el-col :span="14">选中光缆</el-col>
                <el-col :span="10" style="text-align: center"><img src="./site.svg" height="100%"/></el-col>
                <el-col :span="14">站点</el-col>
              </el-row>
            </div>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <p style="float: left; line-height: 38px;">分析时间 : {{this.analyseTime}}</p>
        <el-button type="primary" @click="reanalyse()">重新分析</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  import * as echarts from 'echarts'
  import mapJson from './map.json'
  import {
    // 执行一次分析
    analyseApi,
    // 获取上次分析时间
    getAnalyseTimeApi,
    // 获取同缆光缆表格
    getCommonFiberApi,
    // 获取三路由
    getThreeRoutApi,
    // 获取地图点
    getSiteApi,
    // 获取地图点线
    getFiberApi,
    // 查看业务 高亮业务路由(根据业务 查路由)
    getBuzRouterApi,
  } from "@/api/oneLine"
  export default {
    name: "index",
    components: {zdzywhPage},
    data(){
      return {
        tableName:'DW_TCDEV_ONELINE',
        oneLineData: {},
        show: false,
        loading: false,
        legendShow: true,
        title: '',
        // 同缆表格
        commonFiberTableShow: true,
        commonFiberTable: [],
        commonFiberTableLodaing: true,
        // 三路由
        threeRoutTableShow: true,
        threeRoutTable: [],
        threeRoutTableLoading: true,
        // 地图点
        sites: [],
        // 地图线
        fibers: [],
        mapLoading: false,
        // 分析时间
        analyseTime: '',
        seeType: '',
      }
    },
    watch: {
    },
    methods:{
      seeCommonFiber (data) {
        //this.seeRouter(data.rowData, 'commonFiber')
        this.$router.push({
          path:'/oneLine/route',
          query:{
            id: data.rowData.ID
          }
        })
      },
      seeThreeRout (data) {
        //this.seeRouter(data.rowData, 'threeRout')
        this.$router.push({
          path:'/oneLine/route',
          query:{
            id: data.rowData.ID
          }
        })
      },
      unreliableRoutingAnalysis(data){
        this.$router.push({
          path:'/businesssource/unreliableRouting',
          query:{
            id: data.rowData.ID,
            name: data.rowData.NAME
          }
        })
      },
      // 查看路由
      async seeRouter (row, type) {
        this.seeType = type
        this.commonFiberTableShow = type == 'commonFiber'
        this.threeRoutTableShow = type == 'threeRout'
        this.oneLineData = row
        // this.oneLineData.ID = '120133000000000008'
        // this.oneLineData.ID = '120133000000003953'
        this.title = this.oneLineData.NAME
        this.show = true
        let data = {ontLineId: this.oneLineData.ID}
        getAnalyseTimeApi(data).then(res => {
          this.analyseTime = res.ANALYZE_TIME
        })
        this.commonFiberTableLodaing = true
        this.threeRoutTableLoading = true
        getThreeRoutApi(data).then(res => {
          this.threeRoutTable = res
          this.threeRoutTableLoading = false
        })
        this.mapLoading = true
        await getCommonFiberApi(data).then(res => {
          this.commonFiberTable = res
          this.commonFiberTableLodaing = false
        })
        getSiteApi(data).then(res1 => {
          this.sites = res1
          getFiberApi(data).then(res2 => {
            this.fibers = res2
            this.$nextTick(() => {
              this.mapLoading = false
              this.initEcharts(this.sites, this.fibers, [])
            })
          })
        })
      },
      initEcharts (nodes, links, selLinks, sleSite) {
        if (nodes == undefined || links == undefined) {
          return
        }
        let node = []
        for (let i = 0; i < nodes.length; i++) {
          let item = nodes[i]
          let data = {
            id: item.SITE_ID,
            name: item.SITE_NAME,
            value: [item.LONGITUDE, item.LATITUDE],
            type: 'node',
            symbolSize: 16,
            data: item,
            symbol: 'image://' + require('./site.svg')
          }
          node.push(data)
        }
        let link = []
        for (let i = 0; i < links.length; i++) {
          let item = links[i]
          let source = nodes.find(node => {
            return node.SITE_ID + '' == item.A_RESOBJID + ''
          })
          let target = nodes.find(node => {
            return node.SITE_ID + '' == item.Z_RESOBJID + ''
          })
          let color
          if (selLinks.findIndex(itemm => {
            return itemm.FIBER_NAME == item.FIBER_NAME
          }) != -1) {
            color = '#E6A23C'
          } else if (this.commonFiberTable.findIndex(itemm => {
            return itemm.FIBER_NAME == item.FIBER_NAME
          }) != -1) {
            color = '#ff3b2f'
          } else {
            color = '#68b7f9'
          }
          let data = {
            id: item.ID,
            type: 'link',
            source: item.A_RESOBJID,
            target: item.Z_RESOBJID,
            name: item.FIBER_NAME,
            aName: source.SITE_NAME,
            zName: target.SITE_NAME,
            data: item,
            lineStyle: {
              normal: {
                width: 2,
                opacity: 1,
                color: color,
              }
            }
          }
          link.push(data)
        }
        // 数据变更时 销毁原模型
        var myChart = echarts.getInstanceByDom(document.getElementById('routerEcharts'))
        let zoom = 1.2
        let center = [119.56712417687297, 29.147780225220433]
        if (myChart !== undefined) {
          zoom = myChart.getOption().series[0].zoom
          center = [myChart.getOption().series[0].center[0], myChart.getOption().series[0].center[1]]
          echarts.dispose(myChart)
        }
        myChart = echarts.init(document.getElementById('routerEcharts'))
        myChart.clear()
        // 注册地图json
        echarts.registerMap('zhejiang', mapJson)
        let margin = 0.05
        let option = {
          tooltip: {
            borderColor:"#fff",
            extraCssText: 'box-shadow: 0 0 3px rgba(0, 0, 0, 0.3);',
            padding: [5, 10],
            formatter :function(val){
              if (val.seriesType == 'graph' || val.valseriesType == 'scatter') {
                if (val.data.type == 'node') {
                  return `站点名称 : ${val.data.name}`
                } else if (val.data.type == 'link') {
                  return `光缆名称 : ${val.data.name}`
                }
              }
            }
          },
          geo: [
            {
              map: 'zhejiang',
              type: 'map',
              zoom: zoom,
              aspectScale:1,
              label: {
                normal: {
                  show: false, // 是否显示标签。
                },
                emphasis: {
                  show: false
                }
              },
              animationDurationUpdate: 0,
              roam: false,
              center: [center[0], center[1] + margin],
              zlevel: 1,
              itemStyle: {
                normal: {
                  opacity: 1,
                  areaColor: '#bcc0cb',
                  borderColor: '#bcc0cb', // 线
                  borderWidth: 0.5,
                  shadowBlur: 20,
                  shadowColor: '#c8c9cd',
                },
                emphasis: {
                  // 悬浮区背景
                  areaColor: '#bcc0cb',
                  label: {
                    show: false,
                  },
                },
              }
            }
          ],
          series: [
            {
              map: 'zhejiang',
              type: 'map',
              zoom: zoom,
              aspectScale:1,
              label: {
                normal: {
                  show: false, // 是否显示标签。
                },
                emphasis: {
                  show: false
                }
              },
              animationDurationUpdate: 0,
              roam: true,
              center: center,
              zlevel: 2,
              itemStyle: {
                normal: {
                  opacity: 1,
                  areaColor: '#fff',
                  borderColor: '#bdd0de', // 线
                  borderWidth: 0.5
                },
                emphasis: {
                  // 悬浮区背景
                  areaColor: '#fff',
                  label: {
                    show: false,
                  },
                }
              }
            },
            {
              type: 'graph',
              coordinateSystem: 'geo',
              zlevel: 4,
              data: node,
              links: link,
              roam: true, // 是否开启鼠标缩放和平移漫游
              large: false,
              hoverAnimation: false,
              animation: false,
              // 站点名称显示
              label: {
                normal: {
                  show: true,
                  fontSize: 12,
                  formatter: function (params) {
                    return params.data.name
                  },
                  position: 'bottom',
                  color: '#68b7f9',
                },
                emphasis: {
                  show: true,
                  fontSize: 12,
                  formatter: function (params) {
                    return params.data.name
                  },
                  position: 'bottom',
                  color: '#68b7f9',
                }
              },
            },
            {
              type: 'lines',
              coordinateSystem: 'geo',
              zlevel: 3,
              animationDurationUpdate: 0, // 数据更新动画时长
              effect: {
                show: true,
                period: 2, //箭头指向速度，值越小速度越快
                trailLength: 0.02, //特效尾迹长度[0,1]值越大，尾迹越长重
                symbol: 'arrow', //箭头图标
                symbolSize: 5, //图标大小
              },
              lineStyle: {
                normal: {
                  color: '#409EFF',
                  opacity: 0, //尾迹线条透明度
                },
              },
              data: sleSite
            },
          ]
        }
        myChart.on("georoam", function(params) {
          var option = myChart.getOption()
          if (params.zoom != null && params.zoom != undefined) {
            option.geo[0].zoom = option.series[0].zoom
          }
          //捕捉到拖曳时
          option.geo[0].center = [option.series[0].center[0], parseFloat(option.series[0].center[1] + margin)]
          myChart.setOption(option);
        });

        myChart.setOption(option)
      },

      highlightFiber (name) {
        let fiber = this.fibers.find(item => {
          return item.FIBER_NAME == name
        })

        let source = this.sites.find(node => {
          return node.SITE_ID + '' == fiber.A_RESOBJID + ''
        })
        let target = this.sites.find(node => {
          return node.SITE_ID + '' == fiber.Z_RESOBJID + ''
        })
        let site = [{
          coords: [
            [target.LONGITUDE, target.LATITUDE],
            [source.LONGITUDE, source.LATITUDE],
          ],
        }]
        this.initEcharts (this.sites, this.fibers, [fiber], site)
      },
      handleSelectionChange(val) {
        let fibers = []
        let site = []
        for (let v = 0; v < val.length; v++) {
          let arr = val[v].buzRouter.split('===>')
          for (let i = 0; i < arr.length - 1; i++) {
            let aSite = this.sites.find(item => {
              return item.SITE_NAME == arr[i]
            })
            let zSite = this.sites.find(item => {
              return item.SITE_NAME == arr[i + 1]
            })
            let fiber = this.fibers.find(item => {
              if (
                (item.A_RESOBJID + '' == aSite.SITE_ID + '' &&
                  item.Z_RESOBJID + '' == zSite.SITE_ID + '') ||
                (item.Z_RESOBJID + '' == aSite.SITE_ID + '' &&
                  item.A_RESOBJID + '' == zSite.SITE_ID + '')
              ) {
                return item
              }
            })
            if (fiber) {
              fibers.push(fiber)
            }
            site.push({
              coords: [
                [aSite.LONGITUDE, aSite.LATITUDE],
                [zSite.LONGITUDE, zSite.LATITUDE],
              ],
            })
          }
        }
        this.initEcharts (this.sites, this.fibers, fibers, site)
      },
      reanalyse () {
        this.loading = true
        analyseApi({ontLineId: this.oneLineData.ID}).then(res => {
          this.loading = false
          this.$message.success('分析完成')
          this.seeRouter(this.oneLineData, this.seeType)
        }).catch(e => {
          this.loading = false
          this.$message.error('分析失败 请在控制台查看报错')
          console.log(e)
        })
      },
    },
    mounted () {
    },
  }
</script>

<style scoped lang="scss">
  .oneLineRouter{
    background-color: #f1f2f7;
    position: relative;
    .routerEcharts{
    }
    .commonFiber,
    .threeRout,
    .legend{
      position: absolute;
      background-color: #fff;
      .border{
        height: 100%;
        .title{
          font-size: 14px;
          padding: 8px;
          background-color: #409EFF;
          color: #fff;
          span{
            padding-left: 10px;
            border-left: 3px solid #fff;
          }
        }
        .con{
          width: 100%;
          height: calc(100% - 35px);
          padding: 10px;
          border: 1px solid rgba(0, 0, 0, 0.3);
          border-top: none;
        }
      }
      .tigger{
        display: inline-block;
        position: absolute;
        right: 0;
        height: 35px;
        width: 35px;
        line-height: 35px;
        font-size: 15px;
        text-align: center;
        font-weight: bold;
        color: #fff;
        background-color: #409EFF;
        cursor: pointer;
      }
    }
    .commonFiber{
      width: auto;
      height: calc(100% - 280px);
      top: 10px;
      left: 10px;
      .border{
        width: 20vw;
        .tableTit{
          height: 30px;
          line-height: 30px;
        }
      }
    }
    .threeRout{
      width: auto;
      height: 250px;
      bottom: 10px;
      left: 10px;
      .border{
        width: calc(70vw - 20px - 220px);
      }
    }
    .legend{
      width: auto;
      height: 250px;
      bottom: 10px;
      right: 10px;
      .border{
        width: 190px;
        .el-col{
          height: 20px;
          margin: 5px 0;
          .legendLine{
            width: 100%;
            height: calc(50% + 2px);
            border-bottom: 4px solid;
          }
        }
      }
    }
    .tableLink{
      color: #409EFF;
      cursor: pointer;
    }
  }
  .rightClickBox{
    position: fixed;
    width: 130px;
    background-color: #fff;
    z-index: 100;
    padding: 5px 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
    p{
      cursor: pointer;
      height: 20px;
      font-size: 12px;
      line-height: 20px;
      padding: 0 5px;
      &:hover{
        background-color: rgb(219,235,255);
      }
    }
  }
</style>
