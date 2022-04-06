package vip.xiaonuo.modular.lightup.service;

import vip.xiaonuo.modular.lightup.entity.LightupClock;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.modular.lightup.entity.LightupCourse;

import java.text.ParseException;
import java.util.List;

/**
* @author wanglei1118
* @description 针对表【lightup_clock(教师控制签到状态表)】的数据库操作Service
* @createDate 2022-03-09 16:34:59
*/
public interface LightupClockService extends IService<LightupClock> {

    /**
     * 检查签到状态
     * @param lightupCourse
     * @return
     */
    Boolean checkClockInStatus(LightupCourse lightupCourse) throws ParseException;

    /**
     * 检查签退状态
     * @param lightupCourse
     * @return
     */
    Boolean checkClockOutStatus(LightupCourse lightupCourse) throws ParseException;

    /**
     * 检查迟到状态
     * @param lightupCourse
     * @return
     */
    Boolean checkClockMiddleStatus(LightupCourse lightupCourse) throws ParseException;

    /**
     * 保存签到时间
     * @param lightupCourse
     */
    void saveClockIn(LightupCourse lightupCourse);

    /**
     * 保存签退时间
     * @param lightupCourse
     */
    void saveClockOut(LightupCourse lightupCourse) throws ParseException;

    /**
     * 保存迟到时间
     * @param lightupCourse
     */
    void saveClockLate(LightupCourse lightupCourse);

    List<LightupClock> getClockData(LightupCourse lightupCourse);

}
