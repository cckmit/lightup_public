<template>
  <page-view :avatar="avatar" :title="false">
    <div slot="headerContent">
      <div class="title">{{ timeFix }}，{{ user.name }}</div>
      <div style="margin-bottom:10px">{{ user.loginEmpInfo.orgName }}</div>
    </div>

    <div>
      <a-row :gutter="24">
        <a-col :xl="16" :lg="24" :md="24" :sm="24" :xs="24">
          <a-card
            class="project-list"
            :loading="loading"
            style="margin-bottom: 24px;"
            :bordered="false"
            title="考勤数据概览"
            :body-style="{ padding: 0 }">
            <a slot="extra">数据分析</a>
            <div>
              <a-card-grid class="project-card-grid" :key="i" v-for="(item, i) in projects">
                <a-card :bordered="false" :body-style="{ padding: 0 }">
                  <a-card-meta>
                    <div slot="title" class="card-title">

                      <head-info :title="item.title" content="56*" :center="false" :bordered="false"/>
                    </div>

                  </a-card-meta>

                </a-card>
              </a-card-grid>
            </div>
          </a-card>

          <a-card :loading="loading" title="本周考勤数据" :bordered="false">
            <a-list>
              <a-list-item :key="index" v-for="(item, index) in activities">
                <a-list-item-meta>
                  <a-avatar slot="avatar" :src="item.user.avatar" />
                  <div slot="title">
                    <span>{{ item.user.nickname }}</span>&nbsp;
                    在&nbsp;<a href="#">{{ item.project.name }}</a>&nbsp;
                    <span>{{ item.project.action }}</span>&nbsp;
                    <a href="#">{{ item.project.event }}</a>
                  </div>
                  <div slot="description">{{ item.time }}</div>
                </a-list-item-meta>
              </a-list-item>
            </a-list>
          </a-card>
        </a-col>
        <a-col
          style="padding: 0 12px"
          :xl="8"
          :lg="24"
          :md="24"
          :sm="24"
          :xs="24">
          <a-card title="常用应用" style="margin-bottom: 24px" :bordered="false" :body-style="{padding: 0}">
            <div class="item-group">
              <a>操作一</a>
              <a>操作二</a>
              <a>操作三</a>
              <a>操作四</a>
              <a>操作五</a>
              <a>操作六</a>
              <a-button size="small" type="primary" ghost icon="plus">添加</a-button>
            </div>
          </a-card>
          <a-card title="通知公告" :bordered="false" >
            <div style="min-height: 400px;">
              <a-list>
                <a-list-item :key="index" v-for="(item, index) in activities">
                  <a-list-item-meta>
                    <div slot="title">
                      <span>{{ item.user.nickname }}</span>&nbsp;
                      在&nbsp;<a href="#">{{ item.project.name }}</a>&nbsp;
                      <span>{{ item.project.action }}</span>&nbsp;
                      <a href="#">{{ item.project.event }}</a>
                    </div>
                    <div slot="description">{{ item.time }}</div>
                  </a-list-item-meta>
                </a-list-item>
              </a-list>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </div>
  </page-view>
</template>

