/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.modular.lightup.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.DaoTemplate;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.core.exception.ServiceException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.pojo.login.SysLoginUser;
import vip.xiaonuo.core.pojo.page.PageResult;
import vip.xiaonuo.core.util.PoiUtil;
import vip.xiaonuo.modular.lightup.entity.LightupClock;
import vip.xiaonuo.modular.lightup.entity.LightupCourse;
import vip.xiaonuo.modular.lightup.entity.LightupCourseWithSort;
import vip.xiaonuo.modular.lightup.entity.LightupCourseWithStatus;
import vip.xiaonuo.modular.lightup.enums.LightupCourseExceptionEnum;
import vip.xiaonuo.modular.lightup.mapper.LightupClockMapper;
import vip.xiaonuo.modular.lightup.mapper.LightupCourseMapper;
import vip.xiaonuo.modular.lightup.param.LightupCourseParam;
import vip.xiaonuo.modular.lightup.service.FaceService;
import vip.xiaonuo.modular.lightup.service.LightupClockService;
import vip.xiaonuo.modular.lightup.service.LightupCourseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.xiaonuo.sys.modular.user.entity.SysUser;
import vip.xiaonuo.sys.modular.user.param.SysUserParam;
import vip.xiaonuo.sys.modular.user.result.SysUserResult;
import vip.xiaonuo.sys.modular.user.service.SysUserService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 课程设置表service接口实现类
 *
 * @author leiw
 * @date 2022-03-03 13:21:35
 */
@Service
public class LightupCourseServiceImpl extends ServiceImpl<LightupCourseMapper, LightupCourse> implements LightupCourseService {

    @Resource
    LightupCourseMapper lightupCourseMapper;

    @Resource
    SysUserService sysUserService;

    @Resource
    LightupClockService lightupClockService;

    @Resource
    LightupClockMapper lightupClockMapper;

