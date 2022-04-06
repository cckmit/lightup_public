import { axios } from '@/utils/request'

/**
 * 查询今日课程
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function TodayCourse (parameter) {
  return axios({
    url: '/studentClock/todayCourse',
    method: 'post',
    params: parameter
  })
}

/**
 * 查询本周课程
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function WeekCourse (parameter) {
  return axios({
    url: '/studentClock/weekCourse',
    method: 'post',
    params: parameter
  })
}

/**
 * 查询课程设置表
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function lightupCoursePage (parameter) {
  return axios({
    url: '/lightupCourse/page',
    method: 'get',
    params: parameter
  })
}

/**
 * 课程设置表列表
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function lightupCourseList (parameter) {
  return axios({
    url: '/lightupCourse/list',
    method: 'get',
    params: parameter
  })
}

/**
 * 添加课程设置表
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function lightupCourseAdd (parameter) {
  return axios({
    url: '/lightupCourse/add',
    method: 'post',
    data: parameter
  })
}

/**
 * 编辑课程设置表
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function lightupCourseEdit (parameter) {
  return axios({
    url: '/lightupCourse/edit',
    method: 'post',
    data: parameter
  })
}

/**
 * 删除课程设置表
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function lightupCourseDelete (parameter) {
  return axios({
    url: '/lightupCourse/delete',
    method: 'post',
    data: parameter
  })
}

/**
 * 导出课程设置表
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
export function lightupCourseExport (parameter) {
  return axios({
    url: '/lightupCourse/export',
    method: 'get',
    params: parameter,
    responseType: 'blob'
  })
}

/**
 * 识别接口
 */
export function faceIdentifyApi (parameter) {
  return axios({
    url: '/face/clock',
    method: 'post',
    data: {
      picBase: parameter.picBase,
      avatarId: parameter.avatarId
    }
  })
}
