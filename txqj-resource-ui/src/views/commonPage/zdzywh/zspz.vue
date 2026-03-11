<template>
  <div class="zspz">
    <div class="infoBox">
      <p class="title">
        基本信息
      </p>
      <div class="con">
        <el-row>
          <el-col :span="2">
            <p class="label">名称 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.tableName}}</p>
          </el-col>
          <el-col :span="2">
            <p class="label">中文名称 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.tableNameCn}}</p>
          </el-col>
          <el-col :span="2">
            <p class="label">所属类目 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.categoryName}}</p>
          </el-col>
          <el-col :span="2">
            <p class="label">创建时间 : </p>
          </el-col>
          <el-col :span="4">
            <p class="value">{{essentialInfo.createDate}}</p>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="tabBox">
      <p class="title">
        详细信息
      </p>
      <el-input
        style="padding-top: 20px;padding-left: 20px;width: 500px"
        v-model="searchName"
        placeholder="请输入属性名称或中文名"
      ></el-input>
      <div class="con">
        <el-table
          :data="filterTableData"
          stripe
          height="100%"
          tooltip-effect="dark"
        >
          <el-table-column
            label="序号"
            width="50"
            align="center"
            fixed="left"
            type="index"
          >
          </el-table-column>
          <el-table-column
            prop="columnName"
            align="center"
            label="属性名称"
            fixed="left"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="columnNameCn"
            align="center"
            label="中文名"
            fixed="left"
            width="150"
          >
          </el-table-column>
          <el-table-column
            prop="columnType"
            align="center"
            label="属性类型"
            width="80"
          >
          </el-table-column>
          <el-table-column
            prop="columnLength"
            align="center"
            label="属性长度"
            width="80"
          >
          </el-table-column>
          :src="getIconUrl(editBox.data.icon)"
          <el-table-column
            prop="isVisible"
            align="center"
            label="图标"
            width="60"
          >
            <template slot-scope="scope">
              <img
                width="28px"
                height="28px"
                style="margin-left: 8px; float: left"
                :src="getIconUrl(scope.row.icon)"
              >
            </template>
          </el-table-column>
          <el-table-column
            prop="isVisible"
            align="center"
            label="是否显示"
            width="100"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isVisible == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isVisible)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="sortNum"
            align="center"
            label="显示顺序"
            width="100"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="sortable"
            align="center"
            label="是否支持排序"
            width="140"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.sortable == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.sortable)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isMergeRows"
            align="center"
            label="是否合并行"
            width="120"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isMergeRows == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isMergeRows)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isQuery"
            align="center"
            label="是否查询"
            width="100"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isQuery == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isQuery)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="relateSearchCondition"
            align="center"
            label="关联查询条件"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="relateSearchAttribute"
            align="center"
            label="关联查询属性"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="relateSearchName"
            align="center"
            label="关联查询命名"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            prop="isCustomQuery"
            align="center"
            label="是否自定义查询"
            width="150"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isCustomQuery == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isCustomQuery)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isAdvancedQuery"
            align="center"
            label="是否高级查询"
            width="130"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isAdvancedQuery == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isAdvancedQuery)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="isAdvancedQueryIndex"
            align="center"
            label="是否支持高级查询索引"
            width="190"
            sortable
          >
            <template slot-scope="scope">
              <span class="whether" :style="{
                background: scope.row.isAdvancedQueryIndex == '1' ? '#67C23A' : '#F56C6C'
              }">
                {{translation(scope.row.isAdvancedQueryIndex)}}
              </span>
            </template>
          </el-table-column>
          <el-table-column
            prop="advancedQueryOrder"
            align="center"
            label="高级查询顺序"
            width="130"
            sortable
          >
          </el-table-column>
          <el-table-column
            label="操作"
            width="80"
            align="center"
            fixed="right"
          >
            <template slot-scope="scope">
              <el-button @click="editBoxShow(scope.row)" size="mini">编辑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <el-dialog
      v-dialogDrag
      custom-class="curdBox"
      :close-on-click-modal="false"
      :modal="false"
      title="编辑"
      :visible.sync="editBox.show"
      width="30%">
      <div style="width: 100%; height: 60vh; overflow-y: auto">
        <el-form ref="form" :model="editBox.data" label-width="200px" class="opaForm">
          <el-form-item label="是否显示 : ">
            <el-radio size="mini" v-model="editBox.data.isVisible" label="1">是</el-radio>
            <el-radio size="mini" v-model="editBox.data.isVisible" label="0">否</el-radio>
          </el-form-item>
          <el-form-item label="显示顺序 : ">
            <el-input-number size="mini" s v-model="editBox.data.sortNum" :min="0" :step="1"></el-input-number>
          </el-form-item>
          <el-form-item label="是否支持排序 : ">
            <el-radio size="mini" v-model="editBox.data.sortable" label="1">是</el-radio>
            <el-radio size="mini" v-model="editBox.data.sortable" label="0">否</el-radio>
          </el-form-item>
          <el-form-item label="是否合并行 : ">
            <el-radio size="mini" v-model="editBox.data.isMergeRows" label="1">是</el-radio>
            <el-radio size="mini" v-model="editBox.data.isMergeRows" label="0">否</el-radio>
          </el-form-item>
          <el-form-item label="是否查询 : ">
            <el-radio @change="mutual('isQuery', $event)" size="mini" v-model="editBox.data.isQuery" label="1">是</el-radio>
            <el-radio @change="mutual('isQuery', $event)" size="mini" v-model="editBox.data.isQuery" label="0">否</el-radio>
          </el-form-item>
          <div v-if="editBox.data.isQuery == 1" style="background: rgb(244,244,245)">
            <el-form-item label="关联查询条件 : ">
              <el-radio size="mini" v-model="editBox.data.relateSearchCondition" label="or">or</el-radio>
              <el-radio size="mini" v-model="editBox.data.relateSearchCondition" label="and">and</el-radio>
              <el-button size="mini" icon="el-icon-delete" @click="clearClumn"></el-button>
            </el-form-item>
            <el-form-item label="关联查询属性 : ">
              <el-select
                size="mini"
                filterable
                v-model="editBox.data.relateSearchAttributeId"
                @change="selectedClumn"
              >
                <el-option
                  v-for="item in tableData"
                  :key="item.columnId"
                  :label="item.columnNameCn + '  ' + item.columnName"
                  :value="item.columnId"
                >
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="关联查询命名 : ">
              <el-input style="width: 193px" size="mini" v-model="editBox.data.relateSearchName"></el-input>
            </el-form-item>
          </div>
          <el-form-item label="是否自定义查询 : ">
            <el-radio @change="mutual('isCustomQuery', $event)" size="mini" v-model="editBox.data.isCustomQuery" label="1">是</el-radio>
            <el-radio @change="mutual('isCustomQuery', $event)" size="mini" v-model="editBox.data.isCustomQuery" label="0">否</el-radio>
          </el-form-item>
          <el-form-item label="是否高级查询 : ">
            <el-radio @change="mutual('isAdvancedQuery', $event)" size="mini" v-model="editBox.data.isAdvancedQuery" label="1">是</el-radio>
            <el-radio @change="mutual('isAdvancedQuery', $event)" size="mini" v-model="editBox.data.isAdvancedQuery" label="0">否</el-radio>
          </el-form-item>
          <el-form-item label="是否支持高级查询索引 : ">
            <el-radio size="mini" v-model="editBox.data.isAdvancedQueryIndex" label="1">是</el-radio>
            <el-radio size="mini" v-model="editBox.data.isAdvancedQueryIndex" label="0">否</el-radio>
          </el-form-item>
          <el-form-item label="高级查询顺序 : ">
            <el-input-number size="mini" v-model="editBox.data.advancedQueryOrder" :min="0" :step="1"></el-input-number>
          </el-form-item>
          <el-form-item label="图标路径 : ">
            <el-input style="width: 193px; float: left" size="mini" v-model="editBox.data.icon"></el-input>
            <img
              width="28px"
              height="28px"
              style="margin-left: 8px; margin-top: 4px; float: left"
              :src="getIconUrl(editBox.data.icon)"
            >
          </el-form-item>
          <el-form-item label="配置个性样式 : ">
            <el-form-item>
              <el-button size="mini" icon="el-icon-circle-plus-outline" @click="addStyle"></el-button>
            </el-form-item>
          </el-form-item>
          <div style="background: rgb(244,244,245)" v-if="editBox.data.styleList.length > 0">
            <template v-for="(item, key) in editBox.data.styleList">
              <el-form-item label="值: " style="border-top: 1px solid rgb(220,223,230)">
                <el-input
                  size="mini"
                  v-model="item.value"
                  style="width: 193px"
                ></el-input>
                <el-button
                  size="mini"
                  icon="el-icon-delete"
                  style="margin-left: 7px; padding: 7px"
                  @click="removeStyle(item)"
                ></el-button>
              </el-form-item>
              <el-form-item label="样式: ">
                <el-input
                  size="mini"
                  type="textarea"
                  :autosize="{ minRows: 2}"
                  style="width: 193px; float: left"
                  v-model="item.style">
                </el-input>
                <el-popover
                  placement="bottom"
                  title="说明"
                  width="300"
                  trigger="click">
                  当字段的值与设置的值一致时 采用个性样式<br/>
                  样式书写格式与css一致(样式名: 样式值;)<br/>
                  例:<br/>
                  padding:10px;<br/>
                  background:red;<br/>
                  常用属性: <br/>
                  <ul>
                    <li>margin 外边距</li>
                    <li>padding 内填充</li>
                    <li>font-weight:bold; 粗体</li>
                    <li>font-size 字体大小</li>
                    <li>color 字体颜色</li>
                    <li>background 背景色</li>
                    <li>border: 1px solid red; 边框</li>
                    <li>border-radius 圆角边框</li>
                  </ul>
                  <el-button
                    style="margin-left: 7px; padding: 7px; float: left"
                    icon="el-icon-info"
                    slot="reference"
                  ></el-button>
                </el-popover>
              </el-form-item>
            </template>
          </div>
        </el-form>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editBox.show = false">取 消</el-button>
        <el-button type="primary" @click="editCommit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
  import{tabData, editCommit, getInfo, getInfo1, uploadFile} from "@/api/zdwh/zdzywh";
  export default {
    name: "zspz",
    props: {
      versionObj: Object,
    },
    data () {
      return {
        essentialInfo: {
          tableName: '',
          tableNameCn: '',
          categoryName: '',
          createDate: '',
        },
        tableData: [],
        editBox: {
          show: false,
          data: {
            styleList: [],
            isStyle:undefined,
          },
        },
        searchName:''
      }
    },
    computed:{
      filterTableData(){
        return this.tableData.filter(a=>{
          if(a.columnNameCn || a.columnName){
            let _searchName = this.searchName.toLowerCase();

            if(a.columnNameCn){
              let _columnNameCn = a.columnNameCn.toLowerCase();
              if(_columnNameCn.indexOf(_searchName) !== -1){
                return true;
              }
            }
            if(a.columnName){
              let _columnName = a.columnName.toLowerCase();
              if(_columnName.indexOf(_searchName) !== -1){
                return true
              }
            }
            return false
          }else{
            return true
          }

        })
      }
    },
    // id	编号
    // modelId	元模型编号
    // columnId	技术元数据属性编号
    // isQuery	是否查询1是，0否
    // isAdvancedQueryIndex	是否支持高级查询索引 1是，0否
    // isCustomQuery	是否自定义查询1是，0否
    // advancedQueryOrder	高级查询顺序
    // isAdvancedQuery	是否高级查询1是，0否
    // isVisible	是否显示1是，0否
    // sortNum	显示顺序
    // sortable	是否排序  1是，0否
    // modelVersion	版本号
    // createDate	创建时间
    // updateDate	修改时间
    // isJumpLink	是否跳转链接  1是，0否
    // menuId	菜单编号
    // menuPath	菜单路由
    // linkReMetaModelId	元模型编号
    // linkReMetaModel	元模型名称
    // linkReMetaModelCn	元模型名称中文
    // linkReColumnId	属性编号
    // linkReColumnName	属性名称
    // linkReColumnNameCn	属性名称中文
    // isMergeRows	是否合并行 1是，0否
    // icon 图标
    // isStyle 是否个性样式 1是，0否
    // styleList 个性样式列表
    methods: {
      addStyle() {
        this.editBox.data.styleList.push({
          value: '',
          style: '',
          id: Date.now()
        });
        this.editBox.data.isStyle = '1'
      },
      removeStyle(item) {
        var index = this.editBox.data.styleList.indexOf(item)
        if (index !== -1) {
          this.editBox.data.styleList.splice(index, 1)
        }
        if(this.editBox.data.styleList.length == 0){
          this.editBox.data.isStyle = '0'
        }
      },
      getIconUrl (url) {
        let u = process.env.BASE_URL + 'img/formImg/'
        if (url && url!== '') {
          return u + url
        }
        return u + 'default.png'
      },
      uploadFile(item) {
        let file = item.raw;
        let formData = new FormData();
        formData.append("file", file);
        uploadFile(formData).then(res => {
          this.editBox.data.icon = res.data.url
        })
      },
      handleRemove(file, fileList) {
        this.editBox.data.icon = ''
      },
      translation (str) {
        return str == '1' ? '是' : '否'
      },
      getInfo () {
        getInfo(this.versionObj.parentId).then(res1 => {
          this.essentialInfo.tableName = res1.data.tableName
          this.essentialInfo.tableNameCn = res1.data.tableNameCn
          this.essentialInfo.createDate = res1.data.createDate
          if(res1.data.categoryNameId){
            getInfo1(res1.data.categoryNameId).then(res2 => {
              this.essentialInfo.categoryName = res2.data.categoryName
            })
          }
        })
      },
      getTabData: function () {
        let params = {
          modelId:this.versionObj.parentId,
          modelVersion: this.versionObj.label,
          pageNum:1,
          pageSize:999,
        }
        tabData(params).then((res) => {
          this.tableData = res.rows
        })
      },
      editBoxShow (row) {
        if(!row.icon){
          row.icon = ''
        }
        if(!row.isStyle){
          row.isStyle = '0'
        }
        this.editBox = {
          show: true,
          data: JSON.parse(JSON.stringify(row))
        }
      },
      /* 查询互斥 */
      mutual(name, value) {
        if(value == '1') {
          this.editBox.data.isAdvancedQuery = '0'
          this.editBox.data.isCustomQuery = '0'
          this.editBox.data.isQuery = '0'
          this.editBox.data[name] = value
        }
      },
      selectedClumn (val) {
        let data = this.tableData.find(item => {
          return item.columnId == val
        })
        this.editBox.data.relateSearchAttributeId = val
        this.editBox.data.relateSearchAttribute = data.columnName
      },
      clearClumn () {
        this.$set(this.editBox.data, 'relateSearchCondition', '')
        this.$set(this.editBox.data, 'relateSearchAttributeId', '')
        this.$set(this.editBox.data, 'relateSearchAttribute', '')
        this.$set(this.editBox.data, 'relateSearchName', '')
      },
      editCommit () {

        let data = {
          ...this.editBox.data,
          modelVersion:this.versionObj.label,
          modelId:this.versionObj.parentId,
        }
        if (data.isQuery != 1) {
          data.relateSearchCondition = ''
          data.relateSearchAttributeId = ''
          data.relateSearchAttribute = ''
          data.relateSearchName = ''
        }
        editCommit(data).then(res => {
          this.editBox.show = false
          this.getTabData()
          this.$modal.msgSuccess("操作成功");
        })
      }
    },
    created () {
      this.getInfo()
      this.getTabData()
    }
  }
