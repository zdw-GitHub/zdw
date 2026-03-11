<template>
  <div class="commonWh">
    <div style="width: 100%; height: 0" class="getSpanSize"></div>
    <el-table
      :row-key="getRowKeys"
      stripe
      border
      height="calc(100% - 60px)"
      style="width: 100%; "
      :data="tableData"
      ref="multipleTable"
      v-loading="loading">
      <el-table-column
        v-if="pattern"
        :reserve-selection="true"
        type="selection"
        fixed="left"
        width="50">
      </el-table-column>
      <el-table-column
        v-if="pattern"
        label="序号"
        width="80"
        align="center"
        :formatter="snFormatter"
      >
      </el-table-column>
      <el-table-column
        v-if="tableData[0]"
        resizable
        show-overflow-tooltip
        v-for="item in tableColConfig"
        :sortable="item.sortable=='1'"
        :label="item.attributeNameCn===undefined?item.attributeName:item.attributeNameCn"
        :prop="item.attributeNameUnderline"
        :key="item.id"
        :min-width="getColSize(
                tableData[0][item.attributeNameUnderline],
                item.attributeNameCn===undefined?item.attributeName:item.attributeNameCn
              )"
        align="center">
        <template slot-scope="scope">
          <a
            class="linkColumn"
            v-if="item.isJumpLink=='1'"
            @click="getRouterLink(item,scope.row)"
          >
            {{ scope.row[item.attributeNameUnderline] }}
          </a>
          <span v-else>{{scope.row[item.attributeNameUnderline]}}</span>
        </template>
      </el-table-column>
    </el-table>
    <pagination
      :total="total"
      :page.sync="pageIndex"
      :limit.sync="pageSize"
      @pagination="getTableData"
    />
  </div>
</template>

