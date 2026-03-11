<script>
import { deepClone } from '@/utils/index'
import render from '@/components/render/render.js'
import dowloadFoles from '@/components/render/dowloadFoles'

const ruleTrigger = {
  'el-input': 'blur',
  'el-input-number': 'blur',
  'el-select': 'change',
  'el-button': 'click',
  'el-radio-group': 'change',
  'el-checkbox-group': 'change',
  'el-cascader': 'change',
  'el-time-picker': 'change',
  'el-date-picker': 'change',
  'el-rate': 'change',
  'el-upload': 'on-change'
}

const layouts = {
  colFormItem(h, scheme) {
    const config = scheme.__config__
    const listeners = buildListeners.call(this, scheme)

    let labelWidth = config.labelWidth ? `${config.labelWidth}px` : null
    if (config.showLabel === false) labelWidth = '0'
    if (scheme.__config__.tag === 'el-upload') {
      if (!this.formConf.disabled) {
        scheme.__config__.field = scheme.__vModel__
        return (
          <el-col span={config.span}>
            <el-form-item label-width={labelWidth}
              label={config.showLabel ? config.label : ''}>
            <el-upload
              ref="upload"
              data={scheme.__config__}
              action={scheme.action}>
            <el-button size="small" type="primary" icon="el-icon-upload">{scheme.__config__.buttonText}</el-button>
            </el-upload>
            </el-form-item>
          </el-col>
        )
      } else {
        scheme.__config__.field = scheme.__vModel__
        return (
          <el-col span={config.span}>
            <el-form-item label-width={labelWidth}
              label={config.showLabel ? config.label : ''}>
              <el-upload
                ref="upload"
                data={scheme.__config__}
                action={scheme.action}>
                <dowloadFoles data={scheme.__config__.defaultValue}/>
              </el-upload>
            </el-form-item>
          </el-col>
        )
      }
    }
    if (scheme.__config__.tag === 'el-button') {
        return (
          <el-col span={config.span}>
      <el-form-item label-width={labelWidth}
        label={config.showLabel ? config.label : ''}>
      <el-button
        size={scheme.size}
        type={scheme.type}
        icon={scheme.icon}
      circle={scheme.circle}
      disabled={scheme.disabled}
      plain={scheme.plain}
      round={scheme.round}
      onClick={this.aa.bind(this,scheme.func)}
        >
      {scheme.__slot__.default}
        </el-button>
        </el-form-item>
        </el-col>
      )
    }
    // on-change="aaa"
    return (
      <el-col span={config.span}>
        <el-form-item label-width={labelWidth} prop={scheme.__vModel__}
          label={config.showLabel ? config.label : ''}>
          <render conf={scheme} on={listeners} />
        </el-form-item>
      </el-col>
    )
  },
  rowFormItem(h, scheme) {
    let child = renderChildren.apply(this, arguments)
    if (scheme.type === 'flex') {
      child = <el-row type={scheme.type} justify={scheme.justify} align={scheme.align}>
              {child}
            </el-row>
    }
    return (
      <el-col span={scheme.span}>
        <el-row gutter={scheme.gutter}>
          {child}
        </el-row>
      </el-col>
    )
  }
}
function renderFrom(h) {
  const { formConfCopy } = this
  return (
    <el-row gutter={formConfCopy.gutter}>
      <el-form
        size={formConfCopy.size}
        label-position={formConfCopy.labelPosition}
        disabled={formConfCopy.disabled}
        label-width={`${formConfCopy.labelWidth}px`}
        ref={formConfCopy.formRef}
        // model不能直接赋值 https://github.com/vuejs/jsx/issues/49#issuecomment-472013664
        props={{ model: this[formConfCopy.formModel] }}
        rules={this[formConfCopy.formRules]}
      >
        {renderFormItem.call(this, h, formConfCopy.fields)}
        {formConfCopy.formBtns && formBtns.call(this, h)}
      </el-form>
    </el-row>
  )
}

function formBtns(h) {
  return <el-col>
    <el-form-item size="large">
      <el-button type="primary" onClick={this.submitForm}>提交</el-button>
      <el-button onClick={this.resetForm}>重置</el-button>
    </el-form-item>
  </el-col>
}

