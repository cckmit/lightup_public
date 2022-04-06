package vip.xiaonuo.modular.lightup.entity;

import lombok.Data;

@Data
public class LightupCourseWithStatus extends LightupCourse{

    /**
     * 考勤状态
     */
    private String status;
}
