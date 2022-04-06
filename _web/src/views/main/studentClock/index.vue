<template>
  <div>
    <a-card :bordered="false" title="本周课程">
      <s-table
        ref="table"
        :columns="columnsWeek"
        :data="loadWeekData"
        :alert="options.alert"
        :rowKey="(record) => record.id"
        :rowSelection="options.rowSelection"
      >
      </s-table>
    </a-card>
    <a-card :bordered="false" title="今日课程">
      <s-table
        ref="table"
        :columns="columns"
        :data="loadTodayData"
        :alert="options.alert"
        :rowKey="(record) => record.id"
        :rowSelection="options.rowSelection"
      >
        <span slot="action">
          <a @click="linkToFace">签到</a>
          <a-divider type="vertical"/>
          <a>签退</a>
        </span>
      </s-table>
    </a-card>
  </div>
</template>
<script>
  import { STable, XDown } from '@/components'
  import moment from 'moment'
  import face from './face'
  import { TodayCourse, WeekCourse } from '@/api/modular/main/lightupCourse/lightupCourseManage'
  export default {
    components: {
      face,
      STable,
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
          // {
          //   title: '教师',
          //   align: 'center',
          //   dataIndex: 'teacherId'
          // },
          {
            title: '星期',
            align: 'center',
            dataIndex: 'weakday'
          },
          {
            title: '上课时间',
            align: 'center',
            dataIndex: 'beginTime'
          },
          {
            title: '考勤状态',
            align: 'center',
            dataIndex: 'status'
          }
        ],
        columnsWeek: [
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
          // {
          //   title: '教师',
          //   align: 'center',
          //   dataIndex: 'teacherId'
          // },
          {
            title: '星期',
            align: 'center',
            dataIndex: 'weakday'
          },
          {
            title: '上课时间',
            align: 'center',
            dataIndex: 'beginTime'
          },
          {
            title: '考勤状态',
            align: 'center',
            dataIndex: 'status'
          }
        ],
        tstyle: { 'padding-bottom': '0px', 'margin-bottom': '10px' },
        // 加载数据方法 必须为 Promise 对象
        loadWeekData: parameter => {
          return WeekCourse(parameter).then((res) => {
            return res.data
          })
        },
        loadTodayData: parameter => {
          return TodayCourse(parameter).then((res) => {
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
      this.columns.push({
        title: '操作',
        width: '150px',
        dataIndex: 'action',
        align: 'center',
        fix: 'right',
        scopedSlots: { customRender: 'action' }
      })
    },
    methods: {
      moment,
      linkToFace () {
        this.$router.push('/face')
      },
      toggleAdvanced () {
        this.advanced = !this.advanced
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
