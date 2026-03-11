<template>
  <div>
    <div :class="customClass" v-loading="!formLoading" :style="customStyle">
      <template v-if="formLoading">
        <div class="formTitle" v-show="notNullList.length>0">
          <span>* 必填项</span>
        </div>
        <el-form
          class="opaForm"
          v-show="notNullList.length>0"
          :model="pageForm"
          label-width="250px"
          size="mini">
          <el-form-item
            v-for="(item, key) in notNullListSort"
            v-if="item.show != false"
          >
            <template #label>
              <img
                width="28px"
                height="28px"
                style="margin-right: 8px; float: left"
                :src="getIconUrl(item.icon)"
              >
              <span
                style="display: inline-block; width: calc(100% - 36px);">
                {{item.attributeNameCn}}
              </span>
            </template>
            <el-select
              v-if="item.attType==='select'"
              clearable
              filterable
              remote
              :multiple="modelObj.tableName == 'SG_TCDEV_FIBER_B' && item.attributeNameUnderline == 'PAR_LINE' "
              :disabled="modelObj.tableName == 'SG_TCDEV_FIBER_B' && item.attributeNameUnderline == 'PAR_LINE' "
              :loading="loading"
              v-loadmore="loadmore"
              @focus="focusSelect = notNullList[key]"
              :remote-method="(val) => remoteMethod(val, 'notNullList', key, item.list)"
              @change="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
              v-model="pageForm[item.attributeNameUnderline]"
              :class="
              rulesForm[item.attributeNameUnderline] === undefined ||
              rulesForm[item.attributeNameUnderline] === '' ||
              rulesForm[item.attributeNameUnderline] === null
              ? '' : 'error'"
            >
              <el-option
                v-for="(arr, index) in notNullList[key].list"
                :label="arr.name"
                :key="arr.code + '' + index"
                :value="arr.code"
                :disabled="arr.disabled"
                :title="arr.manageDept ? arr.name+' （ '+arr.manageDept+' ）' : arr.name"
              ></el-option>
            </el-select>
            <el-date-picker
              v-model="pageForm[item.attributeNameUnderline]"
              @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
              type="date"
              :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
              v-else-if="item.attType==='datetime'"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
            </el-date-picker>
            <el-select
              clearable
              ref="treeSelet"
              v-else-if="item.attType==='tree'"
              :disabled="!item.tree.isLastNode == 1"
              @focus="treeBoxShow(item.tree, pageForm[item.attributeNameUnderline])"
              @change="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
              v-model="pageForm[item.attributeNameUnderline]"
              :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
              popper-class="treeSelect"
            >
              <el-option
                v-for="(arr) in notNullList[key].list"
                :label="arr.name"
                :key="arr.code"
                :value="arr.code"
              ></el-option>
              <div
                v-if="tree.show"
                class="treeBox"
              >
                <div style="height: 30px">
                  <el-input size="mini" v-model="tree.search" style="width: 80%"></el-input>
                  <el-button size="mini" type="primary" v-if="!tree.selNode.id" style="width: 20%" @click="searchTreeMultistage" :loading="!tree.treeLoadmoreLoadIng">搜索</el-button>
                  <el-button size="mini" type="primary" v-if="tree.selNode.id"  style="width: 20%" @click="searchTree" :loading="!tree.treeLoadmoreLoadIng">搜索{{tree.buttonName}}</el-button>
<!--                  <el-button size="mini" type="primary" v-if="tree.selNode.id" @click="getMoreNode" style="margin-top: 10px" :loading="!tree.treeLoadmoreLoadIng">加载更多{{tree.buttonName}}</el-button>-->
                </div>
                <div style="height: calc(100% - 30px); overflow-y: auto">
                  <el-tree
                    :default-expanded-keys="tree.expandedKeys"
                    v-if="tree.treeShow"
                    :data="tree.treeData"
                    ref="tree"
                    highlight-current
                    :load="loadNode"
                    :lazy="tree.lazy"
                    node-key="showId"
                    accordion
                    @node-click="nodeClick"
                    @node-collapse="nodeCollapse"
                    :expand-on-click-node="false"
                    :props="{
                      children: 'children',
                      label: 'name',
                      isLeaf: 'leaf'
                    }"
                  >
                  <span
                    class="custom-tree-node"
                    slot-scope="{ node, data }"
                  >
                    <span v-if="!data.isAdd && !data.isLoad" :title="node.label">{{getNodeName(node, data)}} : {{node.label}}</span>
                    <span v-if="data.isAdd" style="color: #409EFF" class="el-icon-circle-plus-outline">{{node.label}}</span>
                    <span v-if="data.isLoad" style="color: #409EFF" class="el-icon-refresh">{{node.label}}</span>
                  </span>
                  </el-tree>
                </div>
              </div>
            </el-select>
            <el-input @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])" v-else v-model="pageForm[item.attributeNameUnderline]"
                      :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
            <!--表单验证信息 别删-->
            <!--<div style="margin-left:210px;margin-top:-30px;width:300px">-->
            <!--<span style="font-size:10px;color:red;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>-->
            <!--<span style="font-size:10px;color:grey;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>-->
            <!--</div>-->
          </el-form-item>
        </el-form>
        <div class="formTitle" v-show="nullList.length>0">
          <span>非必填</span>
        </div>
        <el-form
          class="opaForm"
          v-show="nullList.length>0"
          label-width="250px"
          size="mini">
          <el-form-item
            v-for="(item, key) in nullListSort"
            v-if="item.show != false">
            <template #label>
              <img
                width="28px"
                height="28px"
                style="margin-right: 8px; float: left"
                :src="getIconUrl(item.icon)"
              >
              <span
                style="display: inline-block; width: calc(100% - 36px);">
                {{item.attributeNameCn}}
              </span>
            </template>
            <el-select
              v-if="item.attType==='select'"
              clearable
              filterable
              remote
              :multiple="modelObj.tableName == 'SG_TCDEV_FIBER_B' && item.attributeNameUnderline == 'PAR_LINE' "
              :disabled="modelObj.tableName == 'SG_TCDEV_FIBER_B' && item.attributeNameUnderline == 'PAR_LINE' "
              :loading="loading"
              v-loadmore="loadmore"
              @focus="focusSelect = nullList[key]"
              :remote-method="(val) => remoteMethod(val, 'nullList', key, item.list)"
              @change="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
              v-model="pageForm[item.attributeNameUnderline]"
              :class="
              rulesForm[item.attributeNameUnderline] === undefined ||
              rulesForm[item.attributeNameUnderline] === '' ||
              rulesForm[item.attributeNameUnderline] === null
              ? '' : 'error'"
            >
              <el-option
                v-for="(arr, index) in nullList[key].list"
                :label="arr.name"
                :key="arr.code + '' + index"
                :value="arr.code"
                :disabled="arr.disabled"
                :title="arr.manageDept ? arr.name+' （ '+arr.manageDept+' ）' : arr.name"
              ></el-option>
            </el-select>
            <el-date-picker
              v-model="pageForm[item.attributeNameUnderline]"
              @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
              type="date"
              :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
              v-else-if="item.attType==='datetime'"
              value-format="yyyy-MM-dd"
              placeholder="选择日期">
            </el-date-picker>
            <el-select
              clearable
              ref="treeSelet"
              v-else-if="item.attType==='tree'"
              :disabled="!item.tree.isLastNode == 1"
              @focus="treeBoxShow(item.tree, pageForm[item.attributeNameUnderline])"
              @change="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])"
              v-model="pageForm[item.attributeNameUnderline]"
              :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"
              popper-class="treeSelect"
            >
              <el-option
                v-for="(arr) in nullList[key].list"
                :label="arr.name"
                :key="arr.code"
                :value="arr.code"
              ></el-option>
              <div
                v-if="tree.show"
                class="treeBox"
              >
                <div style="height: 30px">
                  <el-input size="mini" v-model="tree.search" style="width: 80%"></el-input>
                  <el-button size="mini" type="primary" v-if="!tree.selNode.id" style="width: 20%" @click="searchTreeMultistage" :loading="!tree.treeLoadmoreLoadIng">搜索</el-button>
                  <el-button size="mini" type="primary" v-if="tree.selNode.id"  style="width: 20%" @click="searchTree" :loading="!tree.treeLoadmoreLoadIng">搜索{{tree.buttonName}}</el-button>