function renderFormItem(h, elementList) {
  return elementList.map(scheme => {
    const config = scheme.__config__
    const layout = layouts[config.layout]
    if (layout) {
      return layout.call(this, h, scheme)
    }
    throw new Error(`没有与${config.layout}匹配的layout`)
  })
}

function renderChildren(h, scheme) {
  const config = scheme.__config__
  if (!Array.isArray(config.children)) return null
  return renderFormItem.call(this, h, config.children)
}

function setValue(event, config, scheme) {
  this.$set(config, 'defaultValue', event)
  this.$set(this[this.formConf.formModel], scheme.__vModel__, event)
}

function buildListeners(scheme) {
  const config = scheme.__config__
  const methods = this.formConf.__methods__ || {}
  const listeners = {}
  // 给__methods__中的方法绑定this和event
  Object.keys(methods).forEach(key => {
    listeners[key] = event => methods[key].call(this, event)
  })
  // 响应 render.js 中的 vModel $emit('input', val)
  if (config.tag === 'el-upload') {
    listeners.change = event => setValue.call(this, event, config, scheme)
  }else if (config.tag === 'el-button') {
    listeners.click = event => setValue.call(this, event, config, scheme)
  } else {
    listeners.input = event => setValue.call(this, event, config, scheme)
  }
  return listeners
}

export default {
  components: {
    render,
    dowloadFoles
  },
  props: {
    formConf: {
      type: Object,
      required: true
    }
  },
  data() {
    const data = {
      formConfCopy: deepClone(this.formConf),
      [this.formConf.formModel]: {},
      [this.formConf.formRules]: {}
    }
    this.initFormData(data.formConfCopy.fields, data[this.formConf.formModel])
    this.buildRules(data.formConfCopy.fields, data[this.formConf.formRules])
    return data
  },
  methods: {
    initFormData(componentList, formData) {
      componentList.forEach(cur => {
        const config = cur.__config__
        if (cur.__vModel__) formData[cur.__vModel__] = config.defaultValue
        if (config.children) this.initFormData(config.children, formData)
      })
    },
    buildRules(componentList, rules) {
      componentList.forEach(cur => {
        const config = cur.__config__
        if (Array.isArray(config.regList)) {
          if (config.required) {
            const required = { required: config.required, message: cur.placeholder }
            if (Array.isArray(config.defaultValue)) {
              required.type = 'array'
              required.message = `请至少选择一个${config.label}`
            }
            required.message === undefined && (required.message = `${config.label}不能为空`)
            config.regList.push(required)
          }
          rules[cur.__vModel__] = config.regList.map(item => {
            item.pattern && (item.pattern = eval(item.pattern))
            item.trigger = ruleTrigger && ruleTrigger[config.tag]
            return item
          })
        }
        if (config.children) this.buildRules(config.children, rules)
      })
    },
    resetForm() {
      this.formConfCopy = deepClone(this.formConf)
      this.$refs[this.formConf.formRef].resetFields()
    },
    submitForm() {
      let config
      let data
      let field
      if (this.$refs.upload != undefined) {
        console.log(this.$refs.upload)
        config = this.$refs.upload
        field  = this.$refs.upload.data.field
        if (config.data.regList[0] !== undefined) {
          data = this.$refs.upload.uploadFiles
          if (config.data.regList[0].required && data.length < 1) {
            this.$message({
              message: config.data.regList[0].message,
              type: 'error'
            })
            return
          }
        }
      }
      this.$refs[this.formConf.formRef].validate(valid => {
        if (!valid) return false
        // 触发sumit事件
        // this.$emit('submit', this[this.formConf.formModel])
        const params = {
          formData: this.formConfCopy,
          valData: this[this.formConf.formModel]
        }
        let arr = []
        if (data != undefined) {
          for (let i = 0; i < data.length; i++) {
            arr.push(data[i].response)
          }
        }
        if (field) {
          params.valData[field] = JSON.stringify(arr)
        }
        this.$emit('submit', params)
        return true
      })
    },
    // 传值给父组件
    getData(){
      //debugger

      this.$emit('getData', this[this.formConf.formModel])
      // this.$emit('getData',this.formConfCopy)
    },
    aa(func,obj){
      this.$emit('getlist',func)
      return true
    }
  },
  render(h) {
    return renderFrom.call(this, h)
  }
}
</script>
