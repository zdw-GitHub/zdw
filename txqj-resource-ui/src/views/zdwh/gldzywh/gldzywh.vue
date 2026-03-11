<template>
  <div>
   <zdzywhPage :tableName="tableName"></zdzywhPage>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  export default {
    name: "zdzywh",
    components:{
      zdzywhPage
    },
    data() {
      return {
        // 表名
        tableName: 'SG_TCDEV_FIBERSEG_B',
      }
    },
    watch:{

    },
    created() {
      },
    methods: {

    },
  };
</script>
<style>

</style>