<!--                  <el-button size="mini" type="primary" v-if="tree.selNode.id" @click="getMoreNode" style="margin-top: 10px" :loading="!tree.treeLoadmoreLoadIng">加载更多{{tree.buttonName}}</el-button>-->
                </div>
                <div style="height: calc(100% - 30px); overflow-y: auto">
                  <el-tree
                    :default-expanded-keys="tree.expandedKeys"
                    v-if="tree.treeShow"
                    :data="tree.treeData"
                    ref="tree"
                    highlight-current
                    :load="loadNode"
                    :lazy="tree.lazy"
                    node-key="showId"
                    accordion
                    @node-click="nodeClick"
                    @node-collapse="nodeCollapse"
                    :expand-on-click-node="false"
                    :props="{
                      children: 'children',
                      label: 'name',
                      isLeaf: 'leaf'
                    }"
                  >
                  <span
                    class="custom-tree-node"
                    slot-scope="{ node, data }"
                  >
                    <span v-if="!data.isAdd && !data.isLoad" :title="node.label">{{getNodeName(node, data)}} : {{node.label}}</span>
                    <span v-if="data.isAdd" style="color: #409EFF" class="el-icon-circle-plus-outline">{{node.label}}</span>
                    <span v-if="data.isLoad" style="color: #409EFF" class="el-icon-refresh">{{node.label}}</span>
                  </span>
                  </el-tree>
                </div>
              </div>
            </el-select>
            <el-input @blur="checkAdd(item.attributeNameUnderline,pageForm[item.attributeNameUnderline])" v-else v-model="pageForm[item.attributeNameUnderline]"
                      :class="rulesForm[item.attributeNameUnderline] ===undefined||rulesForm[item.attributeNameUnderline]===''||rulesForm[item.attributeNameUnderline]===null?'':'error'"></el-input>
            <!--<div style="margin-left:210px;margin-top:-30px;width:300px">-->
            <!--<span style="font-size:10px;color:red;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]!==undefined&&rulesForm[item.attributeNameUnderline]!==''" v-html="rulesForm[item.attributeNameUnderline]"></span>-->
            <!--<span style="font-size:10px;color:grey;" v-if="showRules" v-show="rulesForm[item.attributeNameUnderline]===undefined" v-html="item.rules"></span>-->
            <!--</div>-->
          </el-form-item>
        </el-form>
      </template>
    </div>
    <addTreeNode
      style="height: 100%; width: 100%;"
      @closeAddTreeNodeBox="closeAddTreeNodeBox"
      :parObj="treeAddNodeBox"
    ></addTreeNode>
  </div>
</template>

