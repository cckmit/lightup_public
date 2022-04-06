package vip.xiaonuo.modular.lightup.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.xiaonuo.core.context.login.LoginContextHolder;
import vip.xiaonuo.modular.lightup.entity.LightupClock;
import vip.xiaonuo.modular.lightup.entity.LightupCourse;
import vip.xiaonuo.modular.lightup.service.FaceService;
import vip.xiaonuo.modular.lightup.service.LightupClockService;
import vip.xiaonuo.modular.lightup.mapper.LightupClockMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
* @author wanglei1118
* @description 针对表【lightup_clock(教师控制签到状态表)】的数据库操作Service实现
* @createDate 2022-03-09 16:34:59
*/
@Service
public class LightupClockServiceImpl extends ServiceImpl<LightupClockMapper, LightupClock>
    implements LightupClockService{

    @Resource
    LightupClockMapper lightupClockMapper;
    @Resource
    FaceService faceService;

    /**
     * 检查签到状态
     *
     * @param lightupCourse
     * @return
     */
    @Override
    public Boolean checkClockInStatus(LightupCourse lightupCourse) throws ParseException {
        List<LightupClock> clockList = getClockData(lightupCourse);
        for (LightupClock lightupClock : clockList) {
            if(faceService.betweenTime(lightupCourse.getBeginTime(),lightupClock.getClockInTime()) < 600000 ){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查签退状态
     *
     * @param lightupCourse
     * @return
     */
    @Override
    public Boolean checkClockOutStatus(LightupCourse lightupCourse) throws ParseException {
        List<LightupClock> clockList = getClockData(lightupCourse);
        for (LightupClock lightupClock : clockList) {
            if(faceService.betweenTime(lightupCourse.getEndTime(),lightupClock.getClockOutTime()) < 600000 ){
                return true;
            }
        }
        return false;
    }

    /**
     * 检查迟到状态
     *
     * @param lightupCourse
     * @return
     */
    @Override
    public Boolean checkClockMiddleStatus(LightupCourse lightupCourse) throws ParseException {
        List<LightupClock> clockList = getClockData(lightupCourse);
        for (LightupClock lightupClock : clockList) {
            if(faceService.betweenTime(lightupCourse.getEndTime()) < 2100000
            && faceService.betweenTime(lightupCourse.getBeginTime()) < 2100000){
                return true;
            }
        }
        return false;
    }

    @Override
    public List<LightupClock> getClockData(LightupCourse lightupCourse) {
        String sysLoginUserAccount = LoginContextHolder.me().getSysLoginUser().getAccount();
        String courseName = lightupCourse.getCourseName();
        QueryWrapper<LightupClock> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(LightupClock::getUserAccount,sysLoginUserAccount).eq(LightupClock::getCourseName,courseName);
        return lightupClockMapper.selectList(queryWrapper);
    }



    /**
     * 保存签到时间
     *
     * @param lightupCourse
     */
    @Override
    public void saveClockIn(LightupCourse lightupCourse) {
        String sysLoginUserAccount = LoginContextHolder.me().getSysLoginUser().getAccount();
        String courseName = lightupCourse.getCourseName();
        LightupClock lightupClock = new LightupClock();
        lightupClock.setId(IdWorker.getId());
        lightupClock.setUserAccount(sysLoginUserAccount);
        lightupClock.setCourseName(courseName);
        lightupClock.setClockInStatus(1);
        lightupClock.setClockInTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        lightupClockMapper.insert(lightupClock);
    }

    /**
     * 保存签退时间
     *
     * @param lightupCourse
     */
    @Override
    public void saveClockOut(LightupCourse lightupCourse) throws ParseException {
        List<LightupClock> clockList = getClockData(lightupCourse);
        for (LightupClock lightupClock : clockList) {
            //保证了是今天的签到状态
            if(ObjectUtil.isNotEmpty(lightupClock.getClockInTime()) && faceService.betweenTime(lightupCourse.getBeginTime(),lightupClock.getClockInTime()) < 60000 ){
                lightupClock.setClockOutTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                lightupClock.setClockOutStatus(1);
                lightupClockMapper.updateById(lightupClock);
                return;
            }
        }
        String sysLoginUserAccount = LoginContextHolder.me().getSysLoginUser().getAccount();
        String courseName = lightupCourse.getCourseName();
        LightupClock lightupClock = new LightupClock();
        lightupClock.setId(IdWorker.getId());
        lightupClock.setUserAccount(sysLoginUserAccount);
        lightupClock.setCourseName(courseName);
        lightupClock.setClockOutStatus(1);
        lightupClock.setClockOutTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        lightupClockMapper.insert(lightupClock);
    }

    /**
     * 保存迟到时间
     *
     * @param lightupCourse
     */
    @Override
    public void saveClockLate(LightupCourse lightupCourse) {
        String sysLoginUserAccount = LoginContextHolder.me().getSysLoginUser().getAccount();
        String courseName = lightupCourse.getCourseName();
        LightupClock lightupClock = new LightupClock();
        lightupClock.setId(IdWorker.getId());
        lightupClock.setUserAccount(sysLoginUserAccount);
        lightupClock.setCourseName(courseName);
        lightupClock.setClockInStatus(2);
        lightupClock.setClockInTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        lightupClockMapper.insert(lightupClock);
    }
}




