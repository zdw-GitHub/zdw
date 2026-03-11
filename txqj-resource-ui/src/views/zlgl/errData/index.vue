<template>
  <div>
    <errDataPage :tableName="tableName"></errDataPage>
  </div>
</template>

<script>
  import errDataPage from '@/views/commonPage/errData/index';
  export default {
    name: "errData",
    components:{
      errDataPage
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