    @Override
    public PageResult<LightupCourse> page(LightupCourseParam lightupCourseParam) {
        QueryWrapper<LightupCourse> queryWrapper = new QueryWrapper<>();
        if (ObjectUtil.isNotNull(lightupCourseParam)) {

            // 根据课程唯一编码 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getId())) {
                queryWrapper.lambda().eq(LightupCourse::getId, lightupCourseParam.getId());
            }
            // 根据课程名称 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getCourseName())) {
                queryWrapper.lambda().eq(LightupCourse::getCourseName, lightupCourseParam.getCourseName());
            }
            // 根据授课教师账户 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getTeacherAccount())) {
                queryWrapper.lambda().eq(LightupCourse::getTeacherAccount, lightupCourseParam.getTeacherAccount());
            }
            // 根据授课班级名称 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getClassName())) {
                queryWrapper.lambda().eq(LightupCourse::getClassName, lightupCourseParam.getClassName());
            }
            // 根据授课星期 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getWeakday())) {
                queryWrapper.lambda().eq(LightupCourse::getWeakday, lightupCourseParam.getWeakday());
            }
            // 根据授课节数数组，每天哪几节课 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getBeginTime())) {
                queryWrapper.lambda().eq(LightupCourse::getBeginTime, lightupCourseParam.getBeginTime());
            }
            // 根据 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getEndTime())) {
                queryWrapper.lambda().eq(LightupCourse::getEndTime, lightupCourseParam.getEndTime());
            }
            // 根据状态（字典 0正常 1停用 2删除） 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getDelStat())) {
                queryWrapper.lambda().eq(LightupCourse::getDelStat, lightupCourseParam.getDelStat());
            }
            // 根据默认打卡状态，1，允许打卡；2，禁止打卡 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getClockInStatus())) {
                queryWrapper.lambda().eq(LightupCourse::getClockInStatus, lightupCourseParam.getClockInStatus());
            }
            // 根据备注 查询
            if (ObjectUtil.isNotEmpty(lightupCourseParam.getRemark())) {
                queryWrapper.lambda().eq(LightupCourse::getRemark, lightupCourseParam.getRemark());
            }
        }
        PageResult<LightupCourse> pageResult = new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
        pageResult.setRows(courseSort(pageResult.getRows()));
        return pageResult;
    }

    @Override
    public List<LightupCourse> list(LightupCourseParam lightupCourseParam) {
        return this.list();
    }

    @Override
    public void add(LightupCourseParam lightupCourseParam) {
        LightupCourse lightupCourse = new LightupCourse();
        BeanUtil.copyProperties(lightupCourseParam, lightupCourse);
        this.save(lightupCourse);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<LightupCourseParam> lightupCourseParamList) {
        lightupCourseParamList.forEach(lightupCourseParam -> {
            this.removeById(lightupCourseParam.getId());
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(LightupCourseParam lightupCourseParam) {
        LightupCourse lightupCourse = this.queryLightupCourse(lightupCourseParam);
        BeanUtil.copyProperties(lightupCourseParam, lightupCourse);
        this.updateById(lightupCourse);
    }

    @Override
    public LightupCourse detail(LightupCourseParam lightupCourseParam) {
        return this.queryLightupCourse(lightupCourseParam);
    }

    /**
     * 获取课程设置表
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    private LightupCourse queryLightupCourse(LightupCourseParam lightupCourseParam) {
        LightupCourse lightupCourse = this.getById(lightupCourseParam.getId());
        if (ObjectUtil.isNull(lightupCourse)) {
            throw new ServiceException(LightupCourseExceptionEnum.NOT_EXIST);
        }
        return lightupCourse;
    }

    @Override
    public void export(LightupCourseParam lightupCourseParam) {
        List<LightupCourse> list = this.list(lightupCourseParam);
        PoiUtil.exportExcelWithStream("SnowyLightupCourse.xls", LightupCourse.class, list);
    }

    @Override
    public PageResult<LightupCourse> getTodayCoursePage(LightupCourseParam lightupCourseParam) {
        PageResult<LightupCourse> pageWeek = getWeekCoursePageWithSort(lightupCourseParam);
        PageResult<LightupCourse> pageToday = new PageResult<>();
        BeanUtil.copyProperties(pageWeek, pageToday);
        List<LightupCourse> listToday = new ArrayList<>();
        pageWeek.getRows().forEach(f -> {
            if(f.getWeakday().equals(getNowWeekString())){
                listToday.add(f);
            }
        });
        //按时间排序
        listToday.sort(Comparator.comparing(LightupCourse::getBeginTime));
        pageToday.setRows(listToday);
        return pageToday;
    }

    @Override
    public PageResult<LightupCourse> getWeekCoursePageWithSort(LightupCourseParam lightupCourseParam) {
        SysUserParam user = new SysUserParam();
        SysLoginUser sysLoginUser = LoginContextHolder.me().getSysLoginUser();
        BeanUtil.copyProperties(sysLoginUser, user);
        SysUserResult sysUserResult = sysUserService.detail(user);
        String className = sysUserResult.getSysEmpInfo().getOrgName();
        QueryWrapper<LightupCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_name", className).eq("del_stat", 0);
        PageResult<LightupCourse> pageResult = new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
        pageResult.setRows(courseSort(pageResult.getRows()));
        return pageResult;
    }

    @Override
    public PageResult<LightupCourseWithStatus> getCoursePageWithSortAndStatus(PageResult<LightupCourse> page) {
        List<LightupCourse> list = new ArrayList<>(page.getRows());
        List<LightupCourseWithStatus> listStatus = new ArrayList<>();
        list.forEach( f -> {
            LightupCourseWithStatus entityWithStatus = new LightupCourseWithStatus();
            String status = "缺勤";
            Integer nowWeek = weekToNum(getNowWeekString());
            Integer aimWeek = weekToNum(f.getWeakday());
            try {
                if(nowWeek < aimWeek){
                    status = "尚未上课";
                } else if(nowWeek > aimWeek){
                    //直接查表
                    //取出所有的考勤数据，取出本周的考勤数据
                    //取出当天的考勤数据
                    //输出考勤数据
                    // 00缺勤，01未签到， 10未签退， 11考勤正常， 20迟到早退，21迟到
                    List<LightupClock> clockData = lightupClockService.getClockData(f);
                    for (LightupClock clockDatum : clockData) {
                        Date clockIn = DateUtil.parse(clockDatum.getClockInTime());
                        Date clockOut = DateUtil.parse(clockDatum.getClockOutTime());
                        if(DateUtil.beginOfWeek(clockIn).equals(DateUtil.beginOfWeek(new Date())) || DateUtil.beginOfWeek(clockOut).equals(DateUtil.beginOfWeek(new Date()))){
                            if(getWeek(clockIn).equals(f.getWeakday())){
                                //当天的数据
                                Integer in = clockDatum.getClockInStatus();
                                Integer out = clockDatum.getClockOutStatus();
                                if(in.equals(0) && out.equals(0)){
                                    status = "缺勤";
                                }
                                if(in.equals(0) && out.equals(1)){
                                    status = "未签到";
                                }
                                if(in.equals(1) && out.equals(0)){
                                    status = "未签退";
                                }
                                if(in.equals(1) && out.equals(1)){
                                    status = "考勤正常";
                                }
                                if(in.equals(2) && out.equals(0)){
                                    status = "迟到早退";
                                }
                                if(in.equals(2) && out.equals(1)){
                                    status = "迟到";
                                }
                            }
                        }
                    }

                } else {
                    status = calculateStatus(f);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            BeanUtil.copyProperties(f, entityWithStatus);
            entityWithStatus.setStatus(status);
            listStatus.add(entityWithStatus);
        });
        PageResult<LightupCourseWithStatus> pageResult = new PageResult<>();
        BeanUtil.copyProperties(page, pageResult);
        pageResult.setRows(listStatus);
        return pageResult;
    }

    /**
     * 计算当前课程当前时间的考勤状态
     * @param lightupCourse
     * @return
     */
    private String calculateStatus(LightupCourse lightupCourse) throws ParseException {
        //需要比对的时间
        String today = DateUtil.today();
        String status = "";
        Date begin = DateUtil.parse(today + " " + lightupCourse.getBeginTime());
        Date end = DateUtil.parse(today + " " + lightupCourse.getEndTime());
        if(System.currentTimeMillis() < begin.getTime()){
            status = "尚未上课";
            //识别状态逻辑
            if(lightupClockService.checkClockInStatus(lightupCourse)){
                status = status + " " + "已签到";
            }
        }
        if(System.currentTimeMillis() > begin.getTime() && System.currentTimeMillis() < end.getTime()){
            status = "上课中";
            //识别状态逻辑
            if(lightupClockService.checkClockMiddleStatus(lightupCourse)){
                status = status + " " + "迟到";
            }
            if(lightupClockService.checkClockInStatus(lightupCourse)){
                status = status + " " + "已签到";
            }
        }
        if(System.currentTimeMillis() > end.getTime()){
            status = "已下课";
            //识别状态逻辑
            if( !lightupClockService.checkClockInStatus(lightupCourse) && !lightupClockService.checkClockMiddleStatus(lightupCourse)){
                status = status + " " + "未签到";
                if(!lightupClockService.checkClockOutStatus(lightupCourse)){
                    status = "缺勤";
                }
            }
        }
        return status;
    }


    private String getNowWeekString(){
        String str = new SimpleDateFormat("EEEE").format(new Date());
        String weekNum = String.valueOf(str.charAt(str.length() - 1));
        return "周" + weekNum;
    }

    private String getWeek(Date date){
        String str = new SimpleDateFormat("EEEE").format(date);
        String weekNum = String.valueOf(str.charAt(str.length() - 1));
        return "周" + weekNum;
    }

    /**
     * 周日期ToNumber
     * @param weekStr
     * @return
     */
    private Integer weekToNum(String weekStr){
        switch (weekStr) {
            case "周一":
                return 1;
            case "周二":
                return 2;
            case "周三":
                return 3;
            case "周四":
                return 4;
            case "周五":
                return 5;
            case "周六":
                return 6;
            case "周日":
                return 7;
            default:
                return 0;
        }
    }

    private List<LightupCourse> courseSort(List<LightupCourse> list){
        List<LightupCourse> result = new ArrayList<>();
        List<LightupCourseWithSort> middle = new ArrayList<>();
        list .forEach( f -> {
            LightupCourseWithSort withSortEntity = new LightupCourseWithSort();
            BeanUtil.copyProperties(f, withSortEntity);
            switch (f.getWeakday()) {
                case "周一":
                    withSortEntity.setWeekNum(1);
                    break;
                case "周二":
                    withSortEntity.setWeekNum(2);
                    break;
                case "周三":
                    withSortEntity.setWeekNum(3);
                    break;
                case "周四":
                    withSortEntity.setWeekNum(4);
                    break;
                case "周五":
                    withSortEntity.setWeekNum(5);
                    break;
                case "周六":
                    withSortEntity.setWeekNum(6);
                    break;
                case "周日":
                    withSortEntity.setWeekNum(7);
                    break;
                default:
                    withSortEntity.setWeekNum(0);
                    break;
            }
            middle.add(withSortEntity);
        });
        middle.sort(Comparator.comparing(LightupCourseWithSort::getWeekNum));
        List<LightupCourse> resultList = new ArrayList<>();
        middle.forEach( f -> {
            LightupCourse entity = new LightupCourse();
            BeanUtil.copyProperties(f, entity);
            resultList.add(entity);
        });
        return resultList;
    }

    public static void main(String[] args) {
        System.out.println(new Date());
    }

}
