package vip.xiaonuo.modular.lightup.controller;

import cn.hutool.core.util.ObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.core.annotion.BusinessLog;
import vip.xiaonuo.core.enums.LogAnnotionOpTypeEnum;
import vip.xiaonuo.core.pojo.response.ErrorResponseData;
import vip.xiaonuo.core.pojo.response.ResponseData;
import vip.xiaonuo.core.pojo.response.SuccessResponseData;
import vip.xiaonuo.modular.lightup.entity.LightupClock;
import vip.xiaonuo.modular.lightup.entity.LightupCourse;
import vip.xiaonuo.modular.lightup.service.FaceService;
import vip.xiaonuo.modular.lightup.service.LightupClockService;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FaceController {

    @Resource
    FaceService faceService;
    @Resource
    LightupClockService lightupClockService;



    /**
     * 人脸识别
     *
     * @author leiw
     * @date 2022-03-03 13:21:35
     */
    @PostMapping("/face/clock")
    @BusinessLog(title = "人脸识别打卡", opType = LogAnnotionOpTypeEnum.QUERY)
    public ResponseData clock(@RequestBody Map<String, String> map) throws ParseException {

        //确认今天有课
        List<LightupCourse> todayCourse = faceService.checkTodayCourse();
        if(ObjectUtil.isEmpty(todayCourse)){
            return new ErrorResponseData(201, "今日无相关课程！");
        }

        for (LightupCourse lightupCourse : todayCourse) {
            if(faceService.checkBeginTime(lightupCourse)){ //检查当前时间是否为上课打卡时间
                if(lightupClockService.checkClockInStatus(lightupCourse)){
                    //return getIdentifyResponseData(map, todayCourse, "请勿重复打卡！");
                    return new ErrorResponseData(202,"请勿重复打卡！");
                }
                lightupClockService.saveClockIn(lightupCourse);
                return getIdentifyResponseData(map, lightupCourse, "签到成功！");
            }
            if(faceService.checkEndTime(lightupCourse)){//检查当前时间是否为下课打卡时间
                if(lightupClockService.checkClockOutStatus(lightupCourse)){
                    //return getIdentifyResponseData(map, todayCourse, "请勿重复打卡！");
                    return new ErrorResponseData(202,"请勿重复打卡！");
                }
                lightupClockService.saveClockOut(lightupCourse);
                return getIdentifyResponseData(map, lightupCourse, "签退成功！");
            }
            //检查当前时间是否为中间时间
            if(faceService.checkMiddleTime(lightupCourse)){
                log.info("现在是中间时间了");
                if(lightupClockService.checkClockMiddleStatus(lightupCourse) || lightupClockService.checkClockInStatus(lightupCourse)){
                    //return getIdentifyResponseData(map, todayCourse, "请勿重复打卡！");
                    return new ErrorResponseData(202,"请勿重复打卡！");
                }
                lightupClockService.saveClockLate(lightupCourse);
                return getIdentifyResponseData(map, lightupCourse, "打卡成功，迟到啦！");
            }
        }
        return new ErrorResponseData(201, "未到打卡时间！");
    }

    private ResponseData getIdentifyResponseData(Map<String, String> map, LightupCourse lightupCourse, String message) {
        try {
            if(faceService.identify(map.get("picBase"), Long.valueOf(map.get("avatarId"))) > 0){
                //记录打卡数据
                return new SuccessResponseData(200,message,lightupCourse);
            } else {
                return new ErrorResponseData(500,"人脸识别失败！");
            }
        } catch (Exception e) {
            return new ErrorResponseData(500,"人脸识别失败！");
        }
    }
}
