<template>
  <div id="fiberCoreDetail">
    <div class="glllBox" @click="removeRelation">
      <!--上方标题和连线-->
      <div class="top">
        <template v-for="(item, k) in data.topoList">
          <div v-if="item.type == 0"><p>{{item.name}}</p></div>
          <div v-else-if="item.type == 1" class="jietouhe"><p>{{item.name}}</p></div>
        </template>
      </div>
      <!--下方圆饼-->
      <div class="bottom">
        <!--循环光缆段（因为可能又会有多个光缆段）-->
        <div class="fibers">
          <div class="b-body" v-for="(item, k) in data.fiberSegList" :key="k">
            <!--光缆段标题点击事件 点击跳转光缆段分析-->
            <p class="glTitle">{{item.name}}</p>
            <!--纤芯小于100条的情况-->
            <div class="b-b-center">
              <!--外圈-->
              <div class="gl-border">
                <!--中间的圈-->
                <div class="glCenter"></div>
                <!--纤芯外圈-->
                <!--getXxNum()方法 因为每4条纤芯一个圈 所以要除4判断纤芯外圆多少-->
                <div class="glxx-border" v-for="(val, key) in getXxNum(item.lineNumber)" :key="key">
                  <!--纤芯-->
                  <!--类名通过index控制-->
                  <template v-if="item.lineNumber < 100">
                    <div
                      @click="relation('fiber', item.linesList[parseInt(key*4+index)].id)"
                      :class="'glxx glxx'+ index"
                      v-for="(i, index) in 4" :key="index"
                      v-if="parseInt(key*4+index+1)<=parseInt(item.lineNumber)"
                    >
                      <!--key*4+index+1为纤芯序号 因可能出现非四的倍数的纤芯数 （如6） 所以p标签加判断-->
                      <p @contextmenu.prevent="openMenu($event, 'fiber', item.linesList[parseInt(key*4+index)].id)">
                        {{key*4+index+1}}
                      </p>
                    </div>
                  </template>
                  <template v-else>
                    <el-tooltip
                      @click="relation('fiber', item.linesList[parseInt(key*4+index)].id)"
                      :class="'glxx glxx'+ index"
                      v-for="(i, index) in 4"
                      :key="index"
                      effect="dark"
                      :content="key*4+index+1+''"
                      v-if="parseInt(key*4+index+1)<=parseInt(item.lineNumber)"
                      placement="top">
                      <div
                        class=""
                        @contextmenu.prevent="openMenu($event, 'fiber', item.linesList[parseInt(key*4+index)].id)"
                      ></div>
                    </el-tooltip>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="bottomFiber">
          <div class="b-body">
            <!--光缆段标题点击事件 点击跳转光缆段分析-->
            <p class="glTitle" @click="seeDetailsBox.show = true">{{data.fiberObj.name}}</p>
            <!--纤芯小于100条的情况-->
            <div class="b-b-center">
              <!--外圈-->
              <div class="gl-border">
                <!--中间的圈-->
                <div class="glCenter"></div>
                <!--纤芯外圈-->
                <!--getXxNum()方法 因为每4条纤芯一个圈 所以要除4判断纤芯外圆多少-->
                <div class="glxx-border" v-for="(val, key) in getXxNum(data.fiberObj.lineNumber)" :key="key">
                  <!--纤芯-->
                  <!--类名通过index控制-->
                  <template v-if="data.fiberObj.lineNumber < 100">
                    <div
                      @click="relation('fibers', data.fiberObj.linesList[parseInt(key*4+index)].id)"
                      :class="'glxx glxx'+ index"
                      v-for="(i, index) in 4" :key="index"
                      v-if="parseInt(key*4+index+1)<=parseInt(data.fiberObj.lineNumber)"
                    >
                      <!--key*4+index+1为纤芯序号 因可能出现非四的倍数的纤芯数 （如6） 所以p标签加判断-->
                      <p
                        @contextmenu.prevent="openMenu($event, 'fibers', data.fiberObj.linesList[parseInt(key*4+index)].id)">
                        {{key*4+index+1}}
                      </p>
                    </div>
                  </template>
                  <template v-else>
                    <el-tooltip
                      @click="relation('fibers', data.fiberObj.linesList[parseInt(key*4+index)].id)"
                      :class="'fiber glxx glxx'+ index"
                      v-for="(i, index) in 4"
                      :key="index"
                      effect="dark"
                      :content="key*4+index+1+''"
                      v-if="parseInt(key*4+index+1)<=parseInt(fiber.lineNumber)"
                      placement="top">
                      <div
                        @contextmenu.prevent="openMenu($event, 'fibers', data.fiberObj.linesList[parseInt(key*4+index)].id)"
                      ></div>
                    </el-tooltip>
                  </template>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="rightClickBox"
      v-show="rightClickBox.show"
      :style="{ left: rightClickBox.left + 'px', top: rightClickBox.top + 'px' }">
      <template v-if="isEdit">
        <li v-if="editBox.type == 'fiber'" @click="editBoxShow">修改光缆段纤芯属性</li>
        <li v-if="editBox.type == 'fibers'" @click="editBoxShow">修改光缆纤芯属性</li>
      </template>
      <template v-if="!isEdit">
        <li v-if="editBox.type == 'fiber'" @click="editBoxShow">查看光缆段纤芯属性</li>
        <li v-if="editBox.type == 'fibers'" @click="editBoxShow">查看光缆纤芯属性</li>
      </template>
    </div>
    <div class="seeDetailsBox" v-if="seeDetailsBox.show">
      <p class="title">
        纤芯路由详情
        <span class="close el-icon-circle-close" @click="seeDetailsBox.show = false"></span>
      </p>
      <div class="tabBox">
        <el-table
          height="100%"
          :data="seeDetailsBox.data"
          style="width: 100%">
          <el-table-column
            v-if="seeDetailsBox.data[0]"
            prop="fiberSn"
            fixed="left"
            :label="seeDetailsBox.data[0].fiberName"
            min-width="180">
            <template slot-scope="scope">
              <el-input-number
                style="width: 100%"
                size="mini"
                :step="1"
                :min="1"
                :controls="false"
                v-if="scope.$index == tableEditBox.index && tableEditBox.show"
                v-model="tableEditBox.sn"
              ></el-input-number>
              <span v-else>{{scope.row.fiberSn}}</span>
            </template>
          </el-table-column>
          <span v-for="item in seeDetailsBox.tabLength">
            <el-table-column
              v-if="seeDetailsBox.data[0]"
              :prop="'fiberSeg' + item + 'Sn'"
              :label="seeDetailsBox.data[0]['fiberSeg' + item]"
              width="180">
            </el-table-column>
          </span>
          <el-table-column
            v-if="isEdit"
            label="操作"
            fixed="right"
            align="center"
            min-width="100">
            <template slot-scope="scope">
              <el-button
                v-if="!(scope.$index == tableEditBox.index && tableEditBox.show)"
                @click="tableEdit(scope.row.fiberLineId, scope.row.fiberSn, scope.$index)"
                icon="el-icon-edit"
              ></el-button>
              <el-button
                v-if="scope.$index == tableEditBox.index && tableEditBox.show"
                @click="editCommit"
                icon="el-icon-check"
              ></el-button>
              <el-button
                v-if="scope.$index == tableEditBox.index && tableEditBox.show"
                @click="tableEditBox.show = false"
                icon="el-icon-close"
              ></el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog
      v-dialogDrag
      :title="isEdit ? '修改纤芯属性' : '查看纤芯属性'"
      append-to-body
      :close-on-click-modal="false"
      :visible.sync="editBox.show"
      width="750px"
      top="100px">
      <div class="el-dialog-div editTopoBox" style="overflow-y: auto;height: 65vh">
        <editData
          ref="child"
          :modelObj="editBox.modelObj"
          :dataObj="editBox.data"
          :type="'`ry-middledb`'"
          :dbName="'`ry-middledb`'"
        ></editData>
      </div>
      <div slot="footer" class="dialog-footer" v-if="isEdit">
        <el-button @click="editBox.show = false">取 消</el-button>
        <el-button type="primary" @click="editCommit()">保 存</el-button>
      </div>
      <div slot="footer" class="dialog-footer" v-if="!isEdit">
        <el-button @click="editBox.show = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {nameTo_, nameToTuoFeng} from "@/utils"
  import { getModelId } from "@/api/zdwh/zdzywh";
  import {
    seeDetailsApi,
    editCommitApi,
    formEditCommitApi
  } from "@/api/gltxh/fiberCoreDetail";
  import editData from "@/views/publicPage/edit/editData";
  export default {
    name: "fiberCoreDetail",
    props: {
      data: Object,
      isEdit: Boolean,
    },
    components: {editData},
    data () {
      return {
        rightClickBox: {
          show: false,
          top: 0,
          left: 0,
        },
        editBox: {
          type: '',
          id: '',
          show: false,
          modelObj: {},
          data: {},
        },
        seeDetailsBox: {
          show: false,
          fiberId: '',
          tabLength: 0,
          data: [],
        },
        tableEditBox: {
          show: false,
          index: 0,
          id: '',
          sn: 0,
        }
      }
    },
    watch: {
      //   监听属性对象，newValue为新的值，也就是改变后的值
      'rightClickBox.show' (newValue, oldValue) {
        if (newValue) {
          document.body.addEventListener("click", this.closeMenu);
        } else {
          document.body.removeEventListener("click", this.closeMenu);
        }
      },
    },
    methods: {
      getXxNum (num) {
        // 向上取整 为避免出现非四的倍数的纤芯数 (如6条纤芯)
        return Math.ceil(parseInt(num) / 4)
      },
      async locationLine () {
        await this.getDetails()
        await this.reSetData()
        if (this.data.fiberSegList.length <= 1) {
          document.getElementsByClassName('bottomFiber')[0].style.padding = '0px 360px'
          document.getElementsByClassName('bottomFiber')[0].getElementsByClassName('b-body')[0].style.padding = '0px'
          document.getElementsByClassName('bottomFiber')[0].style.margin = '0px auto'
        } else {
          document.getElementsByClassName('bottomFiber')[0].style.width = 450 * this.data.fiberSegList.length + 'px'
        }
        let borderSize = 300 - 300 *  0.08
        let centerSize = 300 - 300 *  0.04
        let glBorder = document.getElementsByClassName('b-b-center')
        for (let v = 0; v < glBorder.length; v++) {
          let num = glBorder[v].getElementsByClassName('glxx-border')
          let datas = []
          let divIndex = 0
          let NumLength1
          let NumLength2
          let NumLength3
          let size
          if (num.length <= 15) {
            NumLength1 = num.length
            size = 600 / NumLength1 > (borderSize / 3) ? (borderSize / 3) : 600 / NumLength1
            for (let i = 0; i < NumLength1; i++) {
              let x = Math.cos((360 / NumLength1) * i * Math.PI / 180)
              let y = Math.sin((360 / NumLength1) * i * Math.PI / 180)
              datas.push({
                y: y * 2,
                x: -x * 2
              })
              num[divIndex].style.top = datas[i].x * 46 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.left = datas[i].y * 46 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.width = size + 'px'
              num[divIndex].style.height = size + 'px'
              divIndex++
            }
          } else if (num.length > 15 && num.length <= 26) {
            NumLength1 = parseInt(num.length / 2) - 4
            NumLength2 = num.length - NumLength1
            size = borderSize / 3 / 2
            for (let i = 0; i < NumLength1; i++) {
              let x = Math.cos((360 / NumLength1) * i * Math.PI / 180)
              let y = Math.sin((360 / NumLength1) * i * Math.PI / 180)
              datas.push({
                y: y * 2,
                x: -x * 2
              })
              num[divIndex].style.top = datas[i].x * 35 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.left = datas[i].y * 35 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.width = size + 'px'
              num[divIndex].style.height = size + 'px'
              divIndex++
            }
            datas = []
            for (let i = 0; i < NumLength2; i++) {
              let x = Math.cos((360 / NumLength2) * i * Math.PI / 180)
              let y = Math.sin((360 / NumLength2) * i * Math.PI / 180)
              datas.push({
                y: y * 2,
                x: -x * 2
              })
              num[divIndex].style.top = datas[i].x * 58 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.left = datas[i].y * 58 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.width = size + 'px'
              num[divIndex].style.height = size + 'px'
              divIndex++
            }
          } else {
            NumLength1 = parseInt(num.length / 3) - 6
            NumLength2 = parseInt(num.length / 3)
            NumLength3 = num.length - NumLength1 - NumLength2
            size = (borderSize / 3 / 3) - 1
            datas = []
            for (let i = 0; i < NumLength1; i++) {
              let x = Math.cos((360 / NumLength1) * i * Math.PI / 180)
              let y = Math.sin((360 / NumLength1) * i * Math.PI / 180)
              datas.push({
                y: y * 2,
                x: -x * 2
              })
              num[divIndex].style.top = datas[i].x * 32 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.left = datas[i].y * 32 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.width = size + 'px'
              num[divIndex].style.height = size + 'px'
              divIndex++
            }
            datas = []
            for (let i = 0; i < NumLength2; i++) {
              let x = Math.cos((360 / NumLength2) * i * Math.PI / 180)
              let y = Math.sin((360 / NumLength2) * i * Math.PI / 180)
              datas.push({
                y: y * 2,
                x: -x * 2
              })
              num[divIndex].style.top = datas[i].x * 47 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.left = datas[i].y * 47 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.width = size + 'px'
              num[divIndex].style.height = size + 'px'
              divIndex++
            }
            datas = []
            for (let i = 0; i < NumLength3; i++) {
              let x = Math.cos((360 / NumLength3) * i * Math.PI / 180)
              let y = Math.sin((360 / NumLength3) * i * Math.PI / 180)
              datas.push({
                y: y * 2,
                x: -x * 2
              })
              num[divIndex].style.top = datas[i].x * 62 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.left = datas[i].y * 62 + (centerSize / 2) - size / 2 + 'px'
              num[divIndex].style.width = size + 'px'
              num[divIndex].style.height = size + 'px'
              divIndex++
            }
          }
          let zyxxNum = 0
        }
        this.$emit('loadingEnd')
      },
      openMenu(e, type, id) {
        this.rightClickBox = {
          top: e.pageY,
          left: e.pageX,
          show: true,
        }
        this.editBox.type = type
        this.editBox.id = id
      },
      closeMenu() {
        this.rightClickBox.show = false;
      },
      editBoxShow () {
        let tableName
        if (this.editBox.type == 'fiber') {
          tableName = 'SG_TCDEV_FIBERLINE_B'
        } else if (this.editBox.type == 'fibers') {
          tableName = 'SG_TCDEV_FIBERLINE'
        }
        getModelId({tableName: tableName}).then(res => {
          this.editBox.modelObj = res.data
          this.editBox.data.ID = this.editBox.id
          this.editBox.show = true
          this.$nextTick(()=>{
            this.$refs.child.modelObj = this.editBox.modelObj
            this.$refs.child.dataObj = this.editBox.data
            this.$refs.child.beforeLoading()
          })
        })
      },
      relation (type, id) {
        if (id == '') {
          this.$message({
            message: '此纤芯不存在对应关系',
            type: 'info'
          });
          return
        }
        if (type == 'fibers') {
          window.setTimeout(() => {
            this.removeRelation()
            for (let i = 0; i < this.data.LLlist.length; i++) {
              if (this.data.LLlist[i].fiberlineId == id) {
                let segId = this.data.LLlist[i].parFiberseg
                let segLineId = this.data.LLlist[i].fiberseglineId
                let segIndex = this.data.fiberSegList.findIndex(item => {
                  return item.id == segId
                })
                let segLineIndex = this.data.fiberSegList[segIndex].linesList.findIndex(item => {
                  return item.id == segLineId
                })
                let doc = document.
                getElementsByClassName('fibers')[0].
                getElementsByClassName('b-body')[segIndex].
                getElementsByClassName('glxx')[segLineIndex]
                let className = doc.getAttribute('class')
                doc.setAttribute('class', className + ' selectGlxx')

              }
            }
            let fiberLineIndex = this.data.fiberObj.linesList.findIndex(item => {
              return item.id == id
            })
            let doc = document.
            getElementsByClassName('bottomFiber')[0].
            getElementsByClassName('b-body')[0].
            getElementsByClassName('glxx')[fiberLineIndex]
            let className = doc.getAttribute('class')
            doc.setAttribute('class', className + ' selectGlxx')
          }, 10)
        } else if (type == 'fiber') {
          let line = this.data.LLlist.find(item => {
            return item.fiberseglineId == id
          })
          if (line) {
            line = line.fiberlineId
          } else {
            line = ''
          }
          this.relation('fibers', line)
        }
      },
      removeRelation () {
        let docs = document.getElementsByClassName('selectGlxx')
        for (let i = 0; i < docs.length; i) {
          let oldClass = docs[i].getAttribute('class')
          oldClass = oldClass.split(' ')
          oldClass.splice(oldClass.length - 1, 1)
          let newClass = oldClass.join(' ')
          docs[i].setAttribute('class', newClass)
        }
      },
      getDetails () {
        this.seeDetailsBox.fiberId = this.data.fiberObj.id
        seeDetailsApi({parFiber: this.data.fiberObj.id}).then(res => {
          this.seeDetailsBox.data = res.data
          this.seeDetailsBox.show = true
          let length = 0
          for (let key in this.seeDetailsBox.data[0]) {
            length++
          }
          this.seeDetailsBox.tabLength = (length - 4) / 2 < 0 ? 0 : (length - 4) / 2
        })
      },
      tableEdit (id, sn, index) {
        this.tableEditBox = {
          show: true,
          index: index,
          id: id,
          sn: parseInt(sn),
        }
      },
      editCommit () {
        editCommitApi({
          fiberLineId: this.tableEditBox.id,
          fiberSn: this.tableEditBox.sn
        }).then(res => {
          this.$message({
            showClose: true,
            message: '修改成功',
            type: 'success'
          })
          this.removeRelation()
          this.$emit('openFiberCoreDetail')
        })
      },
      reSetData () {
        this.rightClickBox = {
          show: false,
          top: 0,
          left: 0,
        }
        this.editBox = {
          type: '',
          id: '',
          show: false,
          modelObj: {},
          data: {},
        }
        this.seeDetailsBox.fiberId = 0
        this.tableEditBox = {
          show: false,
          index: 0,
          id: '',
          sn: 0,
        }
      },
      formEditCommit () {
        let url
        if (this.editBox.type == 'fiber') {
          url = '/fiberedit/fiberedit/line/updateSegline'
        } else if (this.editBox.type == 'fibers') {
          url = '/fiberedit/fiberline/updatefiberline'
        }
        let data = {}
        for (let key in this.$refs.child.pageForm) {
          this.editBox.data[key] = this.$refs.child.pageForm[key]
        }1
        if (this.$refs.child.addErrForm.length > 0) {
          data.checkoutType = '1'
        } else {
          data.checkoutType = '0'
        }
        formEditCommitApi(url, data).then(res => {
          if(res.code==200){
            this.$message({
              message: '修改成功',
              type: 'success'
            })
            this.locationLine()
            this.editBox.show = false
          }else{
            this.$message({
              message: '修改失败，请重新操作',
              type: 'warning'
            })
          }
        })
      },
    },
  }
