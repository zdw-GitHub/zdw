<template>
  <div :style="customStyle">
    <info
      style="width: 100%; height: 100%;"
      :isEditMode="isEditMode"
      :dataObj="dataObj"
      :tableName="tableName"
      @switchingModes="switchingModes">
    </info>
  </div>
</template>

<script>

import info from '@/views/commonPage/info/index'

export default {
  name: "index",
  components: {info},
  data() {
    return {
      isEditMode:false,
      dataObj:{},
      tableName: '',
      customStyle: {
        width: '100%',
        height: '100%'
      }
    }
  },
  methods: {
    switchingModes(data) {
      this.customStyle.width = data ? '100%' : '30%'
    },
  },
  created() {
    if (this.$route.query.siteId){
      this.dataObj.ID = this.$route.query.siteId
      this.tableName = 'SG_TCCON_TCSITE_B'
    }else if (this.$route.query.fiberId){
      this.dataObj.ID = this.$route.query.fiberId
      this.tableName = 'SG_TCDEV_FIBER_B'
    }else if (this.$route.query.tableName && this.$route.query.id){
      this.dataObj.ID = this.$route.query.id
      this.tableName = this.$route.query.tableName
    }else {
      this.$router.push({ path:  "/404" })
    }

  }
}
</script>

<style scoped lang="scss">
</style>
