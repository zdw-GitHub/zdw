<template>
  <div class="app-container">
    <div class="sjml">
      <div class="search">
        <el-input placeholder="请输入内容" v-model="searchForm.name" class="input-with-select">
          <el-select clearable v-model="searchForm.type" slot="prepend" placeholder="请选择" style="width:400px">
            <el-option
              v-for="item in pubCodeList"
              :key="item.pubCode"
              :label="item.pubName"
              :value="item.pubCode">
            </el-option>
          </el-select>
          <el-button type="primary" @click="searchName" slot="append" icon="el-icon-search">搜索</el-button>
        </el-input>
      </div>
      <div class="sjml_con">
        <div class="con_left">
          <div class="tit">
            <span style="color: #409eff">▍</span>搜索结果
          </div>
          <div class="finds">
            <div class="find_obj" v-show="item.data.length > 0" :key="index" v-for="(item,index) in resultList">
              <div class="small_tit">{{item.queryType}}</div>
              <div class="find_con">
                <ul :key="obj.resultId" v-for="obj in item.data">
                  <li :id="obj.resultId" @click="getEcharts(obj.resultId, obj.queryTypeCode)">
                    <span v-if=" 'resultNameCn' in obj">{{obj.resultNameCn}}</span>
                    <span v-else>{{obj.resultName}}</span>
                  </li>
                </ul>
              </div>
            </div>
            <!--<div class="find_obj">
              <div class="small_tit">采集源模型</div>
              <div class="find_con">
                <ul>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                </ul>
              </div>
            </div>
            <div class="find_obj">
              <div class="small_tit">元模型</div>
              <div class="find_con">
                <ul>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                  <li>tpology(xxx数据源)</li>
                </ul>
              </div>
            </div>-->
          </div>
        </div>
        <div class="con_right">
          <div class="tit">
            <span style="color: #409eff">▍</span>血缘关系
          </div>
          <div id="r_echarts"></div>
          <div id="msg">
            <el-card shadow="always">
              <el-button @click="closeMsg()" style="position: absolute;top: 0.1rem;right: 0.1rem" type="danger" icon="el-icon-close" size="mini"></el-button>
              <p>名&emsp;&emsp;称：{{msgBox.resultName}}</p>
              <p>中文名称：{{msgBox.resultNameCn}}</p>
              <p>编&emsp;&emsp;号：{{msgBox.resultId}}</p>
              <p>模型类型：{{msgBox.queryType}}</p>
            </el-card>
          </div>
        </div>
      </div>
    </div>
  </div>
  </template>