</script>

<style scoped>
  #fiberCoreDetail{
    width: 100%;
    height: 100%;
    position: relative;
  }
  #fiberCoreDetail {
    --sizeW: 300px;
  }
  .glllBox{
    height: 100%;
    font-size: 12px;
    overflow-x: auto;
    position: relative;
  }
  .glllBox .Title {
    position: absolute;
    width: calc(100% - 30px);
    height: 44px;
    font-size: 16px;
    font-weight: bold;
    line-height: 44px;
    text-align: center;
    margin-bottom: 10px;
    background-color: rgb(220, 243, 252);
    color: #444;
    border-top: 1px solid rgb(110, 209, 229);
  }
  .glllBox .top{
    height: 50px;
    text-align: center;
    white-space: nowrap;
  }
  .glllBox .top div:first-child{
    margin-left: 225px;
  }
  .glllBox .top div{
    display: inline-block;
    width: 50px;
    height: 50px;
    margin: 0 200px;
    background-size: 50px 50px;
    background-image: url("../../../assets/images/zhandian.png");
    background-repeat: no-repeat;
    position: relative;
    z-index: 100;
  }
  .glllBox .top div p{
    position: absolute;
    margin-top: 50px;
    height: 30px;
    line-height: 30px;
    text-align: center;
    left: 50%;
    transform: translate(-50%,0);
  }
  .glllBox .top .jietouhe{
    background-size: 50px 50px;
    background-image: url("../../../assets/images/jietouhe.png");
    background-repeat: no-repeat;
  }
  .glllBox .bottom{
    margin-top: -25px;
    text-align: center;
    white-space: nowrap;
    padding: 0 250px;
    height: calc(100% - 25px);
    position: relative;
  }
  .glllBox .bottom .b-body{
    display: inline-block;
    width: var(--sizeW);
    padding: 0 calc(var(--sizeW) / 4);
    border-top: 5px solid rgb(121, 187, 255);
    box-sizing: content-box;
    position: relative;
  }

  .glllBox .bottom .b-body .glTitle{
    text-align: center;
    line-height: 20px;
    margin: 0 auto;
    position: relative;
  }
  .glllBox .bottom .b-body .b-b-center{
    width: var(--sizeW);
    height: var(--sizeW);
    background: white;
    border-radius: 50%;
    position: relative;
    margin-top: 10px;
    border: calc(var(--sizeW) * 0.02) solid black;
    box-shadow: 0 0 calc(var(--sizeW) * 0.01) calc(var(--sizeW) * 0.02) dodgerblue;
    box-sizing: border-box !important;
  }
  .glllBox .bottom .b-body .b-b-center .gl-border{
    width: 100%;
    height: 100%;
    border-radius: 50%;
    border: calc(var(--sizeW) * 0.02) solid rgb(0,140,97);
    box-sizing: border-box!important;
  }
  .glllBox .bottom .b-body .b-b-center .glCenter{
    width: calc(100% / 3);
    height: calc(100% / 3);
    position: absolute;
    border-radius: 50%;
    top: calc(100% / 3);
    left: calc(100% / 3);
    background: linear-gradient(to bottom,#eee,#bbb);
  }
  .glllBox .bottom .b-body .b-b-center .glxx-border{
    background: #ddd;
    position: absolute;
    border-radius: 50%;
  }
  .glllBox .bottom .b-body .b-b-center .glxx-border .glxx{
    width: 40%;
    height: 40%;
    background-color: #fff;
    border-radius: 50%;
    float: left;
    text-align: center;
    position: relative;
    cursor: pointer;
  }
  .selectGlxx{
    background-color: #409EFF !important;
    color: #fff;
  }
  .glllBox .bottom .b-body .b-b-center .glxx-border .glxx p{
    width: 100%;
    height: 20px !important;
    padding: 0 !important;
    line-height: 20px;
    position: absolute;
    top: calc(50% - 10px);
    margin: 0!important;
  }
  .glllBox .bottom .b-body .b-b-center .glxx-border .glxx0,
  .glllBox .bottom .b-body .b-b-center .glxx-border .glxx2{
    margin-left: 10%;
  }
  .glllBox .bottom .b-body .b-b-center .glxx-border .glxx0,
  .glllBox .bottom .b-body .b-b-center .glxx-border .glxx1{
    margin-top: 10%;
  }
  .glllBox .legend{
    position: absolute;
    right: 0.5rem;
    bottom: 0.5rem;
    line-height: 0.5rem;
  }
  .glllBox .legend .legendItem{
    width: 0.5rem;
    height: 0.3rem;
    border-radius: 0.05rem;
    display: inline-block;
    margin: auto 0.1rem auto 0.2rem;
    transform: translateY(0.05rem);
    border: 1px solid;
  }



  .fibers .b-b-center:before{
    content: "";
    display: block;
    position: absolute;
    top: -30px;
    left: -40px;
    width: 0;
    height: 30px;
    border-left: 1px solid rgb(121, 187, 255);
  }
  .fibers .b-b-center:after{
    content: "";
    display: block;
    position: absolute;
    top: -30px;
    right: -40px;
    width: 0;
    height: 30px;
    border-left: 1px solid rgb(121, 187, 255);
  }
  .fibers .glTitle{
    border-bottom: 1px solid rgb(121, 187, 255);
  }
  .fibers .glTitle:before{
    content: "";
    display: block;
    position: absolute;
    top: 110px;
    right: -10px;
    width: 0;
    height: 0;
    border:10px solid;
    margin-top: -100px;
    border-color: transparent transparent transparent rgb(121, 187, 255);
  }
  .fibers .glTitle::after{
    content: "";
    display: block;
    position: absolute;
    top: 110px;
    left: -10px;
    width: 0;
    height: 0;
    border:10px solid;
    margin-top: -100px;
    border-color: transparent rgb(121, 187, 255) transparent transparent;
  }
  .fibers .b-body:first-child:before{
    content: "";
    display: block;
    position: absolute;
    top: 50px;
    left: -20px;
    width: 0;
    height: 300px;
    border-left: 1px solid rgb(121, 187, 255);
  }
  .fibers .b-body:last-child:after{
    content: "";
    display: block;
    position: absolute;
    top: 50px;
    left: auto;
    right: -20px;
    width: 0;
    height: 300px;
    border-left: 1px solid rgb(121, 187, 255);
  }




  .fiberBox {
    width: 100%;
    height: 350px;
    padding-top: 10px;
    position: absolute;
    z-index: 100;
    bottom: 30px;
  }
  .bottomFiber {
    width: 100%;
    margin: 20px auto 0 auto!important;
  }
  .bottomFiber .b-body{
    width: 100% !important;
    text-align: center;
    border-top: 1px solid rgb(121, 187, 255)!important;
    box-sizing: border-box!important;
  }
  .bottomFiber .b-body:before{
    content: "";
    display: block;
    position: absolute;
    top: 90px;
    right: -10px;
    width: 0;
    height: 0;
    border:10px solid;
    margin-top: -100px;
    border-color: transparent transparent transparent rgb(121, 187, 255);
  }
  .bottomFiber .b-body:after{
    content: "";
    display: block;
    position: absolute;
    top: 90px;
    left: -10px;
    width: 0;
    height: 0;
    border:10px solid;
    margin-top: -100px;
    border-color: transparent rgb(121, 187, 255) transparent transparent;
  }
  .bottomFiber .b-body .b-b-center{
    margin-left: auto!important;
    margin-right: auto!important;
  }
  .bottomFiber .glTitle{
    color: #409EFF;
    cursor: pointer;
    font-weight: bold;
  }
  .rightClickBox{
    margin: 0;
    background: #fff;
    z-index: 3000;
    position: fixed;
    list-style-type: none;
    padding: 5px 0;
    border-radius: 4px;
    font-size: 12px;
    font-weight: 400;
    color: #333;
    box-shadow: 2px 2px 3px 0 rgba(0, 0, 0, 0.3);
  }
  .rightClickBox li{
    margin: 0;
    padding: 7px 16px;
    cursor: pointer;
  }
  .rightClickBox li:hover{
    background: #eee;
  }

  .seeDetailsBox{
    width: calc(50% - 220px);
    height: 40%;
    position: absolute;
    right: 10px;
    bottom: 30px;
    border: 1px solid #eee;
    border-radius: 5px;
  }
  .seeDetailsBox .title{
    height: 40px;
    line-height: 40px;
    margin: 0 auto!important;
    background-color: rgb(121, 187, 255);
    padding: 0 10px;
    color: #fff;
  }
  .seeDetailsBox .title .close{
    float: right;
    font-size: 20px;
    cursor: pointer;
    line-height: 40px;
  }
  .seeDetailsBox .title .close:hover{
    color: #eee;
  }
  .seeDetailsBox .tabBox{
    width: 100%;
    height: calc(100% - 40px);
  }
</style>
