<template>
  <div style="position: relative">
    <el-link style="position: absolute; top: 10px; right: 20px; z-index: 999; font-size: 14px;" type="success"
             @click="switchingModes"
    >切换模式
    </el-link>
    <el-tabs v-model="activeName" type="card" @tab-remove="removeTab">
      <el-tab-pane
        :closable="dataObj.ID != item.ID"
        :key="item.ID"
        v-for="item in editableTabs"
        :label="item.name"
        :name="item.ID"
      >
        <fiberInfo
          v-if="item.tableName === 'SG_TCDEV_FIBER_B'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </fiberInfo>
        <opticalroadInfo
          v-else-if="item.tableName === 'SG_TCCON_TCOPTICALROAD_B'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </opticalroadInfo>
        <siteInfo
          v-else-if="item.tableName === 'SG_TCCON_TCSITE_B'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </siteInfo>
        <sdhInfo
          v-else-if="item.tableName === 'SG_TCDEV_TCNESDH_B'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </sdhInfo>
        <otnInfo
          v-else-if="item.tableName === 'SG_TCDEV_TCNEOTN_B'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </otnInfo>
        <buzInfo
          v-else-if="item.tableName === 'SG_TCCON_TCBUZ_B'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </buzInfo>
        <channelInfo
          v-else-if="item.tableName === 'SG_TCCON_TCCHANNEL_B'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </channelInfo>
        <ymInfo
          v-else-if="item.tableName === 'ITEMLN'"
          :isEditMode="isEditMode"
          :dataObj="item"
          :pattern="pattern"
        >
        </ymInfo>
        <info
          v-else
          :tableName="item.tableName"
          :isEditMode="isEditMode"
          :title="item.title"
          :dataObj="item"
          :pattern="pattern"
        >
        </info>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import { getModelId, getForm } from '@/api/zdwh/zdzywh'
import opticalroadInfo from '@/views/lightPathInfoManage/opticalroadInfo'
import fiberInfo from '@/views/manageFiber/fiber/fiberInfo'
import ymInfo from '@/views/manageFiber/fiberYm/ymInfo'
import siteInfo from '@/views/site/siteInfo'
import sdhInfo from '@/views/sdhEquipment/sdhInfo'
import otnInfo from '@/views/otnEquipment/otnInfo'
import buzInfo from '@/views/serviceInformation/buzInfo'
import channelInfo from '@/views/serviceInformation/channelInfo'
import info from '@/views/commonPage/info/info'

export default {
  name: 'index',
  components: { opticalroadInfo, fiberInfo, siteInfo, sdhInfo, otnInfo, buzInfo,channelInfo, ymInfo, info },
  props: {
    dataObj: Object,
    tableName: String,
    isEditMode: {
      type: Boolean,
      default: () => false
    }
  },
  data() {
    return {
      activeName: '',
      editableTabs: [],
      pattern: true
    }
  },
  watch: {},
  mounted() {
    this.getData()
    this.$bus.$on('openNewTab', this.openNewTab)
  },
  beforeDestroy() {
    this.$off('openNewTab')
  },
  methods: {
    async getData() {
      let modelObj = await getModelId({ tableName: this.tableName })
      if (modelObj.data){
        let params = {
          modelId: modelObj.data.modelId,
          isPage: '0',
          whereAttributes: [
            {
              attributeName: 'ID',
              attributeValue: this.dataObj.ID,
              isPrimaryKey: '1'
            }
          ]
        }
        let title = modelObj.data.tableNameCn
        if (title){
          title = title.replaceAll('基本信息表','详情')
          title = title.replaceAll('基本信息','详情')
        }else {
          title = '详情'
        }
        let form = await getForm(params)
        this.editableTabs.push({
          ID: this.dataObj.ID,
          name: form.data.NAME,
          tableName: this.tableName,
          title: title,
        })
      }else {
        this.editableTabs.push({
          ID: this.dataObj.ID,
          name: this.dataObj.NAME,
          ty_id: this.dataObj.ty_id,
          tableName: this.tableName
        })
      }
      this.activeName = this.dataObj.ID
    },
    openNewTab(data) {
      let index = this.editableTabs.findIndex(f => f.ID === data.ID)
      if (index === -1) {
        this.editableTabs.push(data)
      }
      this.activeName = data.ID
    },
    removeTab(targetName) {
      let index = this.editableTabs.findIndex(tab => tab.ID === targetName)
      this.activeName = this.editableTabs[index - 1].ID
      this.editableTabs = this.editableTabs.filter(tab => tab.ID !== targetName)
    },
    switchingModes() {
      this.pattern = !this.pattern
      this.$emit('switchingModes', this.pattern)
    }
  }
}
</script>

<style scoped lang="scss">
::v-deep .el-tabs--card{

  .el-tabs__header {
    padding: 0 90px 0 20px;

    .el-tabs__item.is-active {
      color: #ffffff;
      background-color: #069a9e;
    }
  }

  .el-tabs__content {
    .el-tabs{

      .el-tabs__header {
        padding: 0 70px 0 20px;

        .el-tabs__item.is-active {
          color: #069a9e;
          background-color: #ffffff;
        }
        .el-tabs__active-bar {
          background-color: #069a9e;
        }
      }

    }
  }

}

</style>
