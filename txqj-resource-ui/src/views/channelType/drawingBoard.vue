<template>
  <div class="drawingBoard">
    <div class="topo">
      <el-card class="box-card" style="height: 100%; padding: 0px">
        <div class="commitDraw">
          <el-button
            v-if="!isEdit"
            size="mini"
            style="padding-top: 6px; padding-bottom: 6px"
            @click="startEdit"
            v-hasPermi="['dataaccess:table:editDrawingBoard']"
            type="primary">
            编辑图形
          </el-button>
          <el-button
            v-if="isEdit"
            size="mini"
            style="padding-top: 6px; padding-bottom: 6px"
            @click="commitDraw()"
            type="primary">
            保存图形
          </el-button>
          <el-button
            v-if="isEdit"
            size="mini"
            style="padding-top: 6px; padding-bottom: 6px"
            @click="endEdit">
            取消编辑
          </el-button>
        </div>
        <div class="legend">
          <p class="title" @click="legendShow = !legendShow">图例
            <span
               style="height: 100%; line-height: 30px; float: right; cursor: pointer"
              :class="{'el-icon-arrow-down' : legendShow, 'el-icon-arrow-up': !legendShow}"></span>
          </p>
          <ul v-if="legendShow">
            <li>
              <span class="icon"><img height="20" style="margin: 5px 0" :src="require('/public/img/sj.png')"/></span>
              <span class="name">深井</span>
            </li>
            <li>
              <span class="icon"><img height="20" style="margin: 5px 0" :src="require('/public/img/glyxxjth.png')"/></span>
              <span class="name">光缆引下线接头盒</span>
            </li>
            <li><span class="link" style="background: #d1ddea; height: 20px; margin-top: 5px"></span><span class="name">沟道</span></li>
            <li><span class="link" style="background: #9f5de5; height: 6px; margin-top: 12px"></span><span class="name">1000kV</span></li>
            <li><span class="link" style="background: #006fe2; height: 6px; margin-top: 12px"></span><span class="name">500kV</span></li>
            <li><span class="link" style="background: #328857; height: 6px; margin-top: 12px"></span><span class="name">220kV</span></li>
            <li><span class="link" style="background: #6bc8e7; height: 6px; margin-top: 12px"></span><span class="name">110kV</span></li>
          </ul>
        </div>
        <div class="setLinePoint" v-if="setDrawShow">
          <div class="setLinePointCon">
            <el-form label-width="100px" v-if="setDrawType == 'link' || setDrawType == 'fiber'">
              <template v-if="setDrawType == 'fiber'">
                <el-form-item label="名称">
                  <el-input v-model="setFiberData.name" @change="setFiber" size="mini"></el-input>
                </el-form-item>
                <el-form-item label="电压等级">
                  <el-select v-model="setFiberData.level" @change="setFiber" size="mini">
                    <el-option label="1000kV" value="1000kV"></el-option>
                    <el-option label="500kV" value="500kV"></el-option>
                    <el-option label="220kV" value="220kV"></el-option>
                    <el-option label="110kV" value="110kV"></el-option>
                  </el-select>
                </el-form-item>
              </template>
              <template
                v-for="(item, key) in setLinePointArr">
                <p style="margin: 5px 0">
                  {{item.name}}
                  <span v-if="item.del" class="el-icon-delete" @click="delPoint(key)"></span>
                </p>
                <el-form-item :label="item.name + 'X坐标'">
                  <el-input-number v-model="item.addresX" @change="setPoint(key, $event, item.addresY)" size="mini"></el-input-number>
                </el-form-item>
                <el-form-item :label="item.name + 'Y坐标'">
                  <el-input-number v-model="item.addresY" @change="setPoint(key, item.addresX, $event)" size="mini"></el-input-number>
                </el-form-item>
              </template>
            </el-form>
            <el-form label-width="100px" v-if="setDrawType == 'area'">
              <el-form-item label="名称">
                <!--<el-input-number v-model="item.x" @change="setPoint(key, $event, item.y)" size="mini"></el-input-number>-->
                <el-input v-model="setAreaData.name" @change="setArea" size="mini"></el-input>
              </el-form-item>
              <el-form-item label="长度">
                <el-input-number v-model="setAreaData.width" @change="setArea" size="mini"></el-input-number>
              </el-form-item>
              <el-form-item label="宽度">
                <el-input-number v-model="setAreaData.height" @change="setArea" size="mini"></el-input-number>
              </el-form-item>
              <el-form-item label="X坐标">
                <el-input-number v-model="setAreaData.addresX" @change="setArea" size="mini"></el-input-number>
              </el-form-item>
              <el-form-item label="Y坐标">
                <el-input-number v-model="setAreaData.addresY" @change="setArea" size="mini"></el-input-number>
              </el-form-item>
              <el-form-item label="文本颜色">
                <el-color-picker v-model="setAreaData.labelcolor" @change="setArea" size="mini"></el-color-picker>
              </el-form-item>
              <el-form-item label="文本位置">
                <el-select v-model="setAreaData.labelposition" @change="setArea" size="mini">
                  <el-option label="内部上方" value="top.bottom"></el-option>
                  <el-option label="内部右方" value="right.left"></el-option>
                  <el-option label="内部下方" value="bottom.top"></el-option>
                  <el-option label="内部左方" value="left.right"></el-option>
                  <el-option label="内部居中" value="center"></el-option>
                  <el-option label="外部上方" value="top.top"></el-option>
                  <el-option label="外部右方" value="right.right"></el-option>
                  <el-option label="外部下方" value="bottom.bottom"></el-option>
                  <el-option label="外部左方" value="left.left"></el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="背景颜色">
                <el-color-picker v-model="setAreaData.bgcolor" @change="setArea" size="mini"></el-color-picker>
              </el-form-item>
            </el-form>
          </div>
          <div class="setLinePointFoot">
            <el-button
              type="primary"
              size="small"
              style="margin: 0 auto"
              @click="setDrawShow = false"
            >完成</el-button>
          </div>
        </div>
        <div class="fiberBox" v-if="fiberBox">
          <p class="title">沟道关联光缆段
            <span
              style="height: 100%; line-height: 30px; float: right; cursor: pointer; font-size: 18px"
              @click="fiberBox = !fiberBox"
              class="el-icon-circle-close"></span>
          </p>
          <div style="height: calc(100% - 30px);">
            <el-table
              height="100%"
              :data="fiberTable"
              style="width: 100%">
              <el-table-column
                type="index"
                label="序号"
                width="80">
              </el-table-column>
              <el-table-column
                prop="SEGNAME"
                label="此沟道已关联光缆段">
              </el-table-column>
              <el-table-column
                prop="pipeStr"
                width="150"
                label="此光缆段已关联沟道">
              </el-table-column>
            </el-table>
          </div>
        </div>
        <iframe
          ref="iframe"
          width="100%"
          id="iframe"
          name="iframe"
          style="border: none"
          height="100%"
          :src="iframeSrc"
        ></iframe>
      </el-card>
    </div>
  </div>
