
<template></template>
  
  <script>
    import { tree,customTree,getTable,getMiddleTableClu,getMidTable,tableDropDownBox,initgriddatasite ,getPrimaryKeyName} from "@/api/sj/audit";
    import { getToken } from "@/utils/auth";
    import {listDefinition} from "@/api/flowable/definition";
    import { mapMessage,conditionalData } from "@/api/zdwh/zdzywh";
    import Treeselect from "@riophae/vue-treeselect";
    import "@riophae/vue-treeselect/dist/vue-treeselect.css";
  
    export default {
      data() {
        return {}
      },
      watch: {
      },
      created() {
        window.open('http://txtywh.zj.sgcc.com.cn/dataaccess', '_blank');
        // window.open('http://localhost:82/dataaccess', '_blank');
        this.setTimeout(() => {
            this.$route.dispatch('tagsView/delView',this.$route)
        }, 2000);
      },
      methods: {}
    }
  </script>
  <style>
  </style>
  
  