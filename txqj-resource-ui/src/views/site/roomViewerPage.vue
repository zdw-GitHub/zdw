<template>
  <div :style="customStyle">
    <roomViewer
      style="height: 100%"
      :siteId="dataObj.ID"
    ></roomViewer>
  </div>
</template>

<script>


import roomViewer from '@/views/site/roomViewer.vue'

export default {
  name: "index",
  components: {roomViewer},
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
  },
  created() {
    if (this.$route.query.siteId){
      this.dataObj.ID = this.$route.query.siteId
    }else {
      this.$router.push({ path:  "/404" })
    }

  }
}
</script>

<style scoped lang="scss">
</style>