</template>

<script>
import {getPipeDataApi, setPipeDataApi, getChannelFiberApi, delPipeDataApi} from "@/api/channelType/drawingBoard";
export default {
  name: "drawingBoard",
  data () {
    return {
      parSite: this.$route.params.dictId,
      iframeSrc:'/stationWh/html/channelType.html',
      setDrawShow: false,
      setDrawType: '',
      setDrawId: '',
      setLinePointArr: [],
      setAreaData: {},
      setFiberData: {},
      legendShow: true,
      fiberBox: false,
      fiberTable: [],
      pipeData: {
        link: [],
        fiber: [],
        area: [],
        node: [],
      },
      isEdit: false
    }
  },
  methods: {
    setPoint (index, x, y) {
      if (index == 0) {
        index = 'str'
      } else if (index == this.setLinePointArr.length - 1) {
        index = 'end'
      }
      this.$refs.iframe.contentWindow.setPoint(this.setDrawId, index, x, y)
    },
    delPoint (index) {
      this.$refs.iframe.contentWindow.delPoint(this.setDrawId, index)
    },
    setArea () {
      this.$refs.iframe.contentWindow.setArea(this.setDrawId, this.setAreaData)
    },
    setFiber () {
      this.$refs.iframe.contentWindow.setFiber(this.setDrawId, this.setFiberData)
    },
    async commitDraw () {
      let data = {}
      let succeed = 0
      let all = 3
      await this.$refs.iframe.contentWindow.getBoxAllData(this.parSite).then(res => { data = res })
      if (data.link[0]) {
        all++
        await setPipeDataApi(data.link, '/pipe/delandadd').then(() => { succeed++ }).catch(e => {
          this.$message({
            message: '沟道保存失败',
            type: 'error'
          })
        })
      }
      if (data.fiber[0]) {
        await setPipeDataApi(data.fiber, '/imgfiberseg/delandadd').then(() => { succeed++ }).catch(e => {
          this.$message({
            message: '光缆段保存失败',
            type: 'error'
          })
        })
      } else {
        await delPipeDataApi('/imgfiberseg/removeAll/' + this.parSite).then(() => { succeed++ }).catch(e => {
          this.$message({
            message: '光缆段保存失败',
            type: 'error'
          })
        })
      }
      if (data.area[0]) {
        await setPipeDataApi(data.area, '/imgarea/delandadd').then(() => { succeed++ }).catch(e => {
          this.$message({
            message: '区域保存失败',
            type: 'error'
          })
        })
      } else {
        await delPipeDataApi('/imgarea/removeAll/' + this.parSite).then(() => { succeed++ }).catch(e => {
          this.$message({
            message: '区域保存失败',
            type: 'error'
          })
        })
      }
      if (data.node[0]) {
        await setPipeDataApi(data.node, '/imgnodes/delandadd').then(() => { succeed++ }).catch(e => {
          this.$message({
            message: '图元保存失败',
            type: 'error'
          })
        })
      } else {
        await delPipeDataApi('/imgnodes/removeAll/' + this.parSite).then(() => { succeed++ }).catch(e => {
          this.$message({
            message: '图元保存失败',
            type: 'error'
          })
        })
      }
      if (succeed == all) {
        this.$message({
          message: '数据保存完毕',
          type: 'success'
        })
      }
    },
    async getPipeData () {
      await getPipeDataApi(
        {parSite: this.parSite}, '/pipe/querypipebysite'
      ).then(res => {
        this.pipeData.link = res.data
      })
      await getPipeDataApi(
        {parSite: this.parSite}, '/imgfiberseg/querysegbysite'
      ).then(res => {
        this.pipeData.fiber = res.data
      })
      await getPipeDataApi(
        {parSite: this.parSite}, '/imgarea/queryareabysite'
      ).then(res => {
        this.pipeData.area = res.data
      })
      await getPipeDataApi(
        {parSite: this.parSite}, '/imgnodes/querylist'
      ).then(res => {
        this.pipeData.node = res.data
      })
      this.$refs.iframe.contentWindow.reSetTopo(
        this.pipeData.link,
        this.pipeData.fiber,
        this.pipeData.area,
        this.pipeData.node,
      )
    },
    startEdit () {
      this.isEdit = true
      this.$refs.iframe.contentWindow.isEdit = true
    },
    endEdit () {
      this.isEdit = false
      this.$refs.iframe.contentWindow.isEdit = false
    }
  },
  activated () {
    this.getPipeData()
  },
  created () {
    this.$route.meta.title = this.$route.query.name + '站内沟道图'
    window.setLinePoint = (id, arr) => {
      this.setDrawShow = true
      this.setDrawId = id
      this.setLinePointArr = JSON.parse(JSON.stringify(arr))
      this.setDrawType = 'link'
    }
    window.setFiberPoint = (id, arr, data) => {
      this.setDrawShow = true
      this.setDrawId = id
      this.setLinePointArr = JSON.parse(JSON.stringify(arr))
      this.setFiberData = JSON.parse(JSON.stringify(data))
      this.setDrawType = 'fiber'
    }
    window.setAreaBox = (id, areaData) => {
      this.setDrawShow = true
      this.setDrawId = id
      this.setAreaData = JSON.parse(JSON.stringify(areaData))
      this.setDrawType = 'area'
    }
    window.getObj = (name) => {
      return this[name]
    }
    window.setObj = (name, val) => {
      this[name] = val
    }
    window.getChannelFiber = (id) => {
      getChannelFiberApi({id: id}).then(res => {
        this.fiberBox = true
        this.fiberTable = res.data
      })
    }
  },
  mounted () {
    this.getPipeData()
  },
}
</script>