<script>
  import {
    getPrimaryKeyName,
    getForm,
    initgriddatasite,
    tableDropDownBox,
    selectAllRules,
    getColunmId,
    getVerrules,
    getTreeConfigApi,
    getTreeOptions,
    getTreeSelDataApi,
    queryResDataTreeAll
  } from "@/api/zdwh/zdzywh";
  import addTreeNode from '../addTreeNode'
  export default {
    name: "editData",
    components: {addTreeNode},
    props: {
      modelObj:Object,
      dataObj:Object,
      type: String,
      dbName: String,
      form:Object,
      // 是否可以新增树的节点
      isAddTreeNode: {
        type: Boolean,
        default: () => true
      },
      customStyle: {
        type: Object,
        default: () => {}
      },
      customClass: {
        type: Array,
        default: () => ['el-dialog-div']
      }
    },
    data() {
      return {
        formLoading: false,
        //主键
        keyClu:'',
        //数据的属性列
        cluList:[],
        //非空与空属性
        notNullList:[],
        nullList:[],
        //校验规则
        rulesForm:{},
        //不符合规范的属性数据
        addErrForm:[],
        //页面展示校验结果
        showRules:false,
        //表单数据
        pageForm:{},
        listMap:[],
        loading: false,
        focusSelect: {},
        searchStr: '',
        // 树
        tree: {
          expandedKeys: [],
          // 是否展示
          show: false,
          treeShow: true,
          // 全部树信息(表单中可能有多个树)
          allTreeData: [],
          // 选中的叶子节点所属的的结构(根据结构获取树的每个节点的传参)
          selTree: [],
          // 搜索框内容
          search: '',
          pageNum: 1,
          pageSize: 40,
          // 防止多次请求导致数据重复问题 并且控制按钮loading
          treeLoadmoreLoadIng: true,
          // 按钮标题(用于展示区分操作节点)
          buttonName: '',
          // 选中的叶子节点所属的树(实际展示树的值)
          treeData: [],
          // 选中的节点(也用去区分操作节点)
          selNode: {},
          // 选中的节点 以及父节点集合 (回填时使用)
          selNodes: [],
          // 是否加载树
          loadTree: true,
          boxStyle: {},
          // 树的id
          id: '',
          //树懒加载
          lazy:true
        },
        treeAddNodeBox: {
          show: false,
        },
        addressId: '',
        addressName: ''
      }
    },
    created() {
      // this.beforeLoading()
    },
    computed:{
      nullListSort() {
        return this.nullList.sort((a1,a2)=>{
          if (a1.sortNum != a2.sortNum){
            return a1.sortNum - a2.sortNum
          }else {
            return a1.id - a2.id
          }

        })
      },
      notNullListSort() {
        return this.notNullList.sort((a1,a2)=>{
          if (a1.sortNum != a2.sortNum){
            return a1.sortNum - a2.sortNum
          }else {
            return a1.id - a2.id
          }
        })
      }
    },
    watch:{
      modelObj(newVal,oldVal){
        /* if(newVal.modelId===oldVal.modelId){
           this.reOpen()
         }else{
           this.beforeLoading()
         }*/
      },
      notNullList:{
        deep:true,
        handler(newVal){
          window.setTimeout(() => {
            this.formLoading = false
            this.$nextTick(() => {
              this.formLoading = true
            })
          }, 1000)
          if(this.modelObj.tableName == 'SG_TCCON_TCBUZ_B'){
            if(this.pageForm.BUZ_TYPE){
              let BUZ_TYPE = this.pageForm.BUZ_TYPE
              this.pageForm.BUZ_TYPE = null
              this.pageForm.BUZ_TYPE = BUZ_TYPE
            }
          }
        }
      },
      nullList:{
        deep:true,
        handler(newVal){
          window.setTimeout(() => {
            this.formLoading = false
            this.$nextTick(() => {
              this.formLoading = true
            })
          }, 1000)
          if(this.modelObj.tableName == 'SG_TCCON_TCBUZ_B'){
            if(this.pageForm.BUZ_TYPE){
              let BUZ_TYPE = this.pageForm.BUZ_TYPE
              this.pageForm.BUZ_TYPE = null
              this.pageForm.BUZ_TYPE = BUZ_TYPE
            }
          }
        }
      },
      pageForm:{
        deep:true,
        handler(newVal){
          if(this.modelObj.tableName == 'SG_TCCON_TCBUZ_B'){
            let show = false
            if(newVal.BUZ_TYPE == '1010' ){
              show = true
            }
            for (let i = 0; i < this.notNullList.length; i++) {
              if(
                this.notNullList[i].attributeNameUnderline == 'VOLTAGE_CLASS' ||
                this.notNullList[i].attributeNameUnderline == 'LINE_NAME' ||
                this.notNullList[i].attributeNameUnderline == 'CHANNEL_CODE' ||
                this.notNullList[i].attributeNameUnderline == 'PROTECT_NUM' ||
                this.notNullList[i].attributeNameUnderline == 'PROTECT_PORT'
              ){
                this.notNullList[i].show = show
              }
            }
            for (let i = 0; i < this.nullList.length; i++) {
              if(
                this.nullList[i].attributeNameUnderline == 'VOLTAGE_CLASS' ||
                this.nullList[i].attributeNameUnderline == 'LINE_NAME' ||
                this.nullList[i].attributeNameUnderline == 'CHANNEL_CODE' ||
                this.nullList[i].attributeNameUnderline == 'PROTECT_NUM' ||
                this.nullList[i].attributeNameUnderline == 'PROTECT_PORT'
              ){
                this.nullList[i].show = show

              }
            }
          }
        }
      }
    },
    methods: {
      getIconUrl (url) {
        let u = process.env.BASE_URL + 'img/formImg/'
        if (url && url!== '') {
          return u + url
        }
        return u + 'default.png'
      },
      // 点击叶子节点 树弹框显示
      async treeBoxShow (tree, val) {
        // 初始化tree变量
        this.tree.treeLoadmoreLoadIng = true
        this.tree.buttonName = ''
        this.tree.treeData = []
        this.tree.selNode = {}
        this.tree.selNodes = []
        this.tree.loadTree = true
        this.tree.id = tree.treeId
        this.$set(this.tree, 'expandedKeys', [])
        this.tree.lazy = true
        // 在表单的多个树结构中 取出对应的树结构(根据点击节点的treeId区分)
        this.tree.selTree = this.tree.allTreeData.find(item => {
          return item.treeInfo.id == tree.treeId
        })
        // 存储选中树的整体结构
        this.tree.selTree = this.tree.selTree.treeNodeInfo
        if (val && val != '') {
          this.tree.loadTree = false
          await getTreeSelDataApi({
            dataCode: val,
            metaModeId: tree.metaModelId,
            nodeId: tree.nodeId,
            nodeLevel: this.getTreeConfigByNodeId(tree.nodeId, this.tree.selTree).nodeLevel,
            treeId: tree.treeId,
          }).then(res => {
            let data = []
            this.tree.treeData = this.setTreeData(res.data, data)
            this.tree.show = true
            this.tree.lazy = false
            this.tree.treeShow = false
            this.$nextTick(() => {
              this.tree.treeShow = true
              this.$set(this.tree, 'expandedKeys', data)
            })
          })
        } else {
          this.tree.show = true
          this.tree.treeShow = false
          this.$nextTick(() => {
            this.tree.treeShow = true
          })
        }
      },
      // 根据某层结构id 获取这一层在树中的所有结构数据
      getTreeConfigByNodeId (id, data) {
        if (id == data.id) {
          return data


        } else {
          return this.getTreeConfigByNodeId(id, data.children[0])
        }
      },
      setTreeData (oldData, arr) {
        let data = [
          {
            code: oldData.dataCodeValue,
            metaModelTechId: oldData.metaModelTechId,
            name: oldData.dataNameValue,
            nodeId: oldData.id,
            nodeLevel: oldData.nodeLevel,
            showId: oldData.nodeLevel+oldData.dataCodeValue,
          }
        ]
        arr.push(oldData.nodeLevel+oldData.dataCodeValue)
        if (oldData.children[0]) {
          data[0].children = this.setTreeData(oldData.children[0], arr)
        }
        return data
      },
      // 树加载事件
      loadNode (node, resolve) {
        this.tree.search = ''
        this.tree.selNode = {}
        if (this.tree.loadTree) {
          this.tree.treeLoadmoreLoadIng = false
          // 我也忘了为什么try 不这样可能会有报错
          try {
            let params = this.getTreeParams(node)
            this.tree.id = params.treeId
            getTreeOptions(params).then(res => {
              this.tree.treeLoadmoreLoadIng = true
              // 是否可以新增树的节点
              if (this.isAddTreeNode) {
                res.data.unshift({
                  code: 'addClick' + (node.level + 1),
                  name: ' 添加' + this.getNodeName({level: node.level + 1}),
                  nodeLevel: (node.level + 1),
                  metaModelTechId: (node.level + 1),
                  showId: 'addClick' +(node.level + 1),
                  isAdd: true,
                  leaf: true
                })
              }
              if (res.data.length >= this.tree.pageSize){
                res.data.push({
                  code: 'loadMore' + (node.level + 1),
                  name: ' 加载更多' + this.getNodeName({level: node.level + 1}),
                  nodeLevel: (node.level + 1),
                  metaModelTechId: (node.level + 1),
                  showId: 'loadMore' +(node.level + 1),
                  isLoad: true,
                  leaf: true
                })
              }
              return resolve(res.data)
            })
          } catch (e) {
            this.tree.treeLoadmoreLoadIng = true
            if (this.isAddTreeNode) {
              return resolve([{

                code: 'addClick' + (node.level + 1),
                name: ' 添加' + this.getNodeName({level: node.level + 1}),
                nodeLevel: (node.level + 1),
                metaModelTechId: (node.level + 1),
                showId: 'addClick' +(node.level + 1),
                isAdd: true,
                leaf: true
              }])
            } else {
              return resolve([])
            }
          }
        } else {
          let data = JSON.parse(JSON.stringify(this.tree.treeData))
          this.tree.treeData = []
          window.setTimeout(() => {
            this.$nextTick(() => {
              this.tree.treeData = data
            })
          }, 1)
        }
      },
      // 用于树label名添加层级备注
      getNodeName (node, data) {
        let treeParams = this.getTreeParams1(node.level, this.tree.selTree)
        return treeParams.name
      },
      closeAddTreeNodeBox () {
        this.treeAddNodeBox.show = false
      },
      // 树的点击事件
      nodeClick (data, node) {
        this.tree.selNode = node
        if (data.isAdd) {
          let treeParams = this.getTreeParams1(node.level, this.tree.selTree)
          treeParams = JSON.parse(JSON.stringify(treeParams))
          this.treeAddNodeBox = {
            show: true,
            tableName: treeParams.tableName.split("'").join(""),
            title: treeParams.name,
          }
          if (node.level > 1) {
            this.treeAddNodeBox.parentLabel = treeParams.parentColumn.toUpperCase()
            this.treeAddNodeBox.parentValue = node.parent.data.code
            //添加端口，需要获取机框
            if(this.treeAddNodeBox.tableName == 'SG_TCDEV_TCPORT_B'){
              this.treeAddNodeBox.parentLabel2 = 'PAR_SHELF'
              this.treeAddNodeBox.parentValue2 = node.parent.parent.parent.data.code
            }
          }
          for (let i = 0; i < this.$refs.treeSelet.length; i++) {
            if (this.$refs.treeSelet[i]) {
              this.$refs.treeSelet[i].blur()
            }
          }
          return
        }
        if (data.isLoad){
          if (this.tree.treeLoadmoreLoadIng) {
            let data
            if (node.level == 0) {
              data = node
            } else {
              data = node.parent
            }
            this.tree.treeLoadmoreLoadIng = false
            let params = this.getTreeParams(data)
            // 获取当前页码
            let num = Math.ceil((data.childNodes.length - 2) / 40)
            params.pageNum = num + 1
            getTreeOptions(params).then(res => {
              this.$refs.tree[0].remove(node)
              for (let i = 0; i < res.data.length; i++) {
                this.$refs.tree[0].append(res.data[i], data)
              }
              if (res.data.length >= this.tree.pageSize){
                this.$refs.tree[0].append({
                  code: 'loadMore' + (data.level + 1),
                  name: ' 加载更多' + this.getNodeName({level: data.level + 1}),
                  nodeLevel: (data.level + 1),
                  metaModelTechId: (data.level + 1),
                  showId: 'loadMore' +(data.level + 1),
                  isLoad: true,
                  leaf: true
                }, data)
              }
              this.tree.treeLoadmoreLoadIng = true
            })
          }
          return
        }
        if (!this.tree.loadTree) {
          this.tree.loadTree = true
          this.tree.treeShow = false
          this.tree.selNode = this.getNodeParent(node)
          this.tree.buttonName = this.tree.selTree.name
          this.$nextTick(() => {
            this.tree.treeShow = true
          })
        } else {
          // 设置选中节点 同时两个操作按钮允许点击
          // 获取当前层级内容
          let treeParams = this.getTreeParams1(this.tree.selNode.level, this.tree.selTree)
          // 标明按钮操作层级
          this.tree.buttonName = treeParams.name
        }

        // 获取点击节点以及父节点的内容 并封装为数组 以下统称“选中所有节点”
        this.tree.selNodes = []
        this.getNodeParents(this.tree.selNode)
        // 循环所有选中节点
        for (let i = 0; i <  this.tree.selNodes.length; i++) {
          // 判断在表单树在表单所有字段（notNullList 和 nullList）中的位置并为其下拉框赋值
          // 判断逻辑 节点的metaModelTechId(对应字段id) == 字段id
          let fieldIndex1 = this.notNullList.findIndex(item => {
            return item.id == this.tree.selNodes[i].metaModelTechId
          })
          let fieldIndex2 = this.nullList.findIndex(item => {
            return item.id == this.tree.selNodes[i].metaModelTechId
          })
          let field
          if (fieldIndex1 != -1) {
            // 为下拉框赋值
            this.notNullList[fieldIndex1].list = [this.tree.selNodes[i]]
            // 获取对应字段名
            field = this.notNullList[fieldIndex1]
          } else if (fieldIndex2 != -1) {
            this.nullList[fieldIndex2].list = [this.tree.selNodes[i]]
            field = this.nullList[fieldIndex2]
          }
          if (field) {
            // 为表单中对应的字段赋值
            this.pageForm[field.attributeNameUnderline] = this.tree.selNodes[i].code
          }
        }
      },
      getNodeParent (node) {
        if (node.parent) {
          return this.getNodeParent(node.parent)
        } else {
          return node
        }
      },
      // 用于获取此节点的所有父节点 并封装为一个数组
      // 逻辑为 如果不是根节点(node.level != 0) 那就放入数组中 并继续递归父节点
      getNodeParents (node) {
        if (node.level != 0) {
          this.getNodeParents(node.parent)
          this.tree.selNodes.push(node.data)
        }
      },
      // 收起节点事件
      nodeCollapse (data, node) {
        // 清空搜索框
        this.tree.search = ''
        // 收起节点时 让选中节点为此节点的上级 以便操作此级节点
        this.tree.selNode = node.parent
        // 我也忘了为什么try 根节点时不try可能报错
        try {
          let treeParams = this.getTreeParams1(node.level, this.tree.selTree)
          // 更新按钮层级说明
          this.tree.buttonName = treeParams.name
        } catch (e) {
        }
      },
      // 获取 在获取该节点下级节点时向后台传递的参数
      getTreeParams (node) {
        let data
        // 根节点特殊处理
        // 不传上级节点code(dataCode) nodeLevel为0
        if (node.level == 0) {
          data = {
            treeId: this.tree.selTree.treeId,
            dataName: this.tree.search,
            metaModeId: this.modelObj.modelId,
            nodeLevel: 0,
            pageNum: this.tree.pageNum,
            pageSize: this.tree.pageSize,
          }
        } else {
          // getTreeParams1 用于获取树结构中对应节点的 nodeLevel, id, treeId
          let treeParams = this.getTreeParams1(node.level, this.tree.selTree)
          data = {
            dataCode: node.data.code,
            dataName: this.tree.search,
            metaModeId: this.modelObj.modelId,
            nodeId: treeParams.id,
            nodeLevel: treeParams.nodeLevel,
            pageNum: this.tree.pageNum,
            pageSize: this.tree.pageSize,
            treeId: treeParams.treeId,
          }
        }
        return data
      },
      // 用于获取树结构中对应节点的数据
      // 逻辑为 如果传入的 层级(level) == 树结构(data)此层级的level 则返回此层结构 否则递归此层的children
      // data初次传入为树顶级结构
      getTreeParams1 (level, data) {
        if (level == data.nodeLevel) {
          return data
        } else {
          return this.getTreeParams1(level, data.children[0])
        }
      },
      // 获取更多按钮
      getMoreNode () {
        // treeLoadmoreLoadIng防止重复请求
        if (this.tree.treeLoadmoreLoadIng) {
          // 进行判断(根节点特殊处理)
          // 如果是根节点 则直接传入 不是跟节点就传入选中节点的parent
          // getTreeParams有对根节点的特殊处理
          let data
          if (this.tree.selNode.level == 0) {
            data = this.tree.selNode
          } else {
            data = this.tree.selNode.parent
          }
          this.tree.treeLoadmoreLoadIng = false
          let params = this.getTreeParams(data)
          // 获取当前页码
          let num = Math.ceil((data.childNodes.length - 1) / 40)
          params.pageNum = num + 1
          getTreeOptions(params).then(res => {
            // 我也忘了为什么要逐个添加而不是用updateKeyChildren方法
            // 好像是因为updateKeyChildren需要特殊处理根节点
            for (let i = 0; i < res.data.length; i++) {
              this.$refs.tree[0].append(res.data[i], data)
            }
            this.tree.treeLoadmoreLoadIng = true
          })
        }
      },
      // 搜索方法
      async searchTree () {
        // 进行判断(根节点特殊处理)
        // 如果是根节点 则直接传入 不是跟节点就传入选中节点的parent
        // getTreeParams有对根节点的特殊处理
        let data
        if (this.tree.selNode.parent) {
          data = this.tree.selNode.parent
        } else {
          data = this.tree.selNode
        }
        this.tree.treeLoadmoreLoadIng = false
        let params = this.getTreeParams(data)
        params.pageNum = 1

        let res
        await getTreeOptions(params).then(r => {
          res = r.data
          if (this.isAddTreeNode) {
            res.unshift({
              code: 'addClick' + (data.level + 1),
              name: ' 添加' + this.getNodeName({level: data.level + 1}),
              nodeLevel: (data.level + 1),
              metaModelTechId: (data.level + 1),
              showId: 'addClick' +(data.level + 1),
              isAdd: true,
              leaf: true
            })
          }
        })
        // 用延时函数重置子节点(必须这么做不然会出现搜出的内容不能展开)
        if (data.level == 0) {
          this.tree.treeData = res
        } else {
          for (let i = 0; i < this.$refs.tree.length; i++) {
            await this.$refs.tree[i].updateKeyChildren(data.data.showId, [])
            window.setTimeout(() => {
              this.$refs.tree[i].updateKeyChildren(data.data.showId, res)
            }, 1)
          }
        }
        this.tree.treeLoadmoreLoadIng = true
        this.tree.selNode = {}
        this.tree.search = ''
        // 对根节点特殊处理
      },
      async searchTreeMultistage(){
        if(this.tree.search){
          this.$set(this.tree, 'expandedKeys', [])
          this.tree.treeLoadmoreLoadIng = false
          this.tree.lazy = true
          this.tree.loadTree = true
          await queryResDataTreeAll({
            treeId : this.tree.id,
            dataName : this.tree.search
          }).then(res => {
            let data = res.data
            this.tree.treeData = data
            let arr = []
            this.getSearchNode(arr, this.tree.treeData)
            this.$nextTick(() => {
              this.$set(this.tree, 'expandedKeys', arr)
            })
          })
          this.tree.treeLoadmoreLoadIng = true
          this.tree.search = ''
        }else {
          this.tree.loadTree = true
          this.tree.lazy = true
          this.tree.treeShow = false
          this.$nextTick(() => {
            this.tree.treeShow = true
          })
          this.$set(this.tree, 'expandedKeys', [])
        }
        this.tree.selNode = {}
      },
      getSearchNode (arr, nodes) {
        for (let i = 0; i < nodes.length; i++) {
          if (nodes[i].children){
            arr.push(nodes[i].showId)
            this.getSearchNode(arr,nodes[i].children)
          }
        }
      },
      loadmore () {
        if (this.focusSelect.total > this.focusSelect.list.length) {
          let pageNum = parseInt(this.focusSelect.list.length / 20) * 20
          tableDropDownBox({
            modelId: this.modelObj.modelId,
            modeVersion: this.modelObj.modelVersion,
            attribute: this.focusSelect.attributeNameUnderline,
            name: this.searchStr,
            pageNum: pageNum,
            pageSize: 20,
          }).then((res) => {
            this.loading = true
            if (res.rows.length > 0) {
              this.focusSelect.list.push.apply(
                this.focusSelect.list,
                res.rows
              )
            }
            this.$nextTick(() => {
              this.loading = false
            })
          })
        } else {
          if (
            !this.focusSelect.list[this.focusSelect.list.length - 1].disabled
          ) {
            this.loading = true
            this.focusSelect.list.push({
              code: 'aaabbbccc',
              name: '没有更多了',
              disabled: true
            })
            this.$nextTick(() => {
              this.loading = false
            })
          }
        }
      },
      remoteMethod (val, objName, showListIndex, list) {
        this.searchStr = val
        tableDropDownBox({
          modelId: this.modelObj.modelId,
          modeVersion: this.modelObj.modelVersion,
          attribute: this.focusSelect.attributeNameUnderline,
          name: this.searchStr,
          pageNum: 0,
          pageSize: 20,
        }).then((res) => {
          this.loading = true
          this.focusSelect.list = res.rows
          this.focusSelect.total = res.total
          this.$nextTick(() => {
            this.loading = false
          })
        })
      },
      //页面初始化
      beforeLoading(){
        this.rulesForm={}
        this.pageForm={}
        this.addErrForm=[]
        this.notNullList = []
        this.nullList = []
        this.getCluList()
      },
      //第二次进入页面
      reOpen(){
        this.rulesForm={}
        this.pageForm={}
        this.addErrForm=[]
        this.getKey()
      },
      //根据modelId获取主键
      getKey(){
        getPrimaryKeyName({modeId:this.modelObj.modelId}).then(response=>{
          this.keyClu = response.msg
          console.log(this.dataObj,'=========1');
          console.log(this.modelObj,'=========2');
          if(this.dataObj[this.keyClu]!==undefined&&this.dataObj[this.keyClu]!==''){
            let params={
              modelId:this.modelObj.modelId,
              isPage:'0',
              whereAttributes:[{attributeName:this.keyClu,attributeValue:this.dataObj[this.keyClu],isPrimaryKey:'1'}]
            }
            if (
              this.type != undefined
            ) {
              params.type = this.type
              params.dbName =  this.dbName
            }
            getForm(params).then(res=>{
              this.pageForm=res.data
              for (let i = 0; i < this.notNullList.length; i++) {
                this.checkAdd(this.notNullList[i].attributeNameUnderline,this.pageForm[this.notNullList[i].attributeNameUnderline])
                if (
                  this.notNullList[i].attType == 'select' &&
                  this.notNullList[i].list &&
                  !this.notNullList[i].list.find(item => { return this.pageForm[this.notNullList[i].attributeNameUnderline] == item.code  }) &&
                  this.pageForm[this.notNullList[i].attributeNameUnderline]
                ) {
                  tableDgetCluListropDownBox({
                    modelId: this.modelObj.modelId,
                    modeVersion: this.modelObj.modelVersion,
                    attribute: this.notNullList[i].attributeNameUnderline,
                    code: this.pageForm[this.notNullList[i].attributeNameUnderline]
                  }).then((res1) => {
                    if (res1.total > 0) {
                      this.notNullList[i].list = [
                        res1.rows[0],
                        ...this.notNullList[i].list
                      ]
                    }else {
                      delete this.pageForm[this.notNullList[i].attributeNameUnderline]
                    }
                  })
                } else if (this.notNullList[i].attType == 'tree') {
                  tableDropDownBox({
                    modelId: this.modelObj.modelId,
                    modeVersion: this.modelObj.modelVersion,
                    attribute: this.notNullList[i].attributeNameUnderline,
                    code: this.pageForm[this.notNullList[i].attributeNameUnderline]
                  }).then((res) => {
                    this.notNullList[i].list = []
                    if (res.total > 0) {
                      this.notNullList[i].list.push(res.rows[0])
                    }else {
                      delete this.pageForm[this.notNullList[i].attributeNameUnderline]
                    }
                  })
                }
              }
              for (let i = 0; i < this.nullList.length; i++) {
                this.checkAdd(this.nullList[i].attributeNameUnderline,this.pageForm[this.nullList[i].attributeNameUnderline])
                if (
                  this.nullList[i].attType == 'select' &&
                  this.nullList[i].list &&
                  !this.nullList[i].list.find(item => { return this.pageForm[this.nullList[i].attributeNameUnderline] == item.code }) &&
                  this.pageForm[this.nullList[i].attributeNameUnderline]
                ) {
                  if(this.modelObj.tableName == 'SG_TCDEV_FIBER_B' && this.nullList[i].attributeNameUnderline == 'PAR_LINE'){
                    for (let j = 0; j < this.pageForm[this.nullList[i].attributeNameUnderline].length; j++) {
                      if(!this.nullList[i].list.find(item => { return this.pageForm[this.nullList[i].attributeNameUnderline][j] == item.code })){
                        tableDropDownBox({
                          modelId: this.modelObj.modelId,
                          modeVersion: this.modelObj.modelVersion,
                          attribute: this.nullList[i].attributeNameUnderline,
                          code: this.pageForm[this.nullList[i].attributeNameUnderline][j]
                        }).then((res1) => {
                          if (res1.total > 0) {
                            this.nullList[i].list = [
                              res1.rows[0],
                              ...this.nullList[i].list
                            ]
                          }
                        })
                      }
                    }
                  }else {
                    tableDropDownBox({
                      modelId: this.modelObj.modelId,
                      modeVersion: this.modelObj.modelVersion,
                      attribute: this.nullList[i].attributeNameUnderline,
                      code: this.pageForm[this.nullList[i].attributeNameUnderline]
                    }).then((res1) => {
                      if (res1.total > 0) {
                        this.nullList[i].list = [
                          res1.rows[0],
                          ...this.nullList[i].list
                        ]
                      }else {
                        delete this.pageForm[this.nullList[i].attributeNameUnderline]
                      }
                    })
                  }
                } else if (this.nullList[i].attType == 'tree') {
                  tableDropDownBox({
                    modelId: this.modelObj.modelId,
                    modeVersion: this.modelObj.modelVersion,
                    attribute: this.nullList[i].attributeNameUnderline,
                    code: this.pageForm[this.nullList[i].attributeNameUnderline]
                  }).then((res) => {
                    this.nullList[i].list = []
                    if (res.total > 0) {
                      this.nullList[i].list.push(res.rows[0])
                    }else {
                      delete this.pageForm[this.nullList[i].attributeNameUnderline]
                    }
                  })
                }
              }
            })
          }else{
            //新增
            if(this.form!=undefined){
              this.pageForm=this.form
            }else{
              this.pageForm={}
            }
          }
        })
      },
      //根据modelId获取属性，区分必填与非必填字段
      getCluList(){
        this.cluList = []
        initgriddatasite(this.modelObj).then(res=>{
          this.cluList = res.data.columns
          for (let i = 0; i < this.cluList.length; i++) {
            this.cluList[i].rules=''
            //业务默认影藏一下属性
            if(this.modelObj.tableName == 'SG_TCCON_TCBUZ_B'){
              if(
                this.cluList[i].attributeNameUnderline == 'VOLTAGE_CLASS' ||
                this.cluList[i].attributeNameUnderline == 'LINE_NAME' ||
                this.cluList[i].attributeNameUnderline == 'CHANNEL_CODE' ||
                this.cluList[i].attributeNameUnderline == 'PROTECT_NUM' ||
                this.cluList[i].attributeNameUnderline == 'PROTECT_PORT'
              ){
                this.cluList[i].show = false
              }
            }
          }
          //获取所有属性初始化规则
          this.getAllRules()
        })
      },
      //获取所有属性的规则
      async getAllRules(){
        // await selectAllRules().then(res=>{
        //   res = res.data
        //   for (let i = 0; i < res.length; i++) {
        //     for (let j = 0; j < this.cluList.length; j++) {
        //       if(res[i].errorColumnNameCn === this.cluList[j].attributeNameCn){
        //         if(this.cluList[j].rules===undefined){
        //           this.cluList[j].rules=res[i].ruleDescription + '<br/>'
        //         }else{
        //           this.cluList[j].rules+=res[i].ruleDescription + '<br/>'
        //         }
        //       }
        //     }
        //   }
        // })
        await getPrimaryKeyName({modeId:this.modelObj.modelId}).then(response=>{
          this.keyClu = response.msg
        })
        if(this.dataObj[this.keyClu]!==undefined&&this.dataObj[this.keyClu]!==''){
          let params={
            modelId:this.modelObj.modelId,
            isPage:'0',
            whereAttributes:[
              {
                attributeName: this.keyClu,
                attributeValue:this.dataObj[this.keyClu],
                isPrimaryKey:'1'
              }
            ]
          }
          if (
            this.type != undefined
          ) {
            params.type = this.type
            params.dbName =  this.dbName
          }
          await getForm(params).then(res=>{
            this.pageForm=res.data
          })
        }else{
          //新增
          if(this.form!=undefined){
            this.pageForm=this.form
          }else{
            this.pageForm={}
          }
        }
        //循环获取必填与非必填数组，是否必填1是，0否
        this.notNullList = []
        this.nullList = []

        // 获取表单中的树 判断那些属性是从树种获取的 为其赋值type=tree 用于区分
        let treeArr = []
        await getTreeConfigApi({metaModelId: this.modelObj.modelId}).then(res => {
          treeArr = res.data.metaModelTreeSetList
          // 存储整个树初始结构
          this.tree.allTreeData = res.data.treeInfoVoList
        })

        for (let i = 0; i < this.cluList.length; i++) {
          this.checkAdd(this.cluList[i].attributeNameUnderline,this.pageForm[this.cluList[i].attributeNameUnderline])
          let treeItem = treeArr.find(item => {
            return item.metaModelTechId == this.cluList[i].id
          })
          if (treeItem) {
            this.cluList[i].tree = treeItem
            this.cluList[i].attType = 'tree'
            if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
              this.notNullList.push(this.cluList[i])
            }else{
              this.nullList.push(this.cluList[i])
            }
            if (
              this.pageForm[this.cluList[i].attributeNameUnderline] &&
              this.pageForm[this.cluList[i].attributeNameUnderline] != ''
            ) {
              tableDropDownBox({
                modelId: this.modelObj.modelId,
                modeVersion: this.modelObj.modelVersion,
                attribute: this.cluList[i].attributeNameUnderline,
                code: this.pageForm[this.cluList[i].attributeNameUnderline]
              }).then((res) => {
                if(res.total > 0){
                  this.cluList[i].list = res.rows
                }else {
                  delete this.pageForm[this.cluList[i].attributeNameUnderline]
                }
              })
            } else {
              this.cluList[i].list = []
            }
          } else {
            // 初始化下拉框
            tableDropDownBox({
              modelId: this.modelObj.modelId,
              modeVersion: this.modelObj.modelVersion,
              attribute: this.cluList[i].attributeNameUnderline,
              pageNum: 0,
              pageSize: 20,
            }).then((res) => {
              if (res.total > 0) {
                this.cluList[i].attType = 'select'
                this.cluList[i].list = res.rows
                this.cluList[i].total = res.total
                if (
                  this.pageForm[this.cluList[i].attributeNameUnderline] &&
                  !this.cluList[i].list.find(item => {
                    return this.pageForm[this.cluList[i].attributeNameUnderline] == item.code
                  })
                ) {
                  if(this.modelObj.tableName == 'SG_TCDEV_FIBER_B' && this.cluList[i].attributeNameUnderline == 'PAR_LINE'){
                    for (let j = 0; j < this.pageForm[this.cluList[i].attributeNameUnderline].length; j++) {
                      if(!this.cluList[i].list.find(item => {
                        return this.pageForm[this.cluList[i].attributeNameUnderline][j] == item.code
                      })){
                        tableDropDownBox({
                          modelId: this.modelObj.modelId,
                          modeVersion: this.modelObj.modelVersion,
                          attribute: this.cluList[i].attributeNameUnderline,
                          code: this.pageForm[this.cluList[i].attributeNameUnderline][j]
                        }).then((res1) => {
                          if (res1.total > 0) {
                            this.cluList[i].list.push(res1.rows[0])
                          }
                        })
                      }

                    }
                  }else {
                    tableDropDownBox({
                      modelId: this.modelObj.modelId,
                      modeVersion: this.modelObj.modelVersion,
                      attribute: this.cluList[i].attributeNameUnderline,
                      code: this.pageForm[this.cluList[i].attributeNameUnderline]
                    }).then((res1) => {
                      if (res1.total > 0) {
                        this.cluList[i].list.push(res1.rows[0])
                      }else {
                        delete this.pageForm[this.cluList[i].attributeNameUnderline]
                      }
                    })
                  }
                }
              }
              if(this.cluList[i].isNotNull===1||this.cluList[i].isNotNull==='1'){
                this.notNullList.push(this.cluList[i])
              }else{
                this.nullList.push(this.cluList[i])
              }
            })
          }
        }
      },
      //校验名字
      checkAdd(column,value) {
        return
        let param = {
          colunmName: column,
          modelId: this.modelObj.modelId
        }
        getColunmId(param).then(response=>{
          this.columnId = response.msg
          this.getRules(column,this.columnId,value)
        })
      },
      getRules(column,columnId,value) {
        //删除addErrForm中原来的错误数据，
        let arr=[]
        for (let i = 0; i < this.addErrForm.length; i++) {
          if(this.addErrForm[i].errorColumnName!==column){
            arr.push(this.addErrForm[i])
          }
        }
        this.addErrForm=arr
        this.rulesForm[column]=''
        let param={
          commId: columnId,
          modelId: this.modelObj.modelId,
          param: value
        }
        getVerrules(param).then(response=>
        {
          this.showRules=false
          if(response.data !== []) {
            let str = ''
            for(var i = 0;i<response.data.length;i++){
              //将错误数据保存到addErrForm中
              this.addErrForm.push(response.data[i])
              str += response.data[i].ruleDescription + '<br/>'
            }
            this.rulesForm[column] = str
            this.$set(this.rulesForm,column,this.rulesForm[column])
          }else{
            this.rulesForm[column] = undefined
          }
          this.showRules=true
        }).catch(e => {

        })
      },
      //提交前校验所有属性
      beforeSubmit(){
        for (let i = 0; i < this.notNullList.length; i++) {
          this.checkAdd(this.notNullList[i].attributeNameUnderline,this.pageForm[this.notNullList[i].attributeNameUnderline])
        }
        for (let i = 0; i < this.nullList.length; i++) {
          this.checkAdd(this.nullList[i].attributeNameUnderline,this.pageForm[this.nullList[i].attributeNameUnderline])
        }
      }
    }
  }
</script>

<style lang="scss">
  .treeSelect{
    /*display: none;*/
    .el-scrollbar {
      display: block !important;
      .el-scrollbar__wrap {
        .el-scrollbar__view {
          .el-select-dropdown__item {
            display: none;
          }
        }
      }
    }
    height: 512px!important;
    .el-scrollbar,
    .el-select-dropdown__wrap,
    .el-scrollbar__wrap {
      height: 524px!important;
      max-height: 524px!important;
    }
  }
  .el-select-dropdown__empty{
    display: none!important;
  }
  .treeBox{
    width: 30vw;
    height: 500px;
    padding: 4px 10px;
    /*position: fixed !important;*/
    /*left: 10px;*/
    /*top: 10px;*/
    /*padding: 10px;*/
    /*background: #fff!important;*/
  }
  .treeAddNodeBox{
    position: fixed;
    top: 0;
    left: 0;
    z-index: 999;
  }
  .oneLineOne {
    .el-form-item{
      width: calc(100% - 34px);
    }
  }
</style>
