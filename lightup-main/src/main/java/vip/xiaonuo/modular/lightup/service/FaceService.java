package vip.xiaonuo.modular.lightup.service;

import vip.xiaonuo.modular.lightup.entity.LightupCourse;

import java.text.ParseException;
import java.util.List;

public interface FaceService {
    /**
     * 人脸识别
     * @param picBase
     * @return
     */
    int identify(String picBase, Long avatarId) throws Exception;

    List<LightupCourse> checkTodayCourse();

    String getTencentUrl(Long avatarId);

    Boolean checkBeginTime(LightupCourse lightupCourse);

    Boolean checkEndTime(LightupCourse lightupCourse);

    Boolean checkMiddleTime(LightupCourse lightupCourse);

    Long betweenTime(String compareTimeToNow);

    Long betweenTime(String compareTime, String compareDate) throws ParseException;

    Long betweenOnlyTime(String compareTimeOne, String compareDateTwo);

}