</script>

<style lang="scss" scoped>
  .zspz{
    width: 100%;
    height: 100%;
    .infoBox{
      width: 100%;
      height: 100px;
    }
    .tabBox{
      width: 100%;
      height: calc(100% - 150px);
    }
    .infoBox,
    .tabBox{
      .title{
        width: 100%;
        height: 24px;
        line-height: 24px;
        font-weight: bold;
        border-bottom: 1px solid #000;
      }
      .con{
        width: 100%;
        height: calc(100% - 24px);
        padding: 10px;
      }
    }
    .infoBox .con{
      padding: 10px 30px;
      p{
        width: 100%;
        height: 56px;
        line-height: 36px;
        padding: 10px;
      }
      .label{
        text-align: right;
        font-weight: bold;
      }
    }
    .tabBox .whether {
      padding: 5px;
      border-radius: 3px;
      color: #fff;
    }
  }
  .deleteImg {
    position: absolute;
    color: #409EFF;
    top: 0;
    right: 0;
    z-index: 999;
  }
  .avatar-uploader{
    margin-top: 5px;
    padding: 10px;
    width: 54px;
    height: 54px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    color: #8c939d;
    width: 32px;
    height: 32px;
    line-height: 32px;
    text-align: center;
  }
  .avatar {
    width: 32px;
    height: 32px;
    display: block;
  }
</style>
