<template>
  <div class="room">
    <div id="container" @mousemove="mouseMove" @click="clickRack"></div>
    <div
        id='plane'
        :style="{left: state.planePos.left,top:state.planePos.top,display: state.planeDisplay}"
    >
      <div class="title">机柜信息</div>
      <p>机柜名称：{{ state.curCabinet.name }}</p>
      <p>机柜类型：{{ state.curCabinet.rackType }}</p>
      <p>机柜编号：{{ state.curCabinet.displaySn }}</p>
      <p>机柜容量：{{ state.curCabinet.capacity }}</p>
    </div>
    <div class="nav">
      <i class="el-icon-house" style="color:#000000;font-size: 18px;margin-right: 10px"></i>
      <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item v-if="siteInfo.name" style="font-size: 16px">{{ siteInfo.name }}</el-breadcrumb-item>
        <el-breadcrumb-item v-if="roomInfo.name" style="font-size: 16px">{{ roomInfo.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>

    <div class="roomList">
      <div class="title">
        <span>{{ siteInfo.name }}</span>
        <i class="el-icon-arrow-down" style="color:#879FA6;"></i>
      </div>
      <div class="content">
        <div
            :class="roomInfo.id == item.id ? 'roomItemActivated' : 'roomItem'"
            @click="switchRoom(item)"
            v-for="item in roomList ">
          <i class="el-icon-notebook-2" style="margin: 0 5px 0 10px;font-size: 16px"></i>
          <span>{{item.name}}</span>
        </div>
      </div>

    </div>

    <div class="roomInfo">
      <div class="title">机房信息</div>
      <div class="content">
        <div class="infoItem">
          <span class="span1">机房名称</span>
          <span class="span2">{{ roomInfo.name }}</span>
        </div>
        <div class="infoItem">
          <span class="span1">机房位置</span>
          <span class="span2">{{ roomInfo.location }}</span>
        </div>
        <div class="infoItem">
          <span class="span1">资产单位</span>
          <span class="span2">{{ roomInfo.assetsOwnershipComId }}</span>
        </div>
      </div>
    </div>
    <!-- 添加或修改通信站基本信息对话框 -->
    <el-dialog :title="box.title" :visible.sync="box.open" width="1000px" append-to-body>
      <div class="shelf" v-loading="box.loading">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in box.shelfList">
            <div class="shelfItem">
              <p class="title">{{item.name}}</p>
              <img src="./ne.png" v-if="item.neName">
              <p v-if="item.neName">{{item.neName}}</p>
            </div>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="box.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as THREE from 'three'
import {OrbitControls} from 'three/examples/jsm/controls/OrbitControls.js'
import {GLTFLoader} from 'three/examples/jsm/loaders/GLTFLoader.js'
import { getRoomListBySiteId,getNeByRackId } from "@/api/info/index"

export default {
  name: 'RoomViewer',
  props:{
    siteId: String
  },
  data() {
    return {
      publicPath: process.env.BASE_URL,
      mesh: null,
      camera: null,
      scene: null,
      renderer: null,
      controls: null,
      maps: null,
      //机柜集合
      cabinets: [],
      //空调集合
      aircondition: [],
      //鼠标划入的机柜
      curCabinet: '',
      state: {
        planePos: {
          //信息面板的位置
          left: 0,
          top: 0
        },
        //信息面板的可见性
        planeDisplay: 'none',
        //机柜信息
        curCabinet: {
          //机柜名称
          name: 'Loading……',
          //机柜类型
          rackType: 0,
          //机柜编号
          displaySn: 0,
          //容量
          capacity: 0
        }
      },
      siteInfo: {
        name:'',
      },
      roomInfo:{
        id:'',
        name:'',
        location:'',
        assetsOwnershipComId:'',
      },
      roomList: [],
      rackList: [],
      box:{
        shelfList: [],
        title:'',
        open: false,
        loading: false,
      },
    }
  },
  mounted() {
    this.maps = new Map()//添加maps属性，用来存储纹理对象，以避免贴图的重复加载
    this.crtTexture("cabinet-hover.jpg")
    this.init()
  },
  methods: {
    // 初始化
    init() {
      this.createScene() // 创建场景
      this.loadGLTF() // 加载GLTF模型
      this.createCamera() // 创建相机
      this.createRender() // 创建渲染器
      this.createControls() // 创建控件对象
      this.render() // 渲染

    },
    // 创建场景
    createScene() {
      this.scene = new THREE.Scene()
    },
    // 加载GLTF模型
    loadGLTF() {
      const loader = new GLTFLoader()
      loader.load(`${process.env.BASE_URL}models/machineRoom.gltf`, (gltf) => {
        gltf.scene.children.forEach((obj) => {
          let map = obj.material.map
          let color = obj.material.color
          if (obj.name.includes('cabinet')) {//机柜
            obj.rotateY(Math.PI)
            this.cabinets.push(obj)
          } else if (obj.name.includes('aircondition')) {//空调
            obj.rotateY(Math.PI)
            this.aircondition.push(obj)
          }else {
            // console.log(obj)
          }
          this.changeMat(obj, map, color)
        })
        this.scene.add(...gltf.scene.children)
        this.cabinets.forEach(obj => {
          this.scene.remove(obj)
        })
        this.cabinets.sort((a,b)=>{
          let sn1 = Number(a.name.replace('cabinet-',''))
          let sn2 = Number(b.name.replace('cabinet-',''))
          return sn1 - sn2
        })
        this.aircondition.forEach(obj => {
          this.scene.remove(obj)
        })
        this.getRoom()
      })
    },

    // 创建相机
    createCamera() {
      const element = document.getElementById('container')
      const width = element.clientWidth // 窗口宽度
      const height = element.clientHeight // 窗口高度
      const k = width / height // 窗口宽高比
      this.camera = new THREE.PerspectiveCamera(30, k, 1, 3000)
      this.camera.position.set(-3, 22, -18) // 设置相机位置
      this.camera.lookAt(-0.2, -0.4, -0.4) // 设置相机方向
      this.scene.add(this.camera)
    },
    // 创建渲染器
    createRender() {
      const element = document.getElementById('container')
      this.renderer = new THREE.WebGLRenderer({antialias: true, alpha: true})
      this.renderer.setSize(element.clientWidth, element.clientHeight) // 设置渲染区域尺寸
      element.appendChild(this.renderer.domElement)
    },

    // 创建控件对象
    createControls() {
      this.controls = new OrbitControls(this.camera, this.renderer.domElement)
      this.controls.target.set(-0.2, -0.4, -0.4)
      this.controls.update()
    },

    render() {
      // console.log('camera.position', this.camera.position)
      // console.log('controls.target', this.controls.target)
      this.renderer.render(this.scene, this.camera)
      requestAnimationFrame(this.render)
    },

    getRoom(){
      getRoomListBySiteId({
        id:this.siteId
      }).then(res=>{
        this.siteInfo = res.data.site
        this.roomList = res.data.roomList
        if (this.roomList.length > 0){
          this.switchRoom(this.roomList[0])
        }
      })
    },
    switchRoom(item){
      this.cabinets.forEach(obj => {
        this.scene.remove(obj)
      })
      this.aircondition.forEach(obj => {
        this.scene.remove(obj)
      })
      this.roomInfo = item
      this.rackList = item.rackList
      for (let i = 0; i < this.rackList.length; i++) {
        this.cabinets[i].rackId = this.rackList[i].id
        this.cabinets[i].name = this.rackList[i].name
        this.cabinets[i].rackType = this.rackList[i].rackType
        this.cabinets[i].displaySn = this.rackList[i].displaySn
        this.cabinets[i].capacity = this.rackList[i].capacity
        this.scene.add(this.cabinets[i])
        if (i % 10 == 0){
          let j = i / 10
          this.scene.add(this.aircondition[j])
        }
      }
    },

    /**
     * obj：需要修改材质的Mesh 对象
     * map：GLTF 模型里的贴图对象
     * color：GLTF 模型的颜色
     */
    changeMat(obj, map, color) {
      if (map) {
        obj.material = new THREE.MeshBasicMaterial({
          map: this.crtTexture(map.name)
        })
      } else {
        obj.material = new THREE.MeshBasicMaterial({color})
      }
    },
    crtTexture(imgName) {
      let curTexture = this.maps.get(imgName)
      if (!curTexture) {
        curTexture = new THREE.TextureLoader().load(`${process.env.BASE_URL}models/` + imgName)
        curTexture.flipY = false
        curTexture.wrapS = 1000
        curTexture.wrapT = 1000
        this.maps.set(
            imgName,
            curTexture
        )
      }
      return curTexture
    },
    selectCabinet(x, y) {
      const {cabinets, renderer, camera, maps, curCabinet} = this
      const {width, height} = renderer.domElement
      //射线投射器，可基于鼠标点和相机，在世界坐标系内建立一条射线，用于选中模型
      const raycaster = new THREE.Raycaster()
      //鼠标在裁剪空间中的点位
      const pointer = new THREE.Vector2()

      // 鼠标的canvas坐标转裁剪坐标
      pointer.set(
          (x / width) * 2 - 1,
          -(y / height) * 2 + 1,
      )
      // 基于鼠标点的裁剪坐标位和相机设置射线投射器
      raycaster.setFromCamera(
          pointer, camera
      )
      // 选择机柜
      const intersect = raycaster.intersectObjects(cabinets)[0]
      let intersectObj = intersect ? intersect.object : null
      // 若之前已有机柜被选择，且不等于当前所选择的机柜，取消之前选择的机柜的高亮
      if (curCabinet && curCabinet !== intersectObj) {
        const material = curCabinet.material
        material.setValues({
          map: maps.get('cabinet.jpg')
        })
      }
      /*
        若当前所选对象不为空：
          触发鼠标在机柜上移动的事件。
          若当前所选对象不等于上一次所选对象：
            更新curCabinet。
            将模型高亮。
            触发鼠标划入机柜事件。
        否则若上一次所选对象存在：
          置空curCabinet。
          触发鼠标划出机柜事件。
      */
      if (intersectObj) {
        this.onMouseMoveCabinet(x, y)
        if (intersectObj !== curCabinet) {
          this.curCabinet = intersectObj
          const material = intersectObj.material
          material.setValues({
            map: maps.get('cabinet-hover.jpg')
          })
          this.onMouseOverCabinet(intersectObj)
        }
      } else if (curCabinet) {
        this.curCabinet = null
        this.onMouseOutCabinet()
      }
    },
    // 鼠标移动事件
    mouseMove(e) {
      this.selectCabinet(e.offsetX, e.offsetY)
    },
    clickRack({offsetX, offsetY}) {
      const {cabinets, renderer, camera} = this
      const {width, height} = renderer.domElement
      //射线投射器，可基于鼠标点和相机，在世界坐标系内建立一条射线，用于选中模型
      const raycaster = new THREE.Raycaster()
      //鼠标在裁剪空间中的点位
      const pointer = new THREE.Vector2()

      // 鼠标的canvas坐标转裁剪坐标
      pointer.set(
          (offsetX / width) * 2 - 1,
          -(offsetY / height) * 2 + 1,
      )
      // 基于鼠标点的裁剪坐标位和相机设置射线投射器
      raycaster.setFromCamera( pointer, camera)
      const intersect = raycaster.intersectObjects(cabinets)[0]
      let intersectObj = intersect ? intersect.object : null
      if (intersectObj) {
        this.onMouseOutCabinet()
        let index = this.rackList.findIndex(item => item.id === intersectObj.rackId)
        if (index !== -1){
          this.box.title = intersectObj.name
          this.box.open = true
          this.box.loading = true
          getNeByRackId({id:intersectObj.rackId}).then(res => {
            this.box.shelfList = res.data
            this.box.loading = false
          })
        }
      }
    },
    //鼠标划入机柜事件，参数为机柜对象
    onMouseOverCabinet(cabinet) {
      let index = this.rackList.findIndex(item => item.id === cabinet.rackId)
      if (index !== -1){
        this.state.curCabinet.name = cabinet.name
        this.state.curCabinet.rackType = cabinet.rackType
        this.state.curCabinet.displaySn = cabinet.displaySn
        this.state.curCabinet.capacity = cabinet.capacity
        this.state.planeDisplay = 'block'
      }
    },
    //鼠标在机柜上移动的事件，参数为鼠标在canvas画布上的坐标位
    onMouseMoveCabinet(x, y) {
      // console.log(x,y);
      this.state.planePos.left = (x + 10) + 'px'
      this.state.planePos.top = (y + 10) + 'px'
    },
    //鼠标划出机柜的事件
    onMouseOutCabinet() {
      this.state.planeDisplay = 'none'
    },
  }
}
</script>
<style scoped lang="scss">
.room {
  width: 100%;
  height: 100%;
  position: relative;

  #container {
    width: 100%;
    height: 100%;
  }

  #plane {
    position: absolute;
    top: 0;
    left: 0;
    display: none;
    width: 300px;
    height: 180px;
    border: 1px #079b9f solid;
    background-color: #2E4A51;
    color: #079b9f;
    .title{
      padding-left: 10px;
      width: 100%;
      height: 30px;
      font-size: 16px;
      line-height: 30px;
      color: #FFFFFF;
      background-image: linear-gradient(to right, rgb(16, 213, 210), rgb(150, 203, 202));
    }
    p{
      line-height: 30px;
      padding: 0 20px;
      font-size: 14px;
      white-space: nowrap; /* 不换行 */
      overflow: hidden; /* 隐藏超出的内容 */
      text-overflow: ellipsis; /* 用省略号表示被隐藏的部分 */
    }
  }
  .nav{
    position: absolute;
    top:  0;
    left: 20px;
    min-width: 230px;
    height: 20px;
    font-size: 14px;
    font-weight: bold;
    display: flex;
    align-items: center;
  }
  .roomList{
    position: absolute;
    top:  30px;
    left: 20px;
    padding: 0 10px;
    width: 210px;
    height: 200px;
    border: 1px #079b9f solid;
    border-right: none;
    border-image: linear-gradient( 115deg, rgba(7, 155, 159, 0.3), rgba(7, 155, 159, 0)) 2 2;
    background-image: linear-gradient(to right, rgba(7, 155, 159, 0.3), rgba(7, 155, 159, 0));
    .title{
      width: 100%;
      height: 40px;
      font-size: 16px;
      font-weight: bold;
      color: #2E4A51;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .content{
      width: 100%;
      height: calc(100% - 40px);
      overflow-y: auto;
      .roomItemActivated{
        width: 100%;
        height: 40px;
        font-size: 14px;
        line-height: 40px;
        color: #2E4A51;
        background-image: linear-gradient(to right, rgba(7, 155, 159, 0.5), rgba(7, 155, 159, 0));
        display: flex;
        align-items: center;
        cursor: pointer;
        span {
          white-space: nowrap; /* 不换行 */
          overflow: hidden; /* 隐藏超出的内容 */
          text-overflow: ellipsis; /* 用省略号表示被隐藏的部分 */
        }
      }
      .roomItem{
        width: 100%;
        height: 40px;
        font-size: 14px;
        line-height: 40px;
        color: #6c758d;
        display: flex;
        align-items: center;
        cursor: pointer;
        span {
          white-space: nowrap; /* 不换行 */
          overflow: hidden; /* 隐藏超出的内容 */
          text-overflow: ellipsis; /* 用省略号表示被隐藏的部分 */
        }
      }
    }

  }

  .roomInfo {
    position: absolute;
    top: 0px;
    right: 20px;
    width: 230px;
    height: 400px;
    border: 1px #079b9f solid;
    border-left: none;
    border-image: linear-gradient(115deg, rgba(7, 155, 159, 0), rgba(7, 155, 159, 0.3)) 2 2;
    background-image: linear-gradient(to right, rgba(7, 155, 159, 0), rgba(7, 155, 159, 0.3));
    .title{
      padding-left: 10px;
      width: calc(100% - 10px);
      height: 40px;
      font-size: 16px;
      font-weight: bold;
      color: #6c758d;
      line-height: 40px;
      background-image: linear-gradient(to right, rgba(7, 155, 159, 0.5), rgba(7, 155, 159, 0));
    }
    .content{
      width:calc(100% - 20px);
      height: calc(100% - 40px);
      overflow-y: auto;
      padding: 0 10px;
      .infoItem{
        width: 100%;
        min-height: 70px;
        display: flex;
        flex-direction: column;
        justify-content: space-evenly;
        font-size: 15px;
        .span1{
          color: rgb(108, 117, 141);
          font-weight: bold;
        }
        .span2{
          color: rgb(108, 117, 141);
        }
      }
    }
  }
}
.shelf{
  height: 500px;
  width: 100%;
  overflow-y: scroll;
  overflow-x: hidden;
  .shelfItem{
    margin-bottom: 10px;
    width: 100%;
    height: 100px;
    display: flex;
    flex-direction: column;
    //justify-content: space-evenly;
    align-items: center;
    overflow: hidden;
    p{
      width: 100%;
      text-align: center;
      line-height: 25px;
      font-size: 14px;
      white-space: nowrap; /* 不换行 */
      overflow: hidden; /* 隐藏超出的内容 */
      text-overflow: ellipsis; /* 用省略号表示被隐藏的部分 */
    }
    .title{
      font-size: 18px;
      font-weight: bold;
      color: #079b9f;
    }
    img{
      width: 50px;
      height: 50px;
    }
  }
}

</style>