<script>
  import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  import {
    getModelId,
    getData,
    initgriddatasite,
    getNewTabInfo,
  } from "@/api/zdwh/zdzywh";
  import {getColSize} from "@/utils/getColSize"
  export default {
    name: "Carrying",
    props:{
      tableName:String,
      fromTableName:String,
      jumpLink:{
        type: Array,
        default:  () => { return []}
      },
      dataList:{
        type: Array,
        default:  () => { return []}
      },
      searchForm:{
        type: Object,
        default:  () => { return {}}
      },
      pattern: {
        type: Boolean,
        default: () => true
      },
      openNewPage: {
        type: Boolean,
        default: () => true
      }
    },
    data() {
      return {
        //表的分页
        pageSize: 15,
        pageIndex: 1,
        total: 0,
        tableData: [],
        // 表格展示属性
        tableColConfig: [],
        searchList:[],
        //表对象信息
        modelObj: {},
        // 遮罩层
        loading: false,
      }
    },
    watch:{
      //解决操作栏固定后表格错位问题
      'tableData':{
        handler(){
          this.$nextTick(()=>{
            this.$refs.multipleTable.doLayout()
          })
        },
        deep:true
      },
    },
    mounted() {
      this.getCommonData()
    },
    methods: {
      getRowKeys (row) {
        return row.ID
      },
      getCommonData(){
        //获取表基本信息
        getModelId({tableName:this.tableName}).then(res=>{
          this.modelObj = res.data
          this.getCluList()
        })
      },
      //动态获取页面搜索条件与列名
      getCluList(){
        initgriddatasite(this.modelObj).then(res=>{
          this.searchList = res.data.searchAttributes
          this.tableColConfig = JSON.parse(JSON.stringify(res.data.columns))
          if (this.modelObj.tableName == 'SG_TCCON_TCOPTICALROAD_B' && this.fromTableName == 'SG_TCDEV_FIBER_B'){
            this.tableColConfig.splice(5,0,{
              "id": "999999999",
              "attributeName": "lineSn",
              "attributeNameCn": "占用光缆纤芯",
              "attributeNameUnderline": "LINE_SN",
            },)
          }
          for (let i = 0; i < this.jumpLink.length; i++) {
            let index = this.tableColConfig.findIndex(item => item.attributeNameUnderline == this.jumpLink[i].attributeNameUnderline)
            if(index != -1){
              this.tableColConfig[index].isJumpLink = '1'
              this.tableColConfig[index].linkReColumnName = this.jumpLink[i].attributeNameUnderline
              this.tableColConfig[index].menuPath = this.jumpLink[i].menuPath
            }
          }
        })
      },
      //获取表格数据
      async getTableData() {
        this.loading = true;
        if(!this.modelObj.modelId){
          await getModelId({tableName:this.tableName}).then(res=>{
            this.modelObj = res.data
          })
        }
        let params={
          modelId:this.modelObj.modelId,
          isPage:'1',
          pageIndex: this.pageIndex,
          pageSize: this.pageSize,
          whereAttributes:[],
          orderAttributes:[],
        }
        //基础查询
        for(let key in this.searchForm){
          let obj={}
          if(this.searchForm[key] !== ''&&this.searchForm[key] !== undefined){
            obj={
              attributeName:key,
              attributeValue:this.searchForm[key]
            }
            let data = this.searchList.find(item => {
              return item.attributeNameUnderline == key
            })
            if (data && data.relateSearchAttributeId) {
              obj.relateSearchAttribute = data.relateSearchAttribute
              obj.relateSearchCondition = data.relateSearchCondition
            }
            params.whereAttributes.push(obj)
          }
        }
        getData(params).then(response => {
            this.tableData = response.rows;
            if (this.modelObj.tableName == 'SG_TCCON_TCOPTICALROAD_B' && this.fromTableName == 'SG_TCDEV_FIBER_B'){
              this.tableData.map(item => this.dataList.find(d => d.ID === item.ID) ? item.LINE_SN = this.dataList.find(d => d.ID === item.ID).LINE_SN : item.LINE_SN = '')
            }
            this.total = parseInt(response.total);
            this.loading = false;
          }
        ).catch(() => {
          this.loading = false
        });
      },
      //获取搜索条件
      getSearchCriteria(){
        let params={
          modelId:this.modelObj.modelId,
          isPage:'0',
          whereAttributes:[]
        }
        for(let key in this.searchForm){
          let obj={}
          if(key!=='pageSize'&&key!=='pageNum'){
            if(this.searchForm[key]!==''&&this.searchForm[key]!==undefined){
              obj={
                attributeName:key,
                attributeValue:this.searchForm[key]
              }
              let data = this.searchList.find(item => {
                return item.attributeNameUnderline == key
              })

              if (data && data.relateSearchAttributeId) {
                obj.relateSearchAttribute = data.relateSearchAttribute
                obj.relateSearchCondition = data.relateSearchCondition
              }
              params.whereAttributes.push(obj)
            }
          }
        }
        return params;
      },
      otherHandleExport(){
        let params = this.getSearchCriteria();
        this.$emit('getSearchCriteria', params)
      },
      // 格式序列化列
      snFormatter: function (row, col, value, index) {
        return (this.pageIndex - 1) * this.pageSize + index + 1
      },
      //跳转

      getRouterLink(item,row){
        if (this.openNewPage){
          this.$router.push({
            path:'/'+item.menuPath,
            query:{
              search: JSON.stringify({[item.linkReColumnName]: row[item.attributeNameUnderline],}),
              refresh:true,
            }
          })
        }else {
          if (item.attributeNameUnderline === 'NAME'){
            let title = this.modelObj.tableNameCn
            if (title){
              title = title.replaceAll('基本信息表','详情')
              title = title.replaceAll('基本信息','详情')
            }else {
              title = '详情'
            }
            this.$bus.$emit('openNewTab',{
              tableName: this.tableName,
              name: row[item.attributeNameUnderline],
              title: title,
              ID: row.ID,
            })
          }else {
            getNewTabInfo({
              tableName: this.tableName,
              attribute: item.attributeNameUnderline,
              id: row.ID
            }).then(res => {
              if (res.data.modelName){
                this.$bus.$emit('openNewTab',{
                  tableName: res.data.modelName,
                  title: res.data.title,
                  name: row[item.attributeNameUnderline],
                  ID: res.data.id,
                })
              }
            })
          }
        }
      },
      getColSize (val, titVal) {
        return getColSize(val, titVal)
      },
    },
  };
</script>
<style scope lang="scss">

</style>
