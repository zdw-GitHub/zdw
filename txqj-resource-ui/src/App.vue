<template>
  <div id="app">
    <keep-alive>
      <router-view />
    </keep-alive>
  </div>
</template>

<script>
export default  {
  name:  'App',
    metaInfo() {
        return {
            title: this.$store.state.settings.dynamicTitle && this.$store.state.settings.title,
            titleTemplate: title => {
                return title ? `${title} - ${process.env.VUE_APP_TITLE}` : process.env.VUE_APP_TITLE
            }
        }
    }
}
</script>

<style lang="scss">
  #app{
    background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
  }
  p{
    margin: 0!important;
    font-size: 14px;
  }
  .el-form-item{
    margin: 0!important;
  }


  .el-button--default,
  .el-button--default:hover,
  .el-button--default:focus{
    border: 1px solid #d9d9d9;
    background-color: #fff;
    color: #595959;
  }
  .el-button--primary,
  .el-button--primary:hover,
  .el-button--primary:focus{
    border: 1px solid #079b9f;
    background-color: #079b9f;
    color: #ffffff;
  }
  .el-button--primary:disabled,
  .el-button--primary:disabled:hover,
  .el-button--primary:disabled:focus {
    border: 1px solid #8ae3e5;
    background-color: #8ae3e5;
    color: #ffffff;
  }
  .el-button--danger,
   .el-button--danger:hover,
   .el-button--danger:focus{
    border: 1px solid #ff4949;
    background-color: #ff4949;
    color: #ffffff;
  }
  .el-button:hover{
    font-weight: bold;
  }

  .commonWh{
    width: 100%;
    height: 100%;
    padding: 20px;

    .fontSize{
      font-size: 14px;
    }

    .basicsQuery{
      background-size: 430px 105px;
      background-repeat: no-repeat;
      background-position: left top;
    }
    .el-button--text{
      color: #079b9f;
    }
    .el-table{
      .el-button{
        padding: 4px 8px !important;
        font-size: 12px !important;
        background-color: transparent !important;
      }
      .el-button--primary{
        border: 1px solid #079b9f;
        background-color: #fff;
        color: #079b9f;
      }
      .el-button--danger{
        border: 1px solid #ff4949;
        background-color: #fff;
        color: #ff4949;
      }

      .el-checkbox__input.is-indeterminate .el-checkbox__inner,
      .el-checkbox__input.is-checked .el-checkbox__inner{
        background-color: #0f9ea2;
        border-color: #0f9ea2;
      }
      .el-table__body tbody tr:hover>td,
      .el-table__body .el-table__row.hover-row td {
        background-color: #aae9ec !important;
      }
      .el-table__body tr.current-row>td{
        background-color: #aae9ec !important;
      }

      tr{
        th{
          background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
          color: #333;
        }
        td{
          color: #333;
        }
      }
    }
    .el-pagination{
      .btn-prev,
      .btn-next,
      .btn-quicknext,
      .number{
        background-color: transparent !important;
        border: 1px solid #d9d9d9 !important;
        &:hover{
          border: 1px solid #01a19f !important;
          color: #01a19f !important;
        }
      }
      .active{
        border: 1px solid #01a19f !important;
        color: #01a19f !important;
      }
    }
    .box-card {
      width: 100%;
      height: 100%;
      padding: 10px;
      padding-bottom: 0;
      background-color: #fff;
      .queryBox{
        .basicsQuery{
          width: 100%;
          min-height: 60px;
          padding: 10px;
          border-radius: 3px;
          display: flex;
          flex-wrap: wrap;
          .el-form{
            width: 100%!important;
            .el-form-item{
              display: inline-block;
              width: 25%;
              margin: 0!important;
              .el-form-item__content{
                .el-input__inner,
                .el-input,
                .el-input_inner,
                .el-input .el-input_inner,
                .el-select {
                  width: 100% !important;
                }
              }
            }
          }
        }
        .advancedQuery>div:last-child{
          padding-bottom: 10px;
        }
        .advancedQuery{
          width: 100%;
          max-height: 40vh;
          overflow-y: auto;
          .queryTitle{
            width: 100%;
            height: 40px;
            line-height: 40px;
            p{
              display: inline-block;
            }
          }
          .moreButton{
            float: right;
          }
          .abcList{
            width: 100%;
            .el-checkbox-group,
            .el-radio-group{
              width: 100% !important;
              .el-checkbox,
              .el-radio{
                min-width: max(calc(12.5% - 20px), 180px);
                margin: 0 20px 0 0!important;
              }
            }
          }
          .el-radio{
            padding: 10px 0;
          }
        }
      }
      .tableBox{
        width: 100%;
        height: calc(100% - 70px);
        padding: 10px 0;
        .tabTopButton{
          width: 100%;
          padding-bottom: 10px;
        }
      }
    }

    .openTit {
      width: 80px;
      height: 18px;
      line-height: 18px;
      color: #fff;
      text-align: center;
      margin: 0 auto;
      cursor: pointer;
      margin-top: -10px;
      border-top: 18px solid #0f9ea2;
      border-left: 18px solid transparent;
      border-right: 18px solid transparent;
      position: relative;
      p {
        width: 80px;
        position: absolute;
        top: -18px;
        font-size: 12px;
        left: calc(50% - 40px);
      }
    }
  }

  .curdBox{
    /*background-color: rgba(222, 222, 222, 0.1);*/
    background-color: #ffffff;
    backdrop-filter: blur(10px)!important;
    .el-dialog__body {
      padding: 10px 0!important;
      /**{*/
      /*background: transparent;*/
      /*}*/
    }
  }
  .el-dialog{
    box-shadow: none !important;
    border: none !important;
    background-color: transparent!important;
    top: 100px;
    .el-dialog__header{
      background: linear-gradient(180deg, #Dffff6 0%, #ffffff 100%);
      border: none !important;
      height: 46px;
      line-height: 46px;
      padding: 14px;
      .el-dialog__headerbtn{
        color: #ffffff !important;
        font-weight: bold;
      }
      .el-dialog__close{
        color: #8c8c8c !important;
        font-weight: bold;
      }
      .el-dialog__title{
        display: block;
        float: left;
        width: 80%;
        height: 18px;
        line-height: 18px;
        font-size: 16px;
        padding-left: 30px;
        color: #ffffff;
        overflow: hidden; /* 确保超出容器的文本被隐藏 */
        white-space: nowrap; /* 防止文本换行 */
        text-overflow: ellipsis; /* 超出部分显示省略号 */
        background-image: url("./assets/images/dialogTitIcon.svg");
        background-size: auto 100%;
        background-repeat: no-repeat;
        background-position: left center;
      }
      .el-dialog__headerbtn{
        position: static;
        float: right;
      }
    }
    .el-dialog__body{
      padding: 10px;
      background-color: #fff;
      border-left: 3px solid #cff1f2;
      border-right: 3px solid #cff1f2;
      border-bottom: 3px solid #cff1f2;
    }
    .el-dialog__footer{
      background-color: #fff;
      padding: 10px!important;
      box-shadow: 0px -2px 5px #eee;
      border: 2px solid #cff1f2;
      border-top: none;
    }
  }

  .el-dialog-div {
    height: 70vh;
    overflow-y: auto;
    padding: 0 20px!important;
    .formTitle{
      width: 100%;
      height: 30px;
      line-height: 30px;
      background-color: #cdebed;
      color: #069a9e;
      padding-left: 14px;
      border-radius: 5px 5px 0 0;
    }
    .el-form-item{
      display: inline-block;
      width: calc(50% - 34px);
      margin: 10px 17px!important;
      background-image: url("./assets/images/formItemBg.png");
      background-size: 100% 100%;
      background-repeat: no-repeat;
      background-position: center center;
      .el-form-item__label{
        text-align: left;
      }
      .el-select, .el-input{
        width: 100%!important;
        .el-input__inner{
          border: none!important;
          background-color: transparent;
          text-align: right;
          color: #02a2a2 !important;
        }
      }
    }
  }

  .serviceInformation{
    .formTitle{
      border: none!important;
    }
    .pageForm{
      .el-form-item{
        margin: 5px 0!important;
        display: inline-block;
        width: 33%;
        .el-select, .el-input{
          width: 100%!important;
        }
      }
    }
  }
  ::-webkit-scrollbar {
    width: 8px;
    height: 10px;
  }
  ::-webkit-scrollbar-button {
    background-color: rgba(186, 204, 220,0);
  }
  ::-webkit-scrollbar-track-piece {
    background: rgba(186, 204, 220,0);
  }
  ::-webkit-scrollbar-thumb {
    background: rgb(0, 161, 158);
    border-radius: 4px;
  }
  .linkColumn,.linkColumn:hover{
    color:#0f9ea2;
    text-decoration:underline;
  }

  .framePosition{
    top: 100px !important;
    left: 72% !important;
  }
</style>