<script>
import axios from 'axios'
import qs from 'qs'
export default {
  name: 'sjml',
  data () {
    return {
      searchForm: {
        type: '',
        name: ''
      },
      pubCodeList: [],
      resultList: [],
      tabPosition: 0,
      msgBox: {},
      dbmsg: false,
      ifModel: false,
      data: [{
        id: '',
        label: '模型',
        icon: 'el-icon-s-home',
        color: 'color: #FFC000',
        children: []
      }]
    }
  },
  methods: {
    // 搜索
    searchName: function () {
      this.resultList = []
      let myChart = this.$echarts.init(document.getElementById('r_echarts'))
      myChart.clear()
      axios.get(localStorage.URL + '/globalquery/globalquery?queryContent=' + this.searchForm.name + '&queryTypeCode=' + this.searchForm.type, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data.data
        this.resultList = res
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 获取血缘关系
    getEcharts: function (id, type) {
      var aa = document.getElementsByClassName('finds')
      var divs = aa[0].getElementsByTagName('li')
      for (var i = 0; i < divs.length; i++) {
        divs[i].style.background = '#fff'
        divs[i].style.color = '#000'
      }
      document.getElementById(id).style.color = '#409eff'
      document.getElementById(id).style.background = '#ECF5FF'
      axios.get(localStorage.URL + '/globalquery/bloodrelationship?queryTypeCode=' + type + '&resultId=' + id, qs.stringify({
      })).then((res) => {
        // 请求成功处理。
        res = res.data.data
        for (let i = 0; i < res.datas.length; i++) {
          if ('resultNameCn' in res.datas[i]) {
            res.datas[i].name = res.datas[i].resultNameCn
          } else {
            res.datas[i].name = res.datas[i].resultName
          }
        }
        this.closeMsg()
        this.drawEcharts(res.datas, res.lines)
      }).catch(e => {
        // 请求失败处理。
        this.$message({
          message: e,
          type: 'error',
          center: true
        })
      })
    },
    // 填充血缘关系图
    drawEcharts: function (data, line) {
      let myChart = this.$echarts.init(document.getElementById('r_echarts'))
      myChart.clear()
      var option = {
        tooltip: {
          formatter: function (params) {
            return params.name
          }
        },
        animationDurationUpdate: 1500,
        label: {
          normal: {
            show: true,
            textStyle: {
              fontSize: 12
            }
          }
        },
        series: [ {
          type: 'graph',
          layout: 'force', // 采用力引导布局
          symbolSize: 45,
          legendHoverLink: true, // 启用图例 hover 时的联动高亮。
          focusNodeAdjacency: true, // 在鼠标移到节点上的时候突出显示节点以及节点的边和邻接节点。
          roam: true,
          label: {
            normal: {
              show: true,
              position: 'inside',
              textStyle: {
                fontSize: 12
              }
            }
          },
          force: {
            repulsion: 1000,
            layoutAnimation: false
          },
          edgeSymbolSize: [ 4, 50 ],
          data: data,
          links: line,
          lineStyle: {
            normal: {
              opacity: 0.6,
              width: 1,
              curveness: 0
            }
          }
        } ]
      }
      myChart.setOption(option)
      myChart.on('click', params => {
        this.msgBox = params.data
        var msgObj = document.getElementById('msg')
        msgObj.style.top = this.getMousePos()['y'] + 'px'
        msgObj.style.left = this.getMousePos()['x'] + 'px'
        msgObj.style.width = 5 + 'rem'
        msgObj.style.height = 3.5 + 'rem'
      })
    },
    // 获取echarts点击处的位置坐标
    getMousePos: function () {
      let e = event || window.event
      let scrollX = document.documentElement.scrollLeft || document.body.scrollLeft
      let scrollY = document.documentElement.scrollTop || document.body.scrollTop
      let x = e.pageX || e.clientX + scrollX
      let y = e.pageY || e.clientY + scrollY
      return {'x': x - 160, 'y': y - 200}
    },
    // 关闭echarts的弹框
    closeMsg: function () {
      var msgObj = document.getElementById('msg')
      msgObj.style.width = msgObj.style.height = 0 + 'rem'
    }
  },
  mounted () {
    this.getPubCode()
  }
}
</script>
  
<style lang="stylus" rel="stylesheet/stylus">
  .sjml
    width 100%
    height 46.5rem
    overflow hidden
    .search
      width 80%
      height 3rem
      margin 0 auto 0.5rem auto
      .el-select .el-input
        width: 400px
        height 2rem
      .el-input__inner
        height 2rem
        width 100%
      .input-with-select .el-input-group__prepend
        background-color: #fff
      .el-input-group__prepend .el-select
        width 3rem
        &.el-input .el-input__inner
          width 3rem
      .el-input-group__append
        width 2rem
        text-align center
    .sjml_con
      width 100%
      display flex
      height calc(100% - 1.5rem)
      .con_left
        flex 6
        height 100%
        border-right 1px solid #d3d3d3
        .finds
          height calc(100% - 50px)
          display block
          overflow hidden
          overflow-y auto
          .find_obj
            width 100%
            padding .2rem 0
            font-size 14px
            text-indent 0.4rem
            border-bottom 1px solid #d3d3d3
            .small_tit
              color #409eff
              display inline
              padding 0.1rem 0.3rem
              background #ECF5FF
              border 1px solid #409eff
            .find_con ul
              width 100%
              margin-top .2rem
            .find_con ul li
              height 0.8rem
              font-size 15px
              width 100%
              display block
              line-height 0.8rem
              &:hover
                color #409eff
                background #ECF5FF
                cursor pointer
      .con_right
        flex 18
        #r_echarts
          width 100%
          height calc(100% - 1rem)
        #msg
          width 0rem
          height 0rem
          overflow hidden
          position absolute
          font-size 14px
          color #000
          line-height 0.6rem
          .el-card
            width 100%
            height 100%
      .tit
        width:400px
        font-size 16px
        height 40px
        line-height 40px
        text-indent 1%
        margin-bottom 10px
        border-bottom 1px solid #d3d3d3
        /*border-top 1px solid #d3d3d3
        border-bottom 1px solid #d3d3d3*/
  </style>
  