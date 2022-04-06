<template>
  <div>
    <a-card :bordered="false" :bodyStyle="tstyle">
      <div class="table-page-search-wrapper" v-if="hasPerm('lightupCourse:page')">
        <a-form layout="inline">
          <a-row :gutter="48">
            <a-col :md="8" :sm="24">
              <a-form-item label="课程编码">
                <a-input v-model="queryParam.courseId" allow-clear placeholder="请输入课程编码"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24">
              <a-form-item label="课程名称">
                <a-input v-model="queryParam.courseName" allow-clear placeholder="请输入课程名称"/>
              </a-form-item>
            </a-col>
            <template v-if="advanced">
              <a-col :md="8" :sm="24">
                <a-form-item label="教师id">
                  <a-input v-model="queryParam.teacherId" allow-clear placeholder="请输入授课教师id"/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="授班级id">
                  <a-input v-model="queryParam.classId" allow-clear placeholder="请输入授课班级id"/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="授课星期">
                  <a-select style="width: 100%" v-model="queryParam.weakday" placeholder="请选择授课星期">
                    <a-select-option v-for="(item,index) in weakdayData" :key="index" :value="item.code">{{ item.name }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="开始时间">
                  <a-date-picker style="width: 100%" placeholder="请输入开始时间" v-model="queryParam.beginTimeDate" @change="onChangebeginTime"/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="">
                  <a-date-picker style="width: 100%" placeholder="请选择" v-model="queryParam.endTimeDate" @change="onChangeendTime"/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="状态（字典 0正常 1停用 2删除）">
                  <a-input v-model="queryParam.delStat" allow-clear placeholder="请输入状态（字典 0正常 1停用 2删除）"/>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="默认打卡状态">
                  <a-select style="width: 100%" v-model="queryParam.clockInStatus" placeholder="请选择默认打卡状态">
                    <a-select-option v-for="(item,index) in clockInStatusData" :key="index" :value="item.code">{{ item.name }}</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="8" :sm="24">
                <a-form-item label="备注">
                  <a-input v-model="queryParam.remark" allow-clear placeholder="请输入备注"/>
                </a-form-item>
              </a-col>
            </template>
            <a-col :md="8" :sm="24" >
              <span class="table-page-search-submitButtons">
                <a-button type="primary" @click="$refs.table.refresh(true)" >查询</a-button>
                <a-button style="margin-left: 8px" @click="() => queryParam = {}">重置</a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>
    </a-card>
    <a-card :bordered="false">
      <s-table
        ref="table"
        :columns="columns"
        :data="loadData"
        :alert="options.alert"
        :rowKey="(record) => record.id"
        :rowSelection="options.rowSelection"
      >
        <template class="table-operator" slot="operator" v-if="hasPerm('lightupCourse:add')" >
          <a-button type="primary" v-if="hasPerm('lightupCourse:add')" icon="plus" @click="$refs.addForm.add()">新增课程</a-button>
          <a-button type="danger" :disabled="selectedRowKeys.length < 1" v-if="hasPerm('lightupCourse:delete')" @click="batchDelete"><a-icon type="delete"/>批量删除</a-button>
          <x-down
            v-if="hasPerm('lightupCourse:export')"
            ref="batchExport"
            @batchExport="batchExport"
          />
        </template>
        <span slot="weakdayScopedSlots" slot-scope="text">
          {{ '${column.dictTypeCode}' | dictType(text) }}
        </span>
        <span slot="clockInStatusScopedSlots" slot-scope="text">
          {{ 'yes_or_no' | dictType(text) }}
        </span>
        <span slot="action" slot-scope="text, record">
          <a v-if="hasPerm('lightupCourse:edit')" @click="$refs.editForm.edit(record)">编辑</a>
          <a-divider type="vertical" v-if="hasPerm('lightupCourse:edit') & hasPerm('lightupCourse:delete')"/>
          <a-popconfirm v-if="hasPerm('lightupCourse:delete')" placement="topRight" title="确认删除？" @confirm="() => singleDelete(record)">
            <a>删除</a>
          </a-popconfirm>
        </span>
      </s-table>
      <add-form ref="addForm" @ok="handleOk" />
      <edit-form ref="editForm" @ok="handleOk" />
    </a-card>
  </div>
</template>
<script>
  import { STable, XDown } from '@/components'
  import moment from 'moment'
  import { lightupCoursePage, lightupCourseDelete, lightupCourseExport } from '@/api/modular/main/lightupCourse/lightupCourseManage'
  import addForm from './addForm.vue'
  import editForm from './editForm.vue'
  export default {
    components: {
      STable,
      addForm,
      editForm,
      XDown
    },
    data () {
      return {
        // 高级搜索 展开/关闭
        advanced: false,
        // 查询参数
        queryParam: {},
        // 表头
        columns: [
          {
            title: '课程编码',
            align: 'center',
            dataIndex: 'id'
          },
          {
            title: '课程名称',
            align: 'center',
            dataIndex: 'courseName'
          },
          {
            title: '教师',
            align: 'center',
            dataIndex: 'teacherAccount'
          },
          {
            title: '班级',
            align: 'center',
            dataIndex: 'className'
          },
          {
            title: '授课星期',
            align: 'center',
            dataIndex: 'weakday'
          },
          {
            title: '开始时间',
            align: 'center',
            dataIndex: 'beginTime'
          },
          {
            title: '结束时间',
            align: 'center',
            dataIndex: 'endTime'
          },
          {
            title: '备注',
            align: 'center',
            dataIndex: 'remark'
          }
        ],
        tstyle: { 'padding-bottom': '0px', 'margin-bottom': '10px' },
        // 加载数据方法 必须为 Promise 对象
        loadData: parameter => {
          return lightupCoursePage(parameter).then((res) => {
            return res.data
          })
        },
        clockInStatusData: [],
        selectedRowKeys: [],
        selectedRows: [],
        options: {
          alert: { show: true, clear: () => { this.selectedRowKeys = [] } },
          rowSelection: {
            selectedRowKeys: this.selectedRowKeys,
            onChange: this.onSelectChange
          }
        }
      }
    },
    created () {
      if (this.hasPerm('lightupCourse:edit') || this.hasPerm('lightupCourse:delete')) {
        this.columns.push({
          title: '操作',
          width: '150px',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        })
      }
      const clockInStatusOption = this.$options
      this.clockInStatusData = clockInStatusOption.filters['dictData']('yes_or_no')
    },
    methods: {
      moment,
      /**
       * 单个删除
       */
      singleDelete (record) {
        const param = [{ 'id': record.id }]
        this.lightupCourseDelete(param)
      },
      /**
       * 批量删除
       */
      batchDelete () {
        const paramIds = this.selectedRowKeys.map((d) => {
            return { 'id': d }
        })
        this.lightupCourseDelete(paramIds)
      },
      lightupCourseDelete (record) {
        lightupCourseDelete(record).then((res) => {
          if (res.success) {
            this.$message.success('删除成功')
            this.$refs.table.clearRefreshSelected()
          } else {
            this.$message.error('删除失败') // + res.message
          }
        })
      },
      toggleAdvanced () {
        this.advanced = !this.advanced
      },
      onChangebeginTime(date, dateString) {
        this.beginTimeDateString = dateString
      },
      onChangeendTime(date, dateString) {
        this.endTimeDateString = dateString
      },
      /**
       * 批量导出
       */
      batchExport () {
        const paramIds = this.selectedRowKeys.map((d) => {
            return { 'id': d }
        })
        lightupCourseExport(paramIds).then((res) => {
            this.$refs.batchExport.downloadfile(res)
        })
      },
      handleOk () {
        this.$refs.table.refresh()
      },
      onSelectChange (selectedRowKeys, selectedRows) {
        this.selectedRowKeys = selectedRowKeys
        this.selectedRows = selectedRows
      }
    }
  }
</script>
<style lang="less">
  .table-operator {
    margin-bottom: 18px;
  }
  button {
    margin-right: 8px;
  }
</style>