<script>
  import { timeFix } from '@/utils/util'
  import { mapState } from 'vuex'
  import { PageView } from '@/layouts'
  import HeadInfo from '@/components/tools/HeadInfo'
  import { Radar } from '@/components'
  import { sysFileInfoPreview } from '@/api/modular/system/fileManage'

  export default {
    name: 'Workplace',
    components: {
      PageView,
      HeadInfo,
      Radar
    },
    data () {
      return {
        timeFix: timeFix(),
        avatar: '',
        user: {},

        projects: [],
        loading: true,
        radarLoading: true,
        activities: [],
        teams: [],

        // data
        axis1Opts: {
          dataKey: 'item',
          line: null,
          tickLine: null,
          grid: {
            lineStyle: {
              lineDash: null
            },
            hideFirstLine: false
          }
        },
        axis2Opts: {
          dataKey: 'score',
          line: null,
          tickLine: null,
          grid: {
            type: 'polygon',
            lineStyle: {
              lineDash: null
            }
          }
        },
        scale: [{
          dataKey: 'score',
          min: 0,
          max: 80
        }],
        axisData: [
          { item: '引用', a: 70, b: 30, c: 40 },
          { item: '口碑', a: 60, b: 70, c: 40 },
          { item: '产量', a: 50, b: 60, c: 40 },
          { item: '贡献', a: 40, b: 50, c: 40 },
          { item: '热度', a: 60, b: 70, c: 40 },
          { item: '引用', a: 70, b: 50, c: 40 }
        ],
        radarData: []
      }
    },
    computed: {
      ...mapState({
        nickname: (state) => state.user.nickname,
        welcome: (state) => state.user.welcome
      }),
      userInfo () {
        return this.$store.getters.userInfo
      }
    },
    created () {
      this.user = this.userInfo
      console.log(this.user)
      sysFileInfoPreview({ id: this.userInfo.avatar }).then(res => {
        return 'data:image/png;base64,' + btoa(
          new Uint8Array(res)
            .reduce((data, byte) => data + String.fromCharCode(byte), '')
        )
      }).then(data => {
        this.avatar = data // 图片地址 <img src='option.img' />
      })
    },
    mounted () {
      this.getProjects()
      this.getActivity()
      this.getTeams()
      this.initRadar()
    },
    methods: {
      getProjects () {
        this.projects = [{
          id: 1,
          cover: 'https://gw.alipayobjects.com/zos/rmsportal/WdGqmHpayyMjiEhcKoVE.png',
          title: '本周课程数目',
          description: '那是一种内在的东西， 他们到达不了，也无法触及的',
          status: 1,
          updatedAt: '2018-07-26 00:00:00'
        },
          {
            id: 2,
            cover: 'https://gw.alipayobjects.com/zos/rmsportal/zOsKZmFRdUtvpqCImOVY.png',
            title: '今日课程数目',
            description: '希望是一个好东西，也许是最好的，好东西是不会消亡的',
            status: 1,
            updatedAt: '2018-07-26 00:00:00'
          },
          {
            id: 3,
            cover: 'https://gw.alipayobjects.com/zos/rmsportal/dURIMkkrRFpPgTuzkwnB.png',
            title: '考勤异常数目',
            description: '城镇中有那么多的酒馆，她却偏偏走进了我的酒馆',
            status: 1,
            updatedAt: '2018-07-26 00:00:00'
          }
        ]
        this.loading = false
      },
      getActivity () {
        this.activities = []
      }
    }
  }
</script>

<style lang="less" scoped>
  .project-list {

    .card-title {
      font-size: 0;

      a {
        color: rgba(0, 0, 0, 0.85);
        margin-left: 12px;
        line-height: 24px;
        height: 24px;
        display: inline-block;
        vertical-align: top;
        font-size: 14px;

        &:hover {
          color: #1890ff;
        }
      }
    }
    .card-description {
      color: rgba(0, 0, 0, 0.45);
      height: 44px;
      line-height: 22px;
      overflow: hidden;
    }
    .project-item {
      display: flex;
      margin-top: 8px;
      overflow: hidden;
      font-size: 12px;
      height: 20px;
      line-height: 20px;
      a {
        color: rgba(0, 0, 0, 0.45);
        display: inline-block;
        flex: 1 1 0;

        &:hover {
          color: #1890ff;
        }
      }
      .datetime {
        color: rgba(0, 0, 0, 0.25);
        flex: 0 0 auto;
        float: right;
      }
    }
    .ant-card-meta-description {
      color: rgba(0, 0, 0, 0.45);
      height: 44px;
      line-height: 22px;
      overflow: hidden;
    }
  }

  .item-group {
    padding: 20px 0 8px 24px;
    font-size: 0;
    a {
      color: rgba(0, 0, 0, 0.65);
      display: inline-block;
      font-size: 14px;
      margin-bottom: 13px;
      width: 25%;
    }
  }

  .members {
    a {
      display: block;
      margin: 12px 0;
      line-height: 24px;
      height: 24px;
      .member {
        font-size: 14px;
        color: rgba(0, 0, 0, .65);
        line-height: 24px;
        max-width: 100px;
        vertical-align: top;
        margin-left: 12px;
        transition: all 0.3s;
        display: inline-block;
      }
      &:hover {
        span {
          color: #1890ff;
        }
      }
    }
  }

  .mobile {

    .project-list {

      .project-card-grid {
        width: 100%;
      }
    }

    .more-info {
      border: 0;
      padding-top: 16px;
      margin: 16px 0 16px;
    }

    .headerContent .title .welcome-text {
      display: none;
    }
  }

</style>
