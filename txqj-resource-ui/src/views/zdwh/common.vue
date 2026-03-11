<template>
  <div>
    <zdzywhPage :tableName="tableName"></zdzywhPage>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
    export default {
        name: "common",
      data() {
        return {
          tableName:''
        }
      },
      components:{
        zdzywhPage
      },
      created() {
        let url=this.$route.path
        let strArr=url.split('/')
        this.tableName=strArr[strArr.length-1]
      },
    }
</script>

<style scoped>

</style>