<style scoped>
  .drawingBoard {
    padding: 20px;
  }
  .topo{
    width: 100%;
    height: 100%;
    position: relative;
  }
  .commitDraw{
    position: absolute;
    right: 10px;
    top: 6px;
  }
  .commitDraw .el-button{
    height: 26px !important;
  }
  .legend {
    width: 200px;
    position: absolute;
    right: 30px;
    bottom: 30px;
    border-radius: 5px;
    border: 1px solid #eee;
  }
  .legend .title{
    width: 100%;
    height: 30px;
    line-height: 30px;
    padding: 0 10px;
    border-bottom: 1px solid #eee;
    margin: 0;
    cursor: pointer;
  }
  .legend ul {
    list-style: none;
    margin: 0 !important;
    padding: 5px 10px !important;
  }
  .legend ul li{
    height: 30px;
    line-height: 30px;
  }
  .legend ul li .icon{
    display: inline-block;
    height: 30px;
    line-height: 30px;
    width: 30%;
    float: left;
  }
  .legend ul li .name{
    display: inline-block;
    height: 30px;
    line-height: 30px;
    width: 70%;
    font-size: 12px;
    float: right;
  }
  .legend ul li .link{
    display: inline-block;
    width: calc(30% - 20px);
  }
  .setLinePoint{
    z-index: 100;
    width: 300px;
    position: absolute;
    top: 50px;
    right: 30px;
    background: #fff;
    border: 1px solid #cccccc;
    border-radius: 3px;
    padding: 10px;
  }
  .setLinePointCon{
    width: 100%;
    max-height: calc(100vh - 375px);
    overflow-y: auto;
    padding: 10px;
  }
  .setLinePointCon span{
    float: right;
    cursor: pointer;
  }
  .setLinePointCon span:hover{
    color: red;
  }
  .setLinePointFoot{
    width: 100%;
    padding-top: 10px;
    text-align: center;
  }
  .fiberBox{
    width: 500px;
    height: 300px;
    background: #fff;
    position: absolute;
    right: 30px;
    bottom: 30px;
    border-radius: 5px;
    border: 1px solid #eee;
  }
  .fiberBox .title{
    width: 100%;
    height: 30px;
    line-height: 30px;
    padding: 0 10px;
    border-bottom: 1px solid #eee;
    margin: 0;
  }
</style>
<style>
  .drawingBoard .el-card__body {
    height: 100%;
    padding: 0px !important;
    overflow-y: hidden;
  }
</style>
