package vip.xiaonuo.modular.lightup.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 教师控制签到状态表
 * @TableName lightup_clock
 */
@TableName(value ="lightup_clock")
@Data
public class LightupClock implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户账户
     */
    @TableField(value = "user_account")
    private String userAccount;

    /**
     * 课程id
     */
    @TableField(value = "course_name")
    private String courseName;

    /**
     * 0,未签到；1,正常；2,迟到
     */
    @TableField(value = "clock_in_status")
    private Integer clockInStatus;

    /**
     * 0,未签退；1,正常
     */
    @TableField(value = "clock_out_status")
    private Integer clockOutStatus;

    /**
     * 签到时间
     */
    @TableField(value = "clock_in_time")
    private String clockInTime;

    /**
     * 签退时间
     */
    @TableField(value = "clock_out_time")
    private String clockOutTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}