package vip.xiaonuo.modular.lightup.entity;

import lombok.Data;

/**
 * 带周排序的LightupCourse
 * @date 2022-03-24 13:18
 * @author wanglei1118
 */
@Data
public class LightupCourseWithSort extends LightupCourse{

    /**
     * 周几的数字值
     */
    private Integer weekNum;
}
