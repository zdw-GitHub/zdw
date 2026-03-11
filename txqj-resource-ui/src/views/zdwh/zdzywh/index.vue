<template>
  <div>
   <zdzywhPage :tableName="tableName"></zdzywhPage>
  </div>
</template>

<script>
  import zdzywhPage from '@/views/commonPage/zdzywh/index';
  export default {
    name: "Zdzywh",
    components:{
      zdzywhPage
    },
    data() {
      return {
        // 表名
        tableName: 'SG_TCCON_TCSITE_B',
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
