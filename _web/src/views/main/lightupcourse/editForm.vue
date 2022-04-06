<template>
  <a-modal
    title="编辑课程"
    :width="900"
    :visible="visible"
    :confirmLoading="confirmLoading"
    @ok="handleSubmit"
    @cancel="handleCancel"
  >
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-form-item v-show="false"><a-input v-decorator="['id']" /></a-form-item>
        <a-form-item
          label="课程名称"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <a-input placeholder="请输入课程名称" v-decorator="['courseName', {rules: [{required: true, message: '请输入课程名称！'}]}]" />
        </a-form-item>
        <a-form-item
          label="授课教师职工号"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <a-input placeholder="请输入授课教师职工号" v-decorator="['teacherAccount', {rules: [{required: true, message: '请输入授课教师职工号！'}]}]" />
        </a-form-item>
        <a-form-item
          label="授课班级"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <a-input placeholder="请输入授课班级" v-decorator="['className', {rules: [{required: true, message: '请输入授课班级！'}]}]" />
        </a-form-item>
        <a-form-item
          label="授课星期"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-select style="width: 100%" placeholder="请选择授课星期" v-decorator="['weakday', {rules: [{ required: true, message: '请选择授课星期！' }]}]">
            <a-select-option v-for="(item,index) in weakdayData" :key="index" :value="item.code">{{ item.name }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          label="开始时间"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
        >
          <a-select style="width: 100%" placeholder="请选择开始上课时间" v-decorator="['beginTime', {rules: [{ required: true, message: '请选择开始上课时间！' }]}]">
            <a-select-option v-for="(item,index) in BeginTimeData" :key="index" :value="item.name">{{ item.name }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          label="结束时间"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <a-select style="width: 100%" placeholder="请选择下课时间" v-decorator="['endTime', {rules: [{ required: true, message: '请选择下课时间！' }]}]">
            <a-select-option v-for="(item,index) in EndTimeData" :key="index" :value="item.name">{{ item.name }}</a-select-option>
          </a-select>
        </a-form-item>
        <a-form-item
          label="备注"
          :labelCol="labelCol"
          :wrapperCol="wrapperCol"
          has-feedback
        >
          <a-input placeholder="请输入备注" v-decorator="['remark']" />
        </a-form-item>
      </a-form>
    </a-spin>
  </a-modal>
</template>

<script>
  import moment from 'moment'
  import { lightupCourseEdit } from '@/api/modular/main/lightupCourse/lightupCourseManage'
  export default {
    data () {
      return {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 5 }
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 15 }
        },
        beginTimeDateString: '',
        endTimeDateString: '',
        clockInStatusData: [],
        visible: false,
        confirmLoading: false,
        form: this.$form.createForm(this),
        weakdayData: [
          { name: '周一', code: '周一' },
          { name: '周二', code: '周二' },
          { name: '周三', code: '周三' },
          { name: '周四', code: '周四' },
          { name: '周五', code: '周五' },
          { name: '周六', code: '周六' },
          { name: '周日', code: '周日' }
        ],
        BeginTimeData: [
          { name: '08:00:00' },
          { name: '08:50:00' },
          { name: '09:50:00' },
          { name: '10:40:00' },
          { name: '11:35:00' },
          { name: '13:00:00' },
          { name: '13:50:00' },
          { name: '14:40:00' },
          { name: '15:30:00' },
          { name: '16:20:00' },
          { name: '18:00:00' },
          { name: '18:50:00' },
          { name: '19:40:00' }
        ],
        EndTimeData: [
          { name: '08:45:00' },
          { name: '09:35:00' },
          { name: '10:35:00' },
          { name: '11:25:00' },
          { name: '12:20:00' },
          { name: '13:45:00' },
          { name: '14:35:00' },
          { name: '15:25:00' },
          { name: '16:15:00' },
          { name: '17:15:00' },
          { name: '18:45:00' },
          { name: '19:35:00' },
          { name: '20:25:00' }
        ]
      }
    },
    methods: {
      moment,
      // 初始化方法
      edit (record) {
        this.visible = true
        setTimeout(() => {
          this.form.setFieldsValue(
            {
              id: record.id,
              courseId: record.courseId,
              courseName: record.courseName,
              teacherAccount: record.teacherAccount,
              className: record.className,
              weakday: record.weakday,
              delStat: record.delStat,
              clockInStatus: record.clockInStatus,
              remark: record.remark,
              beginTime: record.beginTime,
              endTime: record.endTime
            }
          )
        }, 100)
      },
      handleSubmit () {
        const { form: { validateFields } } = this
        this.confirmLoading = true
        validateFields((errors, values) => {
          if (!errors) {
            for (const key in values) {
              if (typeof (values[key]) === 'object' && values[key] != null) {
                values[key] = JSON.stringify(values[key])
              }
            }
            lightupCourseEdit(values).then((res) => {
              if (res.success) {
                this.$message.success('编辑成功')
                this.confirmLoading = false
                this.$emit('ok', values)
                this.handleCancel()
              } else {
                this.$message.error('编辑失败')//  + res.message
              }
            }).finally((res) => {
              this.confirmLoading = false
            })
          } else {
            this.confirmLoading = false
          }
        })
      },
      handleCancel () {
        this.form.resetFields()
        this.visible = false
      }
    }
  }
</script>
